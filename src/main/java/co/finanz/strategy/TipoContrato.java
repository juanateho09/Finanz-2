package co.finanz.strategy;

import java.util.UUID;

/**
 * Implementación configurable del patrón Strategy para cálculo salarial.
 * En lugar de tener clases fijas por cada tipo de contrato, {@code TipoContrato}
 * encapsula las reglas de deducciones como datos: el usuario puede crear nuevos
 * tipos en tiempo de ejecución sin escribir código nuevo.
 *
 * <p>La empresa incluye tres tipos por defecto ({@link #terminoFijo()},
 * {@link #terminoIndefinido()}, {@link #prestacionServicios()}), pero
 * {@code Empresa.crearTipoContrato()} permite agregar más.</p>
 */
public class TipoContrato implements EstrategiaSalario {

    private final String id;
    private String nombre;
    private String descripcion;
    private double porcentajeSalud;
    private double porcentajePension;
    private double porcentajeRetencion;
    private double porcentajeAdicional;

    /**
     * Crea un tipo de contrato con los porcentajes de deducción indicados.
     *
     * @param nombre              nombre descriptivo (ej: "Pasantía Remunerada")
     * @param descripcion         descripción breve del tipo
     * @param porcentajeSalud     porcentaje para salud (ej: 0.04 = 4%)
     * @param porcentajePension   porcentaje para pensión (ej: 0.04 = 4%)
     * @param porcentajeRetencion porcentaje de retención en la fuente (ej: 0.00)
     * @param porcentajeAdicional descuento adicional libre (libranza, fondo, etc.)
     */
    public TipoContrato(String nombre, String descripcion,
                        double porcentajeSalud, double porcentajePension,
                        double porcentajeRetencion, double porcentajeAdicional) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.porcentajeSalud = porcentajeSalud;
        this.porcentajePension = porcentajePension;
        this.porcentajeRetencion = porcentajeRetencion;
        this.porcentajeAdicional = porcentajeAdicional;
    }

    @Override
    public double calcularDeducciones(double salarioBase) {
        return salarioBase * (porcentajeSalud + porcentajePension
                + porcentajeRetencion + porcentajeAdicional);
    }

    @Override
    public double calcularSalarioNeto(double salarioBase) {
        return salarioBase - calcularDeducciones(salarioBase);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    // ── Fábricas de tipos por defecto ─────────────────────────────────────────

    /**
     * Crea el tipo "Término Fijo": descuenta 4% salud + 4% pensión.
     *
     * @return instancia preconfigurada
     */
    public static TipoContrato terminoFijo() {
        return new TipoContrato(
                "Término Fijo",
                "Contrato con fecha de finalización pactada. Aplica descuentos de salud y pensión.",
                0.04, 0.04, 0.00, 0.00
        );
    }

    /**
     * Crea el tipo "Término Indefinido": descuenta 4% salud + 4% pensión.
     *
     * @return instancia preconfigurada
     */
    public static TipoContrato terminoIndefinido() {
        return new TipoContrato(
                "Término Indefinido",
                "Contrato sin fecha de finalización. Incluye todas las prestaciones sociales.",
                0.04, 0.04, 0.00, 0.00
        );
    }

    /**
     * Crea el tipo "Prestación de Servicios": sin deducciones de nómina
     * (el contratista asume su propia seguridad social).
     *
     * @return instancia preconfigurada
     */
    public static TipoContrato prestacionServicios() {
        return new TipoContrato(
                "Prestación de Servicios",
                "Contrato de honorarios. Sin prestaciones sociales; el contratista asume su seguridad social.",
                0.00, 0.00, 0.00, 0.00
        );
    }

    // ── Getters y setters ─────────────────────────────────────────────────────

    public String getId() { return id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPorcentajeSalud() { return porcentajeSalud; }
    public void setPorcentajeSalud(double porcentajeSalud) { this.porcentajeSalud = porcentajeSalud; }

    public double getPorcentajePension() { return porcentajePension; }
    public void setPorcentajePension(double porcentajePension) { this.porcentajePension = porcentajePension; }

    public double getPorcentajeRetencion() { return porcentajeRetencion; }
    public void setPorcentajeRetencion(double porcentajeRetencion) { this.porcentajeRetencion = porcentajeRetencion; }

    public double getPorcentajeAdicional() { return porcentajeAdicional; }
    public void setPorcentajeAdicional(double porcentajeAdicional) { this.porcentajeAdicional = porcentajeAdicional; }

    @Override
    public String toString() {
        return nombre;
    }
}
