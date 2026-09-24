package com.sisbar.modelo;

/**
 * Estados posibles de una cita. Se guardan como texto en la columna
 * {@code estado_cita} de la tabla {@code citas}.
 */
public enum EstadoCita {

    /** Recién agendada por el cliente, pendiente de confirmación. */
    PENDIENTE("Pendiente"),
    /** Confirmada por la barbería. */
    CONFIRMADA("Confirmada"),
    /** El servicio ya se prestó. */
    COMPLETADA("Completada"),
    /** Cancelada por el cliente o por la barbería. */
    CANCELADA("Cancelada");

    private final String etiqueta;

    EstadoCita(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /** Texto amigable para mostrar en las vistas. */
    public String getEtiqueta() {
        return etiqueta;
    }

    /** Una cita está "activa" (ocupa el horario del barbero) si no fue cancelada. */
    public boolean ocupaHorario() {
        return this != CANCELADA;
    }
}
