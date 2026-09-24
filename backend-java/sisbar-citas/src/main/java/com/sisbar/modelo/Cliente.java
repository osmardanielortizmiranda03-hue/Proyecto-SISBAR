package com.sisbar.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entidad de la tabla {@code cliente}. Comparte la llave primaria con
 * {@code usuario} (relación uno a uno): un cliente ES un usuario.
 */
@Entity
@Table(name = "cliente")
public class Cliente {

    /** Mismo id del usuario al que pertenece. */
    @Id
    @Column(name = "idUSUARIO")
    private Integer id;

    @Column(name = "preferencia_cliente", length = 100)
    private String preferencia;

    /** Datos personales del cliente (nombre, correo...). Solo lectura. */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUSUARIO", insertable = false, updatable = false)
    private Usuario usuario;

    public Cliente() {
        // Constructor vacío requerido por JPA
    }

    public Cliente(Integer id) {
        this.id = id;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPreferencia() { return preferencia; }
    public void setPreferencia(String preferencia) { this.preferencia = preferencia; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
