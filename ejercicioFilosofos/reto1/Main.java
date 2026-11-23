package ejercicioFilosofos.reto1;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static final int numFilosofos = 5;
    static final int duracionTotal = 10000; // cuánto dura la simulación (10 segundos)
    static final int tiempoPensando = 1500;  // tiempo máximo pensando
    static final int tiempoComiendo = 1000;  // tiempo máximo comiendo
    static final int tiempoEntreImpresiones = 2000;  // cada cuánto se imprime el estado de los hilos

    public static void main(String[] args) throws InterruptedException {

        // Creamos los tenedores (recursos compartidos)
        // Tenedor i está entre el filósofo i y el filósofo (i+1)
        Tenedor[] tenedores = new Tenedor[numFilosofos];
        for (int i = 0; i < numFilosofos; i++) {
            tenedores[i] = new Tenedor(i);
        }

        // Creamos los filósofos y sus hilos
        // Filósofo i coge tenedor i (izquierdo) y tenedor (i+1)%N (derecho)
        //para que asi se cierre el circulo de tenedores y filosofos
        List<Filosofo> filosofos = new ArrayList<>();
        List<Thread> hilos = new ArrayList<>();

        for (int i = 0; i < numFilosofos; i++) {
            Tenedor izquierdo = tenedores[i];
            Tenedor derecho   = tenedores[(i + 1) % numFilosofos];
            Filosofo f = new Filosofo(i, izquierdo, derecho, tiempoPensando, tiempoComiendo);
            filosofos.add(f);
            hilos.add(new Thread(f, "Filósofo-" + i));
        }

        System.out.println("Simulación arrancando con " + numFilosofos + " filósofos\n");

        // Arrancamos todos los hilos de filósofos
        for (Thread hilo : hilos) {
            hilo.start();
        }

        // El main espera duracionTotal imprimiendo estados cada tiempoEntreImpresiones
        // System.currentTimeMillis() devuelve el tiempo actual en ms
        // tiempoFin = ahora + 10000ms → el bucle sigue mientras no hayamos llegado a ese momento
        long tiempoFin = System.currentTimeMillis() + duracionTotal;

        while (System.currentTimeMillis() < tiempoFin) {
            Thread.sleep(tiempoEntreImpresiones);
            System.out.println("\nEstados:");
            System.out.println("-----------------------------------");
            for (int i = 0; i < hilos.size(); i++) {
                // getState() devuelve el estado actual: RUNNABLE, WAITING, TIMED_WAITING, TERMINATED...
                System.out.println("  " + hilos.get(i).getName()
                        + ": " + hilos.get(i).getState()
                        + " (ha comido " + filosofos.get(i).getVecesComido() + " veces)");
            }
        }

        System.out.println("\nParando filósofos...");

        // Interrumpimos todos los hilos para que salgan del bucle en run()
        for (Thread hilo : hilos) {
            hilo.interrupt();
        }

        // join() hace que el main espere a que cada filósofo termine antes de imprimir el resumen
        for (Thread hilo : hilos) {
            hilo.join();
        }

        // Resumen final
        System.out.println("\nResumen:");
        System.out.println("-----------------------------------");
        int totalComidas = 0;
        for (Filosofo f : filosofos) {
            System.out.println("  Filósofo " + f.getId() + ": comió " + f.getVecesComido() + " veces");
            totalComidas += f.getVecesComido();
        }
        System.out.println("  Total: " + totalComidas + " comidas");
    }
}
