package co.finanz.model;

/**
 * Categoría que clasifica gastos o egresos de la empresa.
 * Solo es compatible con transacciones de tipo {@link Gasto}.
 */
public class CategoriaGasto extends Categoria {

    /**
     * Crea una categoría de gasto.
     *
     * @param nombre      nombre de la categoría (ej: "Arriendo", "Servicios públicos")
     * @param descripcion descripción de la categoría
     */
    public CategoriaGasto(String nombre, String descripcion) {
        super(nombre, descripcion);
    }

    @Override
    public String getTipo() {
        return "GASTO";
    }

    @Override
    public boolean esCompatible(Transaccion transaccion) {
        return transaccion instanceof Gasto;
    }
}
