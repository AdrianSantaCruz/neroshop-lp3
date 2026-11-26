package com.neroshop.util;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Carga, escala y guarda las imagenes de la tienda.
 * <p>
 * Una ruta como {@code imagenes/productos/polo.png} se busca primero como
 * archivo en la carpeta de la aplicacion (fotos subidas por el administrador)
 * y luego dentro de los recursos del proyecto (fotos de ejemplo).
 */
public final class Imagenes {

    /** Carpeta donde se guardan las fotos que sube el administrador. */
    public static final String CARPETA_SUBIDAS = "imagenes/productos";

    private static final Map<String, BufferedImage> CACHE = new HashMap<>();
    private static final Map<String, BufferedImage> CACHE_ESCALADAS = new HashMap<>();

    private Imagenes() {
    }

    /** Carga una imagen (con cache). Si no existe devuelve una imagen "Sin foto". */
    public static synchronized BufferedImage cargar(String ruta) {
        if (ruta == null || ruta.isBlank()) {
            return sinFoto();
        }
        return CACHE.computeIfAbsent(ruta, r -> {
            BufferedImage img = leer(r);
            return img != null ? img : sinFoto();
        });
    }

    private static BufferedImage leer(String ruta) {
        try {
            Path archivo = Path.of(ruta);
            if (Files.exists(archivo)) {
                return ImageIO.read(archivo.toFile());
            }
            try (InputStream in = Imagenes.class.getResourceAsStream("/" + ruta.replace('\\', '/'))) {
                return in == null ? null : ImageIO.read(in);
            }
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    /** Escala la imagen para cubrir todo el area (recortando lo que sobra), con cache. */
    public static synchronized BufferedImage cubrir(String ruta, int ancho, int alto) {
        String clave = ruta + "@" + ancho + "x" + alto;
        return CACHE_ESCALADAS.computeIfAbsent(clave, k -> escalarCubrir(cargar(ruta), ancho, alto));
    }

    public static BufferedImage escalarCubrir(BufferedImage src, int ancho, int alto) {
        double escala = Math.max((double) ancho / src.getWidth(), (double) alto / src.getHeight());
        int w = (int) Math.ceil(src.getWidth() * escala);
        int h = (int) Math.ceil(src.getHeight() * escala);
        BufferedImage escalada = escalarSuave(src, w, h);
        BufferedImage out = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.drawImage(escalada, (ancho - w) / 2, (alto - h) / 2, null);
        g.dispose();
        return out;
    }

    /** Escala manteniendo la proporcion para que quepa en el area indicada. */
    public static BufferedImage escalarContener(BufferedImage src, int ancho, int alto) {
        double escala = Math.min((double) ancho / src.getWidth(), (double) alto / src.getHeight());
        return escalarSuave(src, Math.max(1, (int) (src.getWidth() * escala)), Math.max(1, (int) (src.getHeight() * escala)));
    }

    /** Escalado por pasos (mejor calidad que un solo drawImage al reducir mucho). */
    private static BufferedImage escalarSuave(BufferedImage src, int w, int h) {
        BufferedImage actual = src;
        int cw = src.getWidth(), ch = src.getHeight();
        do {
            // Al reducir se divide a la mitad en cada paso; al ampliar se va directo al tamano final
            cw = cw > w ? Math.max(w, cw / 2) : w;
            ch = ch > h ? Math.max(h, ch / 2) : h;
            BufferedImage tmp = new BufferedImage(cw, ch, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = tmp.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.drawImage(actual, 0, 0, cw, ch, null);
            g.dispose();
            actual = tmp;
        } while (cw != w || ch != h);
        return actual;
    }

    /**
     * Copia una foto elegida por el administrador a la carpeta de la
     * aplicacion y devuelve la ruta relativa que se guarda en la base de datos.
     */
    public static String guardarFotoProducto(File origen) throws IOException {
        if (ImageIO.read(origen) == null) {
            throw new IOException("El archivo seleccionado no es una imagen valida.");
        }
        String nombre = origen.getName().toLowerCase(Locale.ROOT);
        String extension = nombre.contains(".") ? nombre.substring(nombre.lastIndexOf('.')) : ".png";
        Path carpeta = Path.of(CARPETA_SUBIDAS);
        Files.createDirectories(carpeta);
        String destino = CARPETA_SUBIDAS + "/foto-" + System.currentTimeMillis() + extension;
        Files.copy(origen.toPath(), Path.of(destino), StandardCopyOption.REPLACE_EXISTING);
        return destino;
    }

    public static BufferedImage logo() {
        return cargar("imagenes/logo.png");
    }

    public static BufferedImage icono() {
        return cargar("imagenes/icono.png");
    }

    private static BufferedImage sinFoto() {
        BufferedImage img = new BufferedImage(600, 720, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(0x1C, 0x1C, 0x1C));
        g.fillRect(0, 0, 600, 720);
        g.setColor(new Color(0x55, 0x55, 0x55));
        g.setFont(new Font("SansSerif", Font.BOLD, 36));
        FontMetrics fm = g.getFontMetrics();
        String t = "SIN FOTO";
        g.drawString(t, (600 - fm.stringWidth(t)) / 2, 370);
        g.dispose();
        return img;
    }
}
