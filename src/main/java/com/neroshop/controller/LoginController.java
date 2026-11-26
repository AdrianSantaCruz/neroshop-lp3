package com.neroshop.controller;

import com.neroshop.model.Usuario;
import com.neroshop.model.dao.DAOException;
import com.neroshop.model.dao.UsuarioDAO;
import com.neroshop.view.LoginView;
import com.neroshop.view.MainView;

/** Controla el inicio de sesion y abre la ventana principal. */
public class LoginController {

    private final LoginView vista;
    private final UsuarioDAO usuarioDAO;

    public LoginController(LoginView vista, UsuarioDAO usuarioDAO) {
        this.vista = vista;
        this.usuarioDAO = usuarioDAO;
        vista.getBtnIngresar().addActionListener(e -> ingresar());
    }

    private void ingresar() {
        String usuario = vista.getUsuario();
        String password = vista.getPassword();
        if (usuario.isEmpty() || password.isEmpty()) {
            vista.mostrarError("Ingresa usuario y contraseña.");
            return;
        }
        try {
            Usuario u = usuarioDAO.autenticar(usuario, password);
            if (u == null) {
                vista.mostrarError("Usuario o contraseña incorrectos.");
                vista.limpiarPassword();
                return;
            }
            vista.dispose();
            MainView principal = new MainView();
            new MainController(principal, u);
            principal.setVisible(true);
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    /** Muestra una nueva ventana de login (usado al cerrar sesion). */
    public static void mostrarLogin() {
        LoginView v = new LoginView();
        new LoginController(v, new UsuarioDAO());
        v.setVisible(true);
    }
}
