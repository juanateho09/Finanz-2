package co.finanz.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Representa un empleado de la empresa con su información básica,
 * contrato laboral e historial de desprendibles de pago.
 */
public class Empleado {

    private final String id;
    private String nombre;
    private String apellido;
    private String cedula;
    private String cargo;
    private Contrato contrato;
    private final List<DesprendiblePago> historialPagos;

    /**
     * Crea un nuevo empleado. El contrato se asigna posteriormente con
     * {@code Empresa.asignarContrato()}.
     *
     * @param nombre   primer nombre
     * @param apellido apellido
     * @param cedula   número de documento de identidad
     * @param cargo    puesto dentro de la empresa
     */
    public Empleado(String nombre, String apellido, String cedula, String cargo) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.cargo = cargo;
        this.historialPagos = new ArrayList<>();
    }

    /**
     * Retorna el nombre completo del empleado.
     *
     * @return nombre + apellido
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Indica si el empleado tiene un contrato asignado.
     *
     * @return {@code true} si tiene contrato
     */
    public boolean tieneContrato() {
        return contrato != null;
    }

    /**
     * Calcula el salario neto del empleado delegando en su {@link Contrato}.
     *
     * @return salario neto, o {@code 0.0} si no tiene contrato asignado
     */
    public double calcularSalarioNeto() {
        return tieneContrato() ? contrato.calcularSalarioNeto() : 0.0;
    }

    /**
     * Agrega un desprendible al historial de pagos de este empleado.
     *
     * @param desprendible desprendible generado para este empleado
     */
    public void agregarPago(DesprendiblePago desprendible) {
        historialPagos.add(desprendible);
    }

    // ── Getters y setters ─────────────────────────────────────────────────────

    public String getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }

    /** Retorna una vista inmutable del historial de pagos. */
    public List<DesprendiblePago> getHistorialPagos() {
        return Collections.unmodifiableList(historialPagos);
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " — " + cargo;
    }
}
