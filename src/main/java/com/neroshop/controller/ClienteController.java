package com.neroshop.controller;

import com.neroshop.model.Cliente;
import com.neroshop.model.dao.ClienteDAO;
import com.neroshop.model.dao.DAOException;
import com.neroshop.view.ClientePanel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteController implements Refrescable {

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern DOCUMENTO = Pattern.compile("^[0-9A-Za-z]{6,15}$");

    private final ClientePanel vista;
    private final ClienteDAO dao;
    private List<Cliente> clientes = new ArrayList<>();
    private Cliente seleccionado;

    public ClienteController(ClientePanel vista, ClienteDAO dao) {
        this.vista = vista;
        this.dao = dao;
        vista.getBtnNuevo().addActionListener(e -> limpiar());
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getTxtBuscar().getDocument().addDocumentListener(new CambioTextoListener(this::refrescar));
        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionar();
            }
        });
        refrescar();
        limpiar();
    }

    @Override
    public void refrescar() {
        try {
            clientes = dao.listar(vista.getTxtBuscar().getText());
            vista.getModelo().setRowCount(0);
            for (Cliente c : clientes) {
                vista.getModelo().addRow(new Object[]{c.getId(), c.getDocumento(), c.getNombres(),
                        c.getApellidos(), c.getEmail(), c.getTelefono()});
            }
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void seleccionar() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila < 0) {
            return;
        }
        seleccionado = clientes.get(vista.getTabla().convertRowIndexToModel(fila));
        vista.getTxtDocumento().setText(seleccionado.getDocumento());
        vista.getTxtNombres().setText(seleccionado.getNombres());
        vista.getTxtApellidos().setText(seleccionado.getApellidos());
        vista.getTxtEmail().setText(nvl(seleccionado.getEmail()));
        vista.getTxtTelefono().setText(nvl(seleccionado.getTelefono()));
        vista.getTxtDireccion().setText(nvl(seleccionado.getDireccion()));
        vista.setModoEdicion(true);
    }

    private void limpiar() {
        seleccionado = null;
        vista.limpiarFormulario();
    }

    private void guardar() {
        Cliente c = new Cliente();
        c.setId(seleccionado == null ? 0 : seleccionado.getId());
        c.setDocumento(vista.getTxtDocumento().getText().trim());
        c.setNombres(vista.getTxtNombres().getText().trim());
        c.setApellidos(vista.getTxtApellidos().getText().trim());
        c.setEmail(vista.getTxtEmail().getText().trim());
        c.setTelefono(vista.getTxtTelefono().getText().trim());
        c.setDireccion(vista.getTxtDireccion().getText().trim());

        if (!DOCUMENTO.matcher(c.getDocumento()).matches()) {
            vista.mostrarError("El documento debe tener entre 6 y 15 caracteres alfanumericos.");
            return;
        }
        if (c.getNombres().isEmpty() || c.getApellidos().isEmpty()) {
            vista.mostrarError("Nombres y apellidos son obligatorios.");
            return;
        }
        if (!c.getEmail().isEmpty() && !EMAIL.matcher(c.getEmail()).matches()) {
            vista.mostrarError("El email no tiene un formato valido.");
            return;
        }
        try {
            if (seleccionado == null) {
                dao.insertar(c);
                vista.mostrarMensaje("Cliente registrado correctamente.");
            } else {
                dao.actualizar(c);
                vista.mostrarMensaje("Cliente actualizado correctamente.");
            }
            refrescar();
            limpiar();
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            return;
        }
        if (!vista.confirmar("¿Eliminar al cliente " + seleccionado.getNombreCompleto() + "?")) {
            return;
        }
        try {
            dao.eliminar(seleccionado.getId());
            refrescar();
            limpiar();
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}
