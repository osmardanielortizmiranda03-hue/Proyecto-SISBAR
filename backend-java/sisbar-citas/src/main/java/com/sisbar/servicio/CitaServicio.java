package com.sisbar.servicio;

import com.sisbar.dto.CitaForm;
import com.sisbar.modelo.Barbero;
import com.sisbar.modelo.Cita;
import com.sisbar.modelo.Cliente;
import com.sisbar.modelo.EstadoCita;
import com.sisbar.modelo.Servicio;
import com.sisbar.repositorio.BarberoRepositorio;
import com.sisbar.repositorio.CitaRepositorio;
import com.sisbar.repositorio.ClienteRepositorio;
import com.sisbar.repositorio.ServicioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Capa de servicio (lógica de negocio) del módulo de citas.
 *
 * <p>Aquí están TODAS las reglas: qué horarios están libres, quién puede
 * cancelar, qué cambios de estado se permiten, etc. Los controladores solo
 * reciben peticiones y llaman a estos métodos; los repositorios solo guardan
 * y consultan datos.</p>
 */
@Service
public class CitaServicio {

    private final CitaRepositorio citaRepositorio;
    private final ClienteRepositorio clienteRepositorio;
    private final BarberoRepositorio barberoRepositorio;
    private final ServicioRepositorio servicioRepositorio;
    /** Reloj del sistema. Se inyecta para poder fijar la fecha en las pruebas. */
    private final Clock reloj;

    /**
     * Inyección de dependencias por constructor: Spring crea este servicio
     * y le entrega automáticamente los repositorios y el reloj.
     */
    public CitaServicio(CitaRepositorio citaRepositorio,
                        ClienteRepositorio clienteRepositorio,
                        BarberoRepositorio barberoRepositorio,
                        ServicioRepositorio servicioRepositorio,
                        Clock reloj) {
        this.citaRepositorio = citaRepositorio;
        this.clienteRepositorio = clienteRepositorio;
        this.barberoRepositorio = barberoRepositorio;
        this.servicioRepositorio = servicioRepositorio;
        this.reloj = reloj;
    }

    // =====================================================================
    //  Consultas para llenar el formulario
    // =====================================================================

    /** Servicios que el cliente puede elegir (paso 1). */
    @Transactional(readOnly = true)
    public List<Servicio> listarServicios() {
        return servicioRepositorio.findAllByOrderByPrecioAsc();
    }

    /** Barberos activos que el cliente puede elegir (paso 2). */
    @Transactional(readOnly = true)
    public List<Barbero> listarBarberosActivos() {
        return barberoRepositorio.findByEstadoIgnoreCase(Barbero.ESTADO_ACTIVO);
    }

    /** Fecha de hoy según el reloj del sistema. */
    public LocalDate hoy() {
        return LocalDate.now(reloj);
    }

    /** Fecha y hora actual según el reloj del sistema. */
    public LocalDateTime ahora() {
        return LocalDateTime.now(reloj);
    }

    /**
     * Calcula los horarios libres de un barbero en una fecha para un servicio (paso 4).
     *
     * <p>Un horario está libre si la cita cabe completa en la jornada, no está en
     * el pasado y no se cruza con otra cita activa del mismo barbero.</p>
     */
    @Transactional(readOnly = true)
    public List<LocalTime> horariosDisponibles(Integer barberoId, LocalDate fecha, Integer servicioId) {
        List<LocalTime> disponibles = new ArrayList<>();
        if (barberoId == null || fecha == null || !esFechaReservable(fecha)) {
            return disponibles;
        }

        int duracion = servicioRepositorio.findById(servicioId == null ? -1 : servicioId)
                .map(Servicio::getDuracionMinutos)
                .orElse(Cita.DURACION_POR_DEFECTO_MIN);

        List<Cita> ocupadas = citasActivasDelDia(barberoId, fecha);
        LocalDateTime ahora = LocalDateTime.now(reloj);

        for (LocalTime[] jornada : HorarioBarberia.JORNADAS) {
            for (LocalTime hora = jornada[0];
                 !hora.plusMinutes(duracion).isAfter(jornada[1]);
                 hora = hora.plusMinutes(HorarioBarberia.INTERVALO_MINUTOS)) {

                LocalDateTime inicio = fecha.atTime(hora);
                LocalDateTime fin = inicio.plusMinutes(duracion);
                boolean enElFuturo = inicio.isAfter(ahora);
                if (enElFuturo && !seCruza(inicio, fin, ocupadas)) {
                    disponibles.add(hora);
                }
            }
        }
        return disponibles;
    }

