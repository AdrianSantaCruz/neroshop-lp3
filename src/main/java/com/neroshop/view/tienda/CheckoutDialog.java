package com.neroshop.view.tienda;

import com.neroshop.model.Pedido;
import com.neroshop.view.Estilos;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;

/** Ventana para confirmar la compra: direccion de envio y metodo de pago. */
public class CheckoutDialog extends JDialog {

    private final JTextField txtDireccion = Estilos.alto(new JTextField());
    private final JTextField txtTelefono = Estilos.alto(new JTextField());
    private final ButtonGroup grupoPago = new ButtonGroup();
    private final JLabel lblTotal = new JLabel();
    private final JButton btnConfirmar = Estilos.botonPrimario("Confirmar pedido");
    private final JButton btnCancelar = Estilos.botonSecundario("Volver");

    public CheckoutDialog(Window owner) {
        super(owner, "Finalizar compra", ModalityType.APPLICATION_MODAL);
        setSize(520, 600);
        setResizable(false);
        setLocationRelativeTo(owner);

        JPanel contenido = new JPanel(new BorderLayout(0, 20));
        contenido.setBackground(Estilos.FONDO);
        contenido.setBorder(Estilos.margen(32));
        contenido.add(Estilos.lblTitulo("Finalizar compra", 26), BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        Estilos.placeholder(txtDireccion, "Calle, número, distrito");
        Estilos.placeholder(txtTelefono, "Celular de contacto");
        Estilos.agregarCampo(campos, 0, "Dirección de envío", txtDireccion);
        Estilos.agregarCampo(campos, 1, "Teléfono", txtTelefono);

        JPanel pagos = new JPanel();
        pagos.setOpaque(false);
        pagos.setLayout(new BoxLayout(pagos, BoxLayout.Y_AXIS));
        for (int i = 0; i < Pedido.METODOS_PAGO.length; i++) {
            JRadioButton rb = new JRadioButton(Pedido.METODOS_PAGO[i]);
            rb.setActionCommand(Pedido.METODOS_PAGO[i]);
            rb.setOpaque(false);
            rb.setFont(Estilos.texto(Font.PLAIN, 14));
            rb.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            rb.setSelected(i == 0);
            grupoPago.add(rb);
            pagos.add(rb);
        }
        JLabel nota = new JLabel("Pago simulado: proyecto académico, no se realizan cobros reales.");
        nota.setForeground(Estilos.GRIS_OSCURO);
        nota.setFont(Estilos.texto(Font.ITALIC, 11));
        pagos.add(Box.createVerticalStrut(4));
        pagos.add(nota);
        Estilos.agregarCampo(campos, 2, "Método de pago", pagos);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.add(campos, BorderLayout.NORTH);
        contenido.add(centro, BorderLayout.CENTER);

        JPanel sur = new JPanel();
        sur.setOpaque(false);
        sur.setLayout(new BoxLayout(sur, BoxLayout.Y_AXIS));
        lblTotal.setFont(Estilos.titulo(22));
        lblTotal.setForeground(Estilos.BLANCO);
        lblTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 0));
        botones.setOpaque(false);
        botones.add(btnCancelar);
        botones.add(btnConfirmar);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);
        botones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        sur.add(lblTotal);
        sur.add(Box.createVerticalStrut(16));
        sur.add(botones);
        contenido.add(sur, BorderLayout.SOUTH);

        setContentPane(contenido);
        btnCancelar.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnConfirmar);
    }

    public void setDatos(String direccion, String telefono, String total) {
        txtDireccion.setText(direccion == null ? "" : direccion);
        txtTelefono.setText(telefono == null ? "" : telefono);
        lblTotal.setText("TOTAL A PAGAR:  " + total);
    }

    public String getDireccion() { return txtDireccion.getText().trim(); }
    public String getTelefono() { return txtTelefono.getText().trim(); }
    public String getMetodoPago() { return grupoPago.getSelection().getActionCommand(); }
    public JButton getBtnConfirmar() { return btnConfirmar; }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Nero Thread", JOptionPane.WARNING_MESSAGE);
    }
}
