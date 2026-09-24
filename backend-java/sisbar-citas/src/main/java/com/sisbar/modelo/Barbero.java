package com.sisbar.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * Entidad de la tabla {@code barbero}. Igual que el cliente, comparte
 * la llave primaria con {@code usuario}.
 */
@Entity
@Table(name = "barbero")
public class Barbero {

    /** Valor de {@code estado_barbero} para los barberos que atienden citas. */
    public static final String ESTADO_ACTIVO = "ACTIVO";

    @Id
    @Column(name = "idUSUARIO")
    private Integer id;

    @Column(name = "estado_barbero", nullable = false, length = 50)
    private String estado;

    @Column(name = "especializacion_barbero", nullable = false, length = 45)
    private String especializacion;

    @Column(name = "comision", nullable = false, precision = 10, scale = 2)
    private BigDecimal comision;

    /** Datos personales del barbero. Solo lectura. */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUSUARIO", insertable = false, updatable = false)
    private Usuario usuario;

    public Barbero() {
        // Constructor vacío requerido por JPA
    }

    /** Nombre completo del barbero para las vistas. */
    public String getNombreCompleto() {
        return usuario == null ? "Barbero #" + id : usuario.getNombreCompleto();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEspecializacion() { return especializacion; }
    public void setEspecializacion(String especializacion) { this.especializacion = especializacion; }

    public BigDecimal getComision() { return comision; }
    public void setComision(BigDecimal comision) { this.comision = comision; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
