package com.hotel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Gestió de reserves d'un hotel.
 */
public class App {

    // --------- CONSTANTS I VARIABLES GLOBALS ---------

    // Tipus d'habitació
    public static final String TIPUS_ESTANDARD = "Estàndard";
    public static final String TIPUS_SUITE = "Suite";
    public static final String TIPUS_DELUXE = "Deluxe";

    // Serveis addicionals
    public static final String SERVEI_ESMORZAR = "Esmorzar";
    public static final String SERVEI_GIMNAS = "Gimnàs";
    public static final String SERVEI_SPA = "Spa";
    public static final String SERVEI_PISCINA = "Piscina";

    // Capacitat inicial
    public static final int CAPACITAT_ESTANDARD = 30;
    public static final int CAPACITAT_SUITE = 20;
    public static final int CAPACITAT_DELUXE = 10;

    // IVA
    public static final float IVA = 0.21f;

    // Scanner únic
    public static Scanner sc = new Scanner(System.in);

    // HashMaps de consulta
    public static HashMap<String, Float> preusHabitacions = new HashMap<String, Float>();
    public static HashMap<String, Integer> capacitatInicial = new HashMap<String, Integer>();
    public static HashMap<String, Float> preusServeis = new HashMap<String, Float>();

    // HashMaps dinàmics
    public static HashMap<String, Integer> disponibilitatHabitacions = new HashMap<String, Integer>();
    public static HashMap<Integer, ArrayList<String>> reserves = new HashMap<Integer, ArrayList<String>>();

    // Generador de nombres aleatoris per als codis de reserva
    public static Random random = new Random();

    // --------- MÈTODE MAIN ---------

    /**
     * Mètode principal. Mostra el menú en un bucle i gestiona l'opció triada
     * fins que l'usuari decideix eixir.
     */
    public static void main(String[] args) {
        inicialitzarPreus();

        int opcio = 0;
        do {
            mostrarMenu();
            opcio = llegirEnter("\nSeleccione una opció: ");
            gestionarOpcio(opcio);
        } while (opcio != 6);

        System.out.println("\nEixint del sistema... \nGràcies per utilitzar el gestor de reserves!\n");
    }

    // --------- MÈTODES DEMANATS ---------

    /**
     * Configura els preus de les habitacions, serveis addicionals i
     * les capacitats inicials en els HashMaps corresponents.
     */
    public static void inicialitzarPreus() {
        // Preus habitacions
        preusHabitacions.put(TIPUS_ESTANDARD, 50f);
        preusHabitacions.put(TIPUS_SUITE, 100f);
        preusHabitacions.put(TIPUS_DELUXE, 150f);

        // Capacitats inicials
        capacitatInicial.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        capacitatInicial.put(TIPUS_SUITE, CAPACITAT_SUITE);
        capacitatInicial.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Disponibilitat inicial (comença igual que la capacitat)
        disponibilitatHabitacions.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        disponibilitatHabitacions.put(TIPUS_SUITE, CAPACITAT_SUITE);
        disponibilitatHabitacions.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Preus serveis
        preusServeis.put(SERVEI_ESMORZAR, 10f);
        preusServeis.put(SERVEI_GIMNAS, 15f);
        preusServeis.put(SERVEI_SPA, 20f);
        preusServeis.put(SERVEI_PISCINA, 25f);
    }

    /**
     * Mostra el menú principal amb les opcions disponibles per a l'usuari.
     */
    public static void mostrarMenu() {
        System.out.println("===== MENÚ PRINCIPAL =====\n");
        System.out.println("   1. Reservar una habitació");
        System.out.println("   2. Alliberar una habitació");
        System.out.println("   3. Consultar disponibilitat");
        System.out.println("   4. Llistar reserves per tipus");
        System.out.println("   5. Obtindre una reserva");
        System.out.println("   6. Ixir");
    }

