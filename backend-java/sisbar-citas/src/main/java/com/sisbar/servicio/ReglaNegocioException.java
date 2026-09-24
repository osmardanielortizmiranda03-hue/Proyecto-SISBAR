package com.sisbar.servicio;

/**
 * Excepción para errores de reglas del negocio (horario ocupado, fecha pasada, etc.).
 * El controlador la atrapa y muestra el mensaje al usuario.
 */
public class ReglaNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
