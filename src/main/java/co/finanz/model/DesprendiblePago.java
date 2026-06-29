package co.finanz.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

/**
 * Registro inmutable de un pago de nómina realizado a un empleado.
 * Almacena el desglose salarial del periodo y puede convertirse en un
 * {@link Gasto} para afectar el flujo de caja de la empresa.
 */
public class DesprendiblePago {

    private final String id;
    private final String empleadoId;
    private final String empleadoNombre;
    private final YearMonth periodo;
    private final double salarioBruto;
    private final double deducciones;
    private final double salarioNeto;
    private final LocalDate fechaEmision;

    /**
     * Crea un desprendible de pago con los valores calculados por el {@link Contrato}.
     *
     * @param empleadoId     ID del empleado al que corresponde
     * @param empleadoNombre nombre completo del empleado (se guarda para el desprendible)
     * @param periodo        mes y año del periodo pagado (ej: 2026-06)
     * @param salarioBruto   salario antes de deducciones
     * @param deducciones    total de descuentos aplicados
     */
    public DesprendiblePago(String empleadoId, String empleadoNombre,
                            YearMonth periodo, double salarioBruto, double deducciones) {
        this.id = UUID.randomUUID().toString();
        this.empleadoId = empleadoId;
        this.empleadoNombre = empleadoNombre;
        this.periodo = periodo;
        this.salarioBruto = salarioBruto;
        this.deducciones = deducciones;
        this.salarioNeto = salarioBruto - deducciones;
        this.fechaEmision = LocalDate.now();
    }

    /**
     * Genera un {@link Gasto} de nómina a partir de este desprendible.
     * Así el pago queda registrado en el flujo de caja con la etiqueta {@code [NÓMINA]}.
     *
     * @param categoria categoría de gasto donde se registrará el egreso
     * @return un Gasto con el monto del salario neto
     */
    public Gasto convertirAGasto(CategoriaGasto categoria) {
        String descripcion = String.format("Nómina %s — %s", periodo, empleadoNombre);
        return new Gasto(salarioNeto, fechaEmision, descripcion, categoria, true);
    }

    // ── Getters (solo lectura — DesprendiblePago es inmutable) ───────────────

    public String getId() { return id; }
    public String getEmpleadoId() { return empleadoId; }
    public String getEmpleadoNombre() { return empleadoNombre; }
    public YearMonth getPeriodo() { return periodo; }
    public double getSalarioBruto() { return salarioBruto; }
    public double getDeducciones() { return deducciones; }
    public double getSalarioNeto() { return salarioNeto; }
    public LocalDate getFechaEmision() { return fechaEmision; }

    @Override
    public String toString() {
        return String.format("Desprendible %s — %s — Neto: $%,.2f",
                periodo, empleadoNombre, salarioNeto);
    }
}
