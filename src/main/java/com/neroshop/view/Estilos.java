package com.neroshop.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.neroshop.util.Formato;
import com.neroshop.util.Imagenes;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Identidad visual de Nero Thread: monocromatica (negro, grises y blanco),
 * tipografia condensada en mayusculas y botones blancos de alto contraste.
 */
public final class Estilos {

    public static final Color NEGRO = new Color(0x0A, 0x0A, 0x0A);
    public static final Color FONDO = new Color(0x0F, 0x0F, 0x0F);
    public static final Color SUPERFICIE = new Color(0x16, 0x16, 0x16);
    public static final Color SUPERFICIE_2 = new Color(0x1F, 0x1F, 0x1F);
    public static final Color BORDE = new Color(0x2B, 0x2B, 0x2B);
    public static final Color BLANCO = new Color(0xF5, 0xF5, 0xF5);
    public static final Color GRIS = new Color(0x9C, 0x9C, 0x9C);
    public static final Color GRIS_OSCURO = new Color(0x5E, 0x5E, 0x5E);
    public static final Color ROJO = new Color(0xE0, 0x4F, 0x4F);

    /** Familia para titulos: Bahnschrift (Windows 10/11) o Segoe UI como respaldo. */
    public static final String FAMILIA_TITULOS = disponible("Bahnschrift") ? "Bahnschrift" : "Segoe UI";
    public static final String FAMILIA_TEXTO = disponible("Segoe UI") ? "Segoe UI" : Font.SANS_SERIF;

    private Estilos() {
    }

    private static boolean disponible(String familia) {
        List<String> familias = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        return familias.contains(familia);
    }

    /** Instala el tema oscuro FlatLaf adaptado a la marca. */
    public static void instalarTema() {
        FlatLaf.setGlobalExtraDefaults(Map.of(
                "@background", "#0F0F0F",
                "@foreground", "#EDEDED",
                "@accentColor", "#F5F5F5",
                "@selectionBackground", "#3A3A3A",
                "@selectionForeground", "#FFFFFF"));
        FlatDarkLaf.setup();
        UIManager.put("defaultFont", new Font(FAMILIA_TEXTO, Font.PLAIN, 13));
        UIManager.put("Button.arc", 0);
        UIManager.put("Component.arc", 0);
        UIManager.put("TextComponent.arc", 0);
        UIManager.put("CheckBox.arc", 0);
        UIManager.put("Component.focusWidth", 0);
        UIManager.put("Component.innerFocusWidth", 1);
        UIManager.put("Component.borderColor", BORDE);
        UIManager.put("Component.focusedBorderColor", BLANCO);
        UIManager.put("TextField.background", SUPERFICIE);
        UIManager.put("PasswordField.background", SUPERFICIE);
        UIManager.put("TextArea.background", SUPERFICIE);
        UIManager.put("ComboBox.background", SUPERFICIE);
        UIManager.put("Spinner.background", SUPERFICIE);
        UIManager.put("Button.background", SUPERFICIE_2);
        UIManager.put("Button.borderColor", BORDE);
        UIManager.put("Button.default.background", BLANCO);
        UIManager.put("Button.default.foreground", NEGRO);
        UIManager.put("Button.default.focusedBackground", Color.WHITE);
        UIManager.put("Button.default.hoverBackground", new Color(0xDD, 0xDD, 0xDD));
        UIManager.put("ToggleButton.selectedBackground", BLANCO);
        UIManager.put("ToggleButton.selectedForeground", NEGRO);
        UIManager.put("ToggleButton.background", SUPERFICIE);
        UIManager.put("CheckBox.icon.checkmarkColor", NEGRO);
        UIManager.put("CheckBox.icon.selectedBackground", BLANCO);
        UIManager.put("RadioButton.icon.centerDiameter", 7);
        UIManager.put("RadioButton.icon.checkmarkColor", NEGRO);
        UIManager.put("ScrollBar.showButtons", false);
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("ScrollPane.smoothScrolling", true);
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("Table.background", SUPERFICIE);
        UIManager.put("Table.alternateRowColor", new Color(0x1A, 0x1A, 0x1A));
        UIManager.put("Table.showHorizontalLines", false);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.selectionBackground", new Color(0x3A, 0x3A, 0x3A));
        UIManager.put("Table.selectionInactiveBackground", new Color(0x33, 0x33, 0x33));
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("Table.selectionInactiveForeground", Color.WHITE);
        UIManager.put("TableHeader.background", NEGRO);
        UIManager.put("TableHeader.height", 34);
        UIManager.put("TableHeader.separatorColor", NEGRO);
        UIManager.put("TableHeader.bottomSeparatorColor", BORDE);
        UIManager.put("TabbedPane.tabHeight", 44);
        UIManager.put("TabbedPane.underlineColor", BLANCO);
        UIManager.put("TabbedPane.inactiveUnderlineColor", GRIS);
        UIManager.put("TabbedPane.selectedBackground", FONDO);
        UIManager.put("TabbedPane.hoverColor", SUPERFICIE);
        UIManager.put("TitlePane.background", NEGRO);
        UIManager.put("TitlePane.inactiveBackground", NEGRO);
        UIManager.put("OptionPane.background", FONDO);
        UIManager.put("Panel.background", FONDO);
    }

    // ---------- Tipografia ----------

    /** Fuente de titulos con espaciado entre letras. */
    public static Font titulo(float tamano) {
        return new Font(FAMILIA_TITULOS, Font.BOLD, 12).deriveFont(tamano)
                .deriveFont(Map.of(TextAttribute.TRACKING, 0.06f));
    }

