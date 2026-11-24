package ejercicioFilosofos.reto5.solucionA;

// Cada filósofo es un hilo independiente que alterna entre pensar y comer
// Este Filosofo es idéntico al del Reto 4 — la solución asimétrica está en el Main,
// que para el último filósofo pasa los tenedores en orden invertido
public class Filosofo implements Runnable {

    private final int id;
    private final Tenedor tenedorIzquierdo;
    private final Tenedor tenedorDerecho;

    private final int tiempoPensando;
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
                cogerTenedores();
                comer();
                soltarTenedores();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Filósofo " + id + " termina. Comió " + vecesComido + " veces.");
    }

    private void pensar() throws InterruptedException {
        if (tiempoPensando > 0) {
            Thread.sleep((long) (Math.random() * tiempoPensando));
        }
    }

    // Siempre coge primero el "izquierdo" y luego el "derecho"
    // Para el filósofo asimétrico, el Main le habrá pasado los tenedores al revés,
    // así que sin saberlo él coge en orden contrario al resto
    private void cogerTenedores() throws InterruptedException {
        tenedorIzquierdo.coger(id);
        tenedorDerecho.coger(id);
    }

    private void comer() throws InterruptedException {
        vecesComido++;
        System.out.println("Filósofo " + id + " está comiendo (vez nº " + vecesComido + ")");
        if (tiempoComiendo > 0) {
            Thread.sleep((long) (Math.random() * tiempoComiendo));
        }
    }

    private void soltarTenedores() {
        tenedorIzquierdo.soltar(id);
        tenedorDerecho.soltar(id);
    }

    public int getId()          { return id; }
    public int getVecesComido() { return vecesComido; }
}