    /**
     * Processa l'opció seleccionada per l'usuari i crida el mètode corresponent.
     */
    public static void gestionarOpcio(int opcio) {
        switch (opcio) {
            case 1:
                reservarHabitacio();
                break;
            case 2:
                alliberarHabitacio();
                break;
            case 3:
                consultarDisponibilitat();
                break;
            case 4:
                obtindreReservaPerTipus();
                break;
            case 5:
                obtindreReserva();
                break;
            case 6:
                break;
            default:
                System.out.println("Error: Opció no vàlida!!!\n");
        }
    }

    /**
     * Gestiona tot el procés de reserva: selecció del tipus d'habitació,
     * serveis addicionals, càlcul del preu total i generació del codi de reserva.
     */
    public static void reservarHabitacio() {
        System.out.println("\n===== RESERVAR HABITACIÓ =====");
        // seleccionar tipus d'habitació
        String tipus = seleccionarTipusHabitacioDisponible();
        // si hi ha disponibilitat
        if (tipus != null) {
            // seleccionar serveis adicionals
            ArrayList<String> serveis = seleccionarServeis();
            // calcular preu total
            float preu = calcularPreuTotal(tipus, serveis);
            // generar codi de reserva
            int codi = generarCodiReserva();
            // --> enregistrar la reserva
            // crear l'arraylist amb les dades de la reserva
            ArrayList<String> dadesReserva = new ArrayList<>();
            // introduir les dades en l'arraylist
            dadesReserva.add(tipus);
            dadesReserva.add("" + preu);
            for (String servei : serveis) {
                dadesReserva.add(servei);
            }
            // introduir el codi i l'arraylist en el hashmap
            reserves.put(codi, dadesReserva);
            // mostrar missatge de reserva creada
            System.out.println("\nReserva creada amb èxit!");
            System.out.println("Codi de reserva: " + codi + "\n");
            // actualitzar disponibilitat
            disponibilitatHabitacions.replace(tipus, disponibilitatHabitacions.get(tipus) - 1);
        }
    }

    /**
     * Pregunta a l'usuari un tipus d'habitació en format numèric i
     * retorna el nom del tipus.
     */
    public static String seleccionarTipusHabitacio() {
        int numero = llegirEnter("\nSeleccione tipus d'habitació: ");
        switch (numero) {
            case 1:
                return TIPUS_ESTANDARD;
            case 2:
                return TIPUS_SUITE;
            case 3:
                return TIPUS_DELUXE;
            default:
                System.out.println("Error: Opció no vàlida!!!");
                return null;
        }
    }

    /**
     * Mostra la disponibilitat i el preu de cada tipus d'habitació,
     * demana a l'usuari un tipus i només el retorna si encara hi ha
     * habitacions disponibles. En cas contrari, retorna null.
     */
    public static String seleccionarTipusHabitacioDisponible() {
        System.out.println("\nTipus d'habitació disponibles");
        System.out.println("-----------------------------");
        // mostrar disponibilitat
        System.out.printf("   1. " + TIPUS_ESTANDARD + " (" + disponibilitatHabitacions.get(TIPUS_ESTANDARD) 
                                    + " disponibles) - %.0f €\n", preusHabitacions.get(TIPUS_ESTANDARD));
        System.out.printf("   2. " + TIPUS_SUITE + " (" + disponibilitatHabitacions.get(TIPUS_SUITE) 
                                    + " disponibles) - %.0f €\n", preusHabitacions.get(TIPUS_SUITE));
        System.out.printf("   3. " + TIPUS_DELUXE + " (" + disponibilitatHabitacions.get(TIPUS_DELUXE) 
                                    + " disponibles) - %.0f €\n", preusHabitacions.get(TIPUS_DELUXE));
        // demanar tipus d'habitació
        String tipus = "";
        do {
            tipus = seleccionarTipusHabitacio();
        } while (tipus == null);
        // retornar sols si queden disponibles
        if (disponibilitatHabitacions.get(tipus) > 0) {
            return tipus;
        } else {
            System.out.println("\nHo sentim molt!");
            System.out.println("En aquest moment no tenim disponibilitat del tipus " + tipus + "\n");
            return null;
        }
    }

