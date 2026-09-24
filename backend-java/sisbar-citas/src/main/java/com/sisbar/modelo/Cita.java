package com.sisbar.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entidad principal del módulo: la tabla {@code citas}.
 *
 * <p>Relaciones (muchos a uno):</p>
 * <ul>
 *   <li>Muchas citas pertenecen a un {@link Barbero}  (columna BARBERO_idUSUARIO)</li>
 *   <li>Muchas citas pertenecen a un {@link Cliente}  (columna CLIENTE_idUSUARIO)</li>
 *   <li>Muchas citas son de un {@link Servicio}       (columna idservicio)</li>
 * </ul>
 */
@Entity
@Table(name = "citas")
public class Cita {

    /** Duración por defecto si la cita no tiene servicio asociado. */
    public static final int DURACION_POR_DEFECTO_MIN = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcitas")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "BARBERO_idUSUARIO", nullable = false)
    private Barbero barbero;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CLIENTE_idUSUARIO", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idservicio")
    private Servicio servicio;

    /** Se guarda el nombre del enum (PENDIENTE, CONFIRMADA...) como texto. */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cita", nullable = false, length = 100)
    private EstadoCita estado;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    public Cita() {
        // Constructor vacío requerido por JPA
    }

    /** Minutos que dura la cita según su servicio. */
    public int getDuracionMinutos() {
        return servicio == null ? DURACION_POR_DEFECTO_MIN : servicio.getDuracionMinutos();
    }

    /** Hora en que termina la cita. */
    public LocalDateTime getFechaFin() {
        return fechaCita.plusMinutes(getDuracionMinutos());
    }

    /** El cliente solo puede cancelar citas activas que aún no han pasado. */
    public boolean isCancelablePorCliente() {
        return (estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA)
                && fechaCita.isAfter(LocalDateTime.now());
    }

    // ---------- Getters y setters ----------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Barbero getBarbero() { return barbero; }
    public void setBarbero(Barbero barbero) { this.barbero = barbero; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }

    public LocalDateTime getFechaCita() { return fechaCita; }
    public void setFechaCita(LocalDateTime fechaCita) { this.fechaCita = fechaCita; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
