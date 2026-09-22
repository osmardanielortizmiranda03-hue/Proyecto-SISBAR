package com.sisbar.modelo;

public class Servicio {

    private int idServicio;
    private String nombreServicio;
    private double precioServicio;
    private String duracionServicio;

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

    @Override
    public String toString() {
        return "Servicio [id=" + idServicio + ", nombre=" + nombreServicio +
            ", precio=" + precioServicio + ", duracion=" + duracionServicio + "]";
    }
}