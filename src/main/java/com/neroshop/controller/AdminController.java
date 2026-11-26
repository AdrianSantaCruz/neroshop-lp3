package com.neroshop.controller;

import com.neroshop.model.Usuario;
import com.neroshop.model.dao.CategoriaDAO;
import com.neroshop.model.dao.ClienteDAO;
import com.neroshop.model.dao.PedidoDAO;
import com.neroshop.model.dao.ProductoDAO;
import com.neroshop.view.admin.AdminView;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * Controlador del panel de administracion: crea los controladores de cada
 * modulo y recarga los datos de la pestana seleccionada.
 */
public class AdminController {

    private final AdminView vista;
    private final List<Refrescable> controladores;

    public AdminController(AdminView vista, Usuario usuario) {
        this.vista = vista;
        vista.setUsuario(usuario.getNombre() + "  ·  " + usuario.getRol());

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        // El orden coincide con el de las pestanas en AdminView
        controladores = List.of(
                new ProductoController(vista.getProductoPanel(), new ProductoDAO(), categoriaDAO),
                new CategoriaController(vista.getCategoriaPanel(), categoriaDAO),
                new ClienteController(vista.getClientePanel(), new ClienteDAO()),
                new PedidosAdminController(vista.getPedidosPanel(), new PedidoDAO()));

        vista.getPestanas().addChangeListener(e -> {
            int i = vista.getPestanas().getSelectedIndex();
            if (i >= 0 && i < controladores.size()) {
                controladores.get(i).refrescar();
            }
        });
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    private void cerrarSesion() {
        int r = JOptionPane.showConfirmDialog(vista, "¿Deseas cerrar sesión?", "Nero Thread", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            vista.dispose();
            LoginController.mostrarLogin();
        }
    }
}
