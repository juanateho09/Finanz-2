package co.finanz.exception;

import co.finanz.model.Presupuesto;

/**
 * Se lanza cuando un gasto supera el límite de un presupuesto mensual.
 */
public class PresupuestoExcedidoException extends RuntimeException {

    private final Presupuesto presupuesto;

    /**
     * @param presupuesto el presupuesto excedido (para acceder al detalle)
     */
    public PresupuestoExcedidoException(Presupuesto presupuesto) {
        super(String.format(
                "Presupuesto '%s' para %s excedido. Límite: $%,.2f | Actual: $%,.2f",
                presupuesto.getCategoria().getNombre(),
                presupuesto.getMes(),
                presupuesto.getLimiteMonto(),
                presupuesto.getGastoActual()));
        this.presupuesto = presupuesto;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }
}
