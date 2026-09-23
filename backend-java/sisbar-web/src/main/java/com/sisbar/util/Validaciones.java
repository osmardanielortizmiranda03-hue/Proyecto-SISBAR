package com.sisbar.util;

import java.util.regex.Pattern;

/**
 * Métodos de apoyo para validar los datos que llegan desde los formularios HTML.
 * No dependen de la base de datos, por eso se pueden probar con JUnit.
 */
public final class Validaciones {

    private static final Pattern EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[A-Za-z]{2,}$");
    private static final Pattern SOLO_DIGITOS = Pattern.compile("^\\d{6,15}$");

    private Validaciones() {
    }

    /** true si el texto es null o está vacío (o solo tiene espacios) */
    public static boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public static boolean esEmailValido(String email) {
        return !estaVacio(email) && EMAIL.matcher(email.trim()).matches();
    }

    /** La contraseña debe tener mínimo 8 caracteres, al menos una letra y un número */
    public static boolean esPasswordValida(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean tieneLetra = password.chars().anyMatch(Character::isLetter);
        boolean tieneNumero = password.chars().anyMatch(Character::isDigit);
        return tieneLetra && tieneNumero;
    }

    /** Documento o celular: solo dígitos, entre 6 y 15 */
    public static boolean esNumeroDocumentoValido(String numero) {
        return !estaVacio(numero) && SOLO_DIGITOS.matcher(numero.trim()).matches();
    }

    /**
     * Convierte un texto a entero positivo. Devuelve -1 si no es un número válido.
     */
    public static int aEnteroPositivo(String texto) {
        try {
            int valor = Integer.parseInt(texto.trim());
            return valor > 0 ? valor : -1;
        } catch (NumberFormatException | NullPointerException e) {
            return -1;
        }
    }

    /**
     * Convierte un texto a decimal positivo. Devuelve -1 si no es un número válido.
     */
    public static double aDecimalPositivo(String texto) {
        try {
            double valor = Double.parseDouble(texto.trim());
            return valor > 0 ? valor : -1;
        } catch (NumberFormatException | NullPointerException e) {
            return -1;
        }
    }

    /** 45 -> "00:45:00"  |  90 -> "01:30:00"  (formato TIME de MySQL) */
    public static String minutosAHora(int minutos) {
        if (minutos < 0) {
            throw new IllegalArgumentException("Los minutos no pueden ser negativos");
        }
        return String.format("%02d:%02d:00", minutos / 60, minutos % 60);
    }

    /** "01:30:00" -> 90. Si el formato no es válido devuelve 0 */
    public static int horaAMinutos(String hora) {
        if (estaVacio(hora)) {
            return 0;
        }
        String[] partes = hora.trim().split(":");
        try {
            int horas = Integer.parseInt(partes[0]);
            int minutos = partes.length > 1 ? Integer.parseInt(partes[1]) : 0;
            return horas * 60 + minutos;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
