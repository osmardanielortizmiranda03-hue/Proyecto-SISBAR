-- =====================================================================
--  SISBAR - Script del módulo de CITAS (Spring Boot)
--  Evidencia GA7-220501096-AA3-EV01
--
--  Ejecutar UNA vez en MySQL Workbench (File > Open SQL Script > rayo).
--  Es seguro ejecutarlo varias veces: no borra datos.
--
--  Qué hace:
--   1. Agrega a la tabla "citas" las columnas que pide el prototipo
--      (servicio elegido y observaciones), solo si no existen.
--   2. Crea 2 barberos de prueba (los mismos del prototipo agendarcita.html).
--   3. Crea un cliente de prueba para agendar citas.
-- =====================================================================

USE sisbar;
SET NAMES utf8mb4;

-- ---------------------------------------------------------------------
-- 1. Columna "idservicio" en citas (qué servicio se agendó)
-- ---------------------------------------------------------------------
SET @existe := (SELECT COUNT(*) FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'citas' AND COLUMN_NAME = 'idservicio');
SET @sql := IF(@existe = 0,
    'ALTER TABLE citas ADD COLUMN idservicio INT NULL AFTER CLIENTE_idUSUARIO,
         ADD KEY fk_cita_servicio_idx (idservicio),
         ADD CONSTRAINT fk_cita_servicio FOREIGN KEY (idservicio) REFERENCES servicio (idservicio)
             ON DELETE SET NULL ON UPDATE CASCADE',
    'SELECT ''La columna idservicio ya existe'' AS mensaje');
PREPARE sentencia FROM @sql;
EXECUTE sentencia;
DEALLOCATE PREPARE sentencia;

-- ---------------------------------------------------------------------
-- 2. Columna "observaciones" en citas (paso 5 del prototipo)
-- ---------------------------------------------------------------------
SET @existe := (SELECT COUNT(*) FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'citas' AND COLUMN_NAME = 'observaciones');
SET @sql := IF(@existe = 0,
    'ALTER TABLE citas ADD COLUMN observaciones VARCHAR(255) NULL AFTER fecha_cita',
    'SELECT ''La columna observaciones ya existe'' AS mensaje');
PREPARE sentencia FROM @sql;
EXECUTE sentencia;
DEALLOCATE PREPARE sentencia;

-- ---------------------------------------------------------------------
-- 3. Barberos de prueba (contraseña de ambos: Barbero123)
--    Hash SHA-256 de "Barbero123"
-- ---------------------------------------------------------------------
INSERT INTO usuario (nombre, apellido, correo_usuario, telefono_usuario, fecha_de_nacimiento,
                     n_identidad, nacionalidad, fecha_registro, `contraseña`, rol)
SELECT 'Carlos', 'Ramírez', 'carlos.barbero@sisbar.com', '3001112233', '1995-04-12',
       '1100000001', 'Colombiana', CURDATE(),
       '169ed8fc682764ab9a8e677a59dd8b28825d90a356a31d757fc4f24c9edd944f', 'BARBERO'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE correo_usuario = 'carlos.barbero@sisbar.com');

INSERT INTO usuario (nombre, apellido, correo_usuario, telefono_usuario, fecha_de_nacimiento,
                     n_identidad, nacionalidad, fecha_registro, `contraseña`, rol)
SELECT 'Rodrigo', 'Salinas', 'rodrigo.barbero@sisbar.com', '3004445566', '1992-09-30',
       '1100000002', 'Colombiana', CURDATE(),
       '169ed8fc682764ab9a8e677a59dd8b28825d90a356a31d757fc4f24c9edd944f', 'BARBERO'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE correo_usuario = 'rodrigo.barbero@sisbar.com');

INSERT INTO barbero (idUSUARIO, estado_barbero, especializacion_barbero, comision)
SELECT idUSUARIO, 'ACTIVO', 'Cortes clásicos y degradados', 30.00
FROM usuario WHERE correo_usuario = 'carlos.barbero@sisbar.com'
  AND idUSUARIO NOT IN (SELECT idUSUARIO FROM barbero);

INSERT INTO barbero (idUSUARIO, estado_barbero, especializacion_barbero, comision)
SELECT idUSUARIO, 'ACTIVO', 'Barba y diseño', 30.00
FROM usuario WHERE correo_usuario = 'rodrigo.barbero@sisbar.com'
  AND idUSUARIO NOT IN (SELECT idUSUARIO FROM barbero);

-- ---------------------------------------------------------------------
-- 4. Cliente de prueba (correo: cliente@sisbar.com / contraseña: Cliente123)
-- ---------------------------------------------------------------------
INSERT INTO usuario (nombre, apellido, correo_usuario, telefono_usuario, fecha_de_nacimiento,
                     n_identidad, nacionalidad, fecha_registro, `contraseña`, rol)
SELECT 'Andrés', 'Pérez', 'cliente@sisbar.com', '3007778899', '2001-02-20',
       '1100000003', 'Colombiana', CURDATE(),
       '34e422278ea745b5d87ba6592f0ea3fe32a2eb7593f5960ac72d7094fb121f3d', 'CLIENTE'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE correo_usuario = 'cliente@sisbar.com');

INSERT INTO cliente (idUSUARIO, preferencia_cliente)
SELECT idUSUARIO, 'Degradado bajo'
FROM usuario WHERE correo_usuario = 'cliente@sisbar.com'
  AND idUSUARIO NOT IN (SELECT idUSUARIO FROM cliente);

-- Verificación
SELECT u.idUSUARIO, u.nombre, u.apellido, u.correo_usuario, u.rol
FROM usuario u ORDER BY u.idUSUARIO;
