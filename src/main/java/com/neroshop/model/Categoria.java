package com.neroshop.model;

import java.util.Objects;

/** Categoria de prendas (Polos, Pantalones, Casacas, ...). */
public class Categoria {

    private int id;
    private String nombre;
    private String descripcion;

    public Categoria() {
    }

    public Categoria(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** Se usa para mostrar la categoria en los JComboBox. */
    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Categoria c && c.id == id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
