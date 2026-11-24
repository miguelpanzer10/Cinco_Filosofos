package ejercicioFilosofos.reto5.solucionA;

// El tenedor es el recurso compartido entre filósofos vecinos
// Idéntico al Reto 4: synchronized + wait/notify para acceso exclusivo
// La solución al deadlock no está aquí — está en el orden en que el Main asigna los tenedores
public class Tenedor {

    private final int id;
    private boolean enUso;

    public Tenedor(int id) {
        this.id = id;
        this.enUso = false;
    }

    public synchronized void coger(int idFilosofo) throws InterruptedException {
        while (enUso) {
            wait();
        }
        enUso = true;
        System.out.println("  [Tenedor " + id + "] cogido por Filósofo " + idFilosofo);
    }

    public synchronized void soltar(int idFilosofo) {
        enUso = false;
        notify();
        System.out.println("  [Tenedor " + id + "] soltado por Filósofo " + idFilosofo);
    }

    public int getId() {
        return id;
    }
}
