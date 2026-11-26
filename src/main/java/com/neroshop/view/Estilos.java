package com.neroshop.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.neroshop.util.Formato;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Map;

/** Colores, fuentes y fabricas de componentes comunes de la interfaz. */
public final class Estilos {

    public static final Color ACENTO = new Color(0xE9, 0x45, 0x60);
    public static final Color FONDO_OSCURO = new Color(0x0F, 0x0F, 0x12);
    public static final Color TEXTO_SECUNDARIO = new Color(0x9A, 0x9A, 0xA5);

    public static final Font FUENTE_LOGO = new Font("Segoe UI", Font.BOLD, 30);
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_TOTAL = new Font("Segoe UI", Font.BOLD, 22);

    private Estilos() {
    }

    /** Instala el tema oscuro FlatLaf con el color de acento de NeroShop. */
    public static void instalarTema() {
        FlatLaf.setGlobalExtraDefaults(Map.of("@accentColor", "#E94560"));
        FlatDarkLaf.setup();
        UIManager.put("Button.arc", 12);
        UIManager.put("Component.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("ScrollBar.showButtons", false);
        UIManager.put("Table.rowHeight", 28);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("TableHeader.height", 32);
        UIManager.put("TabbedPane.tabHeight", 40);
        UIManager.put("TabbedPane.selectedBackground", new Color(0x26, 0x26, 0x2C));
        UIManager.put("defaultFont", FUENTE_NORMAL);
    }

    public static JButton boton(String texto, boolean primario) {
        JButton b = new JButton(texto);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        if (primario) {
            b.setBackground(ACENTO);
            b.setForeground(Color.WHITE);
            b.setFont(b.getFont().deriveFont(Font.BOLD));
        }
        return b;
    }

    public static JLabel titulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_TITULO);
        return l;
    }

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

    /** Crea una tabla con seleccion simple, ordenable y con formato de moneda en columnas Double. */
    public static JTable tabla(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setAutoCreateRowSorter(true);
        t.getTableHeader().setReorderingAllowed(false);
        t.setFillsViewportHeight(true);
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
        sp.setBorder(BorderFactory.createLineBorder(new Color(0x33, 0x33, 0x3A)));
        return sp;
    }

    public static Border margen(int px) {
        return BorderFactory.createEmptyBorder(px, px, px, px);
    }

    /** Agrega una fila "etiqueta: campo" a un panel con GridBagLayout. */
    public static void agregarCampo(JPanel panel, int fila, String etiqueta, JComponent campo) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = fila;
        gc.insets = new Insets(5, 0, 5, 10);
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 0;
        JLabel l = new JLabel(etiqueta);
        l.setForeground(TEXTO_SECUNDARIO);
        panel.add(l, gc);

        gc.gridx = 1;
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 0, 5, 0);
        panel.add(campo, gc);
    }

    /** Placeholder (texto gris) de FlatLaf para campos de texto. */
    public static void placeholder(JComponent campo, String texto) {
        campo.putClientProperty("JTextField.placeholderText", texto);
    }
}
