package ejercicioFilosofos.reto4;

// El tenedor es el recurso compartido entre filósofos vecinos
// Idéntico al Reto 3: synchronized + wait/notify para acceso exclusivo
// El deadlock no viene de aquí, viene del ORDEN en que los filósofos cogen los tenedores
public class Tenedor {

    private final int id;
    private boolean enUso;

    public Tenedor(int id) {
        this.id = id;
        this.enUso = false;
    }

    // Solo un hilo puede entrar a la vez (synchronized)
    // Si está en uso, wait() suelta la llave y espera a que lo suelten
    public synchronized void coger(int idFilosofo) throws InterruptedException {
        while (enUso) {
            wait(); // suelta la llave y espera hasta que alguien llame a notify()
        }
        enUso = true;
        System.out.println("  [Tenedor " + id + "] cogido por Filósofo " + idFilosofo);
    }

    // Libera el tenedor y avisa al siguiente que está esperando
    public synchronized void soltar(int idFilosofo) {
        enUso = false;
        notify(); // despierta a un hilo que esté en wait() sobre este tenedor
        System.out.println("  [Tenedor " + id + "] soltado por Filósofo " + idFilosofo);
    }

    public int getId() {
        return id;
    }
}
