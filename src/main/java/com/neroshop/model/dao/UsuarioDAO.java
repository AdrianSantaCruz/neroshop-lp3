package com.neroshop.model.dao;

import com.neroshop.model.Cliente;
import com.neroshop.model.Usuario;
import com.neroshop.util.Seguridad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO extends BaseDAO {

    public static final String ROL_ADMIN = "ADMIN";
    public static final String ROL_CLIENTE = "CLIENTE";

    /** Devuelve el usuario si las credenciales son correctas, o null si no. */
    public Usuario autenticar(String username, String password) {
        String sql = "SELECT id, username, password_hash, nombre, rol FROM usuarios WHERE username = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && Seguridad.verificar(password, rs.getString("password_hash"))) {
                    return new Usuario(rs.getInt("id"), rs.getString("username"),
                            rs.getString("nombre"), rs.getString("rol"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error al iniciar sesion: " + e.getMessage(), e);
        }
    }

    public boolean existeUsername(String username) {
        try (PreparedStatement ps = conexion().prepareStatement("SELECT COUNT(*) FROM usuarios WHERE LOWER(username) = LOWER(?)")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Error al validar el usuario: " + e.getMessage(), e);
        }
    }

    /** Inserta un usuario y devuelve su id. */
    public int insertar(String username, String password, String nombre, String rol) {
        try {
            return insertar(conexion(), username, password, nombre, rol);
        } catch (SQLException e) {
            throw new DAOException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }

    private int insertar(Connection c, String username, String password, String nombre, String rol) throws SQLException {
        String sql = "INSERT INTO usuarios (username, password_hash, nombre, rol) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, Seguridad.hash(password));
            ps.setString(3, nombre);
            ps.setString(4, rol);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("No se obtuvo el id del usuario.");
                }
                return keys.getInt(1);
            }
        }
    }

    /**
     * Crea la cuenta de un cliente nuevo (usuario + datos de cliente) en una
     * sola transaccion.
     *
     * @return el usuario creado, listo para iniciar sesion
     */
    public Usuario registrarCliente(String username, String password, Cliente cliente) {
        if (existeUsername(username)) {
            throw new DAOException("El usuario \"" + username + "\" ya esta en uso.");
        }
        new ClienteDAO().validarDocumentoUnico(cliente);
        Connection c = conexion();
        try {
            c.setAutoCommit(false);
            int usuarioId = insertar(c, username, password, cliente.getNombres(), ROL_CLIENTE);
            cliente.setUsuarioId(usuarioId);
            new ClienteDAO().insertar(c, cliente);
            c.commit();
            return new Usuario(usuarioId, username, cliente.getNombres(), ROL_CLIENTE);
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException ignored) {
                // Se informa el error original
            }
            throw new DAOException("Error al crear la cuenta: " + e.getMessage(), e);
        } finally {
            try {
                c.setAutoCommit(true);
            } catch (SQLException ignored) {
                // Nada que hacer
            }
        }
    }

    /** Crea los usuarios de prueba la primera vez que se ejecuta la aplicacion. */
    public void crearUsuariosPorDefecto() {
        try (Statement st = conexion().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM usuarios")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        } catch (SQLException e) {
            throw new DAOException("Error al revisar usuarios: " + e.getMessage(), e);
        }
        insertar("admin", "admin123", "Administrador", ROL_ADMIN);
        int idCliente = insertar("cliente", "cliente123", "Lucía", ROL_CLIENTE);
        try (PreparedStatement ps = conexion().prepareStatement("UPDATE clientes SET usuario_id = ? WHERE documento = ?")) {
            ps.setInt(1, idCliente);
            ps.setString(2, "45879632");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al vincular el cliente de prueba: " + e.getMessage(), e);
        }
    }
}
