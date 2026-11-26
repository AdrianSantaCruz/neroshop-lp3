package com.neroshop.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

/** Vista del historial de ventas con su detalle. */
public class HistorialPanel extends PanelBase {

    private final DefaultTableModel modeloVentas = Estilos.modeloTabla(
            new String[]{"N° Venta", "Fecha", "Cliente", "Vendedor", "Total"},
            new Class<?>[]{Integer.class, String.class, String.class, String.class, Double.class});
    private final JTable tablaVentas = Estilos.tabla(modeloVentas);

    private final DefaultTableModel modeloDetalle = Estilos.modeloTabla(
            new String[]{"Producto", "Talla", "Color", "Cantidad", "P. Unit.", "Subtotal"},
            new Class<?>[]{String.class, String.class, String.class, Integer.class, Double.class, Double.class});
    private final JTable tablaDetalle = Estilos.tabla(modeloDetalle);

    private final JLabel lblResumen = new JLabel();
    private final JButton btnActualizar = Estilos.boton("Actualizar", false);

    public HistorialPanel() {
        JPanel superior = new JPanel(new BorderLayout());
        superior.add(Estilos.titulo("Historial de ventas"), BorderLayout.WEST);
        superior.add(btnActualizar, BorderLayout.EAST);
        add(superior, BorderLayout.NORTH);

        JPanel ventas = new JPanel(new BorderLayout(0, 6));
        ventas.add(Estilos.scroll(tablaVentas), BorderLayout.CENTER);
        lblResumen.setForeground(Estilos.TEXTO_SECUNDARIO);
        ventas.add(lblResumen, BorderLayout.SOUTH);

        JPanel detalle = new JPanel(new BorderLayout(0, 6));
        detalle.add(new JLabel("Detalle de la venta seleccionada"), BorderLayout.NORTH);
        detalle.add(Estilos.scroll(tablaDetalle), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ventas, detalle);
        split.setResizeWeight(0.6);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
        tablaVentas.getColumnModel().getColumn(0).setMaxWidth(80);
    }

    public void setResumen(String texto) {
        lblResumen.setText(texto);
    }

    public DefaultTableModel getModeloVentas() { return modeloVentas; }
    public JTable getTablaVentas() { return tablaVentas; }
    public DefaultTableModel getModeloDetalle() { return modeloDetalle; }
    public JButton getBtnActualizar() { return btnActualizar; }
}
