package co.finanz.model;

import co.finanz.strategy.TipoContrato;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Contrato laboral de un empleado.
 *
 * <p>Aplica el patrón <b>Strategy</b>: {@link TipoContrato} encapsula las reglas
 * de cálculo salarial. Contrato simplemente delega en él, por lo que cambiar
 * el tipo de contrato cambia automáticamente el comportamiento del cálculo.</p>
 */
public class Contrato {

    private final String id;
    private LocalDate fechaInicio;
    private double salarioBase;
    private TipoContrato tipoContrato;

    /**
     * Crea un nuevo contrato.
     *
     * @param salarioBase  salario bruto acordado (debe ser mayor que cero)
     * @param fechaInicio  fecha de inicio de la relación laboral
     * @param tipoContrato tipo de contrato que define las reglas salariales
     * @throws IllegalArgumentException si el salario base es negativo o cero
     */
    public Contrato(double salarioBase, LocalDate fechaInicio, TipoContrato tipoContrato) {
        if (salarioBase <= 0) {
            throw new IllegalArgumentException(
                    "El salario base debe ser mayor que cero. Recibido: " + salarioBase);
        }
        this.id = UUID.randomUUID().toString();
        this.salarioBase = salarioBase;
        this.fechaInicio = fechaInicio;
        this.tipoContrato = tipoContrato;
    }

    /**
     * Calcula el salario neto delegando en el {@link TipoContrato} (Strategy).
     *
     * @return salario neto después de todas las deducciones
     */
    public double calcularSalarioNeto() {
        return tipoContrato.calcularSalarioNeto(salarioBase);
    }

    /**
     * Calcula el total de deducciones delegando en el {@link TipoContrato} (Strategy).
     *
     * @return monto total descontado
     */
    public double calcularDeducciones() {
        return tipoContrato.calcularDeducciones(salarioBase);
    }

    // ── Getters y setters ─────────────────────────────────────────────────────

    public String getId() { return id; }

    public double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(double salarioBase) {
        if (salarioBase <= 0) throw new IllegalArgumentException("El salario base debe ser mayor que cero.");
        this.salarioBase = salarioBase;
    }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public TipoContrato getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(TipoContrato tipoContrato) { this.tipoContrato = tipoContrato; }
}
