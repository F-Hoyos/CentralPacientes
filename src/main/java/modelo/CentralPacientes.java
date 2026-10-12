
package modelo;

public class CentralPacientes {

    private Paciente primero;
    private Paciente ultimo;
    private int numPacientes;

    public CentralPacientes() {
        primero = null;
        ultimo = null;
        numPacientes = 0;
    }

    public Paciente getPrimero() {
        return primero;
    }

    public Paciente getUltimo() {
        return ultimo;
    }

    public int getNumPacientes() {
        return numPacientes;
    }

    public void agregarPacienteAlFinal(Paciente nuevo) {
        if (nuevo == null) {
            return;
        }

        nuevo.setSiguiente(null);

        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }

        numPacientes++;
    }

    public void agregarPacienteAlComienzo(Paciente nuevo) {
        if (nuevo == null) {
            return;
        }

        nuevo.setSiguiente(primero);
        primero = nuevo;

        if (ultimo == null) {
            ultimo = nuevo;
        }

        numPacientes++;
    }

    public boolean agregarPacienteDespuesDe(int codigo, Paciente nuevo) {
        if (nuevo == null) {
            return false;
        }

        Paciente actual = localizar(codigo);

        if (actual == null) {
            return false;
        }

        nuevo.setSiguiente(actual.getSiguiente());
        actual.setSiguiente(nuevo);

        if (actual == ultimo) {
            ultimo = nuevo;
        }

        numPacientes++;
        return true;
    }

    public Paciente localizar(int codigo) {
        Paciente actual = primero;

        while (actual != null) {
            if (actual.getCodigo() == codigo) {
                return actual;
            }

            actual = actual.getSiguiente();
        }

        return null;
    }

    public boolean eliminarPaciente(int codigo) {
        if (primero == null) {
            return false;
        }

        if (primero.getCodigo() == codigo) {
            primero = primero.getSiguiente();
            numPacientes--;

            if (primero == null) {
                ultimo = null;
            }

            return true;
        }

        Paciente anterior = primero;
        Paciente actual = primero.getSiguiente();

        while (actual != null) {
            if (actual.getCodigo() == codigo) {
                anterior.setSiguiente(actual.getSiguiente());

                if (actual == ultimo) {
                    ultimo = anterior;
                }

                actual.setSiguiente(null);
                numPacientes--;
                return true;
            }

            anterior = actual;
            actual = actual.getSiguiente();
        }

        return false;
    }

    public void listar() {
        Paciente actual = primero;

        while (actual != null) {
            System.out.println(
                actual.getCodigo() + " | "
                + actual.getNombre() + " | "
                + actual.getClinica() + " | "
                + actual.getInformacionMedica() + " | "
                + actual.getSexo()
            );

            actual = actual.getSiguiente();
        }
    }
}
