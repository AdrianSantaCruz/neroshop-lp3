package com.neroshop.view;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

/** Ventana de inicio de sesion de Nero Thread. */
public class LoginView extends JFrame {

    private final JTextField txtUsuario = Estilos.alto(new JTextField());
    private final JPasswordField txtPassword = Estilos.alto(new JPasswordField());
    private final JButton btnIngresar = Estilos.botonPrimario("Ingresar");
    private final JButton btnRegistrarse = Estilos.botonTexto("CREAR UNA CUENTA");

    public LoginView() {
        super("Nero Thread - Iniciar sesión");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 560);
        setResizable(false);
        setLocationRelativeTo(null);
        Estilos.iconoVentana(this);

        JPanel contenido = new JPanel(new GridLayout(1, 2));
        contenido.add(crearPanelMarca());
        contenido.add(crearPanelFormulario());
        setContentPane(contenido);
        getRootPane().setDefaultButton(btnIngresar);
    }

    private JPanel crearPanelMarca() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, Color.BLACK, 0, getHeight(), new Color(0x14, 0x14, 0x14)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 10));
                for (int x = -getHeight(); x < getWidth(); x += 24) {
                    g2.drawLine(x, getHeight(), x + getHeight(), 0);
                }
            }
        };
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel logo = Estilos.logo(170);
        JLabel slogan = Estilos.lblEtiqueta("Streetwear con identidad");
        slogan.setFont(Estilos.etiqueta(13));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        textos.add(logo);
        textos.add(Box.createVerticalStrut(22));
        textos.add(slogan);
        panel.add(textos);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel exterior = new JPanel(new GridBagLayout());
        exterior.setBackground(Estilos.FONDO);
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(320, 400));

        JLabel titulo = Estilos.lblTitulo("Bienvenido", 32);
        JLabel subtitulo = new JLabel("Ingresa a tu cuenta para comprar");
        subtitulo.setForeground(Estilos.GRIS);
        subtitulo.setFont(Estilos.texto(Font.PLAIN, 14));

        Estilos.placeholder(txtUsuario, "Tu usuario");
        Estilos.placeholder(txtPassword, "Tu contraseña");
        txtPassword.putClientProperty("JTextField.showRevealButton", true);

        JPanel registro = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        registro.setOpaque(false);
        JLabel pregunta = new JLabel("¿Eres nuevo?  ");
        pregunta.setForeground(Estilos.GRIS);
        btnRegistrarse.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Estilos.BLANCO));
        registro.add(pregunta);
        registro.add(btnRegistrarse);

        JLabel ayuda = new JLabel("<html>Demo cliente: <b>cliente / cliente123</b><br>Demo admin: <b>admin / admin123</b></html>");
        ayuda.setForeground(Estilos.GRIS_OSCURO);
        ayuda.setFont(Estilos.texto(Font.PLAIN, 12));

        agregar(form, titulo, 4);
        agregar(form, subtitulo, 30);
        agregar(form, Estilos.lblEtiqueta("Usuario"), 6);
        agregar(form, txtUsuario, 16);
        agregar(form, Estilos.lblEtiqueta("Contraseña"), 6);
        agregar(form, txtPassword, 24);
        agregar(form, btnIngresar, 20);
        agregar(form, registro, 28);
        agregar(form, ayuda, 0);
        exterior.add(form);
        return exterior;
    }

    private static void agregar(JPanel panel, JComponent c, int espacio) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (c instanceof JTextField || c instanceof JButton) {
            c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        }
        panel.add(c);
        if (espacio > 0) {
            panel.add(Box.createVerticalStrut(espacio));
        }
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public void setUsuario(String usuario) {
        txtUsuario.setText(usuario);
    }

    public JButton getBtnIngresar() { return btnIngresar; }
    public JButton getBtnRegistrarse() { return btnRegistrarse; }

    public void limpiarPassword() {
        txtPassword.setText("");
        txtPassword.requestFocusInWindow();
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Nero Thread", JOptionPane.WARNING_MESSAGE);
    }
}
