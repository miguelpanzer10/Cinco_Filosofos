package ejercicioFilosofos.reto4;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        // --- Configuración ---
        int numFilosofos        = 5;
        int tiempoPensando    = 0;    // 0 = sin pensar → todos se lanzan a coger tenedores a la vez
        int tiempoComiendo    = 50;   // corto para que los filósofos vuelvan rápido a por los tenedores
        int tiempoEspera      = 500;  // cada cuánto comprobamos si hay deadlock
        int tiempoMax         = 10000; // tiempo máximo antes de forzar la parada

        // --- Crear tenedores ---
        Tenedor[] tenedores = new Tenedor[numFilosofos];
        for (int i = 0; i < numFilosofos; i++) {
            tenedores[i] = new Tenedor(i);
        }

        // --- Crear filósofos e hilos ---
        List<Filosofo> filosofos = new ArrayList<>();
        List<Thread>   hilos     = new ArrayList<>();

        for (int i = 0; i < numFilosofos; i++) {
            Tenedor izquierdo = tenedores[i];
            Tenedor derecho   = tenedores[(i + 1) % numFilosofos];
            Filosofo f = new Filosofo(i, izquierdo, derecho, tiempoPensando, tiempoComiendo);
            filosofos.add(f);
            hilos.add(new Thread(f, "Filósofo-" + i));
        }

        System.out.println("Simulación arrancando con " + numFilosofos + " filósofos (deadlock esperado)");
        System.out.println("Todos cogen izquierdo primero, tiempoPensando = 0 → se lanzan a la vez\n");

        // --- Arrancar hilos ---
        for (Thread hilo : hilos) {
            hilo.start();
        }

        // --- Bucle de monitorización y detección de deadlock ---
        long tiempoFin       = System.currentTimeMillis() + tiempoMax;
        boolean deadlock     = false;

        while (System.currentTimeMillis() < tiempoFin && !deadlock) {
            Thread.sleep(tiempoEspera);

            // Imprimimos el estado de cada hilo
            System.out.println("\nEstados:");
            System.out.println("-----------------------------------");
            for (int i = 0; i < numFilosofos; i++) {
                Thread.State estado = hilos.get(i).getState();
                System.out.println("  Filósofo-" + i + ": " + estado + " (ha comido " + filosofos.get(i).getVecesComido() + " veces)");
            }

            // Comprobamos si todos los hilos están en WAITING
            // Si es así, nadie puede avanzar → deadlock
            boolean todosWaiting = true;
            for (Thread hilo : hilos) {
                if (hilo.getState() != Thread.State.WAITING) {
                    todosWaiting = false;
                    break;
                }
            }

            if (todosWaiting) {
                deadlock = true;
                System.out.println("\nDeadlock detectado: todos en WAITING");
                System.out.println("Cada filósofo tiene el izquierdo y espera el derecho,");
                System.out.println("que lo tiene su vecino. Nadie puede avanzar.");
            }
        }

        if (!deadlock) {
            System.out.println("\nTiempo máximo alcanzado sin detectar deadlock (raro, prueba a ejecutarlo de nuevo).");
        }

        // --- Parar todos los hilos ---
        // interrupt() saca a los hilos del wait() lanzando InterruptedException
        System.out.println("\nParando filósofos...");
        for (Thread hilo : hilos) {
            hilo.interrupt();
        }

        // Esperamos a que todos terminen antes de imprimir el resumen
        for (Thread hilo : hilos) {
            hilo.join();
        }

        // --- Resumen final ---
        System.out.println("\nResumen:");
        System.out.println("-----------------------------------");
        int total = 0;
        for (Filosofo f : filosofos) {
            System.out.println("  Filósofo " + f.getId() + ": comió " + f.getVecesComido() + " veces");
            total += f.getVecesComido();
        }
        System.out.println("  Total: " + total + " comidas (con deadlock suele ser 0 o muy bajo)");
    }
}
