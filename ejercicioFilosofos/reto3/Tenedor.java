package ejercicioFilosofos.reto3;

// Tenedor corregido: vuelve a tener synchronized + wait/notify
// Soluciona el problema del reto 2 donde varios filósofos podían coger el mismo tenedor
public class Tenedor {

    private final int id;
    private boolean enUso;

    public Tenedor(int id) {
        this.id = id;
        this.enUso = false;
    }

    // synchronized garantiza que solo un filósofo entra a la vez
    // Si está en uso, wait() libera el monitor del tenedor y espera a que lo suelten
    public synchronized void coger(int idFilosofo) throws InterruptedException {
        while (enUso) {
            // wait() libera el monitor de ESTE TENEDOR temporalmente y deja al hilo en estado WAITING
            // hasta que alguien haga notify() sobre este mismo tenedor (en soltar())
            wait();
        }
        enUso = true;
        System.out.println("  [Tenedor " + id + "] cogido por Filósofo " + idFilosofo);
    }

    // synchronized para proteger el cambio de estado y el notify
    public synchronized void soltar(int idFilosofo) {
        enUso = false;
        System.out.println("  [Tenedor " + id + "] soltado por Filósofo " + idFilosofo);
        // notify() despierta a UN hilo que esté en wait() sobre este tenedor
        notify();
    }

    public int getId() {
        return id;
    }
}
