package com.neroshop.model;

import java.util.ArrayList;
import java.util.List;

/** Prenda o accesorio que se vende en la tienda. */
public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private int categoriaId;
    private String categoriaNombre;
    /** Tallas disponibles separadas por coma, por ejemplo "S,M,L,XL". */
    private String tallas;
    private String color;
    private double precio;
    private int stock;
    /** Ruta de la foto (recurso interno o archivo en la carpeta imagenes/). */
    private String imagen;
    private boolean activo = true;

    public List<String> getListaTallas() {
        List<String> lista = new ArrayList<>();
        if (tallas != null) {
            for (String t : tallas.split(",")) {
                if (!t.isBlank()) {
                    lista.add(t.trim());
                }
            }
        }
        return lista;
    }

    public boolean isAgotado() {
        return stock <= 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }

    public String getTallas() { return tallas; }
    public void setTallas(String tallas) { this.tallas = tallas; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return nombre + " - " + color;
    }
}
