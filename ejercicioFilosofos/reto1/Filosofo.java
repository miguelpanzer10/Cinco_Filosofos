package ejercicioFilosofos.reto1;

// Cada filósofo es un hilo independiente que alterna entre pensar y comer
public class Filosofo implements Runnable {

    private final int id;
    private final Tenedor tenedorIzquierdo;
    private final Tenedor tenedorDerecho;

    // Tiempos configurables desde el Main
    private final int tiempoPensando; // ms máximos pensando
    private final int tiempoComiendo; // ms máximos comiendo

    // Contador de cuántas veces ha comido este filósofo
    private int vecesComido = 0;

    public Filosofo(int id, Tenedor tenedorIzquierdo, Tenedor tenedorDerecho,
                    int tiempoPensando, int tiempoComiendo) {
        this.id = id;
        this.tenedorIzquierdo = tenedorIzquierdo;
        this.tenedorDerecho = tenedorDerecho;
        this.tiempoPensando = tiempoPensando;
        this.tiempoComiendo = tiempoComiendo;
    }

    @Override
    public void run() {
        // El filósofo repite el ciclo hasta que el hilo sea interrumpido 
        // (lo hace el Main al acabar la simulación)
        while (!Thread.currentThread().isInterrupted()) {
            try {
                pensar();
                cogerTenedores();
                comer();
                soltarTenedores();
            } catch (InterruptedException e) {
                // Cuando el Main interrumpe el hilo, salimos del bucle limpiamente
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Filósofo " + id + " termina la simulación. Comió " + vecesComido + " veces.");
    }

    // El filósofo piensa un tiempo aleatorio
    private void pensar() throws InterruptedException {
        System.out.println("Filósofo " + id + " está pensando  [estado del hilo: " + Thread.currentThread().getState() + "]");
        long tiempo = (long) (Math.random() * tiempoPensando);
        Thread.sleep(tiempo);
    }

    // Intenta coger primero el izquierdo y luego el derecho
    // Sin synchronized, puede haber colisiones, pero en el reto 1 solo nos importa ver la simulación
    private void cogerTenedores() throws InterruptedException {
        System.out.println("Filósofo " + id + " intenta coger tenedores...");
        tenedorIzquierdo.coger(id);
        tenedorDerecho.coger(id);
    }

    // Come un tiempo aleatorio con ambos tenedores en mano
    private void comer() throws InterruptedException {
        vecesComido++;
        System.out.println("Filósofo " + id + " está comiendo  (vez nº " + vecesComido + ") [estado del hilo: " + Thread.currentThread().getState() + "]");
        long tiempo = (long) (Math.random() * tiempoComiendo);
        Thread.sleep(tiempo);
    }

    // Suelta los dos tenedores para que sus vecinos puedan usarlos
    private void soltarTenedores() {
        tenedorIzquierdo.soltar(id);
        tenedorDerecho.soltar(id);
    }

    public int getId() {
        return id;
    }

    public int getVecesComido() {
        return vecesComido;
    }
}
