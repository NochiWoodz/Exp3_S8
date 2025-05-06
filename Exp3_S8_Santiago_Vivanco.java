import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

// Clase para representar un Cliente
class Cliente {
    int idCliente;
    String nombre;
    String tipo; // "Estudiante", "Tercera Edad", "General"
    static int proximoId = 1;

    public Cliente(String nombre, String tipo) {
        this.idCliente = proximoId++;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "ID Cliente: " + idCliente + ", Nombre: " + nombre + ", Tipo: " + tipo;
    }
}

// Clase para representar un Asiento
class Asiento {
    String ubicacion; // Ejemplo: "A1", "B5"
    boolean ocupado;
    double precioBase;

    public Asiento(String ubicacion, double precioBase) {
        this.ubicacion = ubicacion;
        this.ocupado = false;
        this.precioBase = precioBase;
    }

    @Override
    public String toString() {
        return "Asiento: " + ubicacion + ", Precio Base: $" + precioBase + ", Estado: " + (ocupado ? "Ocupado" : "Disponible");
    }
}

// Clase para representar una Venta
class Venta {
    int idVenta;
    int idCliente;
    String ubicacionAsiento;
    double precioFinal;
    static int proximoId = 1;

    public Venta(int idCliente, String ubicacionAsiento, double precioFinal) {
        this.idVenta = proximoId++;
        this.idCliente = idCliente;
        this.ubicacionAsiento = ubicacionAsiento;
        this.precioFinal = precioFinal;
    }

    @Override
    public String toString() {
        return "ID Venta: " + idVenta + ", ID Cliente: " + idCliente + ", Asiento: " + ubicacionAsiento + ", Precio Final: $" + precioFinal;
    }
}

// Clase para representar una Reserva (para la lista de reservas)
class Reserva {
    int idReserva;
    int idCliente;
    String ubicacionAsiento;
    // Se podrían añadir más detalles como fecha, etc.
    static int proximoId = 1;

    public Reserva(int idCliente, String ubicacionAsiento) {
        this.idReserva = proximoId++;
        this.idCliente = idCliente;
        this.ubicacionAsiento = ubicacionAsiento;
    }

    @Override
    public String toString() {
        return "ID Reserva: " + idReserva + ", ID Cliente: " + idCliente + ", Asiento Reservado: " + ubicacionAsiento;
    }
}

// Clase principal para la gestión del Teatro Moro
public class Exp3_S8_Santiago_Vivanco {

    // Arreglos para ventas, asientos y clientes
    static Cliente[] clientes = new Cliente[50]; // Capacidad máxima de 50 clientes
    static Asiento[] asientos; // Se inicializará en el main
    static Venta[] ventas = new Venta[100]; // Capacidad máxima de 100 ventas

    static int numClientes = 0;
    static int numVentas = 0;

    // Listas para descuentos/promociones y reservas de asientos
    // Para descuentos, aplicaremos directamente las reglas del 10% y 15% en la venta
    static ArrayList<String> promocionesActivas = new ArrayList<>(); 
    static ArrayList<Reserva> listaReservas = new ArrayList<>();

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarAsientos(5, 10); // Ejemplo: 5 filas, 10 asientos por fila

        // Añadir algunas promociones de ejemplo (no usadas directamente en el cálculo de descuento actual)
        promocionesActivas.add("Descuento de Verano General (No implementado específicamente)");

