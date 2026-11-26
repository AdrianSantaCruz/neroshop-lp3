package com.neroshop.view;

import com.neroshop.model.Cliente;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

/** Vista del punto de venta: catalogo a la izquierda y carrito a la derecha. */
public class VentaPanel extends PanelBase {

    private final JComboBox<Cliente> cboCliente = new JComboBox<>();
    private final JTextField txtBuscar = new JTextField(18);

    private final DefaultTableModel modeloProductos = Estilos.modeloTabla(
            new String[]{"ID", "Producto", "Talla", "Color", "Precio", "Stock"},
            new Class<?>[]{Integer.class, String.class, String.class, String.class, Double.class, Integer.class});
    private final JTable tablaProductos = Estilos.tabla(modeloProductos);
    private final JSpinner spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
    private final JButton btnAgregar = Estilos.boton("Agregar al carrito  →", true);

    private final DefaultTableModel modeloCarrito = Estilos.modeloTabla(
            new String[]{"Producto", "Talla", "Cant.", "P. Unit.", "Subtotal"},
            new Class<?>[]{String.class, String.class, Integer.class, Double.class, Double.class});
    private final JTable tablaCarrito = Estilos.tabla(modeloCarrito);
    private final JButton btnQuitar = Estilos.boton("Quitar", false);
    private final JButton btnCancelar = Estilos.boton("Vaciar", false);
    private final JButton btnConfirmar = Estilos.boton("Confirmar venta", true);
    private final JLabel lblTotal = new JLabel();

    public VentaPanel() {
        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        superior.add(Estilos.titulo("Nueva venta   "));
        superior.add(new JLabel("Cliente:"));
        cboCliente.setPreferredSize(new Dimension(320, cboCliente.getPreferredSize().height));
        superior.add(cboCliente);
        add(superior, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearCatalogo(), crearCarrito());
        split.setResizeWeight(0.55);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
        // Evita que el carrito se ordene: el indice de fila debe coincidir con el del modelo
        tablaCarrito.setAutoCreateRowSorter(false);
        tablaCarrito.setRowSorter(null);
    }

    private JPanel crearCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        JPanel filtro = new JPanel(new BorderLayout(8, 0));
        Estilos.placeholder(txtBuscar, "Buscar producto...");
        filtro.add(new JLabel("Productos"), BorderLayout.WEST);
        filtro.add(txtBuscar, BorderLayout.CENTER);
        panel.add(filtro, BorderLayout.NORTH);

        tablaProductos.getColumnModel().getColumn(0).setMaxWidth(45);
        tablaProductos.getColumnModel().getColumn(2).setMaxWidth(60);
        tablaProductos.getColumnModel().getColumn(5).setMaxWidth(60);
        panel.add(Estilos.scroll(tablaProductos), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.add(new JLabel("Cantidad:"));
        spnCantidad.setPreferredSize(new Dimension(80, spnCantidad.getPreferredSize().height));
        acciones.add(spnCantidad);
        acciones.add(btnAgregar);
        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearCarrito() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        panel.add(new JLabel("Carrito"), BorderLayout.NORTH);
        tablaCarrito.getColumnModel().getColumn(1).setMaxWidth(60);
        tablaCarrito.getColumnModel().getColumn(2).setMaxWidth(55);
        panel.add(Estilos.scroll(tablaCarrito), BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout(0, 10));
        lblTotal.setFont(Estilos.FUENTE_TOTAL);
        lblTotal.setForeground(Estilos.ACENTO);
        lblTotal.setHorizontalAlignment(JLabel.RIGHT);
        inferior.add(lblTotal, BorderLayout.NORTH);

        JPanel botones = new JPanel(new GridLayout(1, 3, 8, 0));
        botones.add(btnQuitar);
        botones.add(btnCancelar);
        botones.add(btnConfirmar);
        inferior.add(botones, BorderLayout.SOUTH);
        panel.add(inferior, BorderLayout.SOUTH);
        return panel;
    }

    public void setTotal(String total) {
        lblTotal.setText("TOTAL:  " + total);
    }

    public JComboBox<Cliente> getCboCliente() { return cboCliente; }
    public JTextField getTxtBuscar() { return txtBuscar; }
    public DefaultTableModel getModeloProductos() { return modeloProductos; }
    public JTable getTablaProductos() { return tablaProductos; }
    public JSpinner getSpnCantidad() { return spnCantidad; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public DefaultTableModel getModeloCarrito() { return modeloCarrito; }
    public JTable getTablaCarrito() { return tablaCarrito; }
    public JButton getBtnQuitar() { return btnQuitar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JButton getBtnConfirmar() { return btnConfirmar; }
}
