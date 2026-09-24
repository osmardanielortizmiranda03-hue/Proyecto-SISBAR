package com.sisbar.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Entidad de la tabla {@code servicio} (corte clásico, barba, etc.).
 */
@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idservicio")
    private Integer id;

    @Column(name = "nombre_servicio", nullable = false, length = 100)
    private String nombre;

    @Column(name = "precio_servicio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    /** Duración en formato TIME de MySQL (ej: 00:45:00). */
    @Column(name = "duracion_servicio", nullable = false)
    private LocalTime duracion;

    public Servicio() {
        // Constructor vacío requerido por JPA
    }

    public Servicio(Integer id, String nombre, BigDecimal precio, LocalTime duracion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.duracion = duracion;
    }

    /** Duración convertida a minutos (00:45:00 → 45). */
    public int getDuracionMinutos() {
        return duracion == null ? 30 : duracion.getHour() * 60 + duracion.getMinute();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public LocalTime getDuracion() { return duracion; }
    public void setDuracion(LocalTime duracion) { this.duracion = duracion; }
}
