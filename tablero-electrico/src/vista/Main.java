package vista;

import java.util.Scanner;
import modelo.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TableroPrincipal tablero = null;

        while (true) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Crear tablero");
            System.out.println("2. Agregar circuito");
            System.out.println("3. Agregar artefacto a circuito");
            System.out.println("4. Mostrar configuración");
            System.out.println("5. Validar tablero");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            try {
                switch (opcion) {
                    case 1:
                        System.out.print("Descripción del tablero: ");
                        String desc = sc.nextLine();

                        InterruptorDiferencial dif = new InterruptorDiferencial("Disyuntor 25A", 25, 30, 1500);
                        InterruptorTermomagnetico igaprincipal = new InterruptorTermomagnetico("IGA 32A", 32, 1200);

                        tablero = new TableroPrincipal(desc, dif, igaprincipal);
                        System.out.println("Tablero creado correctamente.");
                        break;

                    case 2:
                        if (tablero == null) {
                            System.out.println("Primero debe crear el tablero.");
                            break;
                        }

                        System.out.print("Tipo de circuito (IUG/TUG/TUE): ");
                        String tipo = sc.nextLine();
                        System.out.print("Calibre máximo: ");
                        int calibre = sc.nextInt();
                        System.out.print("Cantidad de bocas: ");
                        int bocas = sc.nextInt();
                        sc.nextLine();

                        InterruptorTermomagnetico termico = new InterruptorTermomagnetico("Térmico 10A", 10, 600);
                        Circuito circuito = new Circuito(tipo, calibre, bocas, termico);
                        tablero.agregarCircuito(circuito);
                        System.out.println("Circuito agregado.");
                        break;

                    case 3:
                        if (tablero == null || tablero.getCircuitos().isEmpty()) {
                            System.out.println("No hay circuitos disponibles.");
                            break;
                        }

                        System.out.print("Nombre del artefacto: ");
                        String nombre = sc.nextLine();
                        System.out.print("Wattage: ");
                        int watt = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Tipo (Iluminacion/Aparato): ");
                        String tipoArt = sc.nextLine();

                        Artefacto a = new Artefacto(nombre, watt, tipoArt);
                        tablero.getCircuitos().get(0).agregarArtefacto(a); // simplificado: primer circuito
                        System.out.println("Artefacto agregado al primer circuito.");
                        break;

                    case 4:
                        if (tablero == null) {
                            System.out.println("No hay tablero creado.");
                            break;
                        }

                        System.out.println("\n Tablero: " + tablero.getCircuitos().size() + " circuitos");
                        for (Circuito c : tablero.getCircuitos()) {
                            System.out.println("  - Circuito " + c.getTipo() + ": " + c.getArtefactos().size() + " artefactos");
                            for (Artefacto art : c.getArtefactos()) {
                                System.out.println("    • " + art.getNombre() + " (" + art.getWattage() + "W)");
                            }
                        }
                        break;

                    case 5:
                        if (tablero == null) {
                            System.out.println("No hay tablero creado.");
                            break;
                        }

                        boolean valido = tablero.validarTablero();
                        System.out.println(valido ? "Tablero válido." : "Tablero excede la protección!!!.");
                        break;

                    case 0:
                        System.out.println("Fin del programa.");
                        return;

                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                sc.nextLine(); // limpiar buffer en caso de error
            }
        }
    }
}
