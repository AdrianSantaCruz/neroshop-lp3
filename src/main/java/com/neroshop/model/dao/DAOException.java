package com.neroshop.model.dao;

/** Error de acceso a datos con un mensaje entendible para el usuario. */
public class DAOException extends RuntimeException {

    public DAOException(String mensaje) {
        super(mensaje);
    }

    public DAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
