package com.neroshop.view.componentes;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Panel que acomoda tarjetas de tamano fijo en una grilla que se adapta al
 * ancho de la ventana (mas columnas si la ventana es mas ancha).
 */
public class GrillaPanel extends JPanel implements Scrollable {

    private final int anchoCelda;
    private final int altoCelda;
    private final int espacio;

    public GrillaPanel(int anchoCelda, int altoCelda, int espacio) {
        this.anchoCelda = anchoCelda;
        this.altoCelda = altoCelda;
        this.espacio = espacio;
        setOpaque(false);
        setLayout(new Grilla());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
            }
        });
    }

    private int columnas(int ancho) {
        return Math.max(1, (ancho + espacio) / (anchoCelda + espacio));
    }

    private class Grilla implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            int ancho = parent.getWidth();
            if (ancho <= 0 && parent.getParent() != null) {
                ancho = parent.getParent().getWidth();
            }
            if (ancho <= 0) {
                ancho = 4 * (anchoCelda + espacio);
            }
            int n = parent.getComponentCount();
            int filas = (n + columnas(ancho) - 1) / columnas(ancho);
            return new Dimension(anchoCelda, Math.max(0, filas * (altoCelda + espacio)));
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(anchoCelda, altoCelda);
        }

        @Override
        public void layoutContainer(Container parent) {
            int ancho = parent.getWidth();
            int cols = columnas(ancho);
            int usado = cols * anchoCelda + (cols - 1) * espacio;
            int x0 = Math.max(0, (ancho - usado) / 2);
            for (int i = 0; i < parent.getComponentCount(); i++) {
                int x = x0 + (i % cols) * (anchoCelda + espacio);
                int y = (i / cols) * (altoCelda + espacio);
                parent.getComponent(i).setBounds(x, y, anchoCelda, altoCelda);
            }
        }
    }

    // ----- Scrollable: el panel ocupa el ancho del viewport y crece hacia abajo -----

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 28;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return altoCelda;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
