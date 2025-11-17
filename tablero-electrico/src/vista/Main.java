package vista;

import java.util.Scanner;
import java.util.List;
import java.sql.Connection;
import modelo.*;
import controlador.*;
import conexionDB.DBConnection;

public class Main {
    private static ArtefactoController arteController = new ArtefactoController();
    private static CircuitoController circuitoController = new CircuitoController();
    private static TableroPrincipalController tableroController = new TableroPrincipalController();
    private static InterruptorDiferencialController difController = new InterruptorDiferencialController();
    private static InterruptorTermomagneticoController itController = new InterruptorTermomagneticoController();
    private static ArtefactoPorCircuitoController artCircuitoController = new ArtefactoPorCircuitoController();


    private static boolean verificarConexionBD() {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("[OK] Conexión a base de datos verificada.");
                conn.close();
                return true;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] No se pudo conectar a la base de datos: " + e.getMessage());
            System.out.println("[ADVERTENCIA] Verifica que MySQL esté ejecutándose");
        }
        return false;
    }

    public static void main(String[] args) {

        System.out.println("=== TABLERO ELECTRICO - SISTEMA DE GESTION ===\n");
        System.out.println("Verificando conexión a base de datos...");
        
        if (!verificarConexionBD()) {
            System.out.println("\n[ERROR] No se puede continuar sin conexión a la base de datos.");
            System.out.println("Por favor, asegúrate de:");
            System.out.println("  1. MySQL esté ejecutándose");
            System.out.println("  2. La base de datos 'tableros' exista");
            System.out.println("  3. Las credenciales en db.properties sean correctas");
            return;
        }
        
        System.out.println("\nIniciando aplicación...\n");
        
        Scanner sc = new Scanner(System.in);
        TableroPrincipal tablero = null;

        while (true) {
            try {
                System.out.println("\n--- MENU PRINCIPAL ---");
                
                // Mostrar estado del tablero
                if (tablero != null) {
                    double valor = calcularValorInterruptores(tablero);
                    System.out.printf("[ACTIVO] Tablero: %s (ID=%d) - Valor interruptores: $%.2f\n\n", tablero.getDescripcion(), tablero.getId(), valor);
                } else {
                    System.out.println("[SIN TABLERO] No hay tablero activo");
                    System.out.println();
                }
                
                System.out.println("1. Crear tablero");
                System.out.println("2. Agregar circuito");
                System.out.println("3. Gestion de artefactos en circuito");
                System.out.println("4. Mostrar configuracion");
                System.out.println("5. Validar tablero");
                System.out.println("6. Cargar tablero desde BD");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");

                int opcion = -1;
                if (sc.hasNextInt()) {
                    opcion = sc.nextInt();
                    sc.nextLine();
                } else {
                    System.out.println("[ERROR] Por favor ingrese un número.");
                    sc.nextLine();
                    continue;
                }

                switch (opcion) {
                    case 1:
                        tablero = crearTablero(sc);
                        break;

                    case 2:
                        if (tablero == null) {
                            System.out.println("[ADVERTENCIA] Primero debe crear el tablero.");
                            break;
                        }
                        agregarCircuito(sc, tablero);
                        break;

                    case 3:
                        if (tablero == null || tablero.getCircuitos().isEmpty()) {
                            System.out.println("[ADVERTENCIA] No hay circuitos disponibles.");
                            break;
                        }
                        gestionarArtefactos(sc, tablero);
                        break;

                    case 4:
                        if (tablero == null) {
                            System.out.println("[ADVERTENCIA] No hay tablero creado.");
                            break;
                        }
                        mostrarConfiguracion(tablero);
                        break;

                    case 5:
                        if (tablero == null) {
                            System.out.println("[ADVERTENCIA] No hay tablero creado.");
                            break;
                        }
                        validarTablero(tablero);
                        break;

                    case 6:
                        tablero = cargarTableroDesdeDB(sc);
                        break;

                    case 0:
                        System.out.println("Fin del programa.");
                        return;

                    default:
                        System.out.println("[ADVERTENCIA] Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Error: Ingrese un número válido. " + e.getMessage());
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("[ERROR] Error inesperado: " + e.getMessage());
                e.printStackTrace();
                sc.nextLine();
            }
        }
    }

    private static TableroPrincipal crearTablero(Scanner sc) throws Exception {
        try {
            System.out.print("Descripción del tablero: ");
            String desc = sc.nextLine();

            // Mostrar interruptores diferenciales disponibles
            System.out.println("\n--- SELECCIONAR DISYUNTOR DIFERENCIAL ---");
            List<InterruptorDiferencial> diferencialesDisponibles = difController.listAll();
            
            InterruptorDiferencial dif = null;
            
            if (diferencialesDisponibles != null && !diferencialesDisponibles.isEmpty()) {
                System.out.println("Disyuntores disponibles:");
                int index = 1;
                for (InterruptorDiferencial d : diferencialesDisponibles) {
                    System.out.println(index + ". " + d.getDescripcion() + " // " + d.getAmperaje() + "A - Sensibilidad: " + d.getSensibilidad() + "mA");
                    index++;
                }
                System.out.println((index) + ". Crear nuevo disyuntor");
                System.out.print("Seleccione una opción: ");
                
                int opcion = sc.nextInt();
                sc.nextLine();
                
                if (opcion >= 1 && opcion < index) {
                    dif = diferencialesDisponibles.get(opcion - 1);
                    System.out.println("[OK] Disyuntor seleccionado: " + dif.getDescripcion());
                } else if (opcion == index) {
                    dif = crearNuevoDisyuntor(sc);
                } else {
                    System.out.println("[ADVERTENCIA] Opción inválida. Se creará uno nuevo.");
                    dif = crearNuevoDisyuntor(sc);
                }
            } else {
                System.out.println("No hay disyuntores disponibles. Cree uno nuevo.");
                dif = crearNuevoDisyuntor(sc);
            }

            // Crear interruptor principal
            System.out.println("\n--- SELECCIONAR INTERRUPTOR PRINCIPAL ---");
            List<InterruptorTermomagnetico> termicosDisponibles = itController.listAll();
            
            InterruptorTermomagnetico itPrincipal = null;
            
            if (termicosDisponibles != null && !termicosDisponibles.isEmpty()) {
                System.out.println("Interruptores termomagnéticos disponibles:");
                int index = 1;
                for (InterruptorTermomagnetico it : termicosDisponibles) {
                    System.out.println(index + ". " + it.getDescripcion() + " // " + it.getAmperaje() + "A");
                    index++;
                }
                System.out.println((index) + ". Crear nuevo interruptor");
                System.out.print("Seleccione una opción: ");
                
                int opcion = sc.nextInt();
                sc.nextLine();
                
                if (opcion >= 1 && opcion < index) {
                    itPrincipal = termicosDisponibles.get(opcion - 1);
                    System.out.println("[OK] Interruptor seleccionado: " + itPrincipal.getDescripcion());
                } else if (opcion == index) {
                    itPrincipal = crearNuevoInterruptorTermomagnetico(sc);
                } else {
                    System.out.println("[ADVERTENCIA] Opción inválida. Se creará uno nuevo.");
                    itPrincipal = crearNuevoInterruptorTermomagnetico(sc);
                }
            } else {
                System.out.println("No hay interruptores disponibles. Cree uno nuevo.");
                itPrincipal = crearNuevoInterruptorTermomagnetico(sc);
            }

            TableroPrincipal tableroGuardado = tableroController.create(desc, dif, itPrincipal);
            System.out.println("[OK] Tablero creado correctamente con ID=" + tableroGuardado.getId());
            return tableroGuardado;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Error: Ingrese valores numéricos válidos.");
            throw e;
        } catch (Exception e) {
            System.out.println("[ERROR] Error al crear tablero: " + e.getMessage());
            throw e;
        }
    }

    private static InterruptorDiferencial crearNuevoDisyuntor(Scanner sc) throws Exception {
        try {
            System.out.print("Descripción del Disyuntor Diferencial: ");
            String difDesc = sc.nextLine();
            System.out.print("Amperaje del Disyuntor: ");
            int difAmp = sc.nextInt();
            System.out.print("Sensibilidad del Disyuntor (mA): ");
            int difSens = sc.nextInt();
            sc.nextLine();

            InterruptorDiferencial dif = difController.create(difDesc, difAmp, difSens, 1500);
            System.out.println("[OK] Disyuntor creado correctamente con ID=" + dif.getId());
            return dif;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Error: Ingrese valores numéricos válidos.");
            throw e;
        } catch (Exception e) {
            System.out.println("[ERROR] Error al crear disyuntor: " + e.getMessage());
            throw e;
        }
    }

    private static InterruptorTermomagnetico crearNuevoInterruptorTermomagnetico(Scanner sc) throws Exception {
        try {
            System.out.print("Descripción del Interruptor Principal: ");
            String itDesc = sc.nextLine();
            System.out.print("Amperaje del Interruptor: ");
            int itAmp = sc.nextInt();
            sc.nextLine();

            InterruptorTermomagnetico it = itController.create(itDesc, itAmp, 1200);
            System.out.println("[OK] Interruptor creado correctamente con ID=" + it.getId());
            return it;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Error: Ingrese valores numéricos válidos.");
            throw e;
        } catch (Exception e) {
            System.out.println("[ERROR] Error al crear interruptor: " + e.getMessage());
            throw e;
        }
    }

    private static Artefacto crearNuevoArtefacto(Scanner sc) throws Exception {
        try {
            System.out.print("Nombre del artefacto: ");
            String nombre = sc.nextLine();
            System.out.print("Wattage: ");
            int watt = sc.nextInt();
            sc.nextLine();
            System.out.print("Tipo (Iluminacion/Aparato): ");
            String tipoArt = sc.nextLine();

            Artefacto a = arteController.create(nombre, watt, tipoArt);
            System.out.println("[OK] Artefacto creado correctamente con ID=" + a.getId());
            return a;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Error: Ingrese valores numéricos válidos.");
            throw e;
        } catch (Exception e) {
            System.out.println("[ERROR] Error al crear artefacto: " + e.getMessage());
            throw e;
        }
    }

    private static void agregarCircuito(Scanner sc, TableroPrincipal tablero) throws Exception {
        try {
            System.out.println("IUG - Circuito de iluminación de uso general");
            System.out.println("TUG - Circuito de tomas de uso general");
            System.out.println("TUE - Circuito de tomas de uso especial");
            System.out.print("Tipo de circuito (IUG/TUG/TUE): ");
            String tipo = sc.nextLine();

            if (!tipo.matches("IUG|TUG|TUE")) {
                System.out.println("[ADVERTENCIA] Tipo de circuito inválido. Use: IUG, TUG o TUE");
                return;
            }

            System.out.print("Calibre máximo (A): ");
            int calibre = sc.nextInt();
            System.out.print("Cantidad de bocas: ");
            int bocas = sc.nextInt();
            sc.nextLine();

            // Seleccionar Interruptor Termomagnético
            System.out.println("\n--- INTERRUPTOR TERMOMAGNÉTICO ---");
            List<InterruptorTermomagnetico> interruptoresDisponibles = itController.listAll();
            InterruptorTermomagnetico termico = null;

            if (interruptoresDisponibles != null && !interruptoresDisponibles.isEmpty()) {
                System.out.println("Interruptores disponibles:");
                int index = 1;
                for (InterruptorTermomagnetico it : interruptoresDisponibles) {
                    System.out.println(index + ". " + it.getDescripcion() + " (" + it.getAmperaje() + "A)");
                    index++;
                }
                System.out.println(index + ". Crear nuevo interruptor");
                System.out.print("Seleccione una opción: ");
                int opcion = sc.nextInt();
                sc.nextLine();

                if (opcion >= 1 && opcion < index) {
                    termico = interruptoresDisponibles.get(opcion - 1);
                } else if (opcion == index) {
                    termico = crearNuevoInterruptorTermomagnetico(sc);
                } else {
                    System.out.println("[ADVERTENCIA] Opción inválida.");
                    return;
                }
            } else {
                System.out.println("No hay interruptores disponibles. Crear nuevo:");
                termico = crearNuevoInterruptorTermomagnetico(sc);
            }

            Circuito circuito = circuitoController.create(tipo, calibre, bocas, termico, tablero.getId());

            tablero.agregarCircuito(circuito);
            System.out.println("[OK] Circuito agregado correctamente con ID=" + circuito.getId());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Error: Ingrese valores numéricos válidos.");
        } catch (Exception e) {
            System.out.println("[ERROR] Error al agregar circuito: " + e.getMessage());
        }
    }

    private static void gestionarArtefactos(Scanner sc, TableroPrincipal tablero) throws Exception {
        menuArtefactos: while (true) {
            try {
                System.out.println("\n--- GESTION DE ARTEFACTOS EN CIRCUITO---");
                System.out.println("1. Listar artefactos");
                System.out.println("2. Agregar artefacto");
                System.out.println("3. Editar artefacto");
                System.out.println("4. Eliminar artefacto");
                System.out.println("5. Volver al menú principal");
                System.out.print("Seleccione una opción: ");

                int opcion = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1:
                        listarArtefactos(tablero);
                        break;

                    case 2:
                        agregarArtefacto(sc, tablero);
                        break;

                    case 3:
                        editarArtefacto(sc, tablero);
                        break;

                    case 4:
                        eliminarArtefacto(sc, tablero);
                        break;

                    case 5:
                        break menuArtefactos;

                    default:
                        System.out.println("[ADVERTENCIA] Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Error: Ingrese un número válido.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("[ERROR] Error: " + e.getMessage());
            }
        }
    }

    private static void listarArtefactos(TableroPrincipal tablero) throws Exception {
        System.out.println("\n--- LISTADO DE ARTEFACTOS ---");
        int circNum = 1;
        for (Circuito c : tablero.getCircuitos()) {
            System.out.println("\nCircuito " + circNum + " (" + c.getTipo() + "):");
            if (c.getArtefactos().isEmpty()) {
                System.out.println("  Sin artefactos");
            } else {
                int artNum = 1;
                for (Artefacto art : c.getArtefactos()) {
                    System.out.println(artNum + ". " + art.getNombre() + " - Tipo: " + art.getTipo() + " - Consumo: " + art.getWattage() + "W (ID=" + art.getId() + ")");
                    artNum++;
                }
            }
            circNum++;
        }
    }

    private static double calcularValorInterruptores(TableroPrincipal tablero) {
        double total = 0.0;
        if (tablero == null) return total;
        if (tablero.getInterruptorDiferencial() != null) {
            total += tablero.getInterruptorDiferencial().getPrecio();
        }
        if (tablero.getInterruptorPrincipal() != null) {
            total += tablero.getInterruptorPrincipal().getPrecio();
        }
        if (tablero.getCircuitos() != null) {
            for (Circuito c : tablero.getCircuitos()) {
                if (c.getInterruptor() != null) {
                    total += c.getInterruptor().getPrecio();
                }
            }
        }
        return total;
    }

    private static void agregarArtefacto(Scanner sc, TableroPrincipal tablero) throws Exception {
        try {
            System.out.println("\nSeleccione el circuito (1-" + tablero.getCircuitos().size() + "): ");
            int circuitoIndex = sc.nextInt() - 1;
            sc.nextLine();

            if (circuitoIndex < 0 || circuitoIndex >= tablero.getCircuitos().size()) {
                System.out.println("[ADVERTENCIA] Circuito inválido.");
                return;
            }

            // Seleccionar Artefacto
            System.out.println("\n--- ARTEFACTO ---");
            List<Artefacto> artefactosDisponibles = arteController.listAll();
            Artefacto a = null;

            if (artefactosDisponibles != null && !artefactosDisponibles.isEmpty()) {
                System.out.println("Artefactos disponibles:");
                int index = 1;
                for (Artefacto art : artefactosDisponibles) {
                    System.out.println(index + ". " + art.getNombre() + " - Tipo: " + art.getTipo() + " - Consumo: " + art.getWattage() + "W");
                    index++;
                }
                System.out.println(index + ". Crear nuevo artefacto");
                System.out.print("Seleccione una opción: ");
                int opcion = sc.nextInt();
                sc.nextLine();

                if (opcion >= 1 && opcion < index) {
                    a = artefactosDisponibles.get(opcion - 1);
                } else if (opcion == index) {
                    a = crearNuevoArtefacto(sc);
                } else {
                    System.out.println("[ADVERTENCIA] Opción inválida.");
                    return;
                }
            } else {
                System.out.println("No hay artefactos disponibles. Crear nuevo:");
                a = crearNuevoArtefacto(sc);
            }

            tablero.getCircuitos().get(circuitoIndex).agregarArtefacto(a);
            
            artCircuitoController.asociarArtefactoACircuito(tablero.getCircuitos().get(circuitoIndex).getId(), a.getId());
            
            System.out.println("[OK] Artefacto agregado y persistido con ID=" + a.getId());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Error: Ingrese valores válidos.");
        } catch (Exception e) {
            System.out.println("[ERROR] Error al agregar artefacto: " + e.getMessage());
        }
    }

    private static void editarArtefacto(Scanner sc, TableroPrincipal tablero) throws Exception {
        try {
            System.out.println("\nSeleccione el circuito (1-" + tablero.getCircuitos().size() + "): ");
            int circuitoIndex = sc.nextInt() - 1;
            sc.nextLine();

            if (circuitoIndex < 0 || circuitoIndex >= tablero.getCircuitos().size()) {
                System.out.println("[ADVERTENCIA] Circuito inválido.");
                return;
            }

            Circuito circuito = tablero.getCircuitos().get(circuitoIndex);
            if (circuito.getArtefactos().isEmpty()) {
                System.out.println("[ADVERTENCIA] No hay artefactos en este circuito.");
                return;
            }

            System.out.println("Seleccione el artefacto a editar (1-" + circuito.getArtefactos().size() + "): ");
            int artIndex = sc.nextInt() - 1;
            sc.nextLine();

            if (artIndex < 0 || artIndex >= circuito.getArtefactos().size()) {
                System.out.println("[ADVERTENCIA] Artefacto inválido.");
                return;
            }

            System.out.print("Nuevo nombre (Enter para mantener): ");
            String nuevoNombre = sc.nextLine();
            System.out.print("Nuevo wattage (0 para mantener): ");
            int nuevoWatt = sc.nextInt();
            sc.nextLine();
            System.out.print("Nuevo tipo (Enter para mantener): ");
            String nuevoTipo = sc.nextLine();

            Artefacto artExistente = circuito.getArtefactos().get(artIndex);
            Artefacto artModificado = new Artefacto(
                artExistente.getId(),
                nuevoNombre.isEmpty() ? artExistente.getNombre() : nuevoNombre,
                nuevoWatt == 0 ? artExistente.getWattage() : nuevoWatt,
                nuevoTipo.isEmpty() ? artExistente.getTipo() : nuevoTipo
            );

            arteController.update(artModificado);
            circuito.getArtefactos().set(artIndex, artModificado);
            System.out.println("[OK] Artefacto modificado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Error: Ingrese valores válidos.");
        } catch (Exception e) {
            System.out.println("[ERROR] Error al editar artefacto: " + e.getMessage());
        }
    }

    private static void eliminarArtefacto(Scanner sc, TableroPrincipal tablero) throws Exception {
        try {
            System.out.println("\nSeleccione el circuito (1-" + tablero.getCircuitos().size() + "): ");
            int circuitoIndex = sc.nextInt() - 1;
            sc.nextLine();

            if (circuitoIndex < 0 || circuitoIndex >= tablero.getCircuitos().size()) {
                System.out.println("[ADVERTENCIA] Circuito inválido.");
                return;
            }

            Circuito circuito = tablero.getCircuitos().get(circuitoIndex);
            if (circuito.getArtefactos().isEmpty()) {
                System.out.println("[ADVERTENCIA] No hay artefactos en este circuito.");
                return;
            }

            System.out.println("Seleccione el artefacto a eliminar (1-" + circuito.getArtefactos().size() + "): ");
            int artIndex = sc.nextInt() - 1;
            sc.nextLine();

            if (artIndex < 0 || artIndex >= circuito.getArtefactos().size()) {
                System.out.println("[ADVERTENCIA] Artefacto inválido.");
                return;
            }

            Artefacto artEliminado = circuito.getArtefactos().remove(artIndex);
            artCircuitoController.desasociarArtefactoDelCircuito(circuito.getId(), artEliminado.getId());
            System.out.println("[OK] Artefacto '" + artEliminado.getNombre() + "' eliminado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Error: Ingrese valores válidos.");
        } catch (Exception e) {
            System.out.println("[ERROR] Error al eliminar artefacto: " + e.getMessage());
        }
    }

    private static void mostrarConfiguracion(TableroPrincipal tablero) {
        System.out.println("\n--- CONFIGURACION DEL TABLERO ---");
        System.out.println("Descripción: " + tablero.getDescripcion());
        System.out.println("Circuitos: " + tablero.getCircuitos().size());
        
        int circNum = 1;
        for (Circuito c : tablero.getCircuitos()) {
            System.out.println("\n  Circuito " + circNum + " (" + c.getTipo() + "):");
            System.out.println("    Calibre máximo: " + c.getCalibreMaximo() + "A");
            System.out.println("    Cantidad de bocas: " + c.getCantidadBocas());
            System.out.println("    Artefactos: " + c.getArtefactos().size());
            for (Artefacto art : c.getArtefactos()) {
                System.out.println("      * " + art.getNombre() + " (" + art.getWattage() + "W)");
            }
            circNum++;
        }
    }

    private static void validarTablero(TableroPrincipal tablero) {
        boolean valido = tablero.validarTablero();
        if (valido) {
            System.out.println("[OK] Tablero válido - cumple con protecciones.");
        } else {
            System.out.println("[ERROR] Tablero excede la protección - revise la carga.");
        }
    }

    private static TableroPrincipal cargarTableroDesdeDB(Scanner sc) throws Exception {
        try {
            System.out.println("\n--- CARGAR TABLERO DESDE BASE DE DATOS ---");
            
            // Obtener lista de tableros disponibles
            List<TableroPrincipal> tablerosDisponibles = tableroController.listAll();
            
            if (tablerosDisponibles == null || tablerosDisponibles.isEmpty()) {
                System.out.println("[ADVERTENCIA] No hay tableros disponibles en la base de datos.");
                return null;
            }
            
            System.out.println("Tableros disponibles:");
            int index = 1;
            for (TableroPrincipal t : tablerosDisponibles) {
                System.out.println(index + ". [ID=" + t.getId() + "] " + t.getDescripcion() + " (" + t.getCircuitos().size() + " circuitos)");
                index++;
            }
            System.out.println("0. Cancelar");
            System.out.print("Seleccione una opción: ");
            
            int opcion = sc.nextInt();
            sc.nextLine();
            
            if (opcion == 0) {
                System.out.println("[ADVERTENCIA] Operación cancelada.");
                return null;
            }
            
            if (opcion >= 1 && opcion < index) {
                TableroPrincipal tp = tablerosDisponibles.get(opcion - 1);
                System.out.println("[OK] Tablero cargado: " + tp.getDescripcion() + " (" + tp.getCircuitos().size() + " circuitos)");
                return tp;
            } else {
                System.out.println("[ADVERTENCIA] Opción inválida.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Error: Ingrese un número válido.");
            return null;
        } catch (Exception e) {
            System.out.println("[ERROR] Error al cargar tableros: " + e.getMessage());
            throw e;
        }
    }
}


