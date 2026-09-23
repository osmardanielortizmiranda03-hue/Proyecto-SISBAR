package com.sisbar.dao;

import com.sisbar.conexion.ConexionBD;
import com.sisbar.modelo.Servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) de la tabla "servicio".
 * Contiene las operaciones CRUD: insertar, consultar, actualizar y eliminar.
 * Los errores se lanzan como SQLException para que el Servlet decida qué mostrar.
 */
public class ServicioDAO {

    // 1. INSERTAR un nuevo servicio
    public boolean insertar(Servicio servicio) throws SQLException {
        String sql = "INSERT INTO servicio (nombre_servicio, precio_servicio, duracion_servicio) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, servicio.getNombreServicio());
            ps.setDouble(2, servicio.getPrecioServicio());
            ps.setString(3, servicio.getDuracionServicio());
            return ps.executeUpdate() == 1;
        }
    }

    // 2. CONSULTAR todos los servicios
    public List<Servicio> consultarTodos() throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        String sql = "SELECT idservicio, nombre_servicio, precio_servicio, duracion_servicio "
                + "FROM servicio ORDER BY nombre_servicio";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // 3. CONSULTAR un servicio por su id (para el formulario de edición)
    public Servicio consultarPorId(int idServicio) throws SQLException {
        String sql = "SELECT idservicio, nombre_servicio, precio_servicio, duracion_servicio "
                + "FROM servicio WHERE idservicio = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idServicio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    // 4. ACTUALIZAR un servicio existente
    public boolean actualizar(Servicio servicio) throws SQLException {
        String sql = "UPDATE servicio SET nombre_servicio = ?, precio_servicio = ?, duracion_servicio = ? "
                + "WHERE idservicio = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, servicio.getNombreServicio());
            ps.setDouble(2, servicio.getPrecioServicio());
            ps.setString(3, servicio.getDuracionServicio());
            ps.setInt(4, servicio.getIdServicio());
            return ps.executeUpdate() == 1;
        }
    }

    // 5. ELIMINAR un servicio por su id
    public boolean eliminar(int idServicio) throws SQLException {
        String sql = "DELETE FROM servicio WHERE idservicio = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idServicio);
            return ps.executeUpdate() == 1;
        }
    }

    // Convierte una fila del ResultSet en un objeto Servicio
    private Servicio mapear(ResultSet rs) throws SQLException {
        return new Servicio(
                rs.getInt("idservicio"),
                rs.getString("nombre_servicio"),
                rs.getDouble("precio_servicio"),
                rs.getString("duracion_servicio"));
    }
}
