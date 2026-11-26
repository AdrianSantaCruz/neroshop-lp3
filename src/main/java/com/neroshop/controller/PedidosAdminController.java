package com.neroshop.controller;

import com.neroshop.model.DetallePedido;
import com.neroshop.model.Pedido;
import com.neroshop.model.dao.DAOException;
import com.neroshop.model.dao.PedidoDAO;
import com.neroshop.util.Formato;
import com.neroshop.view.admin.PedidosAdminPanel;

import java.util.ArrayList;
import java.util.List;

/** Gestion de pedidos del panel de administracion. */
public class PedidosAdminController implements Refrescable {

    private final PedidosAdminPanel vista;
    private final PedidoDAO dao;
    private List<Pedido> pedidos = new ArrayList<>();
    private Pedido seleccionado;

    public PedidosAdminController(PedidosAdminPanel vista, PedidoDAO dao) {
        this.vista = vista;
        this.dao = dao;
        vista.getBtnActualizar().addActionListener(e -> refrescar());
        vista.getBtnCambiarEstado().addActionListener(e -> cambiarEstado());
        vista.getTablaPedidos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle();
            }
        });
        refrescar();
    }

    @Override
    public void refrescar() {
        try {
            pedidos = dao.listarTodos();
            vista.getModeloPedidos().setRowCount(0);
            vista.getModeloDetalle().setRowCount(0);
            vista.setInfoEnvio(" ");
            vista.setEdicionEstado(false);
            seleccionado = null;
            double vendido = 0;
            for (Pedido p : pedidos) {
                vista.getModeloPedidos().addRow(new Object[]{p.getId(), Formato.fecha(p.getFecha()),
                        p.getClienteNombre(), p.getMetodoPago(), p.getEstado(), p.getTotal()});
                if (!Pedido.CANCELADO.equals(p.getEstado())) {
                    vendido += p.getTotal();
                }
            }
            vista.setResumen(pedidos.size() + " pedido(s)  ·  Total vendido (sin cancelados): " + Formato.moneda(vendido));
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void mostrarDetalle() {
        vista.getModeloDetalle().setRowCount(0);
        int fila = vista.getTablaPedidos().getSelectedRow();
        if (fila < 0) {
            seleccionado = null;
            vista.setEdicionEstado(false);
            return;
        }
        seleccionado = pedidos.get(vista.getTablaPedidos().convertRowIndexToModel(fila));
        vista.getCboEstado().setSelectedItem(seleccionado.getEstado());
        vista.setEdicionEstado(!Pedido.CANCELADO.equals(seleccionado.getEstado()));
        vista.setInfoEnvio("Envío a: " + seleccionado.getDireccionEnvio()
                + (seleccionado.getTelefono() == null ? "" : "  ·  Tel. " + seleccionado.getTelefono())
                + "  ·  Envío " + Formato.moneda(seleccionado.getEnvio()));
        try {
            for (DetallePedido d : dao.listarDetalles(seleccionado.getId())) {
                vista.getModeloDetalle().addRow(new Object[]{d.getProductoNombre(), d.getTalla(), d.getColor(),
                        d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal()});
            }
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void cambiarEstado() {
        if (seleccionado == null) {
            return;
        }
        String nuevo = (String) vista.getCboEstado().getSelectedItem();
        if (nuevo == null || nuevo.equals(seleccionado.getEstado())) {
            return;
        }
        if (Pedido.CANCELADO.equals(nuevo)
                && !vista.confirmar("¿Cancelar el pedido N° " + seleccionado.getId() + "?\nLas prendas volverán al stock.")) {
            return;
        }
        try {
            dao.actualizarEstado(seleccionado, nuevo);
            vista.mostrarMensaje("Pedido N° " + seleccionado.getId() + " actualizado a " + nuevo + ".");
            refrescar();
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }
}
