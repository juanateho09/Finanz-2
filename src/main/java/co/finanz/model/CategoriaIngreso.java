package co.finanz.model;

/**
 * Categoría que clasifica ingresos de la empresa.
 * Solo es compatible con transacciones de tipo {@link Ingreso}.
 */
public class CategoriaIngreso extends Categoria {

    /**
     * Crea una categoría de ingreso.
     *
     * @param nombre      nombre de la categoría (ej: "Ventas", "Servicios prestados")
     * @param descripcion descripción de la categoría
     */
    public CategoriaIngreso(String nombre, String descripcion) {
        super(nombre, descripcion);
    }

    @Override
    public String getTipo() {
        return "INGRESO";
    }

    @Override
    public boolean esCompatible(Transaccion transaccion) {
        return transaccion instanceof Ingreso;
    }
}
