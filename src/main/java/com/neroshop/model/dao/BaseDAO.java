package com.neroshop.model.dao;

import com.neroshop.config.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;

/** Clase base de los DAO: entrega la conexion compartida. */
public abstract class BaseDAO {

    protected Connection conexion() {
        try {
            return ConexionBD.getInstancia().getConexion();
        } catch (SQLException e) {
            throw new DAOException("No se pudo conectar a la base de datos: " + e.getMessage(), e);
        }
    }

    /** Convierte texto vacio en null para guardarlo en la base de datos. */
    protected static String vacioANull(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }
}
