package com.neroshop.view.tienda;

import com.neroshop.view.Estilos;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

/** Ventana principal de la tienda para el cliente. */
public class TiendaView extends JFrame {

    public static final String CATALOGO = "catalogo";
    public static final String CARRITO = "carrito";
    public static final String PEDIDOS = "pedidos";

    private final JButton btnTienda = Estilos.botonTexto("TIENDA");
    private final JButton btnPedidos = Estilos.botonTexto("MIS PEDIDOS");
    private final JButton btnCarrito = Estilos.botonPrimario("Carrito (0)");
    private final JButton btnSalir = Estilos.botonTexto("SALIR");
    private final JLabel lblUsuario = new JLabel();

    private final CardLayout tarjetas = new CardLayout();
    private final JPanel cuerpo = new JPanel(tarjetas);
    private final CatalogoPanel catalogoPanel = new CatalogoPanel();
    private final CarritoPanel carritoPanel = new CarritoPanel();
    private final MisPedidosPanel misPedidosPanel = new MisPedidosPanel();

    public TiendaView() {
        super("Nero Thread - Tienda online");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1320, 860);
        setMinimumSize(new Dimension(1000, 680));
        setLocationRelativeTo(null);
        Estilos.iconoVentana(this);

        cuerpo.add(catalogoPanel, CATALOGO);
        cuerpo.add(carritoPanel, CARRITO);
        cuerpo.add(misPedidosPanel, PEDIDOS);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.add(crearEncabezado(), BorderLayout.NORTH);
        contenido.add(cuerpo, BorderLayout.CENTER);
        setContentPane(contenido);
    }

    private JPanel crearEncabezado() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Estilos.NEGRO);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Estilos.BORDE),
                BorderFactory.createEmptyBorder(10, 28, 10, 28)));
        header.add(Estilos.logo(52), BorderLayout.WEST);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 8));
        nav.setOpaque(false);
        lblUsuario.setForeground(Estilos.GRIS);
        lblUsuario.setFont(Estilos.texto(Font.PLAIN, 13));
        nav.add(lblUsuario);
        nav.add(separador());
        nav.add(btnTienda);
        nav.add(btnPedidos);
        nav.add(btnSalir);
        nav.add(separador());
        nav.add(btnCarrito);
        header.add(nav, BorderLayout.EAST);
        return header;
    }

    private static JLabel separador() {
        JLabel l = new JLabel("|");
        l.setForeground(Estilos.BORDE);
        l.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        return l;
    }

    public void mostrar(String seccion) {
        tarjetas.show(cuerpo, seccion);
        btnTienda.setForeground(CATALOGO.equals(seccion) ? Estilos.BLANCO : Estilos.GRIS);
        btnPedidos.setForeground(PEDIDOS.equals(seccion) ? Estilos.BLANCO : Estilos.GRIS);
    }

    public void setCantidadCarrito(int cantidad) {
        btnCarrito.setText(("Carrito (" + cantidad + ")").toUpperCase());
    }

    public void setUsuario(String nombre) {
        lblUsuario.setText("Hola, " + nombre);
    }

    public JButton getBtnTienda() { return btnTienda; }
    public JButton getBtnPedidos() { return btnPedidos; }
    public JButton getBtnCarrito() { return btnCarrito; }
    public JButton getBtnSalir() { return btnSalir; }
    public CatalogoPanel getCatalogoPanel() { return catalogoPanel; }
    public CarritoPanel getCarritoPanel() { return carritoPanel; }
    public MisPedidosPanel getMisPedidosPanel() { return misPedidosPanel; }
}
