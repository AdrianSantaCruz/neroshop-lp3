package com.neroshop.model;

/** Linea de una venta: un producto con su cantidad y precio. */
public class DetalleVenta {

    private int productoId;
    private String productoNombre;
    private String talla;
    private String color;
    private int cantidad;
    private double precioUnitario;

    public DetalleVenta() {
    }

    public DetalleVenta(Producto p, int cantidad) {
        this.productoId = p.getId();
        this.productoNombre = p.getNombre();
        this.talla = p.getTalla();
        this.color = p.getColor();
        this.precioUnitario = p.getPrecio();
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return Math.round(cantidad * precioUnitario * 100.0) / 100.0;
    }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}
