-- =====================================================================
-- SISBAR - Script de base de datos para el módulo web (Servlets + JSP)
-- Ejecutar en MySQL Workbench o en consola:  mysql -u root -p < sisbar.sql
-- Si ya tienes las tablas creadas, "IF NOT EXISTS" evita borrarlas.
-- =====================================================================

CREATE DATABASE IF NOT EXISTS sisbar CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sisbar;
SET NAMES utf8mb4;

-- Tabla de servicios (la misma que usa el módulo sisbar-backend)
CREATE TABLE IF NOT EXISTS servicio (
    idservicio        INT AUTO_INCREMENT PRIMARY KEY,
    nombre_servicio   VARCHAR(100)   NOT NULL,
    precio_servicio   DECIMAL(10, 2) NOT NULL,
    duracion_servicio TIME           NOT NULL
);

-- Tabla de usuarios (misma estructura que ya existe en la base de datos sisbar)
CREATE TABLE IF NOT EXISTS usuario (
    idUSUARIO           INT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(45),
    apellido            VARCHAR(100),
    correo_usuario      VARCHAR(100),
    telefono_usuario    VARCHAR(20),
    fecha_de_nacimiento DATE,
    n_identidad         VARCHAR(45),
    nacionalidad        VARCHAR(45),
    fecha_registro      DATE,
    `contraseña`        VARCHAR(250)
);

-- Se agrega la columna "rol" (CLIENTE | BARBERO | ADMIN) solo si todavía no existe.
-- No borra ni cambia ningún dato que ya tengas en la tabla.
SET @existe_rol := (SELECT COUNT(*) FROM information_schema.COLUMNS
                    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'usuario' AND COLUMN_NAME = 'rol');
SET @sql_rol := IF(@existe_rol = 0,
                   'ALTER TABLE usuario ADD COLUMN rol VARCHAR(20) NOT NULL DEFAULT ''CLIENTE''',
                   'SELECT ''La columna rol ya existe'' AS mensaje');
PREPARE sentencia FROM @sql_rol;
EXECUTE sentencia;
DEALLOCATE PREPARE sentencia;

-- Usuario administrador de prueba (solo se crea si no existe)
--   correo:     admin@sisbar.com
--   contraseña: Admin12345   (se guarda cifrada con SHA-256)
INSERT INTO usuario (nombre, apellido, correo_usuario, telefono_usuario, fecha_de_nacimiento,
                     n_identidad, nacionalidad, fecha_registro, `contraseña`, rol)
SELECT 'Administrador', 'SISBAR', 'admin@sisbar.com', '3000000000', '2000-01-01',
       '1000000000', 'Colombiana', CURDATE(),
       '8122cba12b897aa5546baf90b6c82c9f646f976b3555033cbc5e0b72d4f7a5bc', 'ADMIN'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE correo_usuario = 'admin@sisbar.com');

-- Servicios de ejemplo (solo si la tabla está vacía)
INSERT INTO servicio (nombre_servicio, precio_servicio, duracion_servicio)
SELECT * FROM (
    SELECT 'Corte Clásico' AS n, 25000 AS p, '00:45:00' AS d UNION ALL
    SELECT 'Corte Premium', 35000, '01:00:00' UNION ALL
    SELECT 'Arreglo de Barba', 15000, '00:30:00' UNION ALL
    SELECT 'Tratamiento Capilar', 40000, '00:50:00'
) AS datos
WHERE NOT EXISTS (SELECT 1 FROM servicio);
