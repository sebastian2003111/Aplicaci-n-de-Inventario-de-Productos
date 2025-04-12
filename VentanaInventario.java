import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

//  Path: src/VentanaInventario.java
public class VentanaInventario extends JFrame {
    private Inventario<Producto> inventario = new Inventario<>();
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField idField, nombreField, precioField, cantidadField;

    public VentanaInventario() {
        setTitle("Inventario de Productos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        inputPanel.add(nombreField);

        inputPanel.add(new JLabel("Precio:"));
        precioField = new JTextField();
        inputPanel.add(precioField);

        inputPanel.add(new JLabel("Cantidad:"));
        cantidadField = new JTextField();
        inputPanel.add(cantidadField);

        add(inputPanel, BorderLayout.NORTH);

        // Tabla para mostrar productos
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Cantidad"}, 0);
        tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        JButton agregarBtn = new JButton("Agregar");
        JButton eliminarBtn = new JButton("Eliminar");
        JButton modificarBtn = new JButton("Modificar");
        JButton buscarBtn = new JButton("Buscar");
        JButton mostrarBtn = new JButton("Mostrar todo");
        JButton guardarBtn = new JButton("Guardar en archivo");
        JButton cargarBtn = new JButton("Cargar desde archivo");

        buttonPanel.add(agregarBtn);
        buttonPanel.add(eliminarBtn);
        buttonPanel.add(modificarBtn);
        buttonPanel.add(buscarBtn);
        buttonPanel.add(mostrarBtn);
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cargarBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Acciones de los botones ⬇️⬇️⬇️
        agregarBtn.addActionListener(e -> {
            try {
                String id = idField.getText();
                String nombre = nombreField.getText();
                double precio = Double.parseDouble(precioField.getText());
                int cantidad = Integer.parseInt(cantidadField.getText());
                Producto nuevo = new Producto(id, nombre, precio, cantidad);
                inventario.agregarProducto(nuevo);
                modeloTabla.addRow(new Object[]{id, nombre, precio, cantidad});
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Producto Agregado Correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al agregar producto.");
            }
        });

        eliminarBtn.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String id = modeloTabla.getValueAt(fila, 0).toString();
                inventario.eliminarProducto(id);
                modeloTabla.removeRow(fila);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
            }
        });

        // Accion Modificar 
        modificarBtn.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                try {
                    String id = modeloTabla.getValueAt(fila, 0).toString();
                    double precio = Double.parseDouble(precioField.getText());
                    int cantidad = Integer.parseInt(cantidadField.getText());
                    inventario.modificarProducto(id, precio, cantidad);
                    modeloTabla.setValueAt(precio, fila, 2);
                    modeloTabla.setValueAt(cantidad, fila, 3);
                    limpiarCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al modificar producto.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para modificar.");
            }
        });

        // Accion Buscar
        buscarBtn.addActionListener(e -> {
            String id = idField.getText();
            Producto p = inventario.buscarProducto(id);
            if (p != null) {
                JOptionPane.showMessageDialog(this, p.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.");
            }
        });

        // 
        mostrarBtn.addActionListener(e -> {
            modeloTabla.setRowCount(0);
            for (Producto p : inventario.obtenerTodosLosProductos()) {
                modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getCantidad()});
            }
        });

        guardarBtn.addActionListener(e -> {
            try {
                inventario.guardarEnArchivo("inventario.txt");
                JOptionPane.showMessageDialog(this, "Inventario guardado en archivo.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar archivo.");
            }
        });

        cargarBtn.addActionListener(e -> {
            try {
                inventario.cargarDesdeArchivo("inventario.txt");
                modeloTabla.setRowCount(0);
                for (Producto p : inventario.obtenerTodosLosProductos()) {
                    modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getCantidad()});
                }
                JOptionPane.showMessageDialog(this, "Inventario cargado desde archivo.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar archivo.");
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    idField.setText(modeloTabla.getValueAt(fila, 0).toString());
                    nombreField.setText(modeloTabla.getValueAt(fila, 1).toString());
                    precioField.setText(modeloTabla.getValueAt(fila, 2).toString());
                    cantidadField.setText(modeloTabla.getValueAt(fila, 3).toString());
                }
            }
        });
    }

    // Método para limpiar los campos de entrada
    private void limpiarCampos() {
        idField.setText("");
        nombreField.setText("");
        precioField.setText("");
        cantidadField.setText("");
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaInventario().setVisible(true));
    }
}