        int opcion;
        do {
            System.out.println("\n--- Menú Teatro Moro ---");
            System.out.println("1. Registrar Cliente");
            System.out.println("2. Ver Asientos Disponibles");
            System.out.println("3. Realizar Venta de Entrada");
            System.out.println("4. Ver Ventas Realizadas");
            System.out.println("5. Ver Clientes Registrados");
            System.out.println("6. Ver Reservas");
            System.out.println("7. Actualizar Información de Cliente");
            System.out.println("8. Eliminar Cliente (Requiere reestructuración de ventas/reservas asociadas - Simplificado)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea

            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    mostrarAsientos();
                    break;
                case 3:
                    realizarVenta();
                    break;
                case 4:
                    mostrarVentas();
                    break;
                case 5:
                    mostrarClientes();
                    break;
                case 6:
                    mostrarReservas();
                    break;
                case 7:
                    actualizarCliente();
                    break;
                case 8:
                    eliminarCliente();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    // Inicialización de asientos
    public static void inicializarAsientos(int filas, int asientosPorFila) {
        asientos = new Asiento[filas * asientosPorFila];
        char filaChar = 'A';
        double precioBase = 10000; // Precio base ejemplo
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < asientosPorFila; j++) {
                asientos[i * asientosPorFila + j] = new Asiento(filaChar + String.valueOf(j + 1), precioBase);
                if ((i * asientosPorFila + j) % 2 == 0) precioBase += 500; // Variar precios
            }
            filaChar++;
        }
    }

    // --- Funcionalidades Clientes (Adición, Eliminación, Actualización en arreglos) ---
    public static void registrarCliente() {
        if (numClientes >= clientes.length) {
            System.out.println("Capacidad máxima de clientes alcanzada.");
            return;
        }
        System.out.print("Nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Tipo de cliente (Estudiante, Tercera Edad, General): ");
        String tipo = scanner.nextLine();

        // Validación de entrada
        if (!tipo.equals("Estudiante") && !tipo.equals("Tercera edad") && !tipo.equals("General")) {
            System.out.println("Tipo de cliente no válido. Se registrará como General.");
            tipo = "General";
        }

        clientes[numClientes] = new Cliente(nombre, tipo);
        numClientes++;
        System.out.println("Cliente registrado con éxito: " + clientes[numClientes-1].toString());
    }

    public static void mostrarClientes() {
        System.out.println("\n--- Clientes Registrados ---");
        if (numClientes == 0) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (int i = 0; i < numClientes; i++) {
            System.out.println(clientes[i].toString());
        }
    }
    
    public static Cliente buscarClientePorId(int id) {
        for (int i = 0; i < numClientes; i++) {
            if (clientes[i].idCliente == id) {
                return clientes[i];
            }
        }
        return null;
    }

    public static void actualizarCliente() { // Paso 3: Actualización
        System.out.print("Ingrese ID del cliente a actualizar: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine(); // Consumir newline

        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.println("Cliente encontrado: " + cliente.toString());
        System.out.print("Nuevo nombre (dejar en blanco para no cambiar): ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Nuevo tipo (Estudiante, Tercera Edad, General - dejar en blanco para no cambiar): ");
        String nuevoTipo = scanner.nextLine();

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            cliente.nombre = nuevoNombre;
        }
        if (nuevoTipo != null && !nuevoTipo.trim().isEmpty()) {
            if (!nuevoTipo.equals("Estudiante") && !nuevoTipo.equals("Tercera Edad") && !nuevoTipo.equals("General")) {
                System.out.println("Tipo de cliente no válido. No se actualizó el tipo.");
            } else {
                cliente.tipo = nuevoTipo;
            }
        }
        System.out.println("Cliente actualizado: " + cliente.toString());
    }
    
    public static void eliminarCliente() { // Paso 3: Eliminación
        System.out.print("Ingrese ID del cliente a eliminar: ");
        int idClienteAEliminar = scanner.nextInt();
        scanner.nextLine();

        int indiceCliente = -1;
        for (int i = 0; i < numClientes; i++) {
            if (clientes[i].idCliente == idClienteAEliminar) {
                indiceCliente = i;
                break;
            }
        }

        if (indiceCliente == -1) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        // Crear un nuevo arreglo sin el cliente eliminado
        Cliente[] nuevosClientes = new Cliente[clientes.length];
        int nuevoIndice = 0;
        for (int i = 0; i < numClientes; i++) {
            if (i != indiceCliente) {
                nuevosClientes[nuevoIndice++] = clientes[i];
            }
        }
        clientes = nuevosClientes;
        numClientes--;
        
        System.out.println("Cliente eliminado (simplificado). Las ventas/reservas asociadas no se modificaron.");
    }


