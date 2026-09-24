-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: sisbar
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrador`
--

DROP TABLE IF EXISTS `administrador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrador` (
  `idUSUARIO` int NOT NULL,
  `nivel_permiso` varchar(100) NOT NULL,
  PRIMARY KEY (`idUSUARIO`),
  CONSTRAINT `fk_administrador_usuario` FOREIGN KEY (`idUSUARIO`) REFERENCES `usuario` (`idUSUARIO`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `barbero`
--

DROP TABLE IF EXISTS `barbero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `barbero` (
  `idUSUARIO` int NOT NULL,
  `estado_barbero` varchar(50) NOT NULL,
  `especializacion_barbero` varchar(45) NOT NULL,
  `comision` decimal(10,2) NOT NULL,
  PRIMARY KEY (`idUSUARIO`),
  CONSTRAINT `fk_barbero_usuario` FOREIGN KEY (`idUSUARIO`) REFERENCES `usuario` (`idUSUARIO`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `citas`
--

DROP TABLE IF EXISTS `citas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `citas` (
  `idcitas` int NOT NULL AUTO_INCREMENT,
  `BARBERO_idUSUARIO` int NOT NULL,
  `CLIENTE_idUSUARIO` int NOT NULL,
  `estado_cita` varchar(100) NOT NULL,
  `fecha_cita` datetime NOT NULL,
  PRIMARY KEY (`idcitas`),
  KEY `fk_cita_barbero_idx` (`BARBERO_idUSUARIO`),
  KEY `fk_cita_cliente_idx` (`CLIENTE_idUSUARIO`),
  CONSTRAINT `fk_cita_barbero` FOREIGN KEY (`BARBERO_idUSUARIO`) REFERENCES `barbero` (`idUSUARIO`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cita_cliente` FOREIGN KEY (`CLIENTE_idUSUARIO`) REFERENCES `cliente` (`idUSUARIO`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `idUSUARIO` int NOT NULL,
  `preferencia_cliente` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idUSUARIO`),
  KEY `fk_cliente_usuario_idx` (`idUSUARIO`),
  CONSTRAINT `fk_cliente_usuario` FOREIGN KEY (`idUSUARIO`) REFERENCES `usuario` (`idUSUARIO`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `detalle_compra`
--

DROP TABLE IF EXISTS `detalle_compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_compra` (
  `iddetalle_compra` int NOT NULL AUTO_INCREMENT,
  `idproveedor` int NOT NULL,
  `idproducto` int NOT NULL,
  `cantidad_comprada` int NOT NULL,
  `precio_compra` decimal(10,2) NOT NULL,
  PRIMARY KEY (`iddetalle_compra`),
  KEY `fk_detalle_compra_proveedor_idx` (`idproveedor`),
  KEY `fk_detalle_compra_producto_idx` (`idproducto`),
  CONSTRAINT `fk_detalle_compra_producto` FOREIGN KEY (`idproducto`) REFERENCES `producto` (`idproducto`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_detalle_compra_proveedor` FOREIGN KEY (`idproveedor`) REFERENCES `proveedor` (`idproveedor`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `detalle_venta`
--

DROP TABLE IF EXISTS `detalle_venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_venta` (
  `iddetalle_venta` int NOT NULL AUTO_INCREMENT,
  `idfactura` int NOT NULL,
  `idservicio` int DEFAULT NULL,
  `idproducto` int DEFAULT NULL,
  `precio` decimal(12,2) NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`iddetalle_venta`),
  KEY `fk_detalle_venta_factura_idx` (`idfactura`),
  KEY `fk_detalle_venta_servicio_idx` (`idservicio`),
  KEY `fk_detalle_venta_producto_idx` (`idproducto`),
  CONSTRAINT `fk_detalle_venta_factura` FOREIGN KEY (`idfactura`) REFERENCES `factura` (`idfactura`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_detalle_venta_producto` FOREIGN KEY (`idproducto`) REFERENCES `producto` (`idproducto`),
  CONSTRAINT `fk_detalle_venta_servicio` FOREIGN KEY (`idservicio`) REFERENCES `servicio` (`idservicio`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `factura`
--

DROP TABLE IF EXISTS `factura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `factura` (
  `idfactura` int NOT NULL AUTO_INCREMENT,
  `id_metodo_de_pago` int NOT NULL,
  `idcitas` int NOT NULL,
  `iva` decimal(10,2) NOT NULL,
  `sub_total` decimal(10,2) NOT NULL,
  `fecha_emision` datetime NOT NULL,
  PRIMARY KEY (`idfactura`),
  KEY `fk_facturas_cita_idx` (`idcitas`),
  KEY `fk_factura_metodo_de_pago_idx` (`id_metodo_de_pago`),
  CONSTRAINT `fk_factura_cita` FOREIGN KEY (`idcitas`) REFERENCES `citas` (`idcitas`),
  CONSTRAINT `fk_factura_metodo_de_pago` FOREIGN KEY (`id_metodo_de_pago`) REFERENCES `metodo_de_pago` (`idmetodo_de_pago`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `metodo_de_pago`
--

DROP TABLE IF EXISTS `metodo_de_pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `metodo_de_pago` (
  `idmetodo_de_pago` int NOT NULL AUTO_INCREMENT,
  `nombre_metodo_pago` varchar(45) NOT NULL,
  `estado_metodo_pago` varchar(45) NOT NULL,
  PRIMARY KEY (`idmetodo_de_pago`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `idproducto` int NOT NULL AUTO_INCREMENT,
  `nombre_producto` varchar(100) NOT NULL,
  `marca_producto` varchar(100) NOT NULL,
  `precio_venta` decimal(10,2) NOT NULL,
  `stock` int NOT NULL,
  PRIMARY KEY (`idproducto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `proveedor`
--

DROP TABLE IF EXISTS `proveedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedor` (
  `idproveedor` int NOT NULL AUTO_INCREMENT,
  `nit_proveedor` varchar(45) NOT NULL,
  `nombre_empresa` varchar(45) NOT NULL,
  `telefono_proveedor` varchar(20) NOT NULL,
  `correo_proveedor` varchar(200) NOT NULL,
  PRIMARY KEY (`idproveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `servicio`
--

DROP TABLE IF EXISTS `servicio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servicio` (
  `idservicio` int NOT NULL AUTO_INCREMENT,
  `nombre_servicio` varchar(100) NOT NULL,
  `precio_servicio` decimal(10,2) NOT NULL,
  `duracion_servicio` time NOT NULL,
  PRIMARY KEY (`idservicio`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idUSUARIO` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `correo_usuario` varchar(100) NOT NULL,
  `telefono_usuario` varchar(20) NOT NULL,
  `fecha_de_nacimiento` date NOT NULL,
  `n_identidad` varchar(45) NOT NULL,
  `nacionalidad` varchar(45) NOT NULL,
  `fecha_registro` date NOT NULL,
  `contraseña` varchar(250) NOT NULL,
  `rol` varchar(20) NOT NULL DEFAULT 'CLIENTE',
  PRIMARY KEY (`idUSUARIO`),
  UNIQUE KEY `n_identidad_UNIQUE` (`n_identidad`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-23 18:26:52
