package com.neroshop;

import com.neroshop.config.ConexionBD;
import com.neroshop.controller.LoginController;
import com.neroshop.model.dao.UsuarioDAO;
import com.neroshop.view.Estilos;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/** Punto de entrada de la tienda Nero Thread. */
public class Main {

    public static void main(String[] args) {
        Estilos.instalarTema();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> ConexionBD.getInstancia().cerrar()));

        SwingUtilities.invokeLater(() -> {
            try {
                ConexionBD.getInstancia().inicializar();
                new UsuarioDAO().crearUsuariosPorDefecto();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo iniciar la base de datos:\n" + e.getMessage()
                                + "\n\nRevisa la configuracion en db.properties.",
                        "Nero Thread", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            LoginController.mostrarLogin();
        });
    }
}
