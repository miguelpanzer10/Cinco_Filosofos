package ejercicioFilosofos.reto3;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static final int numFilosofos = 5;
    static final int duracionTotal = 10000;
    static final int tiempoPensando = 200;  // mismos tiempos cortos que reto2 para comparar en igualdad de condiciones
    static final int tiempoComiendo = 100;
    static final int tiempoEntreImpresiones = 2000;

    public static void main(String[] args) throws InterruptedException {

        // Mismo contador compartido que en reto2, ahora protegido con synchronized en el filósofo
        int[] totalComidas = {0};

        Tenedor[] tenedores = new Tenedor[numFilosofos];
        for (int i = 0; i < numFilosofos; i++) {
            tenedores[i] = new Tenedor(i);
        }

        List<Filosofo> filosofos = new ArrayList<>();
        List<Thread> hilos = new ArrayList<>();

        for (int i = 0; i < numFilosofos; i++) {
            Tenedor izquierdo = tenedores[i];
            Tenedor derecho   = tenedores[(i + 1) % numFilosofos];
            Filosofo f = new Filosofo(i, izquierdo, derecho, tiempoPensando, tiempoComiendo, totalComidas);
            filosofos.add(f);
            hilos.add(new Thread(f, "Filósofo-" + i));
        }

        System.out.println("Simulación arrancando con " + numFilosofos + " filósofos (con sincronización)\n");

        for (Thread hilo : hilos) {
            hilo.start();
        }

        long tiempoFin = System.currentTimeMillis() + duracionTotal;
        while (System.currentTimeMillis() < tiempoFin) {
            Thread.sleep(tiempoEntreImpresiones);
            System.out.println("\nEstados:");
            System.out.println("-----------------------------------");
            for (int i = 0; i < hilos.size(); i++) {
                System.out.println("  " + hilos.get(i).getName()
                        + ": " + hilos.get(i).getState()
                        + " (ha comido " + filosofos.get(i).getVecesComido() + " veces)");
            }
        }

        for (Thread hilo : hilos) {
            hilo.interrupt();
        }
        for (Thread hilo : hilos) {
            hilo.join();
        }

        // Ahora los dos números deben coincidir → race condition corregida
        System.out.println("\nParando filósofos...");

        int sumaIndividual = 0;
        System.out.println("\nResumen:");
        System.out.println("-----------------------------------");
        for (Filosofo f : filosofos) {
            System.out.println("  Filósofo " + f.getId() + ": comió " + f.getVecesComido() + " veces");
            sumaIndividual += f.getVecesComido();
        }

        System.out.println("\n  Suma individual : " + sumaIndividual);
        System.out.println("  Contador compartido (con sync): " + totalComidas[0]);
        System.out.println("  Diferencia: " + (sumaIndividual - totalComidas[0]) + " (0 = race condition corregida)");
    }
}
