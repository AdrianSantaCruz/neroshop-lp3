package com.neroshop.view.tienda;

import com.neroshop.model.Producto;
import com.neroshop.util.Formato;
import com.neroshop.view.Estilos;
import com.neroshop.view.componentes.ImagenProducto;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Tarjeta de un producto en el catalogo: foto, categoria, nombre y precio. */
public class ProductoCard extends JPanel {

    public static final int ANCHO = 250;
    public static final int ALTO = 420;
    private static final int ALTO_FOTO = 300;

    private final Producto producto;
    private final ImagenProducto foto;
    private Runnable alHacerClic;

    public ProductoCard(Producto producto) {
        super(new BorderLayout());
        this.producto = producto;
        setBackground(Estilos.SUPERFICIE);
        setBorder(BorderFactory.createLineBorder(Estilos.BORDE));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        foto = new ImagenProducto(producto.getImagen(), ANCHO, ALTO_FOTO);
        if (producto.isAgotado()) {
            foto.setSello("AGOTADO");
        }
        add(foto, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel categoria = Estilos.lblEtiqueta(producto.getCategoriaNombre());
        categoria.setFont(Estilos.etiqueta(10));
        JLabel nombre = new JLabel(producto.getNombre());
        nombre.setFont(Estilos.titulo(16));
        nombre.setForeground(Estilos.BLANCO);
        int tallas = producto.getListaTallas().size();
        JLabel detalle = new JLabel(producto.getColor() + "  ·  " + (tallas == 1 ? "Talla única" : tallas + " tallas"));
        detalle.setFont(Estilos.texto(java.awt.Font.PLAIN, 12));
        detalle.setForeground(Estilos.GRIS);
        JLabel precio = new JLabel(Formato.moneda(producto.getPrecio()));
        precio.setFont(Estilos.titulo(17));
        precio.setForeground(producto.isAgotado() ? Estilos.GRIS_OSCURO : Estilos.BLANCO);

        for (JLabel l : new JLabel[]{categoria, nombre, detalle, precio}) {
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        info.add(categoria);
        info.add(Box.createVerticalStrut(5));
        info.add(nombre);
        info.add(Box.createVerticalStrut(3));
        info.add(detalle);
        info.add(Box.createVerticalStrut(9));
        info.add(precio);
        add(info, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Estilos.BLANCO));
                setBackground(Estilos.SUPERFICIE_2);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Estilos.BORDE));
                setBackground(Estilos.SUPERFICIE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (alHacerClic != null) {
                    alHacerClic.run();
                }
            }
        });
    }

    public void alHacerClic(Runnable accion) {
        this.alHacerClic = accion;
    }

    public Producto getProducto() {
        return producto;
    }
}
