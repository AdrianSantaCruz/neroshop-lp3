package com.neroshop.view.componentes;

import com.neroshop.util.Imagenes;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/** Muestra la foto de un producto llenando todo su espacio (como en las tiendas online). */
public class ImagenProducto extends JComponent {

    private String ruta;
    private String sello;

    public ImagenProducto(String ruta, int ancho, int alto) {
        this.ruta = ruta;
        setPreferredSize(new Dimension(ancho, alto));
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
        repaint();
    }

    /** Texto que se muestra sobre la foto (por ejemplo "AGOTADO"). */
    public void setSello(String sello) {
        this.sello = sello;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth(), h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }
        BufferedImage img = Imagenes.cubrir(ruta, w, h);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(img, 0, 0, null);
        if (sello != null) {
            g2.setColor(new Color(0, 0, 0, 120));
            g2.fillRect(0, 0, w, h);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(com.neroshop.view.Estilos.etiqueta(13));
            int tw = g2.getFontMetrics().stringWidth(sello);
            int bx = (w - tw) / 2 - 14, by = h / 2 - 16;
            g2.setColor(Color.BLACK);
            g2.fillRect(bx, by, tw + 28, 32);
            g2.setColor(Color.WHITE);
            g2.drawString(sello, bx + 14, by + 21);
        }
        g2.dispose();
    }
}
