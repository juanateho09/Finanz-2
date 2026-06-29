package co.finanz.service;

import co.finanz.model.Empresa;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Generador de reportes financieros.
 *
 * <p>Aplica el patrón <b>Template Method</b>: {@link #generarReporte} define una
 * secuencia fija de pasos (encabezado → datos → cuerpo → pie), y cada subclase
 * implementa los pasos que varían según el tipo de reporte.</p>
 *
 * <p>Subclases concretas: {@link ReporteBalance}, {@link ReportePorCategoria},
 * {@link ReporteNomina}.</p>
 */
public abstract class AnalizadorFinanciero {

    protected static final String SEPARADOR = "═".repeat(52);
    protected static final DateTimeFormatter FMT_MES =
            DateTimeFormatter.ofPattern("MMMM yyyy");

    /**
     * <b>Método plantilla</b> — genera el reporte completo en texto.
     * La secuencia de pasos es fija; las subclases implementan los pasos abstractos.
     *
     * @param empresa la empresa fuente de los datos
     * @param mes     mes al que aplica el reporte
     * @return reporte completo como String formateado
     */
    public final String generarReporte(Empresa empresa, YearMonth mes) {
        return SEPARADOR + "\n"
                + generarEncabezado(mes) + "\n"
                + SEPARADOR + "\n"
                + formatearResultados(calcularDatos(empresa, mes)) + "\n"
                + SEPARADOR + "\n"
                + generarPie(empresa, mes);
    }

    /**
     * Genera la línea de título del reporte (varía por tipo de reporte).
     *
     * @param mes mes del reporte
     * @return encabezado formateado
     */
    protected abstract String generarEncabezado(YearMonth mes);

    /**
     * Extrae y calcula los datos relevantes de la empresa para este tipo de reporte.
     *
     * @param empresa fuente de datos
     * @param mes     mes a analizar
     * @return mapa de etiqueta → valor para renderizar en el cuerpo
     */
    protected abstract Map<String, Double> calcularDatos(Empresa empresa, YearMonth mes);

    /**
     * Convierte el mapa de datos en el cuerpo de texto del reporte.
     *
     * @param datos resultado de {@link #calcularDatos}
     * @return cuerpo formateado como String
     */
    protected abstract String formatearResultados(Map<String, Double> datos);

    /**
     * Genera el pie del reporte. Común a todos los tipos — puede sobreescribirse.
     *
     * @param empresa fuente de datos (para totales de cierre)
     * @param mes     mes del reporte
     * @return pie formateado
     */
    protected String generarPie(Empresa empresa, YearMonth mes) {
        return String.format("Balance general: $%,.2f%n", empresa.calcularBalance());
    }
}
