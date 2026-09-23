package com.sisbar.dao;

import com.sisbar.conexion.ConexionBD;
import com.sisbar.modelo.Servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {

    // 1. INSERTAR un nuevo servicio
    public void insertar(Servicio servicio) {
        String sql = "INSERT INTO servicio (nombre_servicio, precio_servicio, duracion_servicio) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, servicio.getNombreServicio());
            ps.setDouble(2, servicio.getPrecioServicio());
            ps.setString(3, servicio.getDuracionServicio());

            ps.executeUpdate();
            System.out.println("Servicio insertado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al insertar servicio: " + e.getMessage());
        }
    }

    // 2. CONSULTAR todos los servicios
    public List<Servicio> consultarTodos() {
        List<Servicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM servicio";

        try (Connection con = ConexionBD.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Servicio servicio = new Servicio(
                        rs.getInt("idservicio"),
                        rs.getString("nombre_servicio"),
                        rs.getDouble("precio_servicio"),
                        rs.getString("duracion_servicio")
                );
                lista.add(servicio);
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar servicios: " + e.getMessage());
        }

        return lista;
    }

    // 3. ACTUALIZAR un servicio existente
    public void actualizar(Servicio servicio) {
        String sql = "UPDATE servicio SET nombre_servicio = ?, precio_servicio = ?, duracion_servicio = ? WHERE idservicio = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, servicio.getNombreServicio());
            ps.setDouble(2, servicio.getPrecioServicio());
            ps.setString(3, servicio.getDuracionServicio());
            ps.setInt(4, servicio.getIdServicio());

            ps.executeUpdate();
            System.out.println("Servicio actualizado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al actualizar servicio: " + e.getMessage());
        }
    }

    // 4. ELIMINAR un servicio por su id
    public void eliminar(int idServicio) {
        String sql = "DELETE FROM servicio WHERE idservicio = ?";

        try (Connection con = ConexionBD.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idServicio);
            ps.executeUpdate();
            System.out.println("Servicio eliminado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al eliminar servicio: " + e.getMessage());
        }
    }
}