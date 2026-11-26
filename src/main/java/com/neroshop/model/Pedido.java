package com.neroshop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Pedido realizado por un cliente en la tienda. */
public class Pedido {

    public static final String PAGADO = "PAGADO";
    public static final String PENDIENTE = "PENDIENTE";
    public static final String ENVIADO = "ENVIADO";
    public static final String ENTREGADO = "ENTREGADO";
    public static final String CANCELADO = "CANCELADO";
    public static final String[] ESTADOS = {PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO};

    public static final String[] METODOS_PAGO = {"Tarjeta de crédito / débito", "Yape / Plin", "Pago contra entrega"};

    private int id;
    private int clienteId;
    private String clienteNombre;
    private LocalDateTime fecha;
    private double subtotal;
    private double envio;
    private double total;
    private String estado;
    private String metodoPago;
    private String direccionEnvio;
    private String telefono;
    private List<DetallePedido> detalles = new ArrayList<>();

    public int getCantidadPrendas() {
        int n = 0;
        for (DetallePedido d : detalles) {
            n += d.getCantidad();
        }
        return n;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getEnvio() { return envio; }
    public void setEnvio(double envio) { this.envio = envio; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}
