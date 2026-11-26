package com.neroshop.controller;

import com.neroshop.model.Usuario;
import com.neroshop.model.dao.CategoriaDAO;
import com.neroshop.model.dao.ClienteDAO;
import com.neroshop.model.dao.ProductoDAO;
import com.neroshop.model.dao.VentaDAO;
import com.neroshop.view.MainView;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * Controlador principal: crea los controladores de cada modulo y recarga
 * los datos de la pestaña seleccionada.
 */
public class MainController {

    private final MainView vista;
    private final List<Refrescable> controladores;

    public MainController(MainView vista, Usuario usuario) {
        this.vista = vista;
        vista.setUsuario("Sesion: " + usuario.getNombre() + "  |  " + usuario.getRol());

        ProductoDAO productoDAO = new ProductoDAO();
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        VentaDAO ventaDAO = new VentaDAO();

        // El orden coincide con el orden de las pestañas en MainView
        controladores = List.of(
                new VentaController(vista.getVentaPanel(), productoDAO, clienteDAO, ventaDAO, usuario),
                new ProductoController(vista.getProductoPanel(), productoDAO, categoriaDAO),
                new CategoriaController(vista.getCategoriaPanel(), categoriaDAO),
                new ClienteController(vista.getClientePanel(), clienteDAO),
                new HistorialController(vista.getHistorialPanel(), ventaDAO));

        vista.getPestanas().addChangeListener(e -> {
            int i = vista.getPestanas().getSelectedIndex();
            if (i >= 0 && i < controladores.size()) {
                controladores.get(i).refrescar();
            }
        });
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    private void cerrarSesion() {
        int r = JOptionPane.showConfirmDialog(vista, "¿Deseas cerrar sesion?", "NeroShop",
                JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            vista.dispose();
            LoginController.mostrarLogin();
        }
    }
}