    /**
     * Permet triar serveis addicionals (entre 0 i 4, sense repetir) i
     * els retorna en un ArrayList de String.
     */
    public static ArrayList<String> seleccionarServeis() {
        ArrayList<String> serveis = new ArrayList<>();
        System.out.println("\nServeis adicionals");
        System.out.println("------------------");
        // mostrar els serveis
        int i = 1;
        for (String servei : preusServeis.keySet()) {
            float preu = preusServeis.get(servei);
            System.out.printf("   " + i + ". " + servei + " (%.0f €)\n", preu);
            i++;
        }
        System.out.println("   -------------");
        System.out.println("   0. Finalitzar");
        // oferir serveis
        String extras = "";
        do {
            System.out.print("\nVol afegir un servei? (s/n): ");
            extras = sc.nextLine();
            if (extras.toLowerCase().equals("s")) {
                // demanar servei
                int opcio = 0;
                String servei = "";
                do {
                    opcio = llegirEnter("\nSeleccione servei: ");
                    switch (opcio) {
                        case 1:
                            servei = SERVEI_PISCINA;
                            break;
                        case 2:
                            servei = SERVEI_GIMNAS;
                            break;
                        case 3:
                            servei = SERVEI_SPA;
                            break;
                        case 4:
                            servei = SERVEI_ESMORZAR;
                            break;
                        case 0:
                            System.out.println("Ha finalitzat la introducció de serveis");
                            return serveis;
                        default:
                            System.out.println("Error: Servei no vàlid!!!");
                            servei = null;
                    }
                    // si el servei és vàlid
                    if (servei != null) {
                        // afegir-lo sols si no es troba en la llista
                        if (serveis.contains(servei)) {
                            System.out.println("Ja ha afegit " + servei + "!!!");
                        } else {
                            serveis.add(servei);
                            System.out.println("Servei afegit: " + servei);
                        }
                    }
                } while (opcio != 0);
            } else if (extras.toLowerCase().equals("n")) {
                System.out.println("No ha seleccionat serveis adicionals");
            } else {
                System.out.println("Error: Entrada incorrecta!!!");
            }
        } while (!extras.toLowerCase().equals("n"));
        // retornar la llista
        return serveis;
    }

    /**
     * Calcula i retorna el cost total de la reserva, incloent l'habitació,
     * els serveis seleccionats i l'IVA.
     */
    public static float calcularPreuTotal(String tipusHabitacio, ArrayList<String> serveisSeleccionats) {
        System.out.println("\nDetall de facturació");
        System.out.println("---------------------------");
        // preu de l'habitació
        float preuHabitacio = preusHabitacions.get(tipusHabitacio);
        System.out.printf(" - Preu habitació: %.2f €\n", preuHabitacio);
        // preu dels serveis
        float preuServeis = 0;
        System.out.print(" - Serveis: ");
        if (!serveisSeleccionats.isEmpty()) {
            System.out.println();            
            for (String servei : serveisSeleccionats) {
                float preuServei = preusServeis.get(servei);
                System.out.printf("     > " + servei + ": %.2f €\n", preuServei);
                preuServeis += preuServei;
            }
        } else {
            System.out.printf("%.2f €\n", preuServeis);
        }
        // subtotal
        float subtotal = preuHabitacio + preuServeis;
        System.out.println("---------------------------");
        System.out.printf(" - Subtotal: %.2f €\n", subtotal);
        // IVA
        float preuIVA = IVA * subtotal;
        System.out.printf(" - IVA (%.0f", IVA * 100);
        System.out.print("%): ");
        System.out.printf("%.2f €\n", preuIVA);
        // total factura
        float total = subtotal + preuIVA;
        System.out.println("---------------------------");
        System.out.printf("            TOTAL: %.2f €\n", total);
        // retornar total
        return total;
    }

    /**
     * Genera i retorna un codi de reserva únic de tres xifres
     * (entre 100 i 999) que no estiga repetit.
     */
    public static int generarCodiReserva() {
        int codi = 0;
        // generar un codi fins que no estiga contigut en el hashmap de reserves
        do {
            codi = (int) (Math.random() * 900) + 100;
        } while (reserves.containsKey(codi));        
        // retornar el codi
        return codi;
    }

