import java.io.*;
import java.util.*;

// File: Inventario.java
public class Inventario<T extends Producto> {
    private List<T> productos = new ArrayList<>();

    public void agregarProducto(T producto) { // Método genérico
        productos.add(producto); // Agrega el producto a la lista
    }

    public void eliminarProducto(String id) { // Método genérico
        productos.removeIf(p -> p.getId().equals(id)); // Elimina el producto con el ID especificado
    }

    public void modificarProducto(String id, double nuevoPrecio, int nuevaCantidad) { // Método genérico
        for (T p : productos) { // Itera sobre la lista de productos
            if (p.getId().equals(id)) {
                p.setPrecio(nuevoPrecio);
                p.setCantidad(nuevaCantidad);
                break;
            }
        }
    }

    public T buscarProducto(String id) { // Método genérico
        for (T p : productos) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    // Método para obtener todos los productos
    public List<T> obtenerTodosLosProductos() {
        return productos;
    }

    // Método para guardar y cargar productos desde un archivo
    public void guardarEnArchivo(String nombreArchivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (T p : productos) {
                writer.write(p.getId() + "," + p.getNombre() + "," + p.getPrecio() + "," + p.getCantidad());
                writer.newLine();
            }
        }
    }

    // Método para cargar productos desde un archivo
    public void cargarDesdeArchivo(String nombreArchivo) throws IOException {
        productos.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 4) {
                    String id = partes[0];
                    String nombre = partes[1];
                    double precio = Double.parseDouble(partes[2]);
                    int cantidad = Integer.parseInt(partes[3]);
                    productos.add((T) new Producto(id, nombre, precio, cantidad));
                }
            }
        }
    }
}
