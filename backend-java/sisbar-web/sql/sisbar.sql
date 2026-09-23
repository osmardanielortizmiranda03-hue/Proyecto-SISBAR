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

-- Tabla de usuarios (registro e inicio de sesión)
CREATE TABLE IF NOT EXISTS usuario (
    idusuario         INT AUTO_INCREMENT PRIMARY KEY,
    nombres           VARCHAR(80)  NOT NULL,
    apellidos         VARCHAR(80)  NOT NULL,
    numero_identidad  VARCHAR(20)  NOT NULL UNIQUE,
    celular           VARCHAR(20)  NOT NULL,
    email             VARCHAR(120) NOT NULL UNIQUE,
    fecha_nacimiento  DATE         NOT NULL,
    nacionalidad      VARCHAR(60),
    password_hash     CHAR(64)     NOT NULL,           -- SHA-256 en hexadecimal
    rol               VARCHAR(20)  NOT NULL DEFAULT 'CLIENTE',  -- CLIENTE | BARBERO | ADMIN
    fecha_registro    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Usuario administrador de prueba
--   correo:     admin@sisbar.com
--   contraseña: Admin12345
INSERT IGNORE INTO usuario (nombres, apellidos, numero_identidad, celular, email, fecha_nacimiento,
                            nacionalidad, password_hash, rol)
VALUES ('Administrador', 'SISBAR', '1000000000', '3000000000', 'admin@sisbar.com', '2000-01-01',
        'Colombiana', '8122cba12b897aa5546baf90b6c82c9f646f976b3555033cbc5e0b72d4f7a5bc', 'ADMIN');

-- Servicios de ejemplo (solo si la tabla está vacía)
INSERT INTO servicio (nombre_servicio, precio_servicio, duracion_servicio)
SELECT * FROM (
    SELECT 'Corte Clásico' AS n, 25000 AS p, '00:45:00' AS d UNION ALL
    SELECT 'Corte Premium', 35000, '01:00:00' UNION ALL
    SELECT 'Arreglo de Barba', 15000, '00:30:00' UNION ALL
    SELECT 'Tratamiento Capilar', 40000, '00:50:00'
) AS datos
WHERE NOT EXISTS (SELECT 1 FROM servicio);
