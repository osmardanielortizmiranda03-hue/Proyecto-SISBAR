package com.sisbar.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Las contraseñas NUNCA se guardan en texto plano:
 * se guarda su "huella" (hash) SHA-256 en hexadecimal (64 caracteres).
 */
public final class Seguridad {

    private Seguridad() {
    }

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

    /** Compara la contraseña escrita por el usuario con el hash guardado en la BD */
    public static boolean coincide(String passwordPlano, String hashGuardado) {
        return passwordPlano != null && hashGuardado != null
                && hashSha256(passwordPlano).equalsIgnoreCase(hashGuardado);
    }
}
