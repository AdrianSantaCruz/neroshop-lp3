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
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

/** Vista del mantenimiento de clientes. */
public class ClientePanel extends PanelBase {

    private final JTextField txtBuscar = new JTextField(22);
    private final DefaultTableModel modelo = Estilos.modeloTabla(
            new String[]{"ID", "Documento", "Nombres", "Apellidos", "Email", "Telefono"},
            new Class<?>[]{Integer.class, String.class, String.class, String.class, String.class, String.class});
    private final JTable tabla = Estilos.tabla(modelo);

    private final JLabel lblModo = Estilos.titulo("Nuevo cliente");
    private final JTextField txtDocumento = new JTextField();
    private final JTextField txtNombres = new JTextField();
    private final JTextField txtApellidos = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtDireccion = new JTextField();

    private final JButton btnNuevo = Estilos.boton("Nuevo", false);
    private final JButton btnGuardar = Estilos.boton("Guardar", true);
    private final JButton btnEliminar = Estilos.boton("Eliminar", false);

    public ClientePanel() {
        JPanel listado = new JPanel(new BorderLayout(0, 10));
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        Estilos.placeholder(txtBuscar, "Buscar por nombre o documento...");
        filtros.add(Estilos.titulo("Clientes   "));
        filtros.add(txtBuscar);
        listado.add(filtros, BorderLayout.NORTH);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        listado.add(Estilos.scroll(tabla), BorderLayout.CENTER);
        add(listado, BorderLayout.CENTER);

        JPanel form = new JPanel(new BorderLayout(0, 12));
        form.setPreferredSize(new Dimension(340, 0));
        form.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        form.add(lblModo, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridBagLayout());
        Estilos.agregarCampo(campos, 0, "Documento", txtDocumento);
        Estilos.agregarCampo(campos, 1, "Nombres", txtNombres);
        Estilos.agregarCampo(campos, 2, "Apellidos", txtApellidos);
        Estilos.agregarCampo(campos, 3, "Email", txtEmail);
        Estilos.agregarCampo(campos, 4, "Telefono", txtTelefono);
        Estilos.agregarCampo(campos, 5, "Direccion", txtDireccion);
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
        lblModo.setText(editando ? "Editar cliente" : "Nuevo cliente");
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
