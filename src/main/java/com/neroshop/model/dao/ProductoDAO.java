package com.neroshop.model.dao;

import com.neroshop.model.Producto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO extends BaseDAO {

    private static final String SELECT_BASE =
            "SELECT p.id, p.nombre, p.descripcion, p.categoria_id, c.nombre AS categoria, "
            + "p.talla, p.color, p.precio, p.stock, p.activo "
            + "FROM productos p INNER JOIN categorias c ON c.id = p.categoria_id "
            + "WHERE p.activo = 1 ";

    /**
     * Lista los productos activos.
     *
     * @param filtro      texto a buscar en nombre o color (puede ser vacio)
     * @param categoriaId id de la categoria, o 0 para todas
     */
    public List<Producto> listar(String filtro, int categoriaId) {
        StringBuilder sql = new StringBuilder(SELECT_BASE);
        boolean conFiltro = filtro != null && !filtro.isBlank();
        if (conFiltro) {
            sql.append("AND (LOWER(p.nombre) LIKE ? OR LOWER(p.color) LIKE ?) ");
        }
        if (categoriaId > 0) {
            sql.append("AND p.categoria_id = ? ");
        }
        sql.append("ORDER BY p.nombre");

        List<Producto> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql.toString())) {
            int i = 1;
            if (conFiltro) {
                String patron = "%" + filtro.trim().toLowerCase() + "%";
                ps.setString(i++, patron);
                ps.setString(i++, patron);
            }
            if (categoriaId > 0) {
                ps.setInt(i, categoriaId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar productos: " + e.getMessage(), e);
        }
        return lista;
    }

    public void insertar(Producto p) {
        String sql = "INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            llenarParametros(ps, p);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al registrar el producto: " + e.getMessage(), e);
        }
    }

    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, categoria_id = ?, talla = ?, "
                + "color = ?, precio = ?, stock = ? WHERE id = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            llenarParametros(ps, p);
            ps.setInt(8, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    /**
     * Eliminacion logica: el producto deja de mostrarse pero se conserva
     * para no romper el historial de ventas.
     */
    public void eliminar(int id) {
        try (PreparedStatement ps = conexion().prepareStatement("UPDATE productos SET activo = 0 WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar el producto: " + e.getMessage(), e);
        }
    }

    private void llenarParametros(PreparedStatement ps, Producto p) throws SQLException {
        ps.setString(1, p.getNombre());
        ps.setString(2, vacioANull(p.getDescripcion()));
        ps.setInt(3, p.getCategoriaId());
        ps.setString(4, p.getTalla());
        ps.setString(5, p.getColor());
        ps.setDouble(6, p.getPrecio());
        ps.setInt(7, p.getStock());
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setCategoriaId(rs.getInt("categoria_id"));
        p.setCategoriaNombre(rs.getString("categoria"));
        p.setTalla(rs.getString("talla"));
        p.setColor(rs.getString("color"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setActivo(rs.getInt("activo") == 1);
        return p;
    }
}
