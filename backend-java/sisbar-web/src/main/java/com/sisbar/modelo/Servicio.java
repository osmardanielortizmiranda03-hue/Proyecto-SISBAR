package com.sisbar.modelo;

import com.sisbar.util.Validaciones;

/**
 * Modelo (JavaBean) que representa un servicio de la barbería.
 * Corresponde a la tabla "servicio" de la base de datos.
 */
public class Servicio {

    private int idServicio;
    private String nombreServicio;
    private double precioServicio;
    private String duracionServicio; // formato TIME de MySQL: "HH:MM:SS"

    public Servicio() {
    }

    public Servicio(int idServicio, String nombreServicio, double precioServicio, String duracionServicio) {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.precioServicio = precioServicio;
        this.duracionServicio = duracionServicio;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public double getPrecioServicio() {
        return precioServicio;
    }

    public void setPrecioServicio(double precioServicio) {
        this.precioServicio = precioServicio;
    }

    public String getDuracionServicio() {
        return duracionServicio;
    }

    public void setDuracionServicio(String duracionServicio) {
        this.duracionServicio = duracionServicio;
    }

    /** Duración en minutos, útil para mostrarla en las vistas JSP: ${s.duracionMinutos} */
    public int getDuracionMinutos() {
        return Validaciones.horaAMinutos(duracionServicio);
    }

    /** Precio sin ".0" para mostrarlo en el formulario: 20000.0 -> "20000" */
    public String getPrecioTexto() {
        return java.math.BigDecimal.valueOf(precioServicio).stripTrailingZeros().toPlainString();
    }

    @Override
    public String toString() {
        return "Servicio [id=" + idServicio + ", nombre=" + nombreServicio
                + ", precio=" + precioServicio + ", duracion=" + duracionServicio + "]";
    }
}
