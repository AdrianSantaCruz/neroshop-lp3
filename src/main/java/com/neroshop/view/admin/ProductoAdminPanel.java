package com.neroshop.view.admin;

import com.neroshop.model.Categoria;
import com.neroshop.view.Estilos;
import com.neroshop.view.PanelBase;
import com.neroshop.view.componentes.AnchoAjustable;
import com.neroshop.view.componentes.ImagenProducto;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** Mantenimiento (CRUD) de productos con foto. */
public class ProductoAdminPanel extends PanelBase {

    public static final String[] TALLAS = {"XS", "S", "M", "L", "XL", "XXL", "28", "30", "32", "34", "36", "Única"};

    private final JTextField txtBuscar = Estilos.alto(new JTextField(20));
    private final JComboBox<Categoria> cboFiltroCategoria = new JComboBox<>();

    private final DefaultTableModel modelo = Estilos.modeloTabla(
            new String[]{"", "ID", "Nombre", "Categoría", "Tallas", "Color", "Precio", "Stock"},
            new Class<?>[]{Icon.class, Integer.class, String.class, String.class, String.class, String.class, Double.class, Integer.class});
    private final JTable tabla = Estilos.tabla(modelo);

    private final JLabel lblModo = Estilos.lblTitulo("Nuevo producto", 20);
    private final ImagenProducto foto = new ImagenProducto(null, 150, 180);
    private final JButton btnFoto = Estilos.botonSecundario("Elegir foto...");
    private final JTextField txtNombre = Estilos.alto(new JTextField());
    private final JComboBox<Categoria> cboCategoria = new JComboBox<>();
    private final List<JCheckBox> chkTallas = new ArrayList<>();
    private final JTextField txtColor = Estilos.alto(new JTextField());
    private final JTextField txtPrecio = Estilos.alto(new JTextField());
    private final JSpinner spnStock = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
    private final JTextArea txtDescripcion = new JTextArea(3, 20);

    private final JButton btnNuevo = Estilos.botonSecundario("Nuevo");
    private final JButton btnGuardar = Estilos.botonPrimario("Guardar");
    private final JButton btnEliminar = Estilos.botonSecundario("Eliminar");

    public ProductoAdminPanel() {
        add(crearListado(), BorderLayout.CENTER);
        add(crearFormulario(), BorderLayout.EAST);
    }

