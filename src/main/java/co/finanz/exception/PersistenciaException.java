package co.finanz.exception;

/**
 * Se lanza cuando ocurre un error al guardar o cargar el archivo JSON de datos.
 */
public class PersistenciaException extends RuntimeException {

    /**
     * @param mensaje descripción del error
     * @param causa   excepción original que lo provocó
     */
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
