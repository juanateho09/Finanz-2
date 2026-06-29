package co.finanz.exception;

/**
 * Se lanza cuando se busca un empleado por ID y no existe en el sistema.
 */
public class EmpleadoNoEncontradoException extends RuntimeException {

    /**
     * @param id el ID que no produjo resultado
     */
    public EmpleadoNoEncontradoException(String id) {
        super("No se encontró ningún empleado con el ID: " + id);
    }
}
