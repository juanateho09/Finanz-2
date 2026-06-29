package co.finanz.observer;

import co.finanz.model.Presupuesto;

/**
 * Interfaz del patrón Observer para el control de presupuestos.
 * Cualquier clase que implemente esta interfaz recibirá una notificación
 * automática cada vez que el gasto acumulado supere el límite de un presupuesto.
 *
 * <p>En la Fase 2, {@code PresupuestoController} implementará esta interfaz
 * para mostrar una alerta visual al usuario.</p>
 */
public interface ObservadorPresupuesto {

    /**
     * Llamado automáticamente por {@link Presupuesto} cuando el gasto acumulado
     * supera el límite establecido.
     *
     * @param presupuesto el presupuesto que fue excedido
     */
    void onPresupuestoExcedido(Presupuesto presupuesto);
}
