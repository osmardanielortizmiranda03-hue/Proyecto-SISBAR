package com.sisbar.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Entidad JPA que representa la tabla {@code usuario}.
 * Un usuario puede ser CLIENTE, BARBERO o ADMIN según la columna {@code rol}.
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUSUARIO")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "correo_usuario", nullable = false, length = 100)
    private String correo;

    @Column(name = "telefono_usuario", nullable = false, length = 20)
    private String telefono;

    @Column(name = "fecha_de_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "n_identidad", nullable = false, length = 45)
    private String numeroIdentidad;

    @Column(name = "nacionalidad", nullable = false, length = 45)
    private String nacionalidad;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    /** Hash SHA-256 de la contraseña (la columna se llama "contraseña" con ñ). */
    @Column(name = "`contraseña`", nullable = false, length = 250)
    private String contrasenaHash;

    @Column(name = "rol", nullable = false, length = 20)
    private String rol;

    public Usuario() {
        // Constructor vacío requerido por JPA
    }

    /** Nombre y apellido juntos, para mostrar en pantalla. */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /** Primera letra del nombre, usada en el avatar de la barra superior. */
    public String getInicial() {
        return (nombre == null || nombre.isEmpty()) ? "?" : nombre.substring(0, 1).toUpperCase();
    }

    // ---------- Getters y setters ----------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNumeroIdentidad() { return numeroIdentidad; }
    public void setNumeroIdentidad(String numeroIdentidad) { this.numeroIdentidad = numeroIdentidad; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
