package com.sisbar.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Datos que llegan desde el formulario "Agendar Cita".
 * Spring llena este objeto automáticamente con los campos del formulario
 * (data binding) y valida las anotaciones antes de llamar al servicio.
 */
public class CitaForm {

    @NotNull(message = "Selecciona un servicio.")
    private Integer servicioId;

    @NotNull(message = "Selecciona un barbero.")
    private Integer barberoId;

    @NotNull(message = "Selecciona una fecha.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @NotNull(message = "Selecciona un horario.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hora;

    @Size(max = 255, message = "Las observaciones admiten máximo 255 caracteres.")
    private String observaciones;

    public Integer getServicioId() { return servicioId; }
    public void setServicioId(Integer servicioId) { this.servicioId = servicioId; }

    public Integer getBarberoId() { return barberoId; }
    public void setBarberoId(Integer barberoId) { this.barberoId = barberoId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
