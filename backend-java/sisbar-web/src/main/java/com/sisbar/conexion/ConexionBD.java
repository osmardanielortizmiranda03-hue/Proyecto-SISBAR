package com.sisbar.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase encargada de abrir la conexión con la base de datos MySQL "sisbar".
 * Los datos (url, usuario, contraseña) se leen del archivo db.properties
 * para no tenerlos "quemados" dentro del código.
 */
public class ConexionBD {

    private static final Properties CONFIG = new Properties();

    // Bloque estático: se ejecuta una sola vez cuando se carga la clase
    static {
        try (InputStream in = ConexionBD.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                CONFIG.load(in);
            }
            // En aplicaciones web hay que registrar el driver de forma explícita
            Class.forName(CONFIG.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError("No se pudo configurar la conexión: " + e.getMessage());
        }
    }

    private ConexionBD() {
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(
                CONFIG.getProperty("db.url", "jdbc:mysql://localhost:3306/sisbar"),
                CONFIG.getProperty("db.usuario", "root"),
                CONFIG.getProperty("db.contrasena", ""));
    }
}