    /** Texto pequeno en mayusculas muy espaciado (etiquetas tipo "TALLA", "CATEGORIA"). */
    public static Font etiqueta(float tamano) {
        return new Font(FAMILIA_TITULOS, Font.BOLD, 12).deriveFont(tamano)
                .deriveFont(Map.of(TextAttribute.TRACKING, 0.18f));
    }

    public static Font texto(int estilo, float tamano) {
        return new Font(FAMILIA_TEXTO, estilo, 12).deriveFont(estilo, tamano);
    }

    public static JLabel lblTitulo(String texto, float tamano) {
        JLabel l = new JLabel(texto.toUpperCase());
        l.setFont(titulo(tamano));
        l.setForeground(BLANCO);
        // Espacio arriba para que las tildes de las mayusculas (Á, É, Ñ) no se recorten
        l.setBorder(BorderFactory.createEmptyBorder(Math.round(tamano * 0.18f), 0, 0, 0));
        return l;
    }

    public static JLabel lblEtiqueta(String texto) {
        JLabel l = new JLabel(texto.toUpperCase());
        l.setFont(etiqueta(11));
        l.setForeground(GRIS);
        l.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        return l;
    }

    // ---------- Botones ----------

    /** Boton blanco con texto negro (accion principal). */
    public static JButton botonPrimario(String texto) {
        JButton b = new JButton(texto.toUpperCase());
        b.putClientProperty("JButton.buttonType", null);
        b.setBackground(BLANCO);
        b.setForeground(NEGRO);
        b.setFont(etiqueta(12.5f));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(11, 22, 11, 22));
        return b;
    }

    /** Boton con borde blanco y fondo transparente (accion secundaria). */
    public static JButton botonSecundario(String texto) {
        JButton b = new JButton(texto.toUpperCase());
        b.setBackground(FONDO);
        b.setForeground(BLANCO);
        b.setFont(etiqueta(12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRIS_OSCURO),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)));
        return b;
    }

    /** Boton plano sin borde (navegacion o enlaces). */
    public static JButton botonTexto(String texto) {
        JButton b = new JButton(texto);
        b.putClientProperty("JButton.buttonType", "borderless");
        b.setForeground(BLANCO);
        b.setFont(etiqueta(12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        return b;
    }

    /** Boton seleccionable (chips de categoria y tallas). */
    public static JToggleButton chip(String texto) {
        JToggleButton b = new JToggleButton(texto.toUpperCase());
        b.setFont(etiqueta(11.5f));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        b.setMargin(new Insets(7, 14, 7, 14));
        return b;
    }

    // ---------- Tablas ----------

    /** Modelo de tabla no editable con tipos de columna (para ordenar bien). */
    public static DefaultTableModel modeloTabla(String[] columnas, Class<?>[] tipos) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return tipos[columnIndex];
            }
        };
    }

    /** Tabla con seleccion simple, ordenable y con formato de moneda en columnas Double. */
    public static JTable tabla(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setAutoCreateRowSorter(true);
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setFont(new Font(FAMILIA_TITULOS, Font.BOLD, 12).deriveFont(12f)
                .deriveFont(Map.of(TextAttribute.TRACKING, 0.05f)));
        t.setFillsViewportHeight(true);
        t.setIntercellSpacing(new Dimension(0, 0));
        DefaultTableCellRenderer moneda = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                setText(value instanceof Number n ? Formato.moneda(n.doubleValue()) : "");
            }
        };
        moneda.setHorizontalAlignment(SwingConstants.RIGHT);
        t.setDefaultRenderer(Double.class, moneda);
        return t;
    }

    public static JScrollPane scroll(JComponent c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createLineBorder(BORDE));
        sp.getVerticalScrollBar().setUnitIncrement(24);
        return sp;
    }

    /** Scroll sin borde para el contenido de la tienda. */
    public static JScrollPane scrollLimpio(JComponent c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(null);
        sp.getViewport().setBackground(FONDO);
        sp.getVerticalScrollBar().setUnitIncrement(28);
        return sp;
    }

    // ---------- Formularios ----------

    public static Border margen(int px) {
        return BorderFactory.createEmptyBorder(px, px, px, px);
    }

    /** Agrega una fila "ETIQUETA / campo" (etiqueta arriba) a un panel con GridBagLayout. */
    public static void agregarCampo(JPanel panel, int fila, String etiqueta, JComponent campo) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = fila * 2;
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = new Insets(fila == 0 ? 0 : 10, 0, 4, 0);
        panel.add(lblEtiqueta(etiqueta), gc);
        gc.gridy = fila * 2 + 1;
        gc.insets = new Insets(0, 0, 0, 0);
        panel.add(campo, gc);
    }

    /** Placeholder (texto gris) de FlatLaf para campos de texto. */
    public static void placeholder(JComponent campo, String texto) {
        campo.putClientProperty("JTextField.placeholderText", texto);
    }

    /** Campo de texto con altura comoda. */
    public static <T extends JComponent> T alto(T campo) {
        campo.setPreferredSize(new Dimension(campo.getPreferredSize().width, 38));
        return campo;
    }

    // ---------- Marca ----------

    /** Logo de Nero Thread escalado a la altura indicada. */
    public static JLabel logo(int alto) {
        BufferedImage img = Imagenes.logo();
        int ancho = (int) Math.round(img.getWidth() * (double) alto / img.getHeight());
        return new JLabel(new ImageIcon(Imagenes.escalarContener(img, ancho, alto)));
    }

    public static void iconoVentana(Window w) {
        Image icono = Imagenes.icono();
        w.setIconImages(List.of(icono, Imagenes.escalarContener(Imagenes.icono(), 32, 32)));
    }
}
