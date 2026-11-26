package com.neroshop.view.tienda;

import com.neroshop.model.DetallePedido;
import com.neroshop.util.Formato;
import com.neroshop.view.Estilos;
import com.neroshop.view.PanelBase;
import com.neroshop.view.componentes.ImagenProducto;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

/** Vista del carrito: lista de prendas y resumen de la compra. */
public class CarritoPanel extends PanelBase {

    /** Acciones que el controlador recibe desde cada fila del carrito. */
    public interface Acciones {
        void cambiarCantidad(int indice, int cantidad);

        void quitar(int indice);
    }

    private final JPanel lista = new JPanel();
    private final JLabel lblSubtotal = valor();
    private final JLabel lblEnvio = valor();
    private final JLabel lblTotal = valor();
    private final JLabel lblNotaEnvio = new JLabel();
    private final JButton btnFinalizar = Estilos.botonPrimario("Finalizar compra");
    private final JButton btnSeguir = Estilos.botonSecundario("Seguir comprando");
    private final JButton btnIrTienda = Estilos.botonPrimario("Ir a la tienda");
    private final JLabel lblTitulo = Estilos.lblTitulo("Tu carrito", 28);
    private final CardLayout tarjetas = new CardLayout();
    private final JPanel cuerpo = new JPanel(tarjetas);

