package com.neroshop.controller;

import com.neroshop.model.Carrito;
import com.neroshop.model.Categoria;
import com.neroshop.model.Cliente;
import com.neroshop.model.Producto;
import com.neroshop.model.dao.CategoriaDAO;
import com.neroshop.model.dao.DAOException;
import com.neroshop.model.dao.PedidoDAO;
import com.neroshop.model.dao.ProductoDAO;
import com.neroshop.view.tienda.CatalogoPanel;
import com.neroshop.view.tienda.ProductoDetalleDialog;
import com.neroshop.view.tienda.TiendaView;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * Controlador principal de la tienda: navegacion, catalogo (filtros,
 * busqueda, orden) y detalle de producto. Delega el carrito y los pedidos a
 * sus propios controladores.
 */
public class TiendaController {

    private final TiendaView vista;
    private final CatalogoPanel catalogo;
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final Carrito carrito = new Carrito();
    private final MisPedidosController misPedidosController;

    private int categoriaSeleccionada = 0;

    public TiendaController(TiendaView vista, Cliente cliente) {
        this.vista = vista;
        this.catalogo = vista.getCatalogoPanel();
        vista.setUsuario(cliente.getNombres());

        PedidoDAO pedidoDAO = new PedidoDAO();
        misPedidosController = new MisPedidosController(vista.getMisPedidosPanel(), pedidoDAO, cliente);
        new CarritoController(vista, carrito, productoDAO, pedidoDAO, cliente, this::despuesDeComprar);

        carrito.alCambiar(() -> vista.setCantidadCarrito(carrito.getCantidadTotal()));

        vista.getBtnTienda().addActionListener(e -> irACatalogo());
        vista.getBtnCarrito().addActionListener(e -> vista.mostrar(TiendaView.CARRITO));
        vista.getBtnPedidos().addActionListener(e -> {
            misPedidosController.refrescar();
            vista.mostrar(TiendaView.PEDIDOS);
        });
        vista.getBtnSalir().addActionListener(e -> salir());

        catalogo.getTxtBuscar().getDocument().addDocumentListener(new CambioTextoListener(this::cargarProductos));
        catalogo.getCboOrden().addActionListener(e -> cargarProductos());
        catalogo.alElegirCategoria(id -> {
            categoriaSeleccionada = id;
            cargarProductos();
        });

        cargarCategorias();
        cargarProductos();
        vista.mostrar(TiendaView.CATALOGO);
    }

    private void irACatalogo() {
        cargarProductos();
        vista.mostrar(TiendaView.CATALOGO);
    }

    private void cargarCategorias() {
        try {
            List<Categoria> categorias = categoriaDAO.listar();
            catalogo.setCategorias(categorias, categoriaSeleccionada);
        } catch (DAOException ex) {
            catalogo.mostrarError(ex.getMessage());
        }
    }

    private void cargarProductos() {
        try {
            List<Producto> productos = productoDAO.listar(catalogo.getTxtBuscar().getText(), categoriaSeleccionada,
                    (ProductoDAO.Orden) catalogo.getCboOrden().getSelectedItem());
            catalogo.setProductos(productos, this::verDetalle);
        } catch (DAOException ex) {
            catalogo.mostrarError(ex.getMessage());
        }
    }

    /** Abre el detalle del producto y, si el cliente lo pide, lo agrega al carrito. */
    private void verDetalle(Producto producto) {
        Producto actualizado = productoDAO.buscarPorId(producto.getId());
        if (actualizado == null) {
            catalogo.mostrarError("Este producto ya no está disponible.");
            cargarProductos();
            return;
        }
        ProductoDetalleDialog dialogo = new ProductoDetalleDialog(vista, actualizado);
        dialogo.getBtnAgregar().addActionListener(e -> {
            try {
                carrito.agregar(actualizado, dialogo.getTallaSeleccionada(), dialogo.getCantidad());
                dialogo.dispose();
                int opcion = JOptionPane.showOptionDialog(vista,
                        actualizado.getNombre() + " (talla " + dialogo.getTallaSeleccionada() + ") se agregó a tu carrito.",
                        "Agregado al carrito", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                        new String[]{"Seguir comprando", "Ver carrito"}, "Ver carrito");
                if (opcion == 1) {
                    vista.mostrar(TiendaView.CARRITO);
                }
            } catch (IllegalArgumentException ex) {
                dialogo.mostrarError(ex.getMessage());
            }
        });
        dialogo.setVisible(true);
    }

    /** Despues de una compra: refresca stock del catalogo y muestra "Mis pedidos". */
    private void despuesDeComprar() {
        cargarProductos();
        misPedidosController.refrescar();
        vista.mostrar(TiendaView.PEDIDOS);
    }

    private void salir() {
        String mensaje = carrito.estaVacio() ? "¿Deseas cerrar sesión?"
                : "Tienes prendas en tu carrito. ¿Deseas cerrar sesión de todas formas?";
        int r = JOptionPane.showConfirmDialog(vista, mensaje, "Nero Thread", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            vista.dispose();
            LoginController.mostrarLogin();
        }
    }
}
