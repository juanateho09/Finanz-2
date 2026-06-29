package co.finanz.exception;

/**
 * Se lanza cuando se busca una categoría por ID y no existe en el sistema.
 */
public class CategoriaNoEncontradaException extends RuntimeException {

    /**
     * @param id el ID que no produjo resultado
     */
    public CategoriaNoEncontradaException(String id) {
        super("No se encontró ninguna categoría con el ID: " + id);
    }
}
