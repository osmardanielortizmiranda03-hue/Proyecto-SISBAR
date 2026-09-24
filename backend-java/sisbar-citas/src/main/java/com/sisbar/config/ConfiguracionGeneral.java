package com.sisbar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Beans generales de la aplicación.
 */
@Configuration
public class ConfiguracionGeneral {

    /**
     * Reloj con la zona horaria de Colombia. Se usa en vez de LocalDate.now()
     * directo para que las pruebas puedan simular cualquier fecha.
     */
    @Bean
    public Clock reloj() {
        return Clock.system(ZoneId.of("America/Bogota"));
    }
}
