package com.neroshop.model.dao;

import com.neroshop.model.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO extends BaseDAO {

    /** Lista los clientes filtrando por nombre, apellido o documento. */
    public List<Cliente> listar(String filtro) {
        boolean conFiltro = filtro != null && !filtro.isBlank();
        String sql = "SELECT id, nombres, apellidos, documento, email, telefono, direccion FROM clientes "
                + (conFiltro ? "WHERE LOWER(nombres) LIKE ? OR LOWER(apellidos) LIKE ? OR documento LIKE ? " : "")
                + "ORDER BY apellidos, nombres";
        List<Cliente> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            if (conFiltro) {
                String patron = "%" + filtro.trim().toLowerCase() + "%";
                ps.setString(1, patron);
                ps.setString(2, patron);
                ps.setString(3, patron);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    public void insertar(Cliente c) {
        validarDocumentoUnico(c);
        String sql = "INSERT INTO clientes (nombres, apellidos, documento, email, telefono, direccion) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            llenarParametros(ps, c);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al registrar el cliente: " + e.getMessage(), e);
        }
    }

    public void actualizar(Cliente c) {
        validarDocumentoUnico(c);
        String sql = "UPDATE clientes SET nombres = ?, apellidos = ?, documento = ?, email = ?, "
                + "telefono = ?, direccion = ? WHERE id = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            llenarParametros(ps, c);
            ps.setInt(7, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar el cliente: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        try (PreparedStatement ps = conexion().prepareStatement("SELECT COUNT(*) FROM ventas WHERE cliente_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new DAOException("No se puede eliminar: el cliente tiene ventas registradas.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al validar el cliente: " + e.getMessage(), e);
        }
        try (PreparedStatement ps = conexion().prepareStatement("DELETE FROM clientes WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar el cliente: " + e.getMessage(), e);
        }
    }

    private void validarDocumentoUnico(Cliente c) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE documento = ? AND id <> ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, c.getDocumento());
            ps.setInt(2, c.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new DAOException("Ya existe un cliente con el documento " + c.getDocumento() + ".");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al validar el cliente: " + e.getMessage(), e);
        }
    }

    private void llenarParametros(PreparedStatement ps, Cliente c) throws SQLException {
        ps.setString(1, c.getNombres());
        ps.setString(2, c.getApellidos());
        ps.setString(3, c.getDocumento());
        ps.setString(4, vacioANull(c.getEmail()));
        ps.setString(5, vacioANull(c.getTelefono()));
        ps.setString(6, vacioANull(c.getDireccion()));
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setNombres(rs.getString("nombres"));
        c.setApellidos(rs.getString("apellidos"));
        c.setDocumento(rs.getString("documento"));
        c.setEmail(rs.getString("email"));
        c.setTelefono(rs.getString("telefono"));
        c.setDireccion(rs.getString("direccion"));
        return c;
    }
}
