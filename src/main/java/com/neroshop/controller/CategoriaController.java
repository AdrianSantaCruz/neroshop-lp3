package com.neroshop.controller;

import com.neroshop.model.Categoria;
import com.neroshop.model.dao.CategoriaDAO;
import com.neroshop.model.dao.DAOException;
import com.neroshop.view.admin.CategoriaPanel;

import java.util.ArrayList;
import java.util.List;

public class CategoriaController implements Refrescable {

    private final CategoriaPanel vista;
    private final CategoriaDAO dao;
    private List<Categoria> categorias = new ArrayList<>();
    private Categoria seleccionada;

    public CategoriaController(CategoriaPanel vista, CategoriaDAO dao) {
        this.vista = vista;
        this.dao = dao;
        vista.getBtnNuevo().addActionListener(e -> limpiar());
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionar();
            }
        });
        refrescar();
        limpiar();
    }

    @Override
    public void refrescar() {
        try {
            categorias = dao.listar();
            vista.getModelo().setRowCount(0);
            for (Categoria c : categorias) {
                vista.getModelo().addRow(new Object[]{c.getId(), c.getNombre(), c.getDescripcion()});
            }
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void seleccionar() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila < 0) {
            return;
        }
        seleccionada = categorias.get(vista.getTabla().convertRowIndexToModel(fila));
        vista.getTxtNombre().setText(seleccionada.getNombre());
        vista.getTxtDescripcion().setText(seleccionada.getDescripcion() == null ? "" : seleccionada.getDescripcion());
        vista.setModoEdicion(true);
    }

    private void limpiar() {
        seleccionada = null;
        vista.limpiarFormulario();
    }

    private void guardar() {
        String nombre = vista.getTxtNombre().getText().trim();
        if (nombre.isEmpty()) {
            vista.mostrarError("El nombre de la categoria es obligatorio.");
            return;
        }
        Categoria c = new Categoria(seleccionada == null ? 0 : seleccionada.getId(), nombre,
                vista.getTxtDescripcion().getText().trim());
        try {
            if (seleccionada == null) {
                dao.insertar(c);
                vista.mostrarMensaje("Categoria registrada correctamente.");
            } else {
                dao.actualizar(c);
                vista.mostrarMensaje("Categoria actualizada correctamente.");
            }
            refrescar();
            limpiar();
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionada == null) {
            return;
        }
        if (!vista.confirmar("¿Eliminar la categoria \"" + seleccionada.getNombre() + "\"?")) {
            return;
        }
        try {
            dao.eliminar(seleccionada.getId());
            refrescar();
            limpiar();
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }
}
