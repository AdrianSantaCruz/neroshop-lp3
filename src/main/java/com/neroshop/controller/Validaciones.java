package com.neroshop.controller;

import java.util.regex.Pattern;

/** Validaciones de datos de entrada compartidas por varios controladores. */
public final class Validaciones {

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern DOCUMENTO = Pattern.compile("^[0-9A-Za-z]{6,15}$");

    private Validaciones() {
    }

    /** Devuelve el mensaje de error, o null si los datos del cliente son validos. */
    public static String cliente(String documento, String nombres, String apellidos, String email) {
        if (nombres.isEmpty() || apellidos.isEmpty()) {
            return "Nombres y apellidos son obligatorios.";
        }
        if (!DOCUMENTO.matcher(documento).matches()) {
            return "El documento debe tener entre 6 y 15 caracteres (solo letras y números).";
        }
        if (!email.isEmpty() && !EMAIL.matcher(email).matches()) {
            return "El email no tiene un formato válido.";
        }
        return null;
    }
}