    // --- Funcionalidades Asientos ---
    public static void mostrarAsientos() {
        System.out.println("\n--- Estado de los Asientos ---");
        for (int i = 0; i < asientos.length; i++) {
            System.out.println(asientos[i].toString());
            if ((i + 1) % 10 == 0) System.out.println(); // Salto de línea para mejor visualización
        }
    }

    public static Asiento buscarAsiento(String ubicacion) {
        for (Asiento asiento : asientos) {
            if (asiento.ubicacion.equalsIgnoreCase(ubicacion)) {
                return asiento;
            }
        }
        return null;
    }

    // --- Paso 2: Implementación de funcionalidades ---
    // Venta de entradas
    public static void realizarVenta() {
        if (numVentas >= ventas.length) {
            System.out.println("Capacidad máxima de ventas alcanzada.");
            return;
        }
        if (numClientes == 0) {
            System.out.println("No hay clientes registrados. Registre un cliente primero.");
            return;
        }

        System.out.print("ID del Cliente para la venta: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea

        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.print("Ubicación del asiento deseado (ej. A1): ");
        String ubicacionAsiento = scanner.nextLine();

        Asiento asientoSeleccionado = buscarAsiento(ubicacionAsiento);

        // Validación de entrada y referencia cruzada [cite: 203, 204]
        if (asientoSeleccionado == null) {
            System.out.println("Ubicación de asiento no válida.");
            return;
        }
        if (asientoSeleccionado.ocupado) {
            System.out.println("El asiento seleccionado ya está ocupado.");
            return;
        }

        double precioBase = asientoSeleccionado.precioBase;
        double descuento = 0.0;

        // Descuentos y promociones
        if (cliente.tipo.equals("Estudiante")) {
            descuento = precioBase * 0.10; // 10% descuento
            System.out.println("Descuento Estudiante (10%) aplicado: -$" + descuento);
        } else if (cliente.tipo.equals("Tercera Edad")) {
            descuento = precioBase * 0.15; // 15% descuento
            System.out.println("Descuento Tercera Edad (15%) aplicado: -$" + descuento);
        }

        double precioFinal = precioBase - descuento;

        System.out.println("Precio Base Asiento: $" + precioBase);
        System.out.println("Precio Final con Descuento: $" + precioFinal);
        System.out.print("Confirmar venta? (S/N): ");
        String confirmar = scanner.nextLine();

        if (confirmar.equalsIgnoreCase("S")) {
            asientoSeleccionado.ocupado = true;
            ventas[numVentas] = new Venta(cliente.idCliente, asientoSeleccionado.ubicacion, precioFinal);
            numVentas++;
            
            // Añadir a la lista de reservas 
            listaReservas.add(new Reserva(cliente.idCliente, asientoSeleccionado.ubicacion));

            System.out.println("Venta realizada con éxito: " + ventas[numVentas-1].toString());
            System.out.println("Reserva registrada: " + listaReservas.get(listaReservas.size()-1).toString());
        } else {
            System.out.println("Venta cancelada.");
        }
    }

    public static void mostrarVentas() {
        System.out.println("\n--- Ventas Realizadas ---");
        if (numVentas == 0) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        for (int i = 0; i < numVentas; i++) {
            System.out.println(ventas[i].toString());
        }
    }

    public static void mostrarReservas() {
        System.out.println("\n--- Lista de Reservas ---");
        if (listaReservas.isEmpty()) {
            System.out.println("No hay reservas en la lista.");
            return;
        }
        for (Reserva reserva : listaReservas) {
            System.out.println(reserva.toString());
        }
    }
}