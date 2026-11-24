package ejercicioFilosofos.reto4;

// Cada filósofo es un hilo independiente que alterna entre pensar y comer
// En el Reto 4 tiempoPensando = 0, así todos se lanzan a coger tenedores a la vez
// → todos cogen el izquierdo → todos esperan el derecho → DEADLOCK
public class Filosofo implements Runnable {

    private final int id;
    private final Tenedor tenedorIzquierdo;
    private final Tenedor tenedorDerecho;

    private final int tiempoPensando; // será 0 en este reto para forzar el deadlock
    private final int tiempoComiendo;

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
        while (!Thread.currentThread().isInterrupted()) {
            try {
                pensar();
                cogerTenedores(); // aquí es donde se producirá el deadlock
                comer();
                soltarTenedores();
            } catch (InterruptedException e) {
                // el Main nos ha interrumpido para parar (tras detectar el deadlock)
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Filósofo " + id + " termina. Comió " + vecesComido + " veces.");
    }

    // Con tiempoPensando = 0 no hay espera → todos pasan directamente a cogerTenedores
    private void pensar() throws InterruptedException {
        if (tiempoPensando > 0) {
            long tiempo = (long) (Math.random() * tiempoPensando);
            Thread.sleep(tiempo);
        }
    }

    // ORDEN: siempre izquierdo primero, luego derecho
    // Esto es exactamente la condición que provoca el deadlock:
    // si los 5 cogen el izquierdo a la vez, los 5 se quedan esperando el derecho
    private void cogerTenedores() throws InterruptedException {
        System.out.println("Filósofo " + id + " coge tenedor izquierdo (T" + tenedorIzquierdo.getId() + ")...");
        tenedorIzquierdo.coger(id);  // paso 1: coge el izquierdo

        System.out.println("Filósofo " + id + " coge tenedor derecho  (T" + tenedorDerecho.getId() + ")...");
        tenedorDerecho.coger(id);    // paso 2: intenta coger el derecho → puede quedarse aquí en wait()
    }

    private void comer() throws InterruptedException {
        vecesComido++;
        System.out.println("Filósofo " + id + " está comiendo (vez nº " + vecesComido + ")");
        if (tiempoComiendo > 0) {
            long tiempo = (long) (Math.random() * tiempoComiendo);
            Thread.sleep(tiempo);
        }
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
