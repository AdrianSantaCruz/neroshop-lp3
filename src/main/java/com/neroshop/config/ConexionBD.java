package com.neroshop.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Gestiona la conexion a la base de datos (patron Singleton).
 * <p>
 * Lee la configuracion de {@code db.properties}: primero busca el archivo en la
 * carpeta donde se ejecuta la aplicacion y, si no existe, usa el que viene
 * dentro de {@code src/main/resources}.
 */
public final class ConexionBD {

    private static ConexionBD instancia;

    private final Properties propiedades = new Properties();
    private Connection conexion;

    private ConexionBD() {
        cargarPropiedades();
    }

    public static synchronized ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    private void cargarPropiedades() {
        Path externo = Path.of("db.properties");
        try (InputStream in = Files.exists(externo)
                ? Files.newInputStream(externo)
                : ConexionBD.class.getResourceAsStream("/db.properties")) {
            if (in != null) {
                propiedades.load(in);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer db.properties", e);
        }
    }

    public String getUrl() {
        return propiedades.getProperty("db.url", "jdbc:sqlite:nerothread.db").trim();
    }

    public boolean esSQLite() {
        return getUrl().startsWith("jdbc:sqlite");
    }

    /** Devuelve la conexion abierta (la crea si es necesario). */
    public synchronized Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(
                    getUrl(),
                    propiedades.getProperty("db.user", ""),
                    propiedades.getProperty("db.password", ""));
            if (esSQLite()) {
                try (Statement st = conexion.createStatement()) {
                    st.execute("PRAGMA foreign_keys = ON");
                }
            }
        }
        return conexion;
    }

    /**
     * Crea las tablas (solo en SQLite; en MySQL se usa el script de la carpeta
     * database) e inserta los datos de ejemplo si la base esta vacia.
     */
    public void inicializar() throws SQLException {
        Connection c = getConexion();
        if (esSQLite()) {
            ejecutarScript(c, "/sql/schema-sqlite.sql");
        }
        if (tablaVacia(c, "categorias")) {
            ejecutarScript(c, "/sql/datos-iniciales.sql");
        }
    }

    private boolean tablaVacia(Connection c, String tabla) throws SQLException {
        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + tabla)) {
            return rs.next() && rs.getInt(1) == 0;
        }
    }

    private void ejecutarScript(Connection c, String recurso) throws SQLException {
        String contenido;
        try (InputStream in = ConexionBD.class.getResourceAsStream(recurso)) {
            if (in == null) {
                throw new SQLException("No se encontro el script " + recurso);
            }
            contenido = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SQLException("No se pudo leer el script " + recurso, e);
        }

        // Quitar comentarios de linea y separar por ';'
        StringBuilder sinComentarios = new StringBuilder();
        for (String linea : contenido.split("\\R")) {
            if (!linea.trim().startsWith("--")) {
                sinComentarios.append(linea).append('\n');
            }
        }
        try (Statement st = c.createStatement()) {
            for (String sentencia : sinComentarios.toString().split(";")) {
                if (!sentencia.isBlank()) {
                    st.execute(sentencia.trim());
                }
            }
        }
    }

    public synchronized void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException ignored) {
            // La aplicacion se esta cerrando
        }
    }
}
