package com.neroshop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Carrito de compras de la venta en curso. Contiene la logica de negocio
 * (no repetir productos, respetar el stock, calcular el total).
 */
public class Carrito {

    private final List<DetalleVenta> items = new ArrayList<>();

    /**
     * Agrega un producto al carrito. Si ya estaba, suma la cantidad.
     *
     * @throws IllegalArgumentException si la cantidad no es valida o supera el stock
     */
    public void agregar(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        DetalleVenta existente = buscar(producto.getId());
        int enCarrito = existente == null ? 0 : existente.getCantidad();
        if (enCarrito + cantidad > producto.getStock()) {
            throw new IllegalArgumentException("Stock insuficiente para \"" + producto.getNombre()
                    + "\". Disponible: " + producto.getStock() + ", en carrito: " + enCarrito + ".");
        }
        if (existente == null) {
            items.add(new DetalleVenta(producto, cantidad));
        } else {
            existente.setCantidad(enCarrito + cantidad);
        }
    }

    public void quitar(int indice) {
        if (indice >= 0 && indice < items.size()) {
            items.remove(indice);
        }
    }

    public void vaciar() {
        items.clear();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public double getTotal() {
        double total = 0;
        for (DetalleVenta d : items) {
            total += d.getSubtotal();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public List<DetalleVenta> getItems() {
        return Collections.unmodifiableList(items);
    }

    private DetalleVenta buscar(int productoId) {
        for (DetalleVenta d : items) {
            if (d.getProductoId() == productoId) {
                return d;
            }
        }
        return null;
    }
}