    // =====================================================================
    //  Operaciones del cliente
    // =====================================================================

    /**
     * Crea una cita nueva en estado PENDIENTE.
     *
     * @param usuarioClienteId id del usuario que inició sesión
     * @param form             datos del formulario ya validados por Spring
     * @return la cita guardada (con su id)
     * @throws ReglaNegocioException si alguna regla no se cumple
     */
    @Transactional
    public Cita agendar(Integer usuarioClienteId, CitaForm form) {
        Servicio servicio = servicioRepositorio.findById(form.getServicioId())
                .orElseThrow(() -> new ReglaNegocioException("El servicio seleccionado no existe."));
        Barbero barbero = barberoRepositorio.findById(form.getBarberoId())
                .filter(b -> Barbero.ESTADO_ACTIVO.equalsIgnoreCase(b.getEstado()))
                .orElseThrow(() -> new ReglaNegocioException("El barbero seleccionado no está disponible."));

        if (!esFechaReservable(form.getFecha())) {
            throw new ReglaNegocioException("La fecha debe estar entre hoy y los próximos "
                    + HorarioBarberia.DIAS_MAXIMOS_RESERVA + " días (lunes a sábado).");
        }
        if (!HorarioBarberia.cabeEnJornada(form.getHora(), servicio.getDuracionMinutos())) {
            throw new ReglaNegocioException("El horario elegido está fuera de la jornada de atención.");
        }
        if (!horariosDisponibles(barbero.getId(), form.getFecha(), servicio.getId()).contains(form.getHora())) {
            throw new ReglaNegocioException("Ese horario ya no está disponible. Elige otro, por favor.");
        }

        // Si el usuario aún no está en la tabla "cliente", se registra ahí automáticamente
        Cliente cliente = clienteRepositorio.findById(usuarioClienteId)
                .orElseGet(() -> clienteRepositorio.save(new Cliente(usuarioClienteId)));

        Cita cita = new Cita();
        cita.setCliente(cliente);
        cita.setBarbero(barbero);
        cita.setServicio(servicio);
        cita.setFechaCita(form.getFecha().atTime(form.getHora()));
        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setObservaciones(limpiar(form.getObservaciones()));
        return citaRepositorio.save(cita);
    }

    /** Citas del cliente que inició sesión. */
    @Transactional(readOnly = true)
    public List<Cita> citasDelCliente(Integer usuarioClienteId) {
        return citaRepositorio.findByClienteIdOrderByFechaCitaDesc(usuarioClienteId);
    }

    /**
     * El cliente cancela una de SUS citas. Solo se permite si la cita es suya,
     * está pendiente o confirmada y todavía no ha pasado.
     */
    @Transactional
    public void cancelarPorCliente(Integer citaId, Integer usuarioClienteId) {
        Cita cita = buscar(citaId);
        if (!cita.getCliente().getId().equals(usuarioClienteId)) {
            throw new ReglaNegocioException("No puedes cancelar una cita que no es tuya.");
        }
        if (!cita.isActiva()) {
            throw new ReglaNegocioException("Esta cita ya no se puede cancelar.");
        }
        if (!cita.esCancelableEn(ahora())) {
            throw new ReglaNegocioException("Las citas solo se pueden cancelar con mínimo "
                    + Cita.HORAS_MINIMAS_CANCELACION + " horas de anticipación.");
        }
        cita.setEstado(EstadoCita.CANCELADA);
        citaRepositorio.save(cita);
    }

    // =====================================================================
    //  Operaciones del barbero (HU04 y HU06)
    // =====================================================================

    /** Agenda del barbero para un día, ordenada por hora. */
    @Transactional(readOnly = true)
    public List<Cita> agendaDelBarbero(Integer barberoId, LocalDate fecha) {
        return citaRepositorio.findByBarberoIdAndFechaCitaBetweenOrderByFechaCitaAsc(
                barberoId, fecha.atStartOfDay(), fecha.atTime(LocalTime.MAX));
    }

