package com.neroshop.model;

/** Linea de un pedido (o del carrito): un producto en una talla con su cantidad. */
public class DetallePedido {

    private int productoId;
    private String productoNombre;
    private String imagen;
    private String talla;
    private String color;
    private int cantidad;
    private double precioUnitario;

    public DetallePedido() {
    }

    public DetallePedido(Producto p, String talla, int cantidad) {
        this.productoId = p.getId();
        this.productoNombre = p.getNombre();
        this.imagen = p.getImagen();
        this.color = p.getColor();
        this.precioUnitario = p.getPrecio();
        this.talla = talla;
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return Math.round(cantidad * precioUnitario * 100.0) / 100.0;
    }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}
