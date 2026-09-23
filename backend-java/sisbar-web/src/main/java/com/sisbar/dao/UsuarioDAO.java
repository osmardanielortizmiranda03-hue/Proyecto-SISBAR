package com.sisbar.dao;

import com.sisbar.conexion.ConexionBD;
import com.sisbar.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO de la tabla "usuario": registro de clientes e inicio de sesión.
 * Usa las columnas de la tabla usuario de la base de datos sisbar
 * (nombre, apellido, correo_usuario, telefono_usuario, n_identidad, contraseña...).
 */
public class UsuarioDAO {

    /** Registra un usuario nuevo. El passwordHash ya debe venir cifrado. */
    public boolean registrar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (nombre, apellido, n_identidad, telefono_usuario, correo_usuario, "
                + "fecha_de_nacimiento, nacionalidad, `contraseña`, rol, fecha_registro) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE())";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombres());
            ps.setString(2, u.getApellidos());
            ps.setString(3, u.getNumeroIdentidad());
            ps.setString(4, u.getCelular());
            ps.setString(5, u.getEmail());
            ps.setString(6, u.getFechaNacimiento());
            ps.setString(7, u.getNacionalidad());
            ps.setString(8, u.getPasswordHash());
            ps.setString(9, u.getRol());
            return ps.executeUpdate() == 1;
        }
    }

    /** true si ya hay un usuario con ese correo o ese número de identidad */
    public boolean existe(String email, String numeroIdentidad) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo_usuario = ? OR n_identidad = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, numeroIdentidad);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /** Busca un usuario por correo (para el login). Devuelve null si no existe. */
    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE correo_usuario = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("idUSUARIO"));
                u.setNombres(rs.getString("nombre"));
                u.setApellidos(rs.getString("apellido"));
                u.setNumeroIdentidad(rs.getString("n_identidad"));
                u.setCelular(rs.getString("telefono_usuario"));
                u.setEmail(rs.getString("correo_usuario"));
                u.setFechaNacimiento(rs.getString("fecha_de_nacimiento"));
                u.setNacionalidad(rs.getString("nacionalidad"));
                u.setPasswordHash(rs.getString("contraseña"));
                u.setRol(rs.getString("rol"));
                return u;
            }
        }
    }
}
