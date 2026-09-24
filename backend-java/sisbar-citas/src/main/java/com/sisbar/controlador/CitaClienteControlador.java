package com.sisbar.controlador;

import com.sisbar.dto.CitaForm;
import com.sisbar.dto.UsuarioSesion;
import com.sisbar.modelo.Cita;
import com.sisbar.servicio.CitaServicio;
import com.sisbar.servicio.HorarioBarberia;
import com.sisbar.servicio.ReglaNegocioException;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador del panel del CLIENTE para el módulo de citas.
 *
 * <pre>
 *   GET  /cliente/citas                 → Mis citas
 *   GET  /cliente/citas/nueva           → Formulario "Agendar Cita"
 *   POST /cliente/citas/nueva           → Guarda la cita
 *   GET  /cliente/citas/horarios        → Horarios libres (JSON, lo usa JavaScript)
 *   POST /cliente/citas/{id}/cancelar   → Cancela una cita
 * </pre>
 */
@Controller
@RequestMapping("/cliente/citas")
public class CitaClienteControlador {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final CitaServicio citaServicio;

    public CitaClienteControlador(CitaServicio citaServicio) {
        this.citaServicio = citaServicio;
    }

    /** Mis citas: listado de las citas del cliente que inició sesión. */
    @GetMapping
    public String misCitas(@SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion usuario, Model model) {
        List<Cita> citas = citaServicio.citasDelCliente(usuario.id());
        model.addAttribute("citas", citas);
        model.addAttribute("ahora", java.time.LocalDateTime.now());
        return "cliente/mis-citas";
    }

    /** Muestra el formulario de agendamiento. */
    @GetMapping("/nueva")
    public String formulario(Model model) {
        if (!model.containsAttribute("citaForm")) {
            model.addAttribute("citaForm", new CitaForm());
        }
        cargarDatosFormulario(model);
        return "cliente/agendar-cita";
    }

    /** Recibe el formulario, lo valida y guarda la cita. */
    @PostMapping("/nueva")
    public String agendar(@SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion usuario,
                          @Valid @ModelAttribute("citaForm") CitaForm form,
                          BindingResult validacion,
                          Model model,
                          RedirectAttributes flash) {

        // 1. Errores de las anotaciones (@NotNull, @Size...)
        if (validacion.hasErrors()) {
            model.addAttribute("error", validacion.getAllErrors().get(0).getDefaultMessage());
            cargarDatosFormulario(model);
            return "cliente/agendar-cita";
        }

        // 2. Reglas del negocio (horario ocupado, fecha no válida...)
        try {
            Cita cita = citaServicio.agendar(usuario.id(), form);
            flash.addFlashAttribute("exito", "¡Tu cita #" + cita.getId() + " quedó agendada! "
                    + "Te esperamos el " + cita.getFechaCita().toLocalDate() + " a las "
                    + cita.getFechaCita().toLocalTime().format(FORMATO_HORA) + ".");
            return "redirect:/cliente/citas";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            cargarDatosFormulario(model);
            return "cliente/agendar-cita";
        }
    }

    /**
     * Devuelve en formato JSON los horarios libres, por ejemplo ["09:00","09:30"].
     * Lo consume el JavaScript de la página cada vez que cambia el barbero o la fecha.
     */
    @GetMapping("/horarios")
    @ResponseBody
    public List<String> horarios(@RequestParam Integer barberoId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                                 @RequestParam(required = false) Integer servicioId) {
        return citaServicio.horariosDisponibles(barberoId, fecha, servicioId).stream()
                .map(h -> h.format(FORMATO_HORA))
                .toList();
    }

    /** Cancela una cita del cliente. */
    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Integer id,
                           @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion usuario,
                           RedirectAttributes flash) {
        try {
            citaServicio.cancelarPorCliente(id, usuario.id());
            flash.addFlashAttribute("exito", "La cita #" + id + " fue cancelada.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cliente/citas";
    }

    /** Datos que necesita la vista del formulario (servicios, barberos, horario). */
    private void cargarDatosFormulario(Model model) {
        model.addAttribute("servicios", citaServicio.listarServicios());
        model.addAttribute("barberos", citaServicio.listarBarberosActivos());
        model.addAttribute("hoy", citaServicio.hoy());
        model.addAttribute("diasMaximos", HorarioBarberia.DIAS_MAXIMOS_RESERVA);
        model.addAttribute("todosLosHorarios", todosLosHorarios());
    }

    /** Lista de todos los turnos posibles del día, para dibujar los botones de hora. */
    private List<String> todosLosHorarios() {
        List<String> horas = new java.util.ArrayList<>();
        for (LocalTime[] jornada : HorarioBarberia.JORNADAS) {
            for (LocalTime h = jornada[0]; h.isBefore(jornada[1]); h = h.plusMinutes(HorarioBarberia.INTERVALO_MINUTOS)) {
                horas.add(h.format(FORMATO_HORA));
            }
        }
        return horas;
    }
}
