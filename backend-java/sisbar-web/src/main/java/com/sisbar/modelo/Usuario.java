package com.sisbar.modelo;

/**
 * Modelo (JavaBean) que representa a un usuario del sistema.
 * Roles posibles: CLIENTE, BARBERO, ADMIN.
 */
public class Usuario {

    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String numeroIdentidad;
    private String celular;
    private String email;
    private String fechaNacimiento; // yyyy-MM-dd
    private String nacionalidad;
    private String passwordHash;
    private String rol;

    public Usuario() {
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getNumeroIdentidad() { return numeroIdentidad; }
    public void setNumeroIdentidad(String numeroIdentidad) { this.numeroIdentidad = numeroIdentidad; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    /** Nombre completo para mostrar en las vistas: ${usuario.nombreCompleto} */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    /** Inicial para el avatar de la barra superior */
    public String getInicial() {
        return (nombres == null || nombres.isEmpty()) ? "?" : nombres.substring(0, 1).toUpperCase();
    }
}
