package com.techlab.main;

import com.techlab.productos.Producto;
import com.techlab.pedidos.LineaPedido;
import com.techlab.pedidos.Pedido;
import com.techlab.excepciones.StockInsuficienteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static List<Producto> productos = new ArrayList<>();
    static List<Pedido> pedidos = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Agregar Producto");
            System.out.println("2. Listar Productos");
            System.out.println("3. Buscar/Actualizar Producto");
            System.out.println("4. Eliminar Producto");
            System.out.println("5. Crear Pedido");
            System.out.println("6. Listar Pedidos");
            System.out.println("7. Salir");
            System.out.print("Opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1 -> agregarProducto();
                case 2 -> listarProductos();
                case 3 -> buscarActualizarProducto();
                case 4 -> eliminarProducto();
                case 5 -> crearPedido();
                case 6 -> listarPedidos();
                case 7 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 7);
    }

    static void agregarProducto() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Precio: ");
            double precio = Double.parseDouble(scanner.nextLine());
            System.out.print("Stock: ");
            int stock = Integer.parseInt(scanner.nextLine());
            productos.add(new Producto(nombre, precio, stock));
            System.out.println("Producto agregado.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Entrada no válida.");
        }
    }

    static void listarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos.");
            return;
        }
        productos.forEach(System.out::println);
    }

    static void buscarActualizarProducto() {
        System.out.print("Ingrese ID del producto: ");
        int id = Integer.parseInt(scanner.nextLine());
        Producto p = productos.stream().filter(prod -> prod.getId() == id).findFirst().orElse(null);
        if (p != null) {
            System.out.println(p);
            System.out.print("Actualizar precio? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                System.out.print("Nuevo precio: ");
                p.setPrecio(Double.parseDouble(scanner.nextLine()));
            }
            System.out.print("Actualizar stock? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                System.out.print("Nuevo stock: ");
                p.setStock(Integer.parseInt(scanner.nextLine()));
            }
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    static void eliminarProducto() {
        System.out.print("Ingrese ID del producto a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());
        productos.removeIf(p -> p.getId() == id);
        System.out.println("Producto eliminado (si existía).");
    }

    static void crearPedido() {
        Pedido pedido = new Pedido();
        while (true) {
            listarProductos();
            System.out.print("ID de producto (-1 para terminar): ");
            int id = Integer.parseInt(scanner.nextLine());
            if (id == -1) break;
            Producto p = productos.stream().filter(prod -> prod.getId() == id).findFirst().orElse(null);
            if (p == null) {
                System.out.println("Producto no encontrado.");
                continue;
            }
            System.out.print("Cantidad: ");
            int cantidad = Integer.parseInt(scanner.nextLine());
            try {
                if (cantidad > p.getStock()) {
                    throw new StockInsuficienteException("Stock insuficiente para " + p.getNombre());
                }
                pedido.agregarLinea(new LineaPedido(p, cantidad));
                p.setStock(p.getStock() - cantidad);
                System.out.println("Producto agregado al pedido.");
            } catch (StockInsuficienteException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        if (!pedido.getLineas().isEmpty()) {
            pedidos.add(pedido);
            System.out.println("Pedido creado:\n" + pedido);
        } else {
            System.out.println("Pedido vacío. No se creó.");
        }
    }

    static void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos.");
        } else {
            pedidos.forEach(System.out::println);
        }
    }
}