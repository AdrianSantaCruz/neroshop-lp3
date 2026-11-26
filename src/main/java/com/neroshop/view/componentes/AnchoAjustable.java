package com.neroshop.view.componentes;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * Contenedor para usar dentro de un JScrollPane: toma el ancho visible y solo
 * se desplaza en vertical (evita la barra de desplazamiento horizontal).
 */
public class AnchoAjustable extends JPanel implements Scrollable {

    public AnchoAjustable(JComponent contenido) {
        super(new BorderLayout());
        setOpaque(false);
        add(contenido, BorderLayout.NORTH);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle r, int orientacion, int direccion) {
        return 28;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle r, int orientacion, int direccion) {
        return Math.max(r.height - 40, 40);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        // Si el contenido es mas bajo que la ventana, se estira para llenarla
        return getParent() != null && getParent().getHeight() > getPreferredSize().height;
    }
}
