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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de las reglas de negocio de {@link CitaServicio}.
 *
 * <p>Se usan "mocks" (objetos simulados con Mockito) en lugar de la base de datos
 * real, y un reloj fijo: hoy es miércoles 23 de septiembre de 2026 a las 10:00.</p>
 */
@ExtendWith(MockitoExtension.class)
class CitaServicioTest {

    private static final ZoneId ZONA = ZoneId.of("America/Bogota");
    private static final LocalDate HOY = LocalDate.of(2026, 9, 23);          // miércoles
    private static final LocalDate MANANA = HOY.plusDays(1);                  // jueves

    @Mock private CitaRepositorio citaRepositorio;
    @Mock private ClienteRepositorio clienteRepositorio;
    @Mock private BarberoRepositorio barberoRepositorio;
    @Mock private ServicioRepositorio servicioRepositorio;

    private CitaServicio servicio;
    private Servicio corte45;
    private Barbero barbero;

    @BeforeEach
    void preparar() {
        Clock reloj = Clock.fixed(HOY.atTime(10, 0).atZone(ZONA).toInstant(), ZONA);
        servicio = new CitaServicio(citaRepositorio, clienteRepositorio, barberoRepositorio,
                servicioRepositorio, reloj);

        corte45 = new Servicio(1, "Corte Clásico", new BigDecimal("25000"), LocalTime.of(0, 45));
        barbero = new Barbero();
        barbero.setId(2);
        barbero.setEstado("ACTIVO");

        lenient().when(servicioRepositorio.findById(1)).thenReturn(Optional.of(corte45));
        lenient().when(barberoRepositorio.findById(2)).thenReturn(Optional.of(barbero));
        lenient().when(citaRepositorio.save(any(Cita.class))).thenAnswer(inv -> {
            Cita c = inv.getArgument(0);
            c.setId(99);
            return c;
        });
    }

    // ------------------------------------------------------------------
    //  Horarios disponibles
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Un día libre ofrece todos los turnos donde cabe un servicio de 45 min")
    void horariosDeUnDiaLibre() {
        sinCitas();
        List<LocalTime> horas = servicio.horariosDisponibles(2, MANANA, 1);

        // Mañana: 9:00 a 12:00 (7 turnos, 12:30 no cabe) | Tarde: 14:00 a 17:00 (7 turnos)
        assertEquals(14, horas.size());
        assertEquals(LocalTime.of(9, 0), horas.get(0));
        assertFalse(horas.contains(LocalTime.of(12, 30)), "12:30 + 45 min pasa de la 1:00 pm");
        assertTrue(horas.contains(LocalTime.of(17, 0)));
    }

    @Test
    @DisplayName("Una cita existente bloquea los turnos que se cruzan con ella")
    void citaExistenteBloqueaHorarios() {
        Cita ocupada = citaActiva(MANANA.atTime(10, 0));   // 10:00 a 10:45
        when(citaRepositorio.findByBarberoIdAndFechaCitaBetweenAndEstadoNot(
                eq(2), any(), any(), eq(EstadoCita.CANCELADA))).thenReturn(List.of(ocupada));

        List<LocalTime> horas = servicio.horariosDisponibles(2, MANANA, 1);

        assertFalse(horas.contains(LocalTime.of(9, 30)), "9:30-10:15 se cruza con 10:00");
        assertFalse(horas.contains(LocalTime.of(10, 0)));
        assertFalse(horas.contains(LocalTime.of(10, 30)), "10:30 empieza antes de que termine (10:45)");
        assertTrue(horas.contains(LocalTime.of(9, 0)), "9:00-9:45 termina antes de las 10:00");
        assertTrue(horas.contains(LocalTime.of(11, 0)));
    }

    @Test
    @DisplayName("Hoy no se ofrecen horas que ya pasaron")
    void hoyNoMuestraHorasPasadas() {
        sinCitas();
        List<LocalTime> horas = servicio.horariosDisponibles(2, HOY, 1);

        assertFalse(horas.contains(LocalTime.of(9, 30)));
        assertFalse(horas.contains(LocalTime.of(10, 0)), "Son las 10:00 en punto");
        assertTrue(horas.contains(LocalTime.of(10, 30)));
    }

