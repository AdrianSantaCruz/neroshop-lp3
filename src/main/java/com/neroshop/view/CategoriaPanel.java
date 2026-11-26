package com.neroshop.view;

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

/** Vista del mantenimiento de categorias. */
public class CategoriaPanel extends PanelBase {

    private final DefaultTableModel modelo = Estilos.modeloTabla(
            new String[]{"ID", "Nombre", "Descripcion"},
            new Class<?>[]{Integer.class, String.class, String.class});
    private final JTable tabla = Estilos.tabla(modelo);

    private final JLabel lblModo = Estilos.titulo("Nueva categoria");
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDescripcion = new JTextField();

    private final JButton btnNuevo = Estilos.boton("Nuevo", false);
    private final JButton btnGuardar = Estilos.boton("Guardar", true);
    private final JButton btnEliminar = Estilos.boton("Eliminar", false);

    public CategoriaPanel() {
        JPanel listado = new JPanel(new BorderLayout(0, 10));
        listado.add(Estilos.titulo("Categorias"), BorderLayout.NORTH);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        listado.add(Estilos.scroll(tabla), BorderLayout.CENTER);
        add(listado, BorderLayout.CENTER);

        JPanel form = new JPanel(new BorderLayout(0, 12));
        form.setPreferredSize(new Dimension(340, 0));
        form.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        form.add(lblModo, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridBagLayout());
        Estilos.agregarCampo(campos, 0, "Nombre", txtNombre);
        Estilos.agregarCampo(campos, 1, "Descripcion", txtDescripcion);
        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(campos, BorderLayout.NORTH);
        form.add(arriba, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(1, 3, 8, 0));
        botones.add(btnNuevo);
        botones.add(btnEliminar);
        botones.add(btnGuardar);
        form.add(botones, BorderLayout.SOUTH);
        add(form, BorderLayout.EAST);
    }

    public void setModoEdicion(boolean editando) {
        lblModo.setText(editando ? "Editar categoria" : "Nueva categoria");
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
