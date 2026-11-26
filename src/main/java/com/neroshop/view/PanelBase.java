package com.neroshop.view;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/** Panel base con metodos para mostrar mensajes al usuario. */
public abstract class PanelBase extends JPanel {

    protected PanelBase() {
        super(new BorderLayout(15, 15));
        setBorder(Estilos.margen(18));
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "NeroShop", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "NeroShop - Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
