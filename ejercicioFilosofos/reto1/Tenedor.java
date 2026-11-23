package ejercicioFilosofos.reto1;

// El tenedor es el recurso compartido entre filósofos vecinos
// En el reto 1 NO hay ningún control de concurrencia → solo mostramos la estructura funcionando
public class Tenedor {

    private final int id;
    private boolean enUso;

    public Tenedor(int id) {
        this.id = id;
        this.enUso = false;
    }

    // Sin synchronized no hay garantía de acceso exclusivo
    // Si el tenedor está en uso, el hilo espera con sleep (espera activa)
    // Esto es suficiente para ver la simulación corriendo, pero no es seguro con concurrencia
    public void coger(int idFilosofo) throws InterruptedException {
        while (enUso) {
            Thread.sleep(10); // espera activa: comprueba cada 10ms si se ha liberado
        }
        enUso = true;
        System.out.println("  [Tenedor " + id + "] cogido por Filósofo " + idFilosofo);
    }

    // Sin synchronized → no hay protección al soltar
    public void soltar(int idFilosofo) {
        enUso = false;
        System.out.println("  [Tenedor " + id + "] soltado por Filósofo " + idFilosofo);
    }

    public int getId() {
        return id;
    }
}
