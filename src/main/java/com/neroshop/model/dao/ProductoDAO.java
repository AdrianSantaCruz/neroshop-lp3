package com.neroshop.model.dao;

import com.neroshop.model.Producto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO extends BaseDAO {

    /** Criterios de orden del catalogo. */
    public enum Orden {
        NOVEDADES("Novedades", "p.id DESC"),
        PRECIO_MENOR("Precio: menor a mayor", "p.precio ASC"),
        PRECIO_MAYOR("Precio: mayor a menor", "p.precio DESC"),
        NOMBRE("Nombre A-Z", "p.nombre ASC");

        private final String etiqueta;
        private final String sql;

        Orden(String etiqueta, String sql) {
            this.etiqueta = etiqueta;
            this.sql = sql;
        }

        @Override
        public String toString() {
            return etiqueta;
        }
    }

    private static final String SELECT_BASE =
            "SELECT p.id, p.nombre, p.descripcion, p.categoria_id, c.nombre AS categoria, "
            + "p.tallas, p.color, p.precio, p.stock, p.imagen, p.activo "
            + "FROM productos p INNER JOIN categorias c ON c.id = p.categoria_id "
            + "WHERE p.activo = 1 ";

    /**
     * Lista los productos activos.
     *
     * @param filtro      texto a buscar en nombre, color o categoria (puede ser vacio)
     * @param categoriaId id de la categoria, o 0 para todas
     * @param orden       criterio de orden
     */
    public List<Producto> listar(String filtro, int categoriaId, Orden orden) {
        StringBuilder sql = new StringBuilder(SELECT_BASE);
        boolean conFiltro = filtro != null && !filtro.isBlank();
        if (conFiltro) {
            sql.append("AND (LOWER(p.nombre) LIKE ? OR LOWER(p.color) LIKE ? OR LOWER(c.nombre) LIKE ?) ");
        }
        if (categoriaId > 0) {
            sql.append("AND p.categoria_id = ? ");
        }
        sql.append("ORDER BY ").append((orden == null ? Orden.NOMBRE : orden).sql);

        List<Producto> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql.toString())) {
            int i = 1;
            if (conFiltro) {
                String patron = "%" + filtro.trim().toLowerCase() + "%";
                ps.setString(i++, patron);
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

    /** Devuelve el producto con su stock actualizado, o null si ya no existe. */
    public Producto buscarPorId(int id) {
        try (PreparedStatement ps = conexion().prepareStatement(SELECT_BASE + "AND p.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar el producto: " + e.getMessage(), e);
        }
    }

    public void insertar(Producto p) {
        String sql = "INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            llenarParametros(ps, p);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al registrar el producto: " + e.getMessage(), e);
        }
    }

    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, categoria_id = ?, tallas = ?, "
                + "color = ?, precio = ?, stock = ?, imagen = ? WHERE id = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            llenarParametros(ps, p);
            ps.setInt(9, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    /**
     * Eliminacion logica: el producto deja de mostrarse pero se conserva
     * para no romper el historial de pedidos.
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
        ps.setString(4, p.getTallas());
        ps.setString(5, p.getColor());
        ps.setDouble(6, p.getPrecio());
        ps.setInt(7, p.getStock());
        ps.setString(8, vacioANull(p.getImagen()));
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setCategoriaId(rs.getInt("categoria_id"));
        p.setCategoriaNombre(rs.getString("categoria"));
        p.setTallas(rs.getString("tallas"));
        p.setColor(rs.getString("color"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setImagen(rs.getString("imagen"));
        p.setActivo(rs.getInt("activo") == 1);
        return p;
    }
}
