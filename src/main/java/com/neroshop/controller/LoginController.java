package com.neroshop.controller;

import com.neroshop.model.Cliente;
import com.neroshop.model.Usuario;
import com.neroshop.model.dao.ClienteDAO;
import com.neroshop.model.dao.DAOException;
import com.neroshop.model.dao.UsuarioDAO;
import com.neroshop.view.LoginView;
import com.neroshop.view.RegistroDialog;
import com.neroshop.view.admin.AdminView;
import com.neroshop.view.tienda.TiendaView;

/**
 * Controla el inicio de sesion y el registro. Segun el rol abre la tienda
 * (clientes) o el panel de administracion (administradores).
 */
public class LoginController {

    private final LoginView vista;
    private final UsuarioDAO usuarioDAO;

    public LoginController(LoginView vista, UsuarioDAO usuarioDAO) {
        this.vista = vista;
        this.usuarioDAO = usuarioDAO;
        vista.getBtnIngresar().addActionListener(e -> ingresar());
        vista.getBtnRegistrarse().addActionListener(e -> registrarse());
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
            abrirSesion(u);
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void abrirSesion(Usuario u) {
        if (UsuarioDAO.ROL_ADMIN.equals(u.getRol())) {
            AdminView admin = new AdminView();
            new AdminController(admin, u);
            vista.dispose();
            admin.setVisible(true);
        } else {
            Cliente cliente = new ClienteDAO().buscarPorUsuario(u.getId());
            if (cliente == null) {
                vista.mostrarError("Tu cuenta no tiene datos de cliente asociados.");
                return;
            }
            TiendaView tienda = new TiendaView();
            new TiendaController(tienda, cliente);
            vista.dispose();
            tienda.setVisible(true);
        }
    }

    private void registrarse() {
        RegistroDialog dialogo = new RegistroDialog(vista);
        dialogo.getBtnCrear().addActionListener(e -> crearCuenta(dialogo));
        dialogo.setVisible(true);
    }

    private void crearCuenta(RegistroDialog d) {
        String error = Validaciones.cliente(d.getDocumento(), d.getNombres(), d.getApellidos(), d.getEmail());
        if (error == null && d.getDireccion().isEmpty()) {
            error = "Ingresa tu dirección para los envíos.";
        }
        if (error == null && !d.getUsuario().matches("^[A-Za-z0-9._-]{4,20}$")) {
            error = "El usuario debe tener entre 4 y 20 caracteres (letras, números, punto o guion).";
        }
        if (error == null && d.getPassword().length() < 6) {
            error = "La contraseña debe tener al menos 6 caracteres.";
        }
        if (error == null && !d.getPassword().equals(d.getConfirmacion())) {
            error = "Las contraseñas no coinciden.";
        }
        if (error != null) {
            d.mostrarError(error);
            return;
        }
        Cliente c = new Cliente();
        c.setNombres(d.getNombres());
        c.setApellidos(d.getApellidos());
        c.setDocumento(d.getDocumento());
        c.setEmail(d.getEmail());
        c.setTelefono(d.getTelefono());
        c.setDireccion(d.getDireccion());
        try {
            Usuario u = usuarioDAO.registrarCliente(d.getUsuario(), d.getPassword(), c);
            d.dispose();
            abrirSesion(u);
        } catch (DAOException ex) {
            d.mostrarError(ex.getMessage());
        }
    }

    /** Muestra una nueva ventana de login (al iniciar o al cerrar sesion). */
    public static void mostrarLogin() {
        LoginView v = new LoginView();
        new LoginController(v, new UsuarioDAO());
        v.setVisible(true);
    }
}
