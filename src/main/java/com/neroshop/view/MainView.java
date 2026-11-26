package com.neroshop.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

/** Ventana principal con una pestaña por modulo. */
public class MainView extends JFrame {

    private final JLabel lblUsuario = new JLabel();
    private final JButton btnCerrarSesion = Estilos.boton("Cerrar sesion", false);
    private final JTabbedPane pestanas = new JTabbedPane();

    private final VentaPanel ventaPanel = new VentaPanel();
    private final ProductoPanel productoPanel = new ProductoPanel();
    private final CategoriaPanel categoriaPanel = new CategoriaPanel();
    private final ClientePanel clientePanel = new ClientePanel();
    private final HistorialPanel historialPanel = new HistorialPanel();

    public MainView() {
        super("NeroShop - Tienda de ropa");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 740);
        setMinimumSize(new java.awt.Dimension(1000, 620));
        setLocationRelativeTo(null);

        pestanas.addTab("Nueva venta", ventaPanel);
        pestanas.addTab("Productos", productoPanel);
        pestanas.addTab("Categorias", categoriaPanel);
        pestanas.addTab("Clientes", clientePanel);
        pestanas.addTab("Historial de ventas", historialPanel);
        pestanas.putClientProperty("JTabbedPane.tabAreaInsets", new java.awt.Insets(0, 12, 0, 0));

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.add(crearEncabezado(), BorderLayout.NORTH);
        contenido.add(pestanas, BorderLayout.CENTER);
        setContentPane(contenido);
    }

    private JPanel crearEncabezado() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Estilos.FONDO_OSCURO);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, Estilos.ACENTO),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));

        JLabel logo = new JLabel("NEROSHOP");
        logo.setFont(Estilos.FUENTE_LOGO);
        logo.setForeground(Color.WHITE);
        header.add(logo, BorderLayout.WEST);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 4));
        derecha.setOpaque(false);
        lblUsuario.setForeground(Estilos.TEXTO_SECUNDARIO);
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
    public VentaPanel getVentaPanel() { return ventaPanel; }
    public ProductoPanel getProductoPanel() { return productoPanel; }
    public CategoriaPanel getCategoriaPanel() { return categoriaPanel; }
    public ClientePanel getClientePanel() { return clientePanel; }
    public HistorialPanel getHistorialPanel() { return historialPanel; }
}