    @Test
    @DisplayName("Domingos, fechas pasadas y fechas lejanas no tienen horarios")
    void fechasNoReservables() {
        assertTrue(servicio.horariosDisponibles(2, LocalDate.of(2026, 9, 27), 1).isEmpty(), "domingo");
        assertTrue(servicio.horariosDisponibles(2, HOY.minusDays(1), 1).isEmpty(), "ayer");
        assertTrue(servicio.horariosDisponibles(2, HOY.plusDays(40), 1).isEmpty(), "más de 30 días");
    }

    // ------------------------------------------------------------------
    //  Agendar
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Agendar en un horario libre guarda la cita como PENDIENTE")
    void agendarCitaValida() {
        sinCitas();
        when(clienteRepositorio.findById(4)).thenReturn(Optional.of(new Cliente(4)));

        Cita cita = servicio.agendar(4, formulario(MANANA, LocalTime.of(11, 0)));

        assertEquals(EstadoCita.PENDIENTE, cita.getEstado());
        assertEquals(MANANA.atTime(11, 0), cita.getFechaCita());
        assertEquals("Sin gel", cita.getObservaciones());
        verify(citaRepositorio).save(any(Cita.class));
    }

    @Test
    @DisplayName("Si el usuario no está en la tabla cliente, se crea automáticamente")
    void agendarCreaClienteSiNoExiste() {
        sinCitas();
        when(clienteRepositorio.findById(4)).thenReturn(Optional.empty());
        when(clienteRepositorio.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cita cita = servicio.agendar(4, formulario(MANANA, LocalTime.of(9, 0)));

        assertEquals(4, cita.getCliente().getId());
        verify(clienteRepositorio).save(any(Cliente.class));
    }

    @Test
    @DisplayName("No se puede agendar en un horario ocupado")
    void noAgendaHorarioOcupado() {
        Cita ocupada = citaActiva(MANANA.atTime(11, 0));
        when(citaRepositorio.findByBarberoIdAndFechaCitaBetweenAndEstadoNot(
                eq(2), any(), any(), eq(EstadoCita.CANCELADA))).thenReturn(List.of(ocupada));

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> servicio.agendar(4, formulario(MANANA, LocalTime.of(11, 0))));

        assertTrue(error.getMessage().contains("ya no está disponible"));
        verify(citaRepositorio, never()).save(any());
    }

    @Test
    @DisplayName("No se puede agendar un domingo")
    void noAgendaDomingo() {
        assertThrows(ReglaNegocioException.class,
                () -> servicio.agendar(4, formulario(LocalDate.of(2026, 9, 27), LocalTime.of(10, 0))));
    }

    @Test
    @DisplayName("No se puede agendar fuera de la jornada (almuerzo)")
    void noAgendaEnHoraDeAlmuerzo() {
        assertThrows(ReglaNegocioException.class,
                () -> servicio.agendar(4, formulario(MANANA, LocalTime.of(12, 30))));
    }

    @Test
    @DisplayName("No se puede agendar con un barbero inactivo")
    void noAgendaBarberoInactivo() {
        barbero.setEstado("INACTIVO");
        assertThrows(ReglaNegocioException.class,
                () -> servicio.agendar(4, formulario(MANANA, LocalTime.of(10, 0))));
    }

    // ------------------------------------------------------------------
    //  Cancelar y cambiar estado
    // ------------------------------------------------------------------

    @Test
    @DisplayName("El cliente puede cancelar su propia cita futura")
    void clienteCancelaSuCita() {
        Cita cita = citaActiva(MANANA.atTime(10, 0));
        cita.setCliente(new Cliente(4));
        when(citaRepositorio.findById(7)).thenReturn(Optional.of(cita));

        servicio.cancelarPorCliente(7, 4);

        assertEquals(EstadoCita.CANCELADA, cita.getEstado());
    }

