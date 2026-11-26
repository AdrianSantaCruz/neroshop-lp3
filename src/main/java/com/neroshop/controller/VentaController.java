package com.neroshop.controller;

import com.neroshop.model.Carrito;
import com.neroshop.model.Cliente;
import com.neroshop.model.DetalleVenta;
import com.neroshop.model.Producto;
import com.neroshop.model.Usuario;
import com.neroshop.model.Venta;
import com.neroshop.model.dao.ClienteDAO;
import com.neroshop.model.dao.DAOException;
import com.neroshop.model.dao.ProductoDAO;
import com.neroshop.model.dao.VentaDAO;
import com.neroshop.util.Formato;
import com.neroshop.view.VentaPanel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Controla el punto de venta: carrito, cliente y registro de la venta. */
public class VentaController implements Refrescable {

    private final VentaPanel vista;
    private final ProductoDAO productoDAO;
    private final ClienteDAO clienteDAO;
    private final VentaDAO ventaDAO;
    private final Usuario usuario;
    private final Carrito carrito = new Carrito();

    private List<Producto> productos = new ArrayList<>();

    public VentaController(VentaPanel vista, ProductoDAO productoDAO, ClienteDAO clienteDAO,
                           VentaDAO ventaDAO, Usuario usuario) {
        this.vista = vista;
        this.productoDAO = productoDAO;
        this.clienteDAO = clienteDAO;
        this.ventaDAO = ventaDAO;
        this.usuario = usuario;

        vista.getTxtBuscar().getDocument().addDocumentListener(new CambioTextoListener(this::cargarProductos));
        vista.getBtnAgregar().addActionListener(e -> agregar());
        vista.getBtnQuitar().addActionListener(e -> quitar());
        vista.getBtnCancelar().addActionListener(e -> vaciar());
        vista.getBtnConfirmar().addActionListener(e -> confirmar());
        vista.getTablaProductos().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    agregar();
                }
            }
        });
        refrescar();
        actualizarCarrito();
    }

    @Override
    public void refrescar() {
        cargarClientes();
        cargarProductos();
    }

    private void cargarClientes() {
        try {
            Object actual = vista.getCboCliente().getSelectedItem();
            vista.getCboCliente().removeAllItems();
            for (Cliente c : clienteDAO.listar("")) {
                vista.getCboCliente().addItem(c);
            }
            if (actual != null) {
                vista.getCboCliente().setSelectedItem(actual);
            }
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void cargarProductos() {
        try {
            productos = productoDAO.listar(vista.getTxtBuscar().getText(), 0);
            vista.getModeloProductos().setRowCount(0);
            for (Producto p : productos) {
                vista.getModeloProductos().addRow(new Object[]{
                        p.getId(), p.getNombre(), p.getTalla(), p.getColor(), p.getPrecio(), p.getStock()});
            }
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void agregar() {
        int fila = vista.getTablaProductos().getSelectedRow();
        if (fila < 0) {
            vista.mostrarError("Selecciona un producto del catalogo.");
            return;
        }
        Producto p = productos.get(vista.getTablaProductos().convertRowIndexToModel(fila));
        int cantidad = (Integer) vista.getSpnCantidad().getValue();
        try {
            carrito.agregar(p, cantidad);
            vista.getSpnCantidad().setValue(1);
            actualizarCarrito();
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void quitar() {
        int fila = vista.getTablaCarrito().getSelectedRow();
        if (fila < 0) {
            vista.mostrarError("Selecciona un producto del carrito.");
            return;
        }
        carrito.quitar(fila);
        actualizarCarrito();
    }

    private void vaciar() {
        if (!carrito.estaVacio() && vista.confirmar("¿Vaciar el carrito?")) {
            carrito.vaciar();
            actualizarCarrito();
        }
    }

    private void actualizarCarrito() {
        vista.getModeloCarrito().setRowCount(0);
        for (DetalleVenta d : carrito.getItems()) {
            vista.getModeloCarrito().addRow(new Object[]{
                    d.getProductoNombre() + " (" + d.getColor() + ")", d.getTalla(),
                    d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal()});
        }
        vista.setTotal(Formato.moneda(carrito.getTotal()));
        vista.getBtnConfirmar().setEnabled(!carrito.estaVacio());
    }

    private void confirmar() {
        Cliente cliente = (Cliente) vista.getCboCliente().getSelectedItem();
        if (cliente == null) {
            vista.mostrarError("Selecciona un cliente.");
            return;
        }
        if (carrito.estaVacio()) {
            vista.mostrarError("El carrito esta vacio.");
            return;
        }
        String resumen = "Cliente: " + cliente.getNombreCompleto()
                + "\nProductos: " + carrito.getItems().size()
                + "\nTotal: " + Formato.moneda(carrito.getTotal())
                + "\n\n¿Confirmar la venta?";
        if (!vista.confirmar(resumen)) {
            return;
        }

        Venta venta = new Venta();
        venta.setClienteId(cliente.getId());
        venta.setUsuarioId(usuario.getId());
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(carrito.getTotal());
        venta.setDetalles(new ArrayList<>(carrito.getItems()));
        try {
            int id = ventaDAO.registrar(venta);
            vista.mostrarMensaje("Venta N° " + id + " registrada con exito.\nTotal: " + Formato.moneda(venta.getTotal()));
            carrito.vaciar();
            actualizarCarrito();
            cargarProductos();
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
            cargarProductos();
        }
    }
}
