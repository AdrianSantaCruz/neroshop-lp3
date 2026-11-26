package com.neroshop.view.admin;

import com.neroshop.view.Estilos;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;

/** Ventana del panel de administracion (una pestana por modulo). */
public class AdminView extends JFrame {

    private final JLabel lblUsuario = new JLabel();
    private final JButton btnCerrarSesion = Estilos.botonSecundario("Cerrar sesión");
    private final JTabbedPane pestanas = new JTabbedPane();

    private final ProductoAdminPanel productoPanel = new ProductoAdminPanel();
    private final CategoriaPanel categoriaPanel = new CategoriaPanel();
    private final ClientePanel clientePanel = new ClientePanel();
    private final PedidosAdminPanel pedidosPanel = new PedidosAdminPanel();

    public AdminView() {
        super("Nero Thread - Panel de administración");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1320, 840);
        setMinimumSize(new Dimension(1100, 680));
        setLocationRelativeTo(null);
        Estilos.iconoVentana(this);

        pestanas.addTab("PRODUCTOS", productoPanel);
        pestanas.addTab("CATEGORÍAS", categoriaPanel);
        pestanas.addTab("CLIENTES", clientePanel);
        pestanas.addTab("PEDIDOS", pedidosPanel);
        pestanas.setFont(Estilos.etiqueta(12));
        pestanas.putClientProperty("JTabbedPane.tabAreaInsets", new Insets(0, 20, 0, 0));
        pestanas.putClientProperty("JTabbedPane.tabInsets", new Insets(0, 18, 0, 18));

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.add(crearEncabezado(), BorderLayout.NORTH);
        contenido.add(pestanas, BorderLayout.CENTER);
        setContentPane(contenido);
    }

    private JPanel crearEncabezado() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Estilos.NEGRO);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Estilos.BORDE),
                BorderFactory.createEmptyBorder(10, 28, 10, 28)));
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        izq.setOpaque(false);
        izq.add(Estilos.logo(48));
        JLabel modo = Estilos.lblEtiqueta("Panel de administración");
        izq.add(modo);
        header.add(izq, BorderLayout.WEST);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 8));
        derecha.setOpaque(false);
        lblUsuario.setForeground(Estilos.GRIS);
        lblUsuario.setFont(Estilos.texto(Font.PLAIN, 13));
        derecha.add(lblUsuario);
        derecha.add(btnCerrarSesion);
        header.add(derecha, BorderLayout.EAST);
        return header;
    }

    public void setUsuario(String texto) {
        lblUsuario.setText(texto);
    }

    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JTabbedPane getPestanas() { return pestanas; }
    public ProductoAdminPanel getProductoPanel() { return productoPanel; }
    public CategoriaPanel getCategoriaPanel() { return categoriaPanel; }
    public ClientePanel getClientePanel() { return clientePanel; }
    public PedidosAdminPanel getPedidosPanel() { return pedidosPanel; }
}
