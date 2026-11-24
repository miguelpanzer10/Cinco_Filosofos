package ejercicioFilosofos.reto5.solucionA;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        // --- Configuración ---
        int numFilosofos     = 5;
        int tiempoPensando = 0;    // sin pensar, igual que el Reto 4, para máxima contención
        int tiempoComiendo = 50;
        int tiempoEspera   = 500;
        int tiempoMax      = 30000; // más tiempo que en reto4 para que los filósofos coman más veces y el análisis de equidad sea fiable

        // --- Crear tenedores ---
        Tenedor[] tenedores = new Tenedor[numFilosofos];
        for (int i = 0; i < numFilosofos; i++) {
            tenedores[i] = new Tenedor(i);
        }

        // --- Crear filósofos e hilos ---
        List<Filosofo> filosofos = new ArrayList<>();
        List<Thread>   hilos     = new ArrayList<>();

        for (int i = 0; i < numFilosofos; i++) {
            Tenedor izquierdo;
            Tenedor derecho;

            if (i == numFilosofos - 1) {
                // SOLUCIÓN A — filósofo asimétrico:
                // El último filósofo recibe los tenedores al revés.
                // Desde su punto de vista sigue cogiendo "izquierdo primero",
                // pero en realidad coge T0 antes que T4 → rompe la espera circular.
                izquierdo = tenedores[(i + 1) % numFilosofos]; // T0
                derecho   = tenedores[i];                       // T4
            } else {
                izquierdo = tenedores[i];
                derecho   = tenedores[(i + 1) % numFilosofos];
            }

            Filosofo f = new Filosofo(i, izquierdo, derecho, tiempoPensando, tiempoComiendo);
            filosofos.add(f);
            hilos.add(new Thread(f, "Filósofo-" + i));
        }

        System.out.println("Solución A: filósofo asimétrico");
        System.out.println("F0-F3 cogen izquierdo primero, F4 al revés → sin deadlock\n");

        // --- Arrancar hilos ---
        for (Thread hilo : hilos) {
            hilo.start();
        }

        // --- Bucle de monitorización ---
        long tiempoFin = System.currentTimeMillis() + tiempoMax;

        while (System.currentTimeMillis() < tiempoFin) {
            Thread.sleep(tiempoEspera);

            System.out.println("\nEstados:");
            System.out.println("-----------------------------------");
            boolean todosWaiting = true;
            for (int i = 0; i < numFilosofos; i++) {
                Thread.State estado = hilos.get(i).getState();
                System.out.println("  Filósofo-" + i + ": " + estado + " (ha comido " + filosofos.get(i).getVecesComido() + " veces)");
                if (estado != Thread.State.WAITING) {
                    todosWaiting = false;
                }
            }

            if (todosWaiting) {
                System.out.println("  Aviso: todos en WAITING (inesperado con esta solución)");
            }
        }

        // --- Parar y esperar ---
        for (Thread hilo : hilos) hilo.interrupt();
        for (Thread hilo : hilos) hilo.join();

        // --- Resumen final ---
        System.out.println("\nParando filósofos...");

        System.out.println("\nResumen:");
        System.out.println("-----------------------------------");
        int total = 0;
        // arrancamos min y max con el valor del primero para tener un punto de partida real
        int min = filosofos.get(0).getVecesComido();
        int max = filosofos.get(0).getVecesComido();
        for (Filosofo f : filosofos) {
            System.out.println("  Filósofo " + f.getId() + ": comió " + f.getVecesComido() + " veces");
            total += f.getVecesComido();
            // si este filósofo comió menos que el mínimo que llevamos, actualizamos
            if (f.getVecesComido() < min) min = f.getVecesComido();
            // si este filósofo comió más que el máximo que llevamos, actualizamos
            if (f.getVecesComido() > max) max = f.getVecesComido();
        }
        System.out.println("  Total: " + total + " comidas — sin deadlock");
        System.out.println("\n¿Comieron todos por igual?");
        System.out.println("-----------------------------------");
        System.out.println("  Mínimo : " + min);                          // el que menos comió
        System.out.println("  Máximo : " + max);                          // el que más comió
        System.out.println("  Diferencia (max-min): " + (max - min));     // 0 = perfecto, cuanto mayor, más injusto
    }
}