    public CarritoPanel() {
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        add(lblTitulo, BorderLayout.NORTH);

        // Con productos
        JPanel conItems = new JPanel(new BorderLayout(30, 0));
        conItems.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(Estilos.FONDO);
        JPanel anclaLista = new JPanel(new BorderLayout());
        anclaLista.setBackground(Estilos.FONDO);
        anclaLista.add(lista, BorderLayout.NORTH);
        conItems.add(Estilos.scrollLimpio(anclaLista), BorderLayout.CENTER);
        conItems.add(crearResumen(), BorderLayout.EAST);

        // Vacio
        JPanel vacio = new JPanel(new GridBagLayout());
        vacio.setOpaque(false);
        JPanel textoVacio = new JPanel();
        textoVacio.setOpaque(false);
        textoVacio.setLayout(new BoxLayout(textoVacio, BoxLayout.Y_AXIS));
        JLabel l1 = Estilos.lblTitulo("Tu carrito está vacío", 24);
        JLabel l2 = new JLabel("Explora el catálogo y encuentra tu próxima prenda favorita.");
        l2.setForeground(Estilos.GRIS);
        for (JLabel l : new JLabel[]{l1, l2}) {
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        btnIrTienda.setAlignmentX(Component.CENTER_ALIGNMENT);
        textoVacio.add(l1);
        textoVacio.add(Box.createVerticalStrut(8));
        textoVacio.add(l2);
        textoVacio.add(Box.createVerticalStrut(24));
        textoVacio.add(btnIrTienda);
        vacio.add(textoVacio);

        cuerpo.setOpaque(false);
        cuerpo.add(conItems, "items");
        cuerpo.add(vacio, "vacio");
        add(cuerpo, BorderLayout.CENTER);
    }

    private static JLabel valor() {
        JLabel l = new JLabel("", SwingConstants.RIGHT);
        l.setFont(Estilos.texto(Font.BOLD, 14));
        l.setForeground(Estilos.BLANCO);
        return l;
    }

    private JPanel crearResumen() {
        JPanel resumen = new JPanel();
        resumen.setLayout(new BoxLayout(resumen, BoxLayout.Y_AXIS));
        resumen.setBackground(Estilos.SUPERFICIE);
        resumen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.BORDE),
                BorderFactory.createEmptyBorder(24, 24, 24, 24)));
        resumen.setPreferredSize(new Dimension(340, 400));

        resumen.add(izq(Estilos.lblTitulo("Resumen", 18)));
        resumen.add(Box.createVerticalStrut(20));
        resumen.add(fila("Subtotal", lblSubtotal));
        resumen.add(Box.createVerticalStrut(10));
        resumen.add(fila("Envío", lblEnvio));
        resumen.add(Box.createVerticalStrut(16));
        JPanel linea = new JPanel();
        linea.setBackground(Estilos.BORDE);
        linea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        resumen.add(izq(linea));
        resumen.add(Box.createVerticalStrut(16));
        lblTotal.setFont(Estilos.titulo(24));
        JLabel total = Estilos.lblTitulo("Total", 16);
        JPanel filaTotal = new JPanel(new BorderLayout());
        filaTotal.setOpaque(false);
        filaTotal.add(total, BorderLayout.WEST);
        filaTotal.add(lblTotal, BorderLayout.EAST);
        filaTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        resumen.add(izq(filaTotal));
        resumen.add(Box.createVerticalStrut(10));
        lblNotaEnvio.setForeground(Estilos.GRIS);
        lblNotaEnvio.setFont(Estilos.texto(Font.PLAIN, 12));
        resumen.add(izq(lblNotaEnvio));
        resumen.add(Box.createVerticalStrut(24));
        JPanel botones = new JPanel(new GridLayout(2, 1, 0, 10));
        botones.setOpaque(false);
        botones.add(btnFinalizar);
        botones.add(btnSeguir);
        botones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
        resumen.add(izq(botones));
        resumen.add(Box.createVerticalGlue());

        JPanel envoltura = new JPanel(new BorderLayout());
        envoltura.setOpaque(false);
        envoltura.add(resumen, BorderLayout.NORTH);
        return envoltura;
    }

    private static JPanel fila(String texto, JLabel valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(texto);
        l.setForeground(Estilos.GRIS);
        p.add(l, BorderLayout.WEST);
        p.add(valor, BorderLayout.EAST);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        return izq(p);
    }

    private static <T extends java.awt.Container> T izq(T c) {
        ((javax.swing.JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    // ----- Metodos usados por el controlador -----

    public void setItems(List<DetallePedido> items, Acciones acciones) {
        lista.removeAll();
        for (int i = 0; i < items.size(); i++) {
            lista.add(crearFila(items.get(i), i, acciones));
            lista.add(Box.createVerticalStrut(12));
        }
        int prendas = items.stream().mapToInt(DetallePedido::getCantidad).sum();
        lblTitulo.setText(("Tu carrito (" + prendas + ")").toUpperCase());
        tarjetas.show(cuerpo, items.isEmpty() ? "vacio" : "items");
        lista.revalidate();
        lista.repaint();
    }

    private JPanel crearFila(DetallePedido d, int indice, Acciones acciones) {
        JPanel fila = new JPanel(new BorderLayout(18, 0));
        fila.setBackground(Estilos.SUPERFICIE);
        fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.BORDE),
                BorderFactory.createEmptyBorder(10, 10, 10, 18)));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 142));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        fila.add(new ImagenProducto(d.getImagen(), 100, 120), BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel nombre = new JLabel(d.getProductoNombre());
        nombre.setFont(Estilos.titulo(17));
        nombre.setForeground(Estilos.BLANCO);
        JLabel detalle = new JLabel("Talla " + d.getTalla() + "  ·  " + d.getColor());
        detalle.setForeground(Estilos.GRIS);
        JLabel unitario = new JLabel(Formato.moneda(d.getPrecioUnitario()) + " c/u");
        unitario.setForeground(Estilos.GRIS);
        JButton quitar = Estilos.botonTexto("QUITAR");
        quitar.setForeground(Estilos.GRIS);
        quitar.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        quitar.addActionListener(e -> acciones.quitar(indice));
        info.add(Box.createVerticalStrut(14));
        info.add(nombre);
        info.add(Box.createVerticalStrut(4));
        info.add(detalle);
        info.add(Box.createVerticalStrut(2));
        info.add(unitario);
        info.add(quitar);
        fila.add(info, BorderLayout.CENTER);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 40));
        derecha.setOpaque(false);
        JSpinner cantidad = new JSpinner(new SpinnerNumberModel(d.getCantidad(), 1, 99, 1));
        cantidad.setPreferredSize(new Dimension(72, 36));
        cantidad.addChangeListener(e -> {
            int nueva = (Integer) cantidad.getValue();
            if (nueva != d.getCantidad()) {
                acciones.cambiarCantidad(indice, nueva);
            }
        });
        JLabel subtotal = new JLabel(Formato.moneda(d.getSubtotal()), SwingConstants.RIGHT);
        subtotal.setFont(Estilos.titulo(18));
        subtotal.setForeground(Estilos.BLANCO);
        subtotal.setPreferredSize(new Dimension(120, 36));
        derecha.add(cantidad);
        derecha.add(subtotal);
        fila.add(derecha, BorderLayout.EAST);
        return fila;
    }

    public void setTotales(String subtotal, String envio, String total, String notaEnvio) {
        lblSubtotal.setText(subtotal);
        lblEnvio.setText(envio);
        lblTotal.setText(total);
        lblNotaEnvio.setText(notaEnvio);
    }

    public JButton getBtnFinalizar() { return btnFinalizar; }
    public JButton getBtnSeguir() { return btnSeguir; }
    public JButton getBtnIrTienda() { return btnIrTienda; }
}