    /**
     * Permet alliberar una habitació utilitzant el codi de reserva
     * i actualitza la disponibilitat.
     */
    public static void alliberarHabitacio() {
        if (reserves.isEmpty()) {
            System.out.println("Actualment l'hotel no té reserves\n");
            return;
        }
        System.out.println("\n===== ALLIBERAR HABITACIÓ =====");
        int codi = 0;
        boolean correcte = false;
        do {
            // demanar el codi mentre no siga correcte
            codi = llegirEnter("\nIntrodueix el codi de reserva: ");
            if (codi >= 100 && codi <= 999 && reserves.containsKey(codi)) {
                // si el codi es correcte
                correcte = true;
                System.out.println("Reserva trobada!");
                // obtindre el tipus d'habitació
                ArrayList<String> dades = reserves.get(codi);
                String tipus = dades.get(0);
                // eliminar reserva
                reserves.remove(codi);
                // actualitzar disponibilitat
                disponibilitatHabitacions.replace(tipus, disponibilitatHabitacions.get(tipus)+1);
                // informar de les actuacions realitzades
                System.out.println("   > Habitació alliberada correctament");
                System.out.println("   > Disponibilitat actualitzada\n");
            } else { 
                System.out.println("Error: Codi de reserva incorrecte!!!\n");
                return;
            }
        } while (!correcte);
    }

    /**
     * Mostra la disponibilitat actual de les habitacions (lliures i ocupades).
     */
    public static void consultarDisponibilitat() {
        System.out.println("\n===== DISPONIBILITAT D'HABITACIONS =====\n");
        System.out.println("Tipus\t\tLliures\tOcupades");
        System.out.println("-----\t\t-------\t--------");
        mostrarDisponibilitatTipus(TIPUS_ESTANDARD);
        mostrarDisponibilitatTipus(TIPUS_SUITE);
        mostrarDisponibilitatTipus(TIPUS_DELUXE);
        System.out.println();
    }

    /**
     * Mostra totes les reserves existents per a un tipus d'habitació
     * específic.
     */
    public static void obtindreReservaPerTipus() {
        if (reserves.isEmpty()) {
            System.out.println("Actualment l'hotel no té reserves\n");
            return;
        }
        System.out.println("\n===== CONSULTAR RESERVES PER TIPUS =====");
        System.out.println("\nTipus d'habitació");
        System.out.println("-----------------");
        System.out.println("   1. " + TIPUS_ESTANDARD);
        System.out.println("   2. " + TIPUS_SUITE);
        System.out.println("   3. " + TIPUS_DELUXE);
        // demanar tipus d'habitació
        String tipus = "";
        do {
            tipus = seleccionarTipusHabitacio();
        } while (tipus == null);        
        // obtindre el número d'habitacions ocupades del tipus seleccionat
        int ocupades = capacitatInicial.get(tipus) - disponibilitatHabitacions.get(tipus);
        // si hi ha habitacions ocupades d'eixe tipus
        if (ocupades != 0) {
            // obtindre l'array amb tots el codis de reserva
            // la grandària serà la mateixa que la del hashmap de reserves
            int[] codis = new int[reserves.size()];            
            int index = 0;
            // per a cada codi del hashmap de reserves
            for (int codi : reserves.keySet()) {
                // guardar el codi
                codis[index] = codi;
                index++;
            }
            // llistar les reserves del tipus seleccionat
            llistarReservesPerTipus(codis, tipus);
        } else {
            // si no, informar de l'error i tornar al menú
            System.out.println("No tenim reserves del tipus " + tipus + "\n");
            return;
        }
    }

