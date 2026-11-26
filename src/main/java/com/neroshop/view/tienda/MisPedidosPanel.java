package com.neroshop.view.tienda;

import com.neroshop.view.Estilos;
import com.neroshop.view.PanelBase;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

/** Historial de compras del cliente con el detalle de cada pedido. */
public class MisPedidosPanel extends PanelBase {

    private final DefaultTableModel modeloPedidos = Estilos.modeloTabla(
            new String[]{"N° Pedido", "Fecha", "Prendas", "Pago", "Estado", "Total"},
            new Class<?>[]{Integer.class, String.class, Integer.class, String.class, String.class, Double.class});
    private final JTable tablaPedidos = Estilos.tabla(modeloPedidos);

    private final DefaultTableModel modeloDetalle = Estilos.modeloTabla(
            new String[]{"Producto", "Talla", "Color", "Cantidad", "P. Unit.", "Subtotal"},
            new Class<?>[]{String.class, String.class, String.class, Integer.class, Double.class, Double.class});
    private final JTable tablaDetalle = Estilos.tabla(modeloDetalle);
    private final JLabel lblEnvio = new JLabel(" ");

    public MisPedidosPanel() {
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        JPanel titulo = new JPanel(new BorderLayout());
        titulo.setOpaque(false);
        titulo.add(Estilos.lblTitulo("Mis pedidos", 28), BorderLayout.WEST);
        add(titulo, BorderLayout.NORTH);

        JPanel detalle = new JPanel(new BorderLayout(0, 8));
        detalle.setOpaque(false);
        detalle.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(Estilos.lblEtiqueta("Detalle del pedido"), BorderLayout.WEST);
        lblEnvio.setForeground(Estilos.GRIS);
        cabecera.add(lblEnvio, BorderLayout.EAST);
        detalle.add(cabecera, BorderLayout.NORTH);
        detalle.add(Estilos.scroll(tablaDetalle), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, Estilos.scroll(tablaPedidos), detalle);
        split.setResizeWeight(0.55);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
        tablaPedidos.getColumnModel().getColumn(0).setMaxWidth(100);
        tablaPedidos.getColumnModel().getColumn(2).setMaxWidth(90);
    }

    public void setInfoEnvio(String texto) {
        lblEnvio.setText(texto);
    }

    public DefaultTableModel getModeloPedidos() { return modeloPedidos; }
    public JTable getTablaPedidos() { return tablaPedidos; }
    public DefaultTableModel getModeloDetalle() { return modeloDetalle; }
}
