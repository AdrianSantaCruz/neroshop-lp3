package com.neroshop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Carrito de compras del cliente. Contiene la logica de negocio (agrupar
 * productos, respetar el stock, calcular envio y total) y avisa a quienes lo
 * observan cada vez que cambia (patron Observer).
 */
public class Carrito {

    /** Costo de envio y monto a partir del cual el envio es gratis. */
    public static final double COSTO_ENVIO = 12.00;
    public static final double ENVIO_GRATIS_DESDE = 199.00;

    private final List<DetallePedido> items = new ArrayList<>();
    private final List<Runnable> observadores = new ArrayList<>();

    public void alCambiar(Runnable observador) {
        observadores.add(observador);
    }

    private void notificar() {
        for (Runnable r : observadores) {
            r.run();
        }
    }

    /**
     * Agrega un producto en una talla. Si ya estaba, suma la cantidad.
     *
     * @throws IllegalArgumentException si los datos no son validos o se supera el stock
     */
    public void agregar(Producto producto, String talla, int cantidad) {
        if (talla == null || talla.isBlank()) {
            throw new IllegalArgumentException("Selecciona una talla.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        int enCarrito = cantidadDelProducto(producto.getId());
        if (enCarrito + cantidad > producto.getStock()) {
            throw new IllegalArgumentException("Solo quedan " + producto.getStock() + " unidades de \""
                    + producto.getNombre() + "\"" + (enCarrito > 0 ? " (ya tienes " + enCarrito + " en el carrito)." : "."));
        }
        DetallePedido existente = buscar(producto.getId(), talla);
        if (existente == null) {
            items.add(new DetallePedido(producto, talla, cantidad));
        } else {
            existente.setCantidad(existente.getCantidad() + cantidad);
        }
        notificar();
    }

    /** Cambia la cantidad de una linea validando contra el stock disponible. */
    public void cambiarCantidad(int indice, int nuevaCantidad, int stockDisponible) {
        DetallePedido d = items.get(indice);
        int otrasTallas = cantidadDelProducto(d.getProductoId()) - d.getCantidad();
        if (nuevaCantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        if (otrasTallas + nuevaCantidad > stockDisponible) {
            throw new IllegalArgumentException("Solo quedan " + stockDisponible + " unidades de \""
                    + d.getProductoNombre() + "\".");
        }
        d.setCantidad(nuevaCantidad);
        notificar();
    }

    public void quitar(int indice) {
        if (indice >= 0 && indice < items.size()) {
            items.remove(indice);
            notificar();
        }
    }

    public void vaciar() {
        items.clear();
        notificar();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public int getCantidadTotal() {
        int n = 0;
        for (DetallePedido d : items) {
            n += d.getCantidad();
        }
        return n;
    }

    public double getSubtotal() {
        double total = 0;
        for (DetallePedido d : items) {
            total += d.getSubtotal();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public double getEnvio() {
        return estaVacio() || getSubtotal() >= ENVIO_GRATIS_DESDE ? 0 : COSTO_ENVIO;
    }

    public double getTotal() {
        return Math.round((getSubtotal() + getEnvio()) * 100.0) / 100.0;
    }

    public List<DetallePedido> getItems() {
        return Collections.unmodifiableList(items);
    }

    private int cantidadDelProducto(int productoId) {
        int n = 0;
        for (DetallePedido d : items) {
            if (d.getProductoId() == productoId) {
                n += d.getCantidad();
            }
        }
        return n;
    }

    private DetallePedido buscar(int productoId, String talla) {
        for (DetallePedido d : items) {
            if (d.getProductoId() == productoId && d.getTalla().equals(talla)) {
                return d;
            }
        }
        return null;
    }
}
