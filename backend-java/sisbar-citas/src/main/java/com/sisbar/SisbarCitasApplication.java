package com.sisbar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del módulo de Citas de SISBAR.
 *
 * <p>La anotación {@code @SpringBootApplication} activa la configuración automática
 * de Spring Boot: levanta el servidor Tomcat, conecta con MySQL y registra
 * todos los controladores, servicios y repositorios del paquete {@code com.sisbar}.</p>
 *
 * <p>Para ejecutar: {@code mvn spring-boot:run} y abrir
 * http://localhost:8081/sisbar-citas/</p>
 */
@SpringBootApplication
public class SisbarCitasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SisbarCitasApplication.class, args);
    }
}