    private JPanel crearListado() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);
        JPanel filtros = new JPanel(new BorderLayout(10, 0));
        filtros.setOpaque(false);
        filtros.add(Estilos.lblTitulo("Productos", 24), BorderLayout.WEST);
        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        derecha.setOpaque(false);
        Estilos.placeholder(txtBuscar, "Buscar por nombre o color...");
        cboFiltroCategoria.setPreferredSize(new Dimension(200, 38));
        derecha.add(txtBuscar);
        derecha.add(cboFiltroCategoria);
        filtros.add(derecha, BorderLayout.EAST);
        panel.add(filtros, BorderLayout.NORTH);

        tabla.setRowHeight(64);
        tabla.getColumnModel().getColumn(0).setMaxWidth(60);
        tabla.getColumnModel().getColumn(0).setMinWidth(60);
        tabla.getColumnModel().getColumn(1).setMaxWidth(50);
        tabla.getColumnModel().getColumn(7).setMaxWidth(70);
        panel.add(Estilos.scroll(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane crearFormulario() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 12));
        panel.add(lblModo, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);

        JPanel filaFoto = new JPanel(new BorderLayout(14, 0));
        filaFoto.setOpaque(false);
        foto.setBorder(BorderFactory.createLineBorder(Estilos.BORDE));
        filaFoto.add(foto, BorderLayout.WEST);
        JPanel ayudaFoto = new JPanel(new BorderLayout(0, 8));
        ayudaFoto.setOpaque(false);
        JLabel nota = new JLabel("<html>JPG o PNG.<br>Recomendado: vertical<br>(600 x 720 px).</html>");
        nota.setForeground(Estilos.GRIS);
        ayudaFoto.add(nota, BorderLayout.NORTH);
        ayudaFoto.add(btnFoto, BorderLayout.SOUTH);
        filaFoto.add(ayudaFoto, BorderLayout.CENTER);

        JPanel tallas = new JPanel(new GridLayout(2, 6, 4, 4));
        tallas.setOpaque(false);
        for (String t : TALLAS) {
            JCheckBox chk = new JCheckBox(t);
            chk.setOpaque(false);
            chkTallas.add(chk);
            tallas.add(chk);
        }

        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        cboCategoria.setPreferredSize(new Dimension(100, 38));
        spnStock.setPreferredSize(new Dimension(100, 38));

        Estilos.agregarCampo(campos, 0, "Foto", filaFoto);
        Estilos.agregarCampo(campos, 1, "Nombre", txtNombre);
        Estilos.agregarCampo(campos, 2, "Categoría", cboCategoria);
        Estilos.agregarCampo(campos, 3, "Tallas disponibles", tallas);
        JPanel precioStock = new JPanel(new GridLayout(1, 2, 10, 0));
        precioStock.setOpaque(false);
        JPanel pPrecio = new JPanel(new GridBagLayout());
        pPrecio.setOpaque(false);
        Estilos.agregarCampo(pPrecio, 0, "Precio (S/)", txtPrecio);
        JPanel pStock = new JPanel(new GridBagLayout());
        pStock.setOpaque(false);
        Estilos.agregarCampo(pStock, 0, "Stock", spnStock);
        precioStock.add(pPrecio);
        precioStock.add(pStock);
        Estilos.agregarCampo(campos, 4, "Color", txtColor);
        Estilos.agregarCampo(campos, 5, "", precioStock);
        Estilos.agregarCampo(campos, 6, "Descripción", new JScrollPane(txtDescripcion));

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.setOpaque(false);
        arriba.add(campos, BorderLayout.NORTH);
        panel.add(arriba, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(1, 3, 8, 0));
        botones.setOpaque(false);
        botones.add(btnNuevo);
        botones.add(btnEliminar);
        botones.add(btnGuardar);
        panel.add(botones, BorderLayout.SOUTH);

        JScrollPane sp = Estilos.scrollLimpio(new AnchoAjustable(panel));
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(new Dimension(420, 0));
        return sp;
    }

    // ----- Metodos usados por el controlador -----

    public void setModoEdicion(boolean editando) {
        lblModo.setText(editando ? "EDITAR PRODUCTO" : "NUEVO PRODUCTO");
        btnEliminar.setEnabled(editando);
    }

    public void limpiarFormulario() {
        txtNombre.setText("");
        if (cboCategoria.getItemCount() > 0) {
            cboCategoria.setSelectedIndex(0);
        }
        setTallasSeleccionadas(List.of());
        txtColor.setText("");
        txtPrecio.setText("");
        spnStock.setValue(0);
        txtDescripcion.setText("");
        foto.setRuta(null);
        tabla.clearSelection();
        setModoEdicion(false);
    }

    public List<String> getTallasSeleccionadas() {
        List<String> lista = new ArrayList<>();
        for (JCheckBox c : chkTallas) {
            if (c.isSelected()) {
                lista.add(c.getText());
            }
        }
        return lista;
    }

    public void setTallasSeleccionadas(List<String> tallas) {
        for (JCheckBox c : chkTallas) {
            c.setSelected(tallas.contains(c.getText()));
        }
    }

    public void setFoto(String ruta) {
        foto.setRuta(ruta);
    }

    /** Abre el selector de archivos; devuelve null si el usuario cancela. */
    public File elegirArchivoFoto() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Elegir foto del producto");
        fc.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png"));
        return fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile() : null;
    }

    public JTextField getTxtBuscar() { return txtBuscar; }
    public JComboBox<Categoria> getCboFiltroCategoria() { return cboFiltroCategoria; }
    public DefaultTableModel getModelo() { return modelo; }
    public JTable getTabla() { return tabla; }
    public JButton getBtnFoto() { return btnFoto; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JComboBox<Categoria> getCboCategoria() { return cboCategoria; }
    public JTextField getTxtColor() { return txtColor; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public JSpinner getSpnStock() { return spnStock; }
    public JTextArea getTxtDescripcion() { return txtDescripcion; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnEliminar() { return btnEliminar; }
}
