package com.neroshop.view;

import com.neroshop.model.Categoria;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

/** Vista del mantenimiento (CRUD) de productos. */
public class ProductoPanel extends PanelBase {

    public static final String[] TALLAS = {"XS", "S", "M", "L", "XL", "XXL", "28", "30", "32", "34", "36", "Unica"};

    private final JTextField txtBuscar = new JTextField(18);
    private final JComboBox<Categoria> cboFiltroCategoria = new JComboBox<>();

    private final DefaultTableModel modelo = Estilos.modeloTabla(
            new String[]{"ID", "Nombre", "Categoria", "Talla", "Color", "Precio", "Stock"},
            new Class<?>[]{Integer.class, String.class, String.class, String.class, String.class, Double.class, Integer.class});
    private final JTable tabla = Estilos.tabla(modelo);

    private final JLabel lblModo = Estilos.titulo("Nuevo producto");
    private final JTextField txtNombre = new JTextField();
    private final JComboBox<Categoria> cboCategoria = new JComboBox<>();
    private final JComboBox<String> cboTalla = new JComboBox<>(TALLAS);
    private final JTextField txtColor = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JSpinner spnStock = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
    private final JTextArea txtDescripcion = new JTextArea(3, 20);

    private final JButton btnNuevo = Estilos.boton("Nuevo", false);
    private final JButton btnGuardar = Estilos.boton("Guardar", true);
    private final JButton btnEliminar = Estilos.boton("Eliminar", false);

    public ProductoPanel() {
        add(crearListado(), BorderLayout.CENTER);
        add(crearFormulario(), BorderLayout.EAST);
    }

    private JPanel crearListado() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        Estilos.placeholder(txtBuscar, "Buscar por nombre o color...");
        cboFiltroCategoria.setPreferredSize(new Dimension(190, cboFiltroCategoria.getPreferredSize().height));
        filtros.add(Estilos.titulo("Catalogo de productos   "));
        filtros.add(txtBuscar);
        filtros.add(cboFiltroCategoria);
        panel.add(filtros, BorderLayout.NORTH);

        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(3).setMaxWidth(70);
        tabla.getColumnModel().getColumn(6).setMaxWidth(70);
        panel.add(Estilos.scroll(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setPreferredSize(new Dimension(340, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        panel.add(lblModo, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridBagLayout());
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        Estilos.agregarCampo(campos, 0, "Nombre", txtNombre);
        Estilos.agregarCampo(campos, 1, "Categoria", cboCategoria);
        Estilos.agregarCampo(campos, 2, "Talla", cboTalla);
        Estilos.agregarCampo(campos, 3, "Color", txtColor);
        Estilos.agregarCampo(campos, 4, "Precio", txtPrecio);
        Estilos.agregarCampo(campos, 5, "Stock", spnStock);
        Estilos.agregarCampo(campos, 6, "Descripcion", new JScrollPane(txtDescripcion));

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(campos, BorderLayout.NORTH);
        panel.add(arriba, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(1, 3, 8, 0));
        botones.add(btnNuevo);
        botones.add(btnEliminar);
        botones.add(btnGuardar);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    // ----- Metodos usados por el controlador -----

    public void setModoEdicion(boolean editando) {
        lblModo.setText(editando ? "Editar producto" : "Nuevo producto");
        btnEliminar.setEnabled(editando);
    }

    public void limpiarFormulario() {
        txtNombre.setText("");
        if (cboCategoria.getItemCount() > 0) {
            cboCategoria.setSelectedIndex(0);
        }
        cboTalla.setSelectedIndex(0);
        txtColor.setText("");
        txtPrecio.setText("");
        spnStock.setValue(0);
        txtDescripcion.setText("");
        tabla.clearSelection();
        setModoEdicion(false);
        txtNombre.requestFocusInWindow();
    }

    public JTextField getTxtBuscar() { return txtBuscar; }
    public JComboBox<Categoria> getCboFiltroCategoria() { return cboFiltroCategoria; }
    public DefaultTableModel getModelo() { return modelo; }
    public JTable getTabla() { return tabla; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JComboBox<Categoria> getCboCategoria() { return cboCategoria; }
    public JComboBox<String> getCboTalla() { return cboTalla; }
    public JTextField getTxtColor() { return txtColor; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public JSpinner getSpnStock() { return spnStock; }
    public JTextArea getTxtDescripcion() { return txtDescripcion; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnEliminar() { return btnEliminar; }
}
