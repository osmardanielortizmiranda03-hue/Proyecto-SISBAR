package com.sisbar.util;

import com.sisbar.modelo.Servicio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServicioTest {

    @Test
    void calculaDuracionYPrecioParaLaVista() {
        Servicio s = new Servicio(1, "Corte Clásico", 25000.0, "00:45:00");
        assertEquals(45, s.getDuracionMinutos());
        assertEquals("25000", s.getPrecioTexto());
        s.setPrecioServicio(18500.5);
        assertEquals("18500.5", s.getPrecioTexto());
    }
}
