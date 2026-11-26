package com.neroshop.view.admin;

import com.neroshop.model.Pedido;
import com.neroshop.view.Estilos;
import com.neroshop.view.PanelBase;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

/** Gestion de pedidos: listado, detalle y cambio de estado. */
public class PedidosAdminPanel extends PanelBase {

    private final DefaultTableModel modeloPedidos = Estilos.modeloTabla(
            new String[]{"N°", "Fecha", "Cliente", "Pago", "Estado", "Total"},
            new Class<?>[]{Integer.class, String.class, String.class, String.class, String.class, Double.class});
    private final JTable tablaPedidos = Estilos.tabla(modeloPedidos);

    private final DefaultTableModel modeloDetalle = Estilos.modeloTabla(
            new String[]{"Producto", "Talla", "Color", "Cantidad", "P. Unit.", "Subtotal"},
            new Class<?>[]{String.class, String.class, String.class, Integer.class, Double.class, Double.class});
    private final JTable tablaDetalle = Estilos.tabla(modeloDetalle);

    private final JLabel lblResumen = new JLabel(" ");
    private final JLabel lblEnvio = new JLabel(" ");
    private final JComboBox<String> cboEstado = new JComboBox<>(Pedido.ESTADOS);
    private final JButton btnCambiarEstado = Estilos.botonPrimario("Actualizar estado");
    private final JButton btnActualizar = Estilos.botonSecundario("Recargar");

    public PedidosAdminPanel() {
        JPanel superior = new JPanel(new BorderLayout());
        superior.setOpaque(false);
        superior.add(Estilos.lblTitulo("Pedidos", 24), BorderLayout.WEST);
        superior.add(btnActualizar, BorderLayout.EAST);
        add(superior, BorderLayout.NORTH);

        JPanel pedidos = new JPanel(new BorderLayout(0, 8));
        pedidos.setOpaque(false);
        pedidos.add(Estilos.scroll(tablaPedidos), BorderLayout.CENTER);
        lblResumen.setForeground(Estilos.GRIS);
        pedidos.add(lblResumen, BorderLayout.SOUTH);

        JPanel detalle = new JPanel(new BorderLayout(0, 8));
        detalle.setOpaque(false);
        detalle.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        JPanel infoEnvio = new JPanel(new BorderLayout(0, 4));
        infoEnvio.setOpaque(false);
        infoEnvio.add(Estilos.lblEtiqueta("Detalle del pedido"), BorderLayout.NORTH);
        lblEnvio.setForeground(Estilos.GRIS);
        infoEnvio.add(lblEnvio, BorderLayout.SOUTH);
        cabecera.add(infoEnvio, BorderLayout.WEST);
        JPanel estado = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        estado.setOpaque(false);
        cboEstado.setPreferredSize(new Dimension(160, 40));
        estado.add(Estilos.lblEtiqueta("Estado"));
        estado.add(cboEstado);
        estado.add(btnCambiarEstado);
        cabecera.add(estado, BorderLayout.EAST);
        detalle.add(cabecera, BorderLayout.NORTH);
        detalle.add(Estilos.scroll(tablaDetalle), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pedidos, detalle);
        split.setResizeWeight(0.55);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
        tablaPedidos.getColumnModel().getColumn(0).setMaxWidth(60);
        setEdicionEstado(false);
    }

    public void setEdicionEstado(boolean habilitado) {
        cboEstado.setEnabled(habilitado);
        btnCambiarEstado.setEnabled(habilitado);
    }

    public void setResumen(String texto) { lblResumen.setText(texto); }
    public void setInfoEnvio(String texto) { lblEnvio.setText(texto); }

    public DefaultTableModel getModeloPedidos() { return modeloPedidos; }
    public JTable getTablaPedidos() { return tablaPedidos; }
    public DefaultTableModel getModeloDetalle() { return modeloDetalle; }
    public JComboBox<String> getCboEstado() { return cboEstado; }
    public JButton getBtnCambiarEstado() { return btnCambiarEstado; }
    public JButton getBtnActualizar() { return btnActualizar; }
}
