package com.neroshop.model;

import java.util.Objects;

/** Cliente de la tienda. */
public class Cliente {

    private int id;
    private String nombres;
    private String apellidos;
    private String documento;
    private String email;
    private String telefono;
    private String direccion;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " - " + documento;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Cliente c && c.id == id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
