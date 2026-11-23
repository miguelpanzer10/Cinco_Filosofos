package ejercicioFilosofos.reto2;

// Tenedor SIN ningún control de concurrencia
// No hay synchronized, no hay wait, no hay notify
// Consecuencia: varios filósofos pueden coger el mismo tenedor a la vez
public class Tenedor {

    private final int id;
    private boolean enUso;

    public Tenedor(int id) {
        this.id = id;
        this.enUso = false;
    }

    // SIN synchronized → dos hilos pueden entrar a la vez, leer enUso=false los dos
    // y los dos ponerlo a true → ambos creen que lo han cogido
    public void coger(int idFilosofo) {
        while (enUso) {
            // espera activa sin ceder el monitor → no hay garantía de orden ni visibilidad
        }
        enUso = true;
        System.out.println("  [Tenedor " + id + "] cogido por Filósofo " + idFilosofo);
    }

    public void soltar(int idFilosofo) {
        enUso = false;
        System.out.println("  [Tenedor " + id + "] soltado por Filósofo " + idFilosofo);
    }

    public int getId() {
        return id;
    }
}
