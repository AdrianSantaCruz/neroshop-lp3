package com.neroshop.view.admin;

import com.neroshop.view.Estilos;
import com.neroshop.view.PanelBase;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

/** Mantenimiento de categorias. */
public class CategoriaPanel extends PanelBase {

    private final DefaultTableModel modelo = Estilos.modeloTabla(
            new String[]{"ID", "Nombre", "Descripción"},
            new Class<?>[]{Integer.class, String.class, String.class});
    private final JTable tabla = Estilos.tabla(modelo);

    private final JLabel lblModo = Estilos.lblTitulo("Nueva categoría", 20);
    private final JTextField txtNombre = Estilos.alto(new JTextField());
    private final JTextField txtDescripcion = Estilos.alto(new JTextField());

    private final JButton btnNuevo = Estilos.botonSecundario("Nuevo");
    private final JButton btnGuardar = Estilos.botonPrimario("Guardar");
    private final JButton btnEliminar = Estilos.botonSecundario("Eliminar");

    public CategoriaPanel() {
        JPanel listado = new JPanel(new BorderLayout(0, 14));
        listado.setOpaque(false);
        listado.add(Estilos.lblTitulo("Categorías", 24), BorderLayout.NORTH);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        listado.add(Estilos.scroll(tabla), BorderLayout.CENTER);
        add(listado, BorderLayout.CENTER);

        JPanel form = new JPanel(new BorderLayout(0, 16));
        form.setOpaque(false);
        form.setPreferredSize(new Dimension(360, 0));
        form.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        form.add(lblModo, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        Estilos.agregarCampo(campos, 0, "Nombre", txtNombre);
        Estilos.agregarCampo(campos, 1, "Descripción", txtDescripcion);
        JPanel arriba = new JPanel(new BorderLayout());
        arriba.setOpaque(false);
        arriba.add(campos, BorderLayout.NORTH);
        form.add(arriba, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(1, 3, 8, 0));
        botones.setOpaque(false);
        botones.add(btnNuevo);
        botones.add(btnEliminar);
        botones.add(btnGuardar);
        form.add(botones, BorderLayout.SOUTH);
        add(form, BorderLayout.EAST);
    }

    public void setModoEdicion(boolean editando) {
        lblModo.setText(editando ? "EDITAR CATEGORÍA" : "NUEVA CATEGORÍA");
        btnEliminar.setEnabled(editando);
    }

    public void limpiarFormulario() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        tabla.clearSelection();
        setModoEdicion(false);
    }

    public DefaultTableModel getModelo() { return modelo; }
    public JTable getTabla() { return tabla; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtDescripcion() { return txtDescripcion; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnEliminar() { return btnEliminar; }
}
