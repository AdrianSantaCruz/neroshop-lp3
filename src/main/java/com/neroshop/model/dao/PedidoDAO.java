package com.neroshop.model.dao;

import com.neroshop.model.DetallePedido;
import com.neroshop.model.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO extends BaseDAO {

    private static final DateTimeFormatter FORMATO_BD = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String SELECT_PEDIDOS =
            "SELECT p.id, p.cliente_id, c.nombres, c.apellidos, p.fecha, p.subtotal, p.envio, p.total, "
            + "p.estado, p.metodo_pago, p.direccion_envio, p.telefono "
            + "FROM pedidos p INNER JOIN clientes c ON c.id = p.cliente_id ";

    /**
     * Registra el pedido, sus detalles y descuenta el stock dentro de una
     * transaccion: si algo falla (por ejemplo, alguien compro la ultima
     * unidad) no se guarda nada.
     *
     * @return el numero de pedido generado
     */
    public int registrar(Pedido pedido) {
        Connection c = conexion();
        try {
            c.setAutoCommit(false);

            int pedidoId;
            String sqlPedido = "INSERT INTO pedidos (cliente_id, fecha, subtotal, envio, total, estado, metodo_pago, "
                    + "direccion_envio, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, pedido.getClienteId());
                ps.setString(2, pedido.getFecha().format(FORMATO_BD));
                ps.setDouble(3, pedido.getSubtotal());
                ps.setDouble(4, pedido.getEnvio());
                ps.setDouble(5, pedido.getTotal());
                ps.setString(6, pedido.getEstado());
                ps.setString(7, pedido.getMetodoPago());
                ps.setString(8, pedido.getDireccionEnvio());
                ps.setString(9, vacioANull(pedido.getTelefono()));
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No se obtuvo el numero de pedido.");
                    }
                    pedidoId = keys.getInt(1);
                }
            }

            String sqlDetalle = "INSERT INTO detalle_pedido (pedido_id, producto_id, talla, cantidad, precio_unitario, subtotal) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id = ? AND activo = 1 AND stock >= ?";
            try (PreparedStatement psDetalle = c.prepareStatement(sqlDetalle);
                 PreparedStatement psStock = c.prepareStatement(sqlStock)) {
                for (DetallePedido d : pedido.getDetalles()) {
                    psStock.setInt(1, d.getCantidad());
                    psStock.setInt(2, d.getProductoId());
                    psStock.setInt(3, d.getCantidad());
                    if (psStock.executeUpdate() == 0) {
                        throw new DAOException("Ya no hay stock suficiente de \"" + d.getProductoNombre()
                                + "\". Actualiza tu carrito e intenta de nuevo.");
                    }
                    psDetalle.setInt(1, pedidoId);
                    psDetalle.setInt(2, d.getProductoId());
                    psDetalle.setString(3, d.getTalla());
                    psDetalle.setInt(4, d.getCantidad());
                    psDetalle.setDouble(5, d.getPrecioUnitario());
                    psDetalle.setDouble(6, d.getSubtotal());
                    psDetalle.executeUpdate();
                }
            }

            c.commit();
            pedido.setId(pedidoId);
            return pedidoId;
        } catch (SQLException | DAOException e) {
            rollback(c);
            if (e instanceof DAOException de) {
                throw de;
            }
            throw new DAOException("Error al registrar el pedido: " + e.getMessage(), e);
        } finally {
            autoCommit(c);
        }
    }

    /** Todos los pedidos (panel de administracion). */
    public List<Pedido> listarTodos() {
        return listar(SELECT_PEDIDOS + "ORDER BY p.id DESC", 0);
    }

    /** Pedidos de un cliente (seccion "Mis pedidos"). */
    public List<Pedido> listarPorCliente(int clienteId) {
        return listar(SELECT_PEDIDOS + "WHERE p.cliente_id = ? ORDER BY p.id DESC", clienteId);
    }

    private List<Pedido> listar(String sql, int clienteId) {
        List<Pedido> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            if (clienteId > 0) {
                ps.setInt(1, clienteId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getInt("id"));
                    p.setClienteId(rs.getInt("cliente_id"));
                    p.setClienteNombre(rs.getString("nombres") + " " + rs.getString("apellidos"));
                    p.setFecha(leerFecha(rs.getString("fecha")));
                    p.setSubtotal(rs.getDouble("subtotal"));
                    p.setEnvio(rs.getDouble("envio"));
                    p.setTotal(rs.getDouble("total"));
                    p.setEstado(rs.getString("estado"));
                    p.setMetodoPago(rs.getString("metodo_pago"));
                    p.setDireccionEnvio(rs.getString("direccion_envio"));
                    p.setTelefono(rs.getString("telefono"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar pedidos: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<DetallePedido> listarDetalles(int pedidoId) {
        String sql = "SELECT d.producto_id, p.nombre, p.imagen, p.color, d.talla, d.cantidad, d.precio_unitario "
                + "FROM detalle_pedido d INNER JOIN productos p ON p.id = d.producto_id "
                + "WHERE d.pedido_id = ? ORDER BY d.id";
        List<DetallePedido> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido d = new DetallePedido();
                    d.setProductoId(rs.getInt("producto_id"));
                    d.setProductoNombre(rs.getString("nombre"));
                    d.setImagen(rs.getString("imagen"));
                    d.setColor(rs.getString("color"));
                    d.setTalla(rs.getString("talla"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar el detalle del pedido: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Cambia el estado de un pedido. Si se cancela, devuelve las prendas al
     * stock (en una transaccion).
     */
    public void actualizarEstado(Pedido pedido, String nuevoEstado) {
        if (Pedido.CANCELADO.equals(pedido.getEstado())) {
            throw new DAOException("El pedido ya fue cancelado y no se puede modificar.");
        }
        Connection c = conexion();
        try {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement("UPDATE pedidos SET estado = ? WHERE id = ?")) {
                ps.setString(1, nuevoEstado);
                ps.setInt(2, pedido.getId());
                ps.executeUpdate();
            }
            if (Pedido.CANCELADO.equals(nuevoEstado)) {
                String sql = "UPDATE productos SET stock = stock + "
                        + "(SELECT SUM(d.cantidad) FROM detalle_pedido d WHERE d.pedido_id = ? AND d.producto_id = productos.id) "
                        + "WHERE id IN (SELECT producto_id FROM detalle_pedido WHERE pedido_id = ?)";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, pedido.getId());
                    ps.setInt(2, pedido.getId());
                    ps.executeUpdate();
                }
            }
            c.commit();
            pedido.setEstado(nuevoEstado);
        } catch (SQLException e) {
            rollback(c);
            throw new DAOException("Error al actualizar el pedido: " + e.getMessage(), e);
        } finally {
            autoCommit(c);
        }
    }

    private static void rollback(Connection c) {
        try {
            c.rollback();
        } catch (SQLException ignored) {
            // Se informa el error original
        }
    }

    private static void autoCommit(Connection c) {
        try {
            c.setAutoCommit(true);
        } catch (SQLException ignored) {
            // Nada que hacer
        }
    }

    /** Acepta "yyyy-MM-dd HH:mm:ss" (SQLite) o con milisegundos (MySQL). */
    private static LocalDateTime leerFecha(String texto) {
        return LocalDateTime.parse(texto.substring(0, 19), FORMATO_BD);
    }
}
