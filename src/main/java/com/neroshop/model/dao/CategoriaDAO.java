package com.neroshop.model.dao;

import com.neroshop.model.Categoria;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO extends BaseDAO {

    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM categorias ORDER BY nombre";
        try (PreparedStatement ps = conexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar categorias: " + e.getMessage(), e);
        }
        return lista;
    }

    public void insertar(Categoria c) {
        validarNombreUnico(c);
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, vacioANull(c.getDescripcion()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al registrar la categoria: " + e.getMessage(), e);
        }
    }

    public void actualizar(Categoria c) {
        validarNombreUnico(c);
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, vacioANull(c.getDescripcion()));
            ps.setInt(3, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar la categoria: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        if (contarProductos(id) > 0) {
            throw new DAOException("No se puede eliminar: la categoria tiene productos registrados.");
        }
        try (PreparedStatement ps = conexion().prepareStatement("DELETE FROM categorias WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar la categoria: " + e.getMessage(), e);
        }
    }

    private int contarProductos(int categoriaId) {
        String sql = "SELECT COUNT(*) FROM productos WHERE categoria_id = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setInt(1, categoriaId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Error al contar productos: " + e.getMessage(), e);
        }
    }

    private void validarNombreUnico(Categoria c) {
        String sql = "SELECT COUNT(*) FROM categorias WHERE LOWER(nombre) = LOWER(?) AND id <> ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new DAOException("Ya existe una categoria con el nombre \"" + c.getNombre() + "\".");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al validar la categoria: " + e.getMessage(), e);
        }
    }
}
