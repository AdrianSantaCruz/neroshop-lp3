package com.neroshop.model.dao;

import com.neroshop.model.DetalleVenta;
import com.neroshop.model.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO extends BaseDAO {

    private static final DateTimeFormatter FORMATO_BD = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Registra la venta, sus detalles y descuenta el stock dentro de una
     * transaccion: si algo falla no se guarda nada.
     *
     * @return el id generado de la venta
     */
    public int registrar(Venta venta) {
        Connection c = conexion();
        try {
            c.setAutoCommit(false);

            int ventaId;
            String sqlVenta = "INSERT INTO ventas (cliente_id, usuario_id, fecha, total) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, venta.getClienteId());
                ps.setInt(2, venta.getUsuarioId());
                ps.setString(3, venta.getFecha().format(FORMATO_BD));
                ps.setDouble(4, venta.getTotal());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No se obtuvo el id de la venta.");
                    }
                    ventaId = keys.getInt(1);
                }
            }

            String sqlDetalle = "INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal) "
                    + "VALUES (?, ?, ?, ?, ?)";
            String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";
            try (PreparedStatement psDetalle = c.prepareStatement(sqlDetalle);
                 PreparedStatement psStock = c.prepareStatement(sqlStock)) {
                for (DetalleVenta d : venta.getDetalles()) {
                    psStock.setInt(1, d.getCantidad());
                    psStock.setInt(2, d.getProductoId());
                    psStock.setInt(3, d.getCantidad());
                    if (psStock.executeUpdate() == 0) {
                        throw new DAOException("Stock insuficiente para \"" + d.getProductoNombre() + "\".");
                    }
                    psDetalle.setInt(1, ventaId);
                    psDetalle.setInt(2, d.getProductoId());
                    psDetalle.setInt(3, d.getCantidad());
                    psDetalle.setDouble(4, d.getPrecioUnitario());
                    psDetalle.setDouble(5, d.getSubtotal());
                    psDetalle.executeUpdate();
                }
            }

            c.commit();
            venta.setId(ventaId);
            return ventaId;
        } catch (SQLException | DAOException e) {
            try {
                c.rollback();
            } catch (SQLException ignored) {
                // Se informa el error original
            }
            if (e instanceof DAOException de) {
                throw de;
            }
            throw new DAOException("Error al registrar la venta: " + e.getMessage(), e);
        } finally {
            try {
                c.setAutoCommit(true);
            } catch (SQLException ignored) {
                // Nada que hacer
            }
        }
    }

    public List<Venta> listar() {
        String sql = "SELECT v.id, v.cliente_id, c.nombres, c.apellidos, v.usuario_id, u.nombre AS usuario, "
                + "v.fecha, v.total "
                + "FROM ventas v "
                + "INNER JOIN clientes c ON c.id = v.cliente_id "
                + "INNER JOIN usuarios u ON u.id = v.usuario_id "
                + "ORDER BY v.id DESC";
        List<Venta> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Venta v = new Venta();
                v.setId(rs.getInt("id"));
                v.setClienteId(rs.getInt("cliente_id"));
                v.setClienteNombre(rs.getString("nombres") + " " + rs.getString("apellidos"));
                v.setUsuarioId(rs.getInt("usuario_id"));
                v.setUsuarioNombre(rs.getString("usuario"));
                v.setFecha(leerFecha(rs.getString("fecha")));
                v.setTotal(rs.getDouble("total"));
                lista.add(v);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar ventas: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<DetalleVenta> listarDetalles(int ventaId) {
        String sql = "SELECT d.producto_id, p.nombre, p.talla, p.color, d.cantidad, d.precio_unitario "
                + "FROM detalle_venta d INNER JOIN productos p ON p.id = d.producto_id "
                + "WHERE d.venta_id = ? ORDER BY d.id";
        List<DetalleVenta> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setInt(1, ventaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta d = new DetalleVenta();
                    d.setProductoId(rs.getInt("producto_id"));
                    d.setProductoNombre(rs.getString("nombre"));
                    d.setTalla(rs.getString("talla"));
                    d.setColor(rs.getString("color"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar el detalle de la venta: " + e.getMessage(), e);
        }
        return lista;
    }

    /** Acepta "yyyy-MM-dd HH:mm:ss" (SQLite) o con milisegundos (MySQL). */
    private static LocalDateTime leerFecha(String texto) {
        return LocalDateTime.parse(texto.substring(0, 19), FORMATO_BD);
    }
}
