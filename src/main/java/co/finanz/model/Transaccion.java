package co.finanz.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Representa una operación financiera registrada en la empresa.
 *
 * <p>Clase abstracta que garantiza polimorfismo: {@link #getTipo()} y
 * {@link #getResumen()} son implementados de forma distinta por
 * {@link Ingreso} y {@link Gasto}, permitiendo tratar ambos de manera
 * uniforme a través de esta referencia.</p>
 */
public abstract class Transaccion {

    private final String id;
    private double monto;
    private LocalDate fecha;
    private String descripcion;
    private Categoria categoria;

    /**
     * Constructor base para todas las transacciones.
     *
     * @param monto       valor monetario (debe ser mayor que cero)
     * @param fecha       fecha en que ocurrió la transacción
     * @param descripcion descripción libre de la transacción
     * @param categoria   categoría que clasifica esta transacción
     * @throws IllegalArgumentException si el monto es negativo o cero
     */
    protected Transaccion(double monto, LocalDate fecha,
                          String descripcion, Categoria categoria) {
        if (monto <= 0) {
            throw new IllegalArgumentException(
                    "El monto debe ser mayor que cero. Recibido: " + monto);
        }
        this.id = UUID.randomUUID().toString();
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    /**
     * Retorna el tipo de transacción.
     * {@link Ingreso} devuelve {@code "INGRESO"}; {@link Gasto} devuelve {@code "GASTO"}.
     *
     * @return tipo de la transacción
     */
    public abstract String getTipo();

    /**
     * Retorna un texto descriptivo y resumido de esta transacción.
     * Cada subclase lo formatea según su naturaleza.
     *
     * @return resumen legible de la transacción
     */
    public abstract String getResumen();

    // ── Getters y setters ─────────────────────────────────────────────────────

    public String getId() { return id; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        this.monto = monto;
    }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return getResumen();
    }
}
