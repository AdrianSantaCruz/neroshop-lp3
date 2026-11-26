package com.neroshop.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Utilidades para no guardar las contrasenas en texto plano.
 * Se guarda el hash SHA-256 de la contrasena junto con una "sal" fija.
 */
public final class Seguridad {

    private static final String SAL = "NeroShop#2026";

    private Seguridad() {
    }

    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest((SAL + password).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }

    public static boolean verificar(String password, String hashGuardado) {
        return hash(password).equalsIgnoreCase(hashGuardado);
    }
}
