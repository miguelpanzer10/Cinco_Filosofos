package ejercicioFilosofos.reto3;

// Filósofo corregido: el contador compartido ahora está protegido con synchronized
public class Filosofo implements Runnable {

    private final int id;
    private final Tenedor tenedorIzquierdo;
    private final Tenedor tenedorDerecho;
    private final int tiempoPensando;
    private final int tiempoComiendo;

    // Contador individual: solo lo toca este filósofo, no necesita sincronización
    private int vecesComido = 0;

    // Contador compartido entre todos los filósofos
    // int[] para pasar por referencia
    // Ahora lo protegeremos con synchronized al incrementar
    private final int[] totalComidas;

    public Filosofo(int id, Tenedor izquierdo, Tenedor derecho,
                    int tiempoPensando, int tiempoComiendo, int[] totalComidas) {
        this.id = id;
        this.tenedorIzquierdo = izquierdo;
        this.tenedorDerecho = derecho;
        this.tiempoPensando = tiempoPensando;
        this.tiempoComiendo = tiempoComiendo;
        this.totalComidas = totalComidas;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                pensar();
                cogerTenedores();
                comer();
                soltarTenedores();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(">>> Filósofo " + id + " termina. Comió " + vecesComido + " veces.");
    }

    private void pensar() throws InterruptedException {
        System.out.println("Filósofo " + id + " está pensando  [estado: " + Thread.currentThread().getState() + "]");
        Thread.sleep((long) (Math.random() * tiempoPensando));
    }

    private void cogerTenedores() throws InterruptedException {
        tenedorIzquierdo.coger(id);
        tenedorDerecho.coger(id);
    }

    private void comer() throws InterruptedException {
        vecesComido++;

        // synchronized sobre el array compartido → solo un hilo a la vez puede incrementarlo
        // Esto evita la race condition que teníamos en el reto 2
        synchronized (totalComidas) {
            totalComidas[0]++;
        }

        System.out.println("Filósofo " + id + " está comiendo (vez nº " + vecesComido + ")  [estado: " + Thread.currentThread().getState() + "]");
        Thread.sleep((long) (Math.random() * tiempoComiendo));
    }

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
