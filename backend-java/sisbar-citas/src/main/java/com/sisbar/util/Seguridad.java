package com.sisbar.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para cifrar contraseñas con SHA-256.
 * Es el mismo método usado en el módulo web (Servlets), por eso
 * los usuarios pueden iniciar sesión en ambos módulos con la misma clave.
 */
public final class Seguridad {

    private Seguridad() {
        // Clase de utilidades: no se instancia
    }

    /** Devuelve el hash SHA-256 en hexadecimal (64 caracteres). */
    public static String hashSha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }

    /** Compara una contraseña escrita con el hash guardado en la base de datos. */
    public static boolean coincide(String contrasenaPlana, String hashGuardado) {
        return contrasenaPlana != null && hashGuardado != null
                && hashSha256(contrasenaPlana).equalsIgnoreCase(hashGuardado);
    }
}
