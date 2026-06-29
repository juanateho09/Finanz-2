package co.finanz.model;

import java.time.LocalDate;

/**
 * Representa un ingreso de dinero a la empresa.
 * Implementa los métodos abstractos de {@link Transaccion} para describirse
 * como un ingreso con su fuente de origen.
 */
public class Ingreso extends Transaccion {

    private String fuente;

    /**
     * Crea un nuevo ingreso.
     *
     * @param monto       valor del ingreso (mayor que cero)
     * @param fecha       fecha del ingreso
     * @param descripcion descripción del ingreso
     * @param categoria   categoría de ingreso a la que pertenece
     * @param fuente      origen del ingreso (ej: "Cliente XYZ", "Venta producto")
     */
    public Ingreso(double monto, LocalDate fecha, String descripcion,
                   CategoriaIngreso categoria, String fuente) {
        super(monto, fecha, descripcion, categoria);
        this.fuente = fuente;
    }

    @Override
    public String getTipo() {
        return "INGRESO";
    }

    @Override
    public String getResumen() {
        return String.format("[INGRESO] $%,.2f — %s (%s)",
                getMonto(), getDescripcion(), getCategoria().getNombre());
    }

    public String getFuente() { return fuente; }
    public void setFuente(String fuente) { this.fuente = fuente; }
}
