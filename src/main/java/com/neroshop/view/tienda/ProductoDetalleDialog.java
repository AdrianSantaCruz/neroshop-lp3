package com.neroshop.view.tienda;

import com.neroshop.model.Producto;
import com.neroshop.util.Formato;
import com.neroshop.view.Estilos;
import com.neroshop.view.componentes.ImagenProducto;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;

/** Ventana con el detalle de un producto: foto grande, tallas, cantidad y boton de compra. */
public class ProductoDetalleDialog extends JDialog {

    private final ButtonGroup grupoTallas = new ButtonGroup();
    private final JSpinner spnCantidad;
    private final JButton btnAgregar = Estilos.botonPrimario("Agregar al carrito");
    private final JButton btnSeguir = Estilos.botonSecundario("Seguir viendo");

    public ProductoDetalleDialog(Window owner, Producto p) {
        super(owner, p.getNombre(), ModalityType.APPLICATION_MODAL);
        setSize(960, 640);
        setResizable(false);
        setLocationRelativeTo(owner);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(Estilos.FONDO);

        ImagenProducto foto = new ImagenProducto(p.getImagen(), 480, 600);
        if (p.isAgotado()) {
            foto.setSello("AGOTADO");
        }
        contenido.add(foto, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(40, 40, 32, 40));

        JLabel categoria = Estilos.lblEtiqueta(p.getCategoriaNombre());
        JLabel nombre = Estilos.lblTitulo(p.getNombre(), 30);
        JLabel precio = new JLabel(Formato.moneda(p.getPrecio()));
        precio.setFont(Estilos.titulo(26));
        precio.setForeground(Estilos.BLANCO);
        JLabel color = new JLabel("Color: " + p.getColor());
        color.setForeground(Estilos.GRIS);
        color.setFont(Estilos.texto(Font.PLAIN, 13));

        JTextArea descripcion = new JTextArea(p.getDescripcion() == null ? "" : p.getDescripcion());
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setEditable(false);
        descripcion.setFocusable(false);
        descripcion.setOpaque(false);
        descripcion.setForeground(new java.awt.Color(0xC8, 0xC8, 0xC8));
        descripcion.setFont(Estilos.texto(Font.PLAIN, 14));
        descripcion.setMaximumSize(new Dimension(380, 90));

        JPanel tallas = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        tallas.setOpaque(false);
        for (String t : p.getListaTallas()) {
            JToggleButton chip = Estilos.chip(t);
            chip.setActionCommand(t);
            chip.setPreferredSize(new Dimension(Math.max(54, chip.getPreferredSize().width), 40));
            chip.setEnabled(!p.isAgotado());
            grupoTallas.add(chip);
            tallas.add(chip);
        }
        if (p.getListaTallas().size() == 1) {
            grupoTallas.getElements().nextElement().setSelected(true);
        }

        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, Math.max(1, p.getStock()), 1));
        spnCantidad.setPreferredSize(new Dimension(90, 40));
        spnCantidad.setMaximumSize(new Dimension(90, 40));
        spnCantidad.setEnabled(!p.isAgotado());

        JLabel stock = new JLabel(p.isAgotado() ? "Sin stock por ahora"
                : p.getStock() <= 5 ? "¡Últimas " + p.getStock() + " unidades!" : p.getStock() + " unidades disponibles");
        stock.setForeground(p.getStock() <= 5 ? Estilos.ROJO : Estilos.GRIS);
        stock.setFont(Estilos.texto(Font.PLAIN, 12));

        btnAgregar.setEnabled(!p.isAgotado());
        JPanel botones = new JPanel(new GridLayout(2, 1, 0, 10));
        botones.setOpaque(false);
        botones.add(btnAgregar);
        botones.add(btnSeguir);
        botones.setMaximumSize(new Dimension(380, 100));

        JLabel beneficios = new JLabel("<html>•  Envío a todo el país &nbsp;&nbsp; •  Cambios hasta 30 días</html>");
        beneficios.setForeground(Estilos.GRIS);
        beneficios.setFont(Estilos.texto(Font.PLAIN, 12));

        agregar(info, categoria, 8);
        agregar(info, nombre, 10);
        agregar(info, precio, 4);
        agregar(info, color, 20);
        agregar(info, descripcion, 24);
        agregar(info, Estilos.lblEtiqueta("Talla"), 8);
        agregar(info, tallas, 20);
        agregar(info, Estilos.lblEtiqueta("Cantidad"), 8);
        JPanel filaCantidad = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaCantidad.setOpaque(false);
        filaCantidad.add(spnCantidad);
        filaCantidad.add(Box.createHorizontalStrut(14));
        filaCantidad.add(stock);
        agregar(info, filaCantidad, 26);
        agregar(info, botones, 16);
        agregar(info, beneficios, 0);
        contenido.add(info, BorderLayout.CENTER);

        setContentPane(contenido);
        btnSeguir.addActionListener(e -> dispose());
        getRootPane().registerKeyboardAction(e -> dispose(),
                javax.swing.KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private static void agregar(JPanel panel, JComponent c, int espacioDespues) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(c);
        if (espacioDespues > 0) {
            panel.add(Box.createVerticalStrut(espacioDespues));
        }
    }

    /** Talla elegida, o null si no se selecciono ninguna. */
    public String getTallaSeleccionada() {
        ButtonModel m = grupoTallas.getSelection();
        return m == null ? null : m.getActionCommand();
    }

    public int getCantidad() {
        return (Integer) spnCantidad.getValue();
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Nero Thread", JOptionPane.WARNING_MESSAGE);
    }
}
