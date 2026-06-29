package co.finanz.model;

import co.finanz.observer.ObservadorPresupuesto;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Límite de gasto mensual asignado a una categoría.
 *
 * <p>Aplica el patrón <b>Observer</b>: al registrar un gasto que supera el
 * límite, notifica automáticamente a todos los observadores registrados
 * (ej: la UI del controller de presupuestos).</p>
 */
public class Presupuesto {

    private final String id;
    private CategoriaGasto categoria;
    private double limiteMonto;
    private YearMonth mes;
    private double gastoActual;
    private final List<ObservadorPresupuesto> observadores;

    /**
     * Crea un presupuesto mensual para una categoría de gasto.
     *
     * @param categoria   categoría de gasto a presupuestar
     * @param limiteMonto monto máximo permitido para el mes (mayor que cero)
     * @param mes         mes y año al que aplica este presupuesto
     * @throws IllegalArgumentException si el límite es negativo o cero
     */
    public Presupuesto(CategoriaGasto categoria, double limiteMonto, YearMonth mes) {
        if (limiteMonto <= 0) {
            throw new IllegalArgumentException(
                    "El límite del presupuesto debe ser mayor que cero. Recibido: " + limiteMonto);
        }
        this.id = UUID.randomUUID().toString();
        this.categoria = categoria;
        this.limiteMonto = limiteMonto;
        this.mes = mes;
        this.gastoActual = 0.0;
        this.observadores = new ArrayList<>();
    }

    /**
     * Registra un gasto en este presupuesto y notifica a los observadores
     * si el acumulado supera el límite.
     *
     * @param monto monto del gasto a registrar
     */
    public void registrarGasto(double monto) {
        if (monto <= 0) return;
        gastoActual += monto;
        if (isExcedido()) {
            notificarObservadores();
        }
    }

    /**
     * Indica si el gasto acumulado superó el límite establecido.
     *
     * @return {@code true} si el presupuesto fue excedido
     */
    public boolean isExcedido() {
        return gastoActual > limiteMonto;
    }

    /**
     * Retorna qué porcentaje del presupuesto ya fue consumido.
     * Puede superar 1.0 si el presupuesto está excedido.
     *
     * @return fracción consumida (ej: 0.75 = 75%)
     */
    public double getPorcentajeConsumido() {
        return gastoActual / limiteMonto;
    }

    /**
     * Retorna cuánto dinero queda disponible. Negativo si fue excedido.
     *
     * @return {@code limiteMonto - gastoActual}
     */
    public double getMontoRestante() {
        return limiteMonto - gastoActual;
    }

    /**
     * Registra un observador que será notificado cuando el presupuesto se exceda.
     *
     * @param observador observador a agregar
     */
    public void agregarObservador(ObservadorPresupuesto observador) {
        observadores.add(observador);
    }

    /**
     * Elimina un observador previamente registrado.
     *
     * @param observador observador a eliminar
     */
    public void eliminarObservador(ObservadorPresupuesto observador) {
        observadores.remove(observador);
    }

    private void notificarObservadores() {
        for (ObservadorPresupuesto obs : observadores) {
            obs.onPresupuestoExcedido(this);
        }
    }

    // ── Getters y setters ─────────────────────────────────────────────────────

    public String getId() { return id; }

    public CategoriaGasto getCategoria() { return categoria; }
    public void setCategoria(CategoriaGasto categoria) { this.categoria = categoria; }

    public double getLimiteMonto() { return limiteMonto; }
    public void setLimiteMonto(double limiteMonto) {
        if (limiteMonto <= 0) throw new IllegalArgumentException("El límite debe ser mayor que cero.");
        this.limiteMonto = limiteMonto;
    }

    public YearMonth getMes() { return mes; }
    public void setMes(YearMonth mes) { this.mes = mes; }

    public double getGastoActual() { return gastoActual; }

    @Override
    public String toString() {
        return String.format("Presupuesto [%s] %s — $%,.2f / $%,.2f (%.0f%%)",
                mes, categoria.getNombre(),
                gastoActual, limiteMonto,
                getPorcentajeConsumido() * 100);
    }
}
