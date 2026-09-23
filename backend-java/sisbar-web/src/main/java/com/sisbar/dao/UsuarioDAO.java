package com.sisbar.dao;

import com.sisbar.conexion.ConexionBD;
import com.sisbar.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO de la tabla "usuario": registro de clientes e inicio de sesión.
 */
public class UsuarioDAO {

    /** Registra un usuario nuevo. El passwordHash ya debe venir cifrado. */
    public boolean registrar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (nombres, apellidos, numero_identidad, celular, email, "
                + "fecha_nacimiento, nacionalidad, password_hash, rol) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ? OR numero_identidad = ?";
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
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("idusuario"));
                u.setNombres(rs.getString("nombres"));
                u.setApellidos(rs.getString("apellidos"));
                u.setNumeroIdentidad(rs.getString("numero_identidad"));
                u.setCelular(rs.getString("celular"));
                u.setEmail(rs.getString("email"));
                u.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                u.setNacionalidad(rs.getString("nacionalidad"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setRol(rs.getString("rol"));
                return u;
            }
        }
    }
}
