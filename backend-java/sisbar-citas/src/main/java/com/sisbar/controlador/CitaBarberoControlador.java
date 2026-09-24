package com.sisbar.controlador;

import com.sisbar.dto.UsuarioSesion;
import com.sisbar.modelo.Cita;
import com.sisbar.modelo.EstadoCita;
import com.sisbar.servicio.CitaServicio;
import com.sisbar.servicio.ReglaNegocioException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador del panel del BARBERO.
 *
 * <pre>
 *   GET  /barbero/citas?fecha=2026-09-25   → agenda del día (HU04)
 *   POST /barbero/citas/{id}/confirmar     → confirma que el servicio se realizó (HU06)
 * </pre>
 */
@Controller
@RequestMapping("/barbero/citas")
public class CitaBarberoControlador {

    private final CitaServicio citaServicio;

    public CitaBarberoControlador(CitaServicio citaServicio) {
        this.citaServicio = citaServicio;
    }

    /** Agenda del barbero para el día elegido (por defecto, hoy). */
    @GetMapping
    public String agenda(@SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion usuario,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                         Model model) {
        LocalDate dia = fecha == null ? citaServicio.hoy() : fecha;
        List<Cita> citas = citaServicio.agendaDelBarbero(usuario.id(), dia);

        // Datos de las tarjetas de resumen
        long completadas = citas.stream().filter(c -> c.getEstado() == EstadoCita.COMPLETADA).count();
        BigDecimal ingresos = citas.stream()
                .filter(c -> c.getEstado() == EstadoCita.COMPLETADA && c.getServicio() != null)
                .map(c -> c.getServicio().getPrecio())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("citas", citas);
        model.addAttribute("dia", dia);
        model.addAttribute("hoy", citaServicio.hoy());
        model.addAttribute("totalCitas", citas.stream().filter(c -> c.getEstado() != EstadoCita.CANCELADA).count());
        model.addAttribute("completadas", completadas);
        model.addAttribute("ingresos", ingresos);
        return "barbero/citas";
    }

    /** El barbero marca la cita como realizada. */
    @PostMapping("/{id}/confirmar")
    public String confirmar(@PathVariable Integer id,
                            @RequestParam(required = false) String fecha,
                            @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion usuario,
                            RedirectAttributes flash) {
        try {
            citaServicio.confirmarServicio(id, usuario.id());
            flash.addFlashAttribute("exito", "Servicio de la cita #" + id + " confirmado como realizado.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return fecha == null || fecha.isBlank() ? "redirect:/barbero/citas" : "redirect:/barbero/citas?fecha=" + fecha;
    }
}
