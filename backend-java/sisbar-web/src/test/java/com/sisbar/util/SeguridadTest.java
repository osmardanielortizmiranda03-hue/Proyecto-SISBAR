package com.sisbar.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeguridadTest {

    @Test
    void elHashTiene64CaracteresYEsSiempreIgual() {
        String hash = Seguridad.hashSha256("Admin12345");
        assertEquals(64, hash.length());
        assertEquals("8122cba12b897aa5546baf90b6c82c9f646f976b3555033cbc5e0b72d4f7a5bc", hash);
    }

    @Test
    void comparaContrasenas() {
        String guardado = Seguridad.hashSha256("Sisbar2026");
        assertTrue(Seguridad.coincide("Sisbar2026", guardado));
        assertFalse(Seguridad.coincide("sisbar2026", guardado));
        assertFalse(Seguridad.coincide(null, guardado));
    }
}