    @Test
    @DisplayName("Un cliente NO puede cancelar la cita de otro cliente")
    void clienteNoCancelaCitaAjena() {
        Cita cita = citaActiva(MANANA.atTime(10, 0));
        cita.setCliente(new Cliente(4));
        when(citaRepositorio.findById(7)).thenReturn(Optional.of(cita));

        assertThrows(ReglaNegocioException.class, () -> servicio.cancelarPorCliente(7, 999));
        assertEquals(EstadoCita.PENDIENTE, cita.getEstado());
    }

    @Test
    @DisplayName("No se puede cancelar con menos de 2 horas de anticipación (HU05)")
    void noCancelaCercaDeLaHora() {
        Cita cita = citaActiva(HOY.atTime(11, 30));   // son las 10:00: faltan 1 h 30 min
        cita.setCliente(new Cliente(4));
        when(citaRepositorio.findById(7)).thenReturn(Optional.of(cita));

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> servicio.cancelarPorCliente(7, 4));

        assertTrue(error.getMessage().contains("2 horas"));
        assertEquals(EstadoCita.PENDIENTE, cita.getEstado());
    }

    @Test
    @DisplayName("El barbero confirma un servicio de hoy y queda la fecha de atención (HU06)")
    void barberoConfirmaServicio() {
        Cita cita = citaActiva(HOY.atTime(9, 0));
        when(citaRepositorio.findById(3)).thenReturn(Optional.of(cita));

        servicio.confirmarServicio(3, 2);

        assertEquals(EstadoCita.COMPLETADA, cita.getEstado());
        assertEquals(HOY.atTime(10, 0), cita.getFechaAtencion());
    }

    @Test
    @DisplayName("El barbero no puede confirmar citas de otro barbero ni citas futuras")
    void barberoNoConfirmaAjenaNiFutura() {
        Cita ajena = citaActiva(HOY.atTime(9, 0));
        when(citaRepositorio.findById(3)).thenReturn(Optional.of(ajena));
        assertThrows(ReglaNegocioException.class, () -> servicio.confirmarServicio(3, 999));

        Cita futura = citaActiva(MANANA.atTime(9, 0));
        when(citaRepositorio.findById(4)).thenReturn(Optional.of(futura));
        assertThrows(ReglaNegocioException.class, () -> servicio.confirmarServicio(4, 2));
        assertEquals(EstadoCita.PENDIENTE, futura.getEstado());
    }

    @Test
    @DisplayName("El administrador no puede modificar una cita completada")
    void adminNoModificaCitaCompletada() {
        Cita cita = citaActiva(HOY.minusDays(2).atTime(10, 0));
        cita.setEstado(EstadoCita.COMPLETADA);
        when(citaRepositorio.findById(anyInt())).thenReturn(Optional.of(cita));

        assertThrows(ReglaNegocioException.class, () -> servicio.cambiarEstado(1, EstadoCita.PENDIENTE));
    }

    @Test
    @DisplayName("El administrador confirma una cita pendiente")
    void adminConfirmaCita() {
        Cita cita = citaActiva(MANANA.atTime(10, 0));
        when(citaRepositorio.findById(1)).thenReturn(Optional.of(cita));

        servicio.cambiarEstado(1, EstadoCita.CONFIRMADA);

        assertEquals(EstadoCita.CONFIRMADA, cita.getEstado());
    }

    // ------------------------------------------------------------------
    //  Métodos de apoyo de las pruebas
    // ------------------------------------------------------------------

    private void sinCitas() {
        when(citaRepositorio.findByBarberoIdAndFechaCitaBetweenAndEstadoNot(
                eq(2), any(LocalDateTime.class), any(LocalDateTime.class), eq(EstadoCita.CANCELADA)))
                .thenReturn(List.of());
    }

    private Cita citaActiva(LocalDateTime fecha) {
        Cita cita = new Cita();
        cita.setBarbero(barbero);
        cita.setServicio(corte45);
        cita.setFechaCita(fecha);
        cita.setEstado(EstadoCita.PENDIENTE);
        return cita;
    }

    private CitaForm formulario(LocalDate fecha, LocalTime hora) {
        CitaForm form = new CitaForm();
        form.setServicioId(1);
        form.setBarberoId(2);
        form.setFecha(fecha);
        form.setHora(hora);
        form.setObservaciones("  Sin gel  ");
        return form;
    }
}
