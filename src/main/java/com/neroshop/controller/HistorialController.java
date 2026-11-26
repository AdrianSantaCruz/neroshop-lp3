package com.neroshop.controller;

import com.neroshop.model.DetalleVenta;
import com.neroshop.model.Venta;
import com.neroshop.model.dao.DAOException;
import com.neroshop.model.dao.VentaDAO;
import com.neroshop.util.Formato;
import com.neroshop.view.HistorialPanel;

import java.util.ArrayList;
import java.util.List;

public class HistorialController implements Refrescable {

    private final HistorialPanel vista;
    private final VentaDAO dao;
    private List<Venta> ventas = new ArrayList<>();

    public HistorialController(HistorialPanel vista, VentaDAO dao) {
        this.vista = vista;
        this.dao = dao;
        vista.getBtnActualizar().addActionListener(e -> refrescar());
        vista.getTablaVentas().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle();
            }
        });
        refrescar();
    }

    @Override
    public void refrescar() {
        try {
            ventas = dao.listar();
            vista.getModeloVentas().setRowCount(0);
            vista.getModeloDetalle().setRowCount(0);
            double total = 0;
            for (Venta v : ventas) {
                vista.getModeloVentas().addRow(new Object[]{v.getId(), Formato.fecha(v.getFecha()),
                        v.getClienteNombre(), v.getUsuarioNombre(), v.getTotal()});
                total += v.getTotal();
            }
            vista.setResumen(ventas.size() + " venta(s) registradas  |  Total vendido: " + Formato.moneda(total));
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void mostrarDetalle() {
        vista.getModeloDetalle().setRowCount(0);
        int fila = vista.getTablaVentas().getSelectedRow();
        if (fila < 0) {
            return;
        }
        Venta venta = ventas.get(vista.getTablaVentas().convertRowIndexToModel(fila));
        try {
            for (DetalleVenta d : dao.listarDetalles(venta.getId())) {
                vista.getModeloDetalle().addRow(new Object[]{d.getProductoNombre(), d.getTalla(), d.getColor(),
                        d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal()});
            }
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }
}
