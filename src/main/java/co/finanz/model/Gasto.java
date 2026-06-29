package co.finanz.model;

import java.time.LocalDate;

/**
 * Representa un gasto o egreso de dinero de la empresa.
 * Implementa los métodos abstractos de {@link Transaccion}.
 * Puede ser registrado manualmente o generado automáticamente al pagar nómina
 * (ver {@link DesprendiblePago#convertirAGasto(CategoriaGasto)}).
 */
public class Gasto extends Transaccion {

    private boolean generadoPorNomina;

    /**
     * Crea un nuevo gasto.
     *
     * @param monto             valor del gasto (mayor que cero)
     * @param fecha             fecha del gasto
     * @param descripcion        descripción del gasto
     * @param categoria         categoría de gasto a la que pertenece
     * @param generadoPorNomina {@code true} si fue creado desde un {@link DesprendiblePago}
     */
    public Gasto(double monto, LocalDate fecha, String descripcion,
                 CategoriaGasto categoria, boolean generadoPorNomina) {
        super(monto, fecha, descripcion, categoria);
        this.generadoPorNomina = generadoPorNomina;
    }

    @Override
    public String getTipo() {
        return "GASTO";
    }

    @Override
    public String getResumen() {
        String etiqueta = generadoPorNomina ? "[NÓMINA]" : "[GASTO] ";
        return String.format("%s $%,.2f — %s (%s)",
                etiqueta, getMonto(), getDescripcion(), getCategoria().getNombre());
    }

    public boolean isGeneradoPorNomina() { return generadoPorNomina; }
    public void setGeneradoPorNomina(boolean generadoPorNomina) {
        this.generadoPorNomina = generadoPorNomina;
    }
}
