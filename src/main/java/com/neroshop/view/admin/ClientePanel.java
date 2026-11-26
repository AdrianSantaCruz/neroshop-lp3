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
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

/** Mantenimiento de clientes. */
public class ClientePanel extends PanelBase {

    private final JTextField txtBuscar = Estilos.alto(new JTextField(22));
    private final DefaultTableModel modelo = Estilos.modeloTabla(
            new String[]{"ID", "Documento", "Nombres", "Apellidos", "Email", "Teléfono", "Cuenta"},
            new Class<?>[]{Integer.class, String.class, String.class, String.class, String.class, String.class, String.class});
    private final JTable tabla = Estilos.tabla(modelo);

    private final JLabel lblModo = Estilos.lblTitulo("Nuevo cliente", 20);
    private final JTextField txtDocumento = Estilos.alto(new JTextField());
    private final JTextField txtNombres = Estilos.alto(new JTextField());
    private final JTextField txtApellidos = Estilos.alto(new JTextField());
    private final JTextField txtEmail = Estilos.alto(new JTextField());
    private final JTextField txtTelefono = Estilos.alto(new JTextField());
    private final JTextField txtDireccion = Estilos.alto(new JTextField());

    private final JButton btnNuevo = Estilos.botonSecundario("Nuevo");
    private final JButton btnGuardar = Estilos.botonPrimario("Guardar");
    private final JButton btnEliminar = Estilos.botonSecundario("Eliminar");

    public ClientePanel() {
        JPanel listado = new JPanel(new BorderLayout(0, 14));
        listado.setOpaque(false);
        JPanel filtros = new JPanel(new BorderLayout());
        filtros.setOpaque(false);
        Estilos.placeholder(txtBuscar, "Buscar por nombre o documento...");
        filtros.add(Estilos.lblTitulo("Clientes", 24), BorderLayout.WEST);
        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        derecha.setOpaque(false);
        derecha.add(txtBuscar);
        filtros.add(derecha, BorderLayout.EAST);
        listado.add(filtros, BorderLayout.NORTH);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(6).setMaxWidth(90);
        listado.add(Estilos.scroll(tabla), BorderLayout.CENTER);
        add(listado, BorderLayout.CENTER);

        JPanel form = new JPanel(new BorderLayout(0, 16));
        form.setOpaque(false);
        form.setPreferredSize(new Dimension(360, 0));
        form.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        form.add(lblModo, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        Estilos.agregarCampo(campos, 0, "Documento", txtDocumento);
        Estilos.agregarCampo(campos, 1, "Nombres", txtNombres);
        Estilos.agregarCampo(campos, 2, "Apellidos", txtApellidos);
        Estilos.agregarCampo(campos, 3, "Email", txtEmail);
        Estilos.agregarCampo(campos, 4, "Teléfono", txtTelefono);
        Estilos.agregarCampo(campos, 5, "Dirección", txtDireccion);
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
        lblModo.setText(editando ? "EDITAR CLIENTE" : "NUEVO CLIENTE");
        btnEliminar.setEnabled(editando);
    }

    public void limpiarFormulario() {
        for (JTextField t : new JTextField[]{txtDocumento, txtNombres, txtApellidos, txtEmail, txtTelefono, txtDireccion}) {
            t.setText("");
        }
        tabla.clearSelection();
        setModoEdicion(false);
    }

    public JTextField getTxtBuscar() { return txtBuscar; }
    public DefaultTableModel getModelo() { return modelo; }
    public JTable getTabla() { return tabla; }
    public JTextField getTxtDocumento() { return txtDocumento; }
    public JTextField getTxtNombres() { return txtNombres; }
    public JTextField getTxtApellidos() { return txtApellidos; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JTextField getTxtTelefono() { return txtTelefono; }
    public JTextField getTxtDireccion() { return txtDireccion; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnEliminar() { return btnEliminar; }
}