    /**
     * Funció recursiva. Mostra les dades de totes les reserves
     * associades a un tipus d'habitació.
     */
    public static void llistarReservesPerTipus(int[] codis, String tipus) {
        // cas base
        if (codis.length == 0) {
            return;
        }
        // cas general: obtindre el primer codi
        int codi = codis[0];
        // si el codi pertany a una habitació del tipus especificat
        if (reserves.get(codi).get(0).equals(tipus)) {
            // mostrar reserva
            mostrarDadesReserva(codi);
        }
        // crear un array de grandària una unitat menor que codis
        int[] newCodis = new int[codis.length - 1];
        // eliminar la primera posició de l'array de codis
        System.arraycopy(codis, 1, newCodis, 0, newCodis.length);
        // cridada recursiva
        llistarReservesPerTipus(newCodis, tipus);
    }

    /**
     * Permet consultar els detalls d'una reserva introduint el codi.
     */
    public static void obtindreReserva() {
        if (reserves.isEmpty()) {
            System.out.println("Actualment l'hotel no té reserves\n");
            return;
        }
        System.out.println("\n===== CONSULTAR RESERVA =====");
        int codi = 0;
        boolean correcte = false;
        do {
            // demanar el codi mentre no siga correcte
            codi = llegirEnter("\nIntrodueix el codi de reserva: ");
            if (codi >= 100 && codi <= 999 && reserves.containsKey(codi)) {
                correcte = true;
                mostrarDadesReserva(codi);
            } else {
                System.out.println("Error: Codi de reserva incorrecte!!!\n");
                return;
            }
        } while (!correcte); 
    }

    /**
     * Consulta i mostra en detall la informació d'una reserva.
     */
    public static void mostrarDadesReserva(int codi) {
        // obtindre l'arraylist amb les dades de la reserva
        ArrayList<String> dades = reserves.get(codi);
        // obtindre tipus d'habitació (posició 0 de l'arraylist)
        String tipus = dades.get(0);
        // obtindre el cost (posició 1 de l'arraylist)
        String cost = dades.get(1);
        // si s'han contractat, obtindre serveis adicionals (entre les posicions 2 i final)
        ArrayList<String> serveis = new ArrayList<>();
        if (dades.size() > 2) {            
            for (int i = 2; i <= dades.size()-1; i++) {
                serveis.add(dades.get(i));
            }
        }
        // mostrar informació
        System.out.println("\nDades de la reserva");
        System.out.println("-------------------");
        System.out.println("Codi: " + codi);
        System.out.println("Tipus d'habitació: " + tipus);
        System.out.println("Serveis adicionals: ");
        if (!serveis.isEmpty()) {
            for (String servei : serveis) {
                System.out.println("   > " + servei);
            }
        } else {
            System.out.println("   > No s'han contractat serveis");
        }
        System.out.println("Cost total: " + cost + " €\n");
    }

    // --------- MÈTODES AUXILIARS (PER MILLORAR LEGIBILITAT) ---------

    /**
     * Llig un enter per teclat mostrant un missatge i gestiona possibles
     * errors d'entrada.
     */
    static int llegirEnter(String missatge) {
        int valor = 0;
        boolean correcte = false;
        while (!correcte) {
                System.out.print(missatge);
                if (sc.hasNextInt()) {
                    valor = sc.nextInt();
                    correcte = true;
                } else {
                    System.out.println("Error: Entrada incorrecta!!!");
                }
                sc.nextLine();               
        }
        return valor;
    }

    /**
     * Mostra per pantalla informació d'un tipus d'habitació: preu i
     * habitacions disponibles.
     */
    static void mostrarInfoTipus(String tipus) {
        int disponibles = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        float preu = preusHabitacions.get(tipus);
        System.out.println("- " + tipus + " (" + disponibles + " disponibles de " + capacitat + ") - " + preu + "€");
    }

    /**
     * Mostra la disponibilitat (lliures i ocupades) d'un tipus d'habitació.
     */
    static void mostrarDisponibilitatTipus(String tipus) {
        int lliures = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        int ocupades = capacitat - lliures;

        String etiqueta = tipus;
        if (etiqueta.length() < 8) {
            etiqueta = etiqueta + "\t"; // per a quadrar la taula
        }

        System.out.println(etiqueta + "\t" + lliures + "\t" + ocupades);
    }
}
