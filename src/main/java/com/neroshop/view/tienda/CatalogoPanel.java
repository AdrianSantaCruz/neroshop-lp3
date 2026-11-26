package com.neroshop.view.tienda;

import com.neroshop.model.Categoria;
import com.neroshop.model.Producto;
import com.neroshop.model.dao.ProductoDAO;
import com.neroshop.view.Estilos;
import com.neroshop.view.PanelBase;
import com.neroshop.view.componentes.AnchoAjustable;
import com.neroshop.view.componentes.GrillaPanel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/** Catalogo de la tienda: banner, filtros por categoria, busqueda y grilla de productos. */
public class CatalogoPanel extends PanelBase {

    private final JTextField txtBuscar = Estilos.alto(new JTextField(22));
    private final JComboBox<ProductoDAO.Orden> cboOrden = new JComboBox<>(ProductoDAO.Orden.values());
    private final JPanel panelCategorias = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    private final JLabel lblResultados = Estilos.lblEtiqueta("");
    private final GrillaPanel grilla = new GrillaPanel(ProductoCard.ANCHO, ProductoCard.ALTO, 22);
    private final JLabel lblVacio = new JLabel("No encontramos prendas con esos filtros.", SwingConstants.CENTER);
    private final JScrollPane scroll;

    private IntConsumer alElegirCategoria;

    public CatalogoPanel() {
        setBorder(null);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Estilos.FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        contenido.add(alinear(crearBanner()));
        contenido.add(alinear(crearFiltros()));

        JPanel envolturaGrilla = new JPanel(new BorderLayout());
        envolturaGrilla.setOpaque(false);
        envolturaGrilla.setBorder(BorderFactory.createEmptyBorder(6, 28, 0, 28));
        envolturaGrilla.add(grilla, BorderLayout.CENTER);
        lblVacio.setForeground(Estilos.GRIS);
        lblVacio.setFont(Estilos.titulo(16));
        lblVacio.setBorder(BorderFactory.createEmptyBorder(60, 0, 60, 0));
        lblVacio.setVisible(false);
        envolturaGrilla.add(lblVacio, BorderLayout.NORTH);
        contenido.add(alinear(envolturaGrilla));

        scroll = Estilos.scrollLimpio(new AnchoAjustable(contenido));
        add(scroll, BorderLayout.CENTER);
    }

    private static JComponent alinear(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    /** Banner negro con el logo y el mensaje de la coleccion. */
    private JPanel crearBanner() {
        JPanel banner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, Color.BLACK, getWidth(), 0, new Color(0x1B, 0x1B, 0x1B)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 14));
                for (int x = -getHeight(); x < getWidth(); x += 22) {
                    g2.drawLine(x, getHeight(), x + getHeight(), 0);
                }
            }
        };
        banner.setPreferredSize(new Dimension(800, 230));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));
        banner.setBorder(BorderFactory.createEmptyBorder(28, 48, 28, 48));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel temporada = Estilos.lblEtiqueta("Nueva colección · Temporada 2026");
        JLabel titulo = Estilos.lblTitulo("Streetwear sin reglas", 44);
        JLabel sub = new JLabel("Prendas oversize, básicos esenciales y piezas de edición limitada.");
        sub.setForeground(Estilos.GRIS);
        sub.setFont(Estilos.texto(java.awt.Font.PLAIN, 15));
        JLabel envio = Estilos.lblEtiqueta("Envío gratis desde S/ 199");
        envio.setForeground(Estilos.BLANCO);
        textos.add(temporada);
        textos.add(Box.createVerticalStrut(8));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(6));
        textos.add(sub);
        textos.add(Box.createVerticalStrut(18));
        textos.add(envio);
        banner.add(textos, BorderLayout.CENTER);
        banner.add(Estilos.logo(150), BorderLayout.EAST);
        return banner;
    }

    private JPanel crearFiltros() {
        JPanel filtros = new JPanel(new BorderLayout(16, 14));
        filtros.setOpaque(false);
        filtros.setBorder(BorderFactory.createEmptyBorder(26, 28, 14, 28));
        filtros.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        JPanel fila1 = new JPanel(new BorderLayout(12, 0));
        fila1.setOpaque(false);
        fila1.add(Estilos.lblTitulo("Catálogo", 26), BorderLayout.WEST);
        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        derecha.setOpaque(false);
        Estilos.placeholder(txtBuscar, "Buscar polos, jeans, casacas...");
        txtBuscar.putClientProperty("JTextField.showClearButton", true);
        cboOrden.setPreferredSize(new Dimension(200, 38));
        derecha.add(txtBuscar);
        derecha.add(cboOrden);
        fila1.add(derecha, BorderLayout.EAST);

        panelCategorias.setOpaque(false);
        JPanel fila2 = new JPanel(new BorderLayout());
        fila2.setOpaque(false);
        fila2.add(panelCategorias, BorderLayout.WEST);
        fila2.add(lblResultados, BorderLayout.EAST);

        filtros.add(fila1, BorderLayout.NORTH);
        filtros.add(fila2, BorderLayout.SOUTH);
        return filtros;
    }

    // ----- Metodos usados por el controlador -----

    /** Dibuja los chips de categoria; el id 0 representa "Todo". */
    public void setCategorias(List<Categoria> categorias, int seleccionada) {
        panelCategorias.removeAll();
        ButtonGroup grupo = new ButtonGroup();
        List<Categoria> todas = new ArrayList<>();
        todas.add(new Categoria(0, "Todo", null));
        todas.addAll(categorias);
        for (Categoria c : todas) {
            JToggleButton chip = Estilos.chip(c.getNombre());
            chip.setSelected(c.getId() == seleccionada);
            chip.addActionListener(e -> {
                if (alElegirCategoria != null) {
                    alElegirCategoria.accept(c.getId());
                }
            });
            grupo.add(chip);
            panelCategorias.add(chip);
        }
        panelCategorias.revalidate();
        panelCategorias.repaint();
    }

    /** Muestra los productos; cada tarjeta avisa al controlador cuando se hace clic. */
    public void setProductos(List<Producto> productos, Consumer<Producto> alElegir) {
        grilla.removeAll();
        for (Producto p : productos) {
            ProductoCard card = new ProductoCard(p);
            card.alHacerClic(() -> alElegir.accept(p));
            grilla.add(card);
        }
        lblVacio.setVisible(productos.isEmpty());
        lblResultados.setText(productos.size() + (productos.size() == 1 ? " PRENDA" : " PRENDAS"));
        grilla.revalidate();
        grilla.repaint();
    }

    public void irArriba() {
        SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(0));
    }

    public void alElegirCategoria(IntConsumer accion) {
        this.alElegirCategoria = accion;
    }

    public JTextField getTxtBuscar() { return txtBuscar; }
    public JComboBox<ProductoDAO.Orden> getCboOrden() { return cboOrden; }
}
