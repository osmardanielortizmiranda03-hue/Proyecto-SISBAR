package com.sisbar.controlador;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * Controlador del panel del ADMINISTRADOR para gestionar citas.
 *
 * <pre>
 *   GET  /admin/citas?estado=PENDIENTE&amp;fecha=2026-09-25  → listado con filtros
 *   POST /admin/citas/{id}/estado                         → cambia el estado de una cita
 * </pre>
 */
@Controller
@RequestMapping("/admin/citas")
public class CitaAdminControlador {

    private final CitaServicio citaServicio;

    public CitaAdminControlador(CitaServicio citaServicio) {
        this.citaServicio = citaServicio;
    }

    /** Listado de citas con filtros opcionales. */
    @GetMapping
    public String listar(@RequestParam(required = false) EstadoCita estado,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                         Model model) {
        model.addAttribute("citas", citaServicio.listarParaAdministrador(estado, fecha));
        model.addAttribute("resumen", citaServicio.resumenPorEstado());
        model.addAttribute("estados", EstadoCita.values());
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("fechaFiltro", fecha);
        return "admin/citas";
    }

    /** Cambia el estado de una cita (confirmar, completar o cancelar). */
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id,
                                @RequestParam EstadoCita estado,
                                RedirectAttributes flash) {
        try {
            citaServicio.cambiarEstado(id, estado);
            flash.addFlashAttribute("exito", "La cita #" + id + " ahora está " + estado.getEtiqueta().toLowerCase() + ".");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/citas";
    }
}
