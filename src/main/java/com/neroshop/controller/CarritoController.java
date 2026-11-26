package com.neroshop.controller;

import com.neroshop.model.Carrito;
import com.neroshop.model.Cliente;
import com.neroshop.model.DetallePedido;
import com.neroshop.model.Pedido;
import com.neroshop.model.Producto;
import com.neroshop.model.dao.DAOException;
import com.neroshop.model.dao.PedidoDAO;
import com.neroshop.model.dao.ProductoDAO;
import com.neroshop.util.Formato;
import com.neroshop.view.tienda.CarritoPanel;
import com.neroshop.view.tienda.CheckoutDialog;
import com.neroshop.view.tienda.TiendaView;

import javax.swing.SwingUtilities;
import java.time.LocalDateTime;
import java.util.ArrayList;

/** Controla el carrito y el proceso de compra (checkout). */
public class CarritoController implements CarritoPanel.Acciones {

    private final TiendaView ventana;
    private final CarritoPanel vista;
    private final Carrito carrito;
    private final ProductoDAO productoDAO;
    private final PedidoDAO pedidoDAO;
    private final Cliente cliente;
    private final Runnable alComprar;

    public CarritoController(TiendaView ventana, Carrito carrito, ProductoDAO productoDAO, PedidoDAO pedidoDAO,
                             Cliente cliente, Runnable alComprar) {
        this.ventana = ventana;
        this.vista = ventana.getCarritoPanel();
        this.carrito = carrito;
        this.productoDAO = productoDAO;
        this.pedidoDAO = pedidoDAO;
        this.cliente = cliente;
        this.alComprar = alComprar;

        carrito.alCambiar(this::actualizarVista);
        vista.getBtnFinalizar().addActionListener(e -> finalizarCompra());
        vista.getBtnSeguir().addActionListener(e -> ventana.getBtnTienda().doClick());
        vista.getBtnIrTienda().addActionListener(e -> ventana.getBtnTienda().doClick());
        actualizarVista();
    }

    private void actualizarVista() {
        vista.setItems(carrito.getItems(), this);
        String nota = carrito.getEnvio() == 0 && !carrito.estaVacio()
                ? "¡Tu envío es gratis!"
                : "Te faltan " + Formato.moneda(Math.max(0, Carrito.ENVIO_GRATIS_DESDE - carrito.getSubtotal()))
                  + " para envío gratis.";
        vista.setTotales(Formato.moneda(carrito.getSubtotal()),
                carrito.getEnvio() == 0 ? "GRATIS" : Formato.moneda(carrito.getEnvio()),
                Formato.moneda(carrito.getTotal()), nota);
    }

    @Override
    public void cambiarCantidad(int indice, int cantidad) {
        // Se difiere para no reconstruir la fila mientras se procesa el evento de su propio spinner
        SwingUtilities.invokeLater(() -> {
            DetallePedido d = carrito.getItems().get(indice);
            try {
                Producto p = productoDAO.buscarPorId(d.getProductoId());
                carrito.cambiarCantidad(indice, cantidad, p == null ? 0 : p.getStock());
            } catch (IllegalArgumentException | DAOException ex) {
                vista.mostrarError(ex.getMessage());
                actualizarVista(); // restaura el valor valido en el spinner
            }
        });
    }

    @Override
    public void quitar(int indice) {
        // Se difiere para no reconstruir la fila mientras se procesa su propio evento
        SwingUtilities.invokeLater(() -> carrito.quitar(indice));
    }

    private void finalizarCompra() {
        if (carrito.estaVacio()) {
            return;
        }
        CheckoutDialog dialogo = new CheckoutDialog(ventana);
        dialogo.setDatos(cliente.getDireccion(), cliente.getTelefono(), Formato.moneda(carrito.getTotal()));
        dialogo.getBtnConfirmar().addActionListener(e -> confirmar(dialogo));
        dialogo.setVisible(true);
    }

    private void confirmar(CheckoutDialog dialogo) {
        if (dialogo.getDireccion().length() < 5) {
            dialogo.mostrarError("Ingresa una dirección de envío válida.");
            return;
        }
        if (!dialogo.getTelefono().matches("^[0-9+ ]{6,15}$")) {
            dialogo.mostrarError("Ingresa un teléfono de contacto válido.");
            return;
        }
        Pedido pedido = new Pedido();
        pedido.setClienteId(cliente.getId());
        pedido.setFecha(LocalDateTime.now());
        pedido.setSubtotal(carrito.getSubtotal());
        pedido.setEnvio(carrito.getEnvio());
        pedido.setTotal(carrito.getTotal());
        pedido.setMetodoPago(dialogo.getMetodoPago());
        pedido.setEstado(dialogo.getMetodoPago().equals(Pedido.METODOS_PAGO[2]) ? Pedido.PENDIENTE : Pedido.PAGADO);
        pedido.setDireccionEnvio(dialogo.getDireccion());
        pedido.setTelefono(dialogo.getTelefono());
        pedido.setDetalles(new ArrayList<>(carrito.getItems()));
        try {
            int numero = pedidoDAO.registrar(pedido);
            dialogo.dispose();
            vista.mostrarMensaje("¡Gracias por tu compra, " + cliente.getNombres() + "!\n\n"
                    + "Pedido N° " + numero + "  ·  Total " + Formato.moneda(pedido.getTotal()) + "\n"
                    + "Lo enviaremos a: " + pedido.getDireccionEnvio());
            carrito.vaciar();
            alComprar.run();
        } catch (DAOException ex) {
            dialogo.mostrarError(ex.getMessage());
        }
    }
}