    /**
     * El barbero confirma que realizó el servicio (HU06 / RF06).
     * La cita debe ser suya, estar activa y ser de hoy o de un día anterior.
     */
    @Transactional
    public void confirmarServicio(Integer citaId, Integer barberoId) {
        Cita cita = buscar(citaId);
        if (!cita.getBarbero().getId().equals(barberoId)) {
            throw new ReglaNegocioException("Esa cita no está asignada a ti.");
        }
        if (!cita.isActiva()) {
            throw new ReglaNegocioException("La cita #" + citaId + " ya está "
                    + cita.getEstado().getEtiqueta().toLowerCase() + ".");
        }
        if (cita.getFechaCita().toLocalDate().isAfter(hoy())) {
            throw new ReglaNegocioException("Solo puedes confirmar servicios de hoy o de días anteriores.");
        }
        cita.setEstado(EstadoCita.COMPLETADA);
        cita.setFechaAtencion(ahora());
        citaRepositorio.save(cita);
    }

    // =====================================================================
    //  Operaciones del administrador
    // =====================================================================

    /** Listado de citas con filtros opcionales por estado y por día. */
    @Transactional(readOnly = true)
    public List<Cita> listarParaAdministrador(EstadoCita estado, LocalDate fecha) {
        LocalDateTime desde = fecha == null ? null : fecha.atStartOfDay();
        LocalDateTime hasta = fecha == null ? null : fecha.plusDays(1).atStartOfDay();
        return citaRepositorio.buscarConFiltros(estado, desde, hasta);
    }

    /**
     * Cantidad de citas por cada estado (tarjetas de resumen).
     * La llave es el nombre del estado (PENDIENTE, CONFIRMADA...) para usarla fácil en la vista.
     */
    @Transactional(readOnly = true)
    public Map<String, Long> resumenPorEstado() {
        Map<String, Long> resumen = new LinkedHashMap<>();
        for (EstadoCita estado : EstadoCita.values()) {
            resumen.put(estado.name(), citaRepositorio.countByEstado(estado));
        }
        return resumen;
    }

    /**
     * El administrador cambia el estado de una cita.
     * Una cita cancelada o completada ya no puede cambiar.
     */
    @Transactional
    public void cambiarEstado(Integer citaId, EstadoCita nuevoEstado) {
        if (nuevoEstado == null) {
            throw new ReglaNegocioException("Estado no válido.");
        }
        Cita cita = buscar(citaId);
        if (cita.getEstado() == EstadoCita.CANCELADA || cita.getEstado() == EstadoCita.COMPLETADA) {
            throw new ReglaNegocioException("La cita #" + citaId + " ya está "
                    + cita.getEstado().getEtiqueta().toLowerCase() + " y no se puede modificar.");
        }
        cita.setEstado(nuevoEstado);
        if (nuevoEstado == EstadoCita.COMPLETADA) {
            cita.setFechaAtencion(ahora());
        }
        citaRepositorio.save(cita);
    }

    // =====================================================================
    //  Métodos de apoyo (privados)
    // =====================================================================

    private Cita buscar(Integer citaId) {
        return citaRepositorio.findById(citaId)
                .orElseThrow(() -> new ReglaNegocioException("La cita #" + citaId + " no existe."));
    }

    /** Fecha entre hoy y hoy + 30 días, y que no sea domingo. */
    private boolean esFechaReservable(LocalDate fecha) {
        LocalDate hoy = hoy();
        return !fecha.isBefore(hoy)
                && !fecha.isAfter(hoy.plusDays(HorarioBarberia.DIAS_MAXIMOS_RESERVA))
                && HorarioBarberia.esDiaLaboral(fecha);
    }

    private List<Cita> citasActivasDelDia(Integer barberoId, LocalDate fecha) {
        return citaRepositorio.findByBarberoIdAndFechaCitaBetweenAndEstadoNot(
                barberoId, fecha.atStartOfDay(), fecha.atTime(LocalTime.MAX), EstadoCita.CANCELADA);
    }

    /** Dos intervalos [inicio, fin) se cruzan si uno empieza antes de que termine el otro. */
    private boolean seCruza(LocalDateTime inicio, LocalDateTime fin, List<Cita> ocupadas) {
        for (Cita cita : ocupadas) {
            if (inicio.isBefore(cita.getFechaFin()) && cita.getFechaCita().isBefore(fin)) {
                return true;
            }
        }
        return false;
    }

    private String limpiar(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }
}
