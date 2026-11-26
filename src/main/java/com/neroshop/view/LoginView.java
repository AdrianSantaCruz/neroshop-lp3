package com.neroshop.view;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

/** Ventana de inicio de sesion. */
public class LoginView extends JFrame {

    private final JTextField txtUsuario = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JButton btnIngresar = Estilos.boton("Ingresar", true);

    public LoginView() {
        super("NeroShop - Iniciar sesion");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(780, 460);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel contenido = new JPanel(new GridLayout(1, 2));
        contenido.add(crearPanelMarca());
        contenido.add(crearPanelFormulario());
        setContentPane(contenido);

        getRootPane().setDefaultButton(btnIngresar);
    }

    private JPanel crearPanelMarca() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Estilos.FONDO_OSCURO);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("NEROSHOP");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel linea = new JPanel();
        linea.setBackground(Estilos.ACENTO);
        linea.setPreferredSize(new Dimension(70, 4));
        linea.setMaximumSize(new Dimension(70, 4));
        linea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel slogan = new JLabel("Viste diferente. Viste Nero.");
        slogan.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        slogan.setForeground(Estilos.TEXTO_SECUNDARIO);
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        textos.add(logo);
        textos.add(Box.createVerticalStrut(6));
        textos.add(linea);
        textos.add(Box.createVerticalStrut(12));
        textos.add(slogan);
        panel.add(textos);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel exterior = new JPanel(new GridBagLayout());
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(280, 280));

        JLabel titulo = new JLabel("Bienvenido");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel subtitulo = new JLabel("Ingresa tus credenciales para continuar");
        subtitulo.setForeground(Estilos.TEXTO_SECUNDARIO);

        Estilos.placeholder(txtUsuario, "Usuario");
        Estilos.placeholder(txtPassword, "Contraseña");
        txtPassword.putClientProperty("JTextField.showRevealButton", true);

        for (JLabel l : new JLabel[]{titulo, subtitulo}) {
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        for (Component c : new Component[]{txtUsuario, txtPassword, btnIngresar}) {
            ((javax.swing.JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
            c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        }

        JLabel ayuda = new JLabel("<html>Demo: <b>admin</b> / <b>admin123</b></html>");
        ayuda.setForeground(Estilos.TEXTO_SECUNDARIO);
        ayuda.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        ayuda.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(titulo);
        form.add(Box.createVerticalStrut(4));
        form.add(subtitulo);
        form.add(Box.createVerticalStrut(24));
        form.add(etiqueta("Usuario"));
        form.add(txtUsuario);
        form.add(Box.createVerticalStrut(12));
        form.add(etiqueta("Contraseña"));
        form.add(txtPassword);
        form.add(Box.createVerticalStrut(20));
        form.add(btnIngresar);
        form.add(ayuda);

        exterior.add(form, new java.awt.GridBagConstraints());
        JPanel envoltura = new JPanel(new BorderLayout());
        envoltura.add(exterior);
        return envoltura;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Estilos.TEXTO_SECUNDARIO);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public JButton getBtnIngresar() {
        return btnIngresar;
    }

    public void limpiarPassword() {
        txtPassword.setText("");
        txtPassword.requestFocusInWindow();
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "NeroShop", JOptionPane.ERROR_MESSAGE);
    }
}
