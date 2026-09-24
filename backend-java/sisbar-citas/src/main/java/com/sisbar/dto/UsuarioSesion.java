package com.sisbar.dto;

import java.io.Serializable;

/**
 * Datos mínimos del usuario que se guardan en la sesión HTTP después del login.
 * Se usa un "record" de Java: una clase inmutable con getters automáticos.
 *
 * @param id       id del usuario
 * @param nombre   nombre
 * @param apellido apellido
 * @param rol      CLIENTE, BARBERO o ADMIN
 */
public record UsuarioSesion(Integer id, String nombre, String apellido, String rol) implements Serializable {

    /** Nombre del atributo con el que se guarda en la sesión. */
    public static final String ATRIBUTO = "usuarioSesion";

    public String nombreCompleto() {
        return nombre + " " + apellido;
    }

    public String inicial() {
        return nombre == null || nombre.isEmpty() ? "?" : nombre.substring(0, 1).toUpperCase();
    }

    public boolean esAdmin() {
        return "ADMIN".equals(rol);
    }

    public boolean esCliente() {
        return "CLIENTE".equals(rol);
    }

    public boolean esBarbero() {
        return "BARBERO".equals(rol);
    }
}
