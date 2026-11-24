package ejercicioFilosofos.reto5.solucionB;

// Cada filósofo es un hilo independiente que alterna entre pensar y comer
// Antes de intentar coger tenedores, pide permiso al ControlAcceso
// Si ya hay N-1 filósofos intentando comer, espera su turno
public class Filosofo implements Runnable {

    private final int id;
    private final Tenedor tenedorIzquierdo;
    private final Tenedor tenedorDerecho;
    private final ControlAcceso control; // límite compartido entre todos los filósofos

    private final int tiempoPensando;
    private final int tiempoComiendo;

    private int vecesComido = 0;

    public Filosofo(int id, Tenedor tenedorIzquierdo, Tenedor tenedorDerecho,
                    ControlAcceso control, int tiempoPensando, int tiempoComiendo) {
        this.id = id;
        this.tenedorIzquierdo = tenedorIzquierdo;
        this.tenedorDerecho = tenedorDerecho;
        this.control = control;
        this.tiempoPensando = tiempoPensando;
        this.tiempoComiendo = tiempoComiendo;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                pensar();
                control.entrar();       // pide permiso: si hay N-1 dentro, espera aquí
                cogerTenedores();
                comer();
                soltarTenedores();
                control.salir();        // libera su plaza y despierta a uno que esperaba
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

    // Todos cogen izquierdo primero, igual que en el Reto 4
    // El deadlock no puede formarse porque el ControlAcceso garantiza
    // que al menos un filósofo siempre puede conseguir los dos tenedores
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
