package com.neroshop.controller;

import com.neroshop.model.Cliente;
import com.neroshop.model.DetallePedido;
import com.neroshop.model.Pedido;
import com.neroshop.model.dao.DAOException;
import com.neroshop.model.dao.PedidoDAO;
import com.neroshop.util.Formato;
import com.neroshop.view.tienda.MisPedidosPanel;

import java.util.ArrayList;
import java.util.List;

/** Historial de compras del cliente que inicio sesion. */
public class MisPedidosController implements Refrescable {

    private final MisPedidosPanel vista;
    private final PedidoDAO dao;
    private final Cliente cliente;
    private List<Pedido> pedidos = new ArrayList<>();

    public MisPedidosController(MisPedidosPanel vista, PedidoDAO dao, Cliente cliente) {
        this.vista = vista;
        this.dao = dao;
        this.cliente = cliente;
        vista.getTablaPedidos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle();
            }
        });
    }

    @Override
    public void refrescar() {
        try {
            pedidos = dao.listarPorCliente(cliente.getId());
            vista.getModeloPedidos().setRowCount(0);
            vista.getModeloDetalle().setRowCount(0);
            for (Pedido p : pedidos) {
                p.setDetalles(dao.listarDetalles(p.getId()));
                vista.getModeloPedidos().addRow(new Object[]{p.getId(), Formato.fecha(p.getFecha()),
                        p.getCantidadPrendas(), p.getMetodoPago(), p.getEstado(), p.getTotal()});
            }
            if (!pedidos.isEmpty()) {
                vista.getTablaPedidos().setRowSelectionInterval(0, 0);
            } else {
                vista.setInfoEnvio("Aún no tienes pedidos.");
            }
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void mostrarDetalle() {
        vista.getModeloDetalle().setRowCount(0);
        int fila = vista.getTablaPedidos().getSelectedRow();
        if (fila < 0) {
            return;
        }
        Pedido p = pedidos.get(vista.getTablaPedidos().convertRowIndexToModel(fila));
        vista.setInfoEnvio("Envío a " + p.getDireccionEnvio() + "  ·  Costo de envío "
                + (p.getEnvio() == 0 ? "GRATIS" : Formato.moneda(p.getEnvio())));
        for (DetallePedido d : p.getDetalles()) {
            vista.getModeloDetalle().addRow(new Object[]{d.getProductoNombre(), d.getTalla(), d.getColor(),
                    d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal()});
        }
    }
}
