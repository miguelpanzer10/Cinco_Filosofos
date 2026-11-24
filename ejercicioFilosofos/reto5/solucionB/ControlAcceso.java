package ejercicioFilosofos.reto5.solucionB;

// Controla cuántos filósofos pueden intentar coger tenedores a la vez
// Se usa para limitar a N-1 filósofos simultáneos → garantiza que siempre
// haya al menos uno que pueda conseguir los dos tenedores sin deadlock
public class ControlAcceso {

    private final int maximo;   // número máximo de filósofos que pueden intentar comer a la vez
    private int dentro = 0;     // cuántos filósofos están intentando coger tenedores ahora mismo

    public ControlAcceso(int maximo) {
        this.maximo = maximo;
    }

    // El filósofo pide permiso para intentar coger tenedores
    // Si ya hay 'maximo' filósofos dentro, espera hasta que alguno salga
    public synchronized void entrar() throws InterruptedException {
        while (dentro >= maximo) {
            wait(); // espera a que algún filósofo termine de comer y llame a salir()
        }
        dentro++;
    }

    // El filósofo avisa de que ha terminado de comer y ha soltado sus tenedores
    // Despierta a uno que esté esperando permiso para entrar
    public synchronized void salir() {
        dentro--;
        notify();
    }
}
