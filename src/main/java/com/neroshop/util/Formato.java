package com.neroshop.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Formatos de texto usados en las vistas. */
public final class Formato {

    /** Cambia aqui el simbolo de moneda (por ejemplo "S/ " o "$ "). */
    public static final String MONEDA = "S/ ";

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Formato() {
    }

    public static String moneda(double monto) {
        return MONEDA + String.format("%,.2f", monto);
    }

    public static String fecha(LocalDateTime fecha) {
        return fecha == null ? "" : fecha.format(FECHA);
    }
}
