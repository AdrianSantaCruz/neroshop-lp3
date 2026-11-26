package com.neroshop.model.dao;

import com.neroshop.model.Usuario;
import com.neroshop.util.Seguridad;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO extends BaseDAO {

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

    public void insertar(String username, String password, String nombre, String rol) {
        String sql = "INSERT INTO usuarios (username, password_hash, nombre, rol) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, Seguridad.hash(password));
            ps.setString(3, nombre);
            ps.setString(4, rol);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }

    /** Crea los usuarios por defecto la primera vez que se ejecuta la aplicacion. */
    public void crearUsuariosPorDefecto() {
        try (Statement st = conexion().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM usuarios")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        } catch (SQLException e) {
            throw new DAOException("Error al revisar usuarios: " + e.getMessage(), e);
        }
        insertar("admin", "admin123", "Administrador", "ADMIN");
        insertar("vendedor", "vendedor123", "Vendedor de tienda", "VENDEDOR");
    }
}
