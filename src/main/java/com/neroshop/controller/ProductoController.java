package com.neroshop.controller;

import com.neroshop.model.Categoria;
import com.neroshop.model.Producto;
import com.neroshop.model.dao.CategoriaDAO;
import com.neroshop.model.dao.DAOException;
import com.neroshop.model.dao.ProductoDAO;
import com.neroshop.util.Imagenes;
import com.neroshop.view.admin.ProductoAdminPanel;

import javax.swing.ImageIcon;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Mantenimiento de productos del panel de administracion. */
public class ProductoController implements Refrescable {

    private static final Categoria TODAS = new Categoria(0, "Todas las categorías", null);

    private final ProductoAdminPanel vista;
    private final ProductoDAO productoDAO;
    private final CategoriaDAO categoriaDAO;

    private List<Producto> productos = new ArrayList<>();
    private Producto seleccionado;
    /** Foto del formulario (puede cambiar antes de guardar). */
    private String fotoActual;
    private boolean cargandoCombos;

    public ProductoController(ProductoAdminPanel vista, ProductoDAO productoDAO, CategoriaDAO categoriaDAO) {
        this.vista = vista;
        this.productoDAO = productoDAO;
        this.categoriaDAO = categoriaDAO;
        registrarEventos();
        refrescar();
        limpiar();
    }

    private void registrarEventos() {
        vista.getBtnNuevo().addActionListener(e -> limpiar());
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnFoto().addActionListener(e -> elegirFoto());
        vista.getTxtBuscar().getDocument().addDocumentListener(new CambioTextoListener(this::cargarProductos));
        vista.getCboFiltroCategoria().addActionListener(e -> {
            if (!cargandoCombos) {
                cargarProductos();
            }
        });
        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionar();
            }
        });
    }

    @Override
    public void refrescar() {
        cargarCategorias();
        cargarProductos();
    }

    private void cargarCategorias() {
        try {
            cargandoCombos = true;
            Object filtroActual = vista.getCboFiltroCategoria().getSelectedItem();
            Object categoriaActual = vista.getCboCategoria().getSelectedItem();
            vista.getCboFiltroCategoria().removeAllItems();
            vista.getCboCategoria().removeAllItems();
            vista.getCboFiltroCategoria().addItem(TODAS);
            for (Categoria c : categoriaDAO.listar()) {
                vista.getCboFiltroCategoria().addItem(c);
                vista.getCboCategoria().addItem(c);
            }
            if (filtroActual != null) {
                vista.getCboFiltroCategoria().setSelectedItem(filtroActual);
            }
            if (categoriaActual != null) {
                vista.getCboCategoria().setSelectedItem(categoriaActual);
            }
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        } finally {
            cargandoCombos = false;
        }
    }

    private void cargarProductos() {
        try {
            Categoria filtro = (Categoria) vista.getCboFiltroCategoria().getSelectedItem();
            productos = productoDAO.listar(vista.getTxtBuscar().getText(), filtro == null ? 0 : filtro.getId(),
                    ProductoDAO.Orden.NOMBRE);
            vista.getModelo().setRowCount(0);
            for (Producto p : productos) {
                vista.getModelo().addRow(new Object[]{
                        new ImageIcon(Imagenes.cubrir(p.getImagen(), 48, 58)),
                        p.getId(), p.getNombre(), p.getCategoriaNombre(), p.getTallas().replace(",", ", "),
                        p.getColor(), p.getPrecio(), p.getStock()});
            }
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void seleccionar() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila < 0) {
            return;
        }
        seleccionado = productos.get(vista.getTabla().convertRowIndexToModel(fila));
        fotoActual = seleccionado.getImagen();
        vista.setFoto(fotoActual);
        vista.getTxtNombre().setText(seleccionado.getNombre());
        vista.getCboCategoria().setSelectedItem(new Categoria(seleccionado.getCategoriaId(), null, null));
        vista.setTallasSeleccionadas(seleccionado.getListaTallas());
        vista.getTxtColor().setText(seleccionado.getColor());
        vista.getTxtPrecio().setText(String.format(Locale.US, "%.2f", seleccionado.getPrecio()));
        vista.getSpnStock().setValue(seleccionado.getStock());
        vista.getTxtDescripcion().setText(seleccionado.getDescripcion() == null ? "" : seleccionado.getDescripcion());
        vista.setModoEdicion(true);
    }

    private void limpiar() {
        seleccionado = null;
        fotoActual = null;
        vista.limpiarFormulario();
    }

    private void elegirFoto() {
        File archivo = vista.elegirArchivoFoto();
        if (archivo == null) {
            return;
        }
        try {
            fotoActual = Imagenes.guardarFotoProducto(archivo);
            vista.setFoto(fotoActual);
        } catch (IOException ex) {
            vista.mostrarError("No se pudo cargar la foto: " + ex.getMessage());
        }
    }

    private void guardar() {
        Producto p;
        try {
            p = leerFormulario();
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
            return;
        }
        try {
            if (seleccionado == null) {
                productoDAO.insertar(p);
                vista.mostrarMensaje("Producto registrado. Ya aparece en la tienda.");
            } else {
                p.setId(seleccionado.getId());
                productoDAO.actualizar(p);
                vista.mostrarMensaje("Producto actualizado correctamente.");
            }
            cargarProductos();
            limpiar();
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    /** Valida los datos ingresados y construye el objeto Producto. */
    private Producto leerFormulario() {
        String nombre = vista.getTxtNombre().getText().trim();
        String color = vista.getTxtColor().getText().trim();
        Categoria categoria = (Categoria) vista.getCboCategoria().getSelectedItem();
        List<String> tallas = vista.getTallasSeleccionadas();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Primero registra una categoría.");
        }
        if (tallas.isEmpty()) {
            throw new IllegalArgumentException("Marca al menos una talla disponible.");
        }
        if (color.isEmpty()) {
            throw new IllegalArgumentException("El color es obligatorio.");
        }
        double precio;
        try {
            precio = Double.parseDouble(vista.getTxtPrecio().getText().trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El precio debe ser un número válido (ej. 49.90).");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setCategoriaId(categoria.getId());
        p.setTallas(String.join(",", tallas));
        p.setColor(color);
        p.setPrecio(Math.round(precio * 100.0) / 100.0);
        p.setStock((Integer) vista.getSpnStock().getValue());
        p.setDescripcion(vista.getTxtDescripcion().getText().trim());
        p.setImagen(fotoActual);
        return p;
    }

    private void eliminar() {
        if (seleccionado == null) {
            return;
        }
        if (!vista.confirmar("¿Eliminar el producto \"" + seleccionado.getNombre() + "\"?\nDejará de mostrarse en la tienda.")) {
            return;
        }
        try {
            productoDAO.eliminar(seleccionado.getId());
            cargarProductos();
            limpiar();
        } catch (DAOException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }
}
