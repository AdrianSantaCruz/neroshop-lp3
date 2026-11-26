package com.neroshop.view;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;

/** Formulario para que un cliente nuevo cree su cuenta. */
public class RegistroDialog extends JDialog {

    private final JTextField txtNombres = Estilos.alto(new JTextField());
    private final JTextField txtApellidos = Estilos.alto(new JTextField());
    private final JTextField txtDocumento = Estilos.alto(new JTextField());
    private final JTextField txtEmail = Estilos.alto(new JTextField());
    private final JTextField txtTelefono = Estilos.alto(new JTextField());
    private final JTextField txtDireccion = Estilos.alto(new JTextField());
    private final JTextField txtUsuario = Estilos.alto(new JTextField());
    private final JPasswordField txtPassword = Estilos.alto(new JPasswordField());
    private final JPasswordField txtConfirmar = Estilos.alto(new JPasswordField());
    private final JButton btnCrear = Estilos.botonPrimario("Crear cuenta");
    private final JButton btnCancelar = Estilos.botonSecundario("Cancelar");

    public RegistroDialog(Window owner) {
        super(owner, "Crear cuenta", ModalityType.APPLICATION_MODAL);
        setSize(640, 680);
        setResizable(false);
        setLocationRelativeTo(owner);

        JPanel contenido = new JPanel(new BorderLayout(0, 22));
        contenido.setBackground(Estilos.FONDO);
        contenido.setBorder(Estilos.margen(32));
        contenido.add(Estilos.lblTitulo("Crea tu cuenta", 28), BorderLayout.NORTH);

        JPanel columnas = new JPanel(new GridLayout(1, 2, 20, 0));
        columnas.setOpaque(false);
        JPanel izq = new JPanel(new GridBagLayout());
        izq.setOpaque(false);
        Estilos.agregarCampo(izq, 0, "Nombres", txtNombres);
        Estilos.agregarCampo(izq, 1, "Apellidos", txtApellidos);
        Estilos.agregarCampo(izq, 2, "DNI / Documento", txtDocumento);
        Estilos.agregarCampo(izq, 3, "Email", txtEmail);
        Estilos.agregarCampo(izq, 4, "Teléfono", txtTelefono);
        JPanel der = new JPanel(new GridBagLayout());
        der.setOpaque(false);
        Estilos.agregarCampo(der, 0, "Dirección", txtDireccion);
        Estilos.agregarCampo(der, 1, "Usuario", txtUsuario);
        Estilos.agregarCampo(der, 2, "Contraseña", txtPassword);
        Estilos.agregarCampo(der, 3, "Confirmar contraseña", txtConfirmar);
        columnas.add(arriba(izq));
        columnas.add(arriba(der));
        contenido.add(columnas, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 0));
        botones.setOpaque(false);
        botones.add(btnCancelar);
        botones.add(btnCrear);
        contenido.add(botones, BorderLayout.SOUTH);

        setContentPane(contenido);
        btnCancelar.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnCrear);
    }

    private static JPanel arriba(JPanel p) {
        JPanel envoltura = new JPanel(new BorderLayout());
        envoltura.setOpaque(false);
        envoltura.add(p, BorderLayout.NORTH);
        return envoltura;
    }

    public String getNombres() { return txtNombres.getText().trim(); }
    public String getApellidos() { return txtApellidos.getText().trim(); }
    public String getDocumento() { return txtDocumento.getText().trim(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getTelefono() { return txtTelefono.getText().trim(); }
    public String getDireccion() { return txtDireccion.getText().trim(); }
    public String getUsuario() { return txtUsuario.getText().trim(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public String getConfirmacion() { return new String(txtConfirmar.getPassword()); }
    public JButton getBtnCrear() { return btnCrear; }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Nero Thread", JOptionPane.WARNING_MESSAGE);
    }
}
