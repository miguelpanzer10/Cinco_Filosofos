package ejercicioFilosofos.reto2;

// Filósofo SIN control de concurrencia en el contador compartido
public class Filosofo implements Runnable {

    private final int id;
    private final Tenedor tenedorIzquierdo;
    private final Tenedor tenedorDerecho;
    private final int tiempoPensando;
    private final int tiempoComiendo;

    // Contador individual: cuántas veces ha comido ESTE filósofo (no compartido)
    private int vecesComido = 0;

    // Contador compartido entre TODOS los filósofos, se pasa como array para poder modificarlo
    // Un int[] de un elemento es el truco para pasar un entero por referencia en Java
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
        System.out.println("Filósofo " + id + " está pensando");
        Thread.sleep((long) (Math.random() * tiempoPensando));
    }

    private void cogerTenedores() {
        tenedorIzquierdo.coger(id);
        tenedorDerecho.coger(id);
    }

    private void comer() throws InterruptedException {
        vecesComido++;

        // RACE CONDITION AQUÍ: totalComidas[0]++ son 3 pasos (leer, sumar, escribir)
        // Si dos hilos ejecutan esto a la vez, uno sobreescribe al otro → se pierde una comida
        totalComidas[0]++;

        System.out.println("Filósofo " + id + " está comiendo (vez nº " + vecesComido + ")");
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
