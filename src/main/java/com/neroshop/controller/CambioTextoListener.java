package com.neroshop.controller;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** Ejecuta una accion cada vez que cambia el texto de un campo (busqueda en vivo). */
public class CambioTextoListener implements DocumentListener {

    private final Runnable accion;

    public CambioTextoListener(Runnable accion) {
        this.accion = accion;
    }

    @Override
    public void insertUpdate(DocumentEvent e) { accion.run(); }

    @Override
    public void removeUpdate(DocumentEvent e) { accion.run(); }

    @Override
    public void changedUpdate(DocumentEvent e) { accion.run(); }
}
