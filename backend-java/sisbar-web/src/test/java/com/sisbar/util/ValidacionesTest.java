package com.sisbar.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de las validaciones usadas por los servlets.
 * Ejecutar con:  mvn test
 */
class ValidacionesTest {

    @Test
    @DisplayName("Detecta textos vacíos")
    void estaVacio() {
        assertTrue(Validaciones.estaVacio(null));
        assertTrue(Validaciones.estaVacio(""));
        assertTrue(Validaciones.estaVacio("   "));
        assertFalse(Validaciones.estaVacio("Corte"));
    }

    @Test
    @DisplayName("Valida correos electrónicos")
    void email() {
        assertTrue(Validaciones.esEmailValido("carlos@ejemplo.com"));
        assertTrue(Validaciones.esEmailValido("osmar.ortiz@sena.edu.co"));
        assertFalse(Validaciones.esEmailValido("carlos@"));
        assertFalse(Validaciones.esEmailValido("carlos.com"));
        assertFalse(Validaciones.esEmailValido(null));
    }

    @Test
    @DisplayName("La contraseña exige 8 caracteres con letras y números")
    void password() {
        assertTrue(Validaciones.esPasswordValida("Sisbar2026"));
        assertFalse(Validaciones.esPasswordValida("corta1"));
        assertFalse(Validaciones.esPasswordValida("soloLetras"));
        assertFalse(Validaciones.esPasswordValida("12345678"));
        assertFalse(Validaciones.esPasswordValida(null));
    }

    @Test
    @DisplayName("Documento y celular solo aceptan dígitos")
    void documento() {
        assertTrue(Validaciones.esNumeroDocumentoValido("1234567801"));
        assertFalse(Validaciones.esNumeroDocumentoValido("12a456"));
        assertFalse(Validaciones.esNumeroDocumentoValido("123"));
    }

    @Test
    @DisplayName("Convierte números positivos y rechaza los inválidos")
    void numeros() {
        assertEquals(45, Validaciones.aEnteroPositivo("45"));
        assertEquals(-1, Validaciones.aEnteroPositivo("-5"));
        assertEquals(-1, Validaciones.aEnteroPositivo("abc"));
        assertEquals(-1, Validaciones.aEnteroPositivo(null));
        assertEquals(25000.0, Validaciones.aDecimalPositivo("25000"));
        assertEquals(-1, Validaciones.aDecimalPositivo("0"));
    }

    @Test
    @DisplayName("Convierte minutos <-> formato TIME de MySQL")
    void conversionDuracion() {
        assertEquals("00:45:00", Validaciones.minutosAHora(45));
        assertEquals("01:30:00", Validaciones.minutosAHora(90));
        assertEquals(90, Validaciones.horaAMinutos("01:30:00"));
        assertEquals(30, Validaciones.horaAMinutos("00:30:00"));
        assertEquals(0, Validaciones.horaAMinutos(null));
        assertThrows(IllegalArgumentException.class, () -> Validaciones.minutosAHora(-1));
    }
}
