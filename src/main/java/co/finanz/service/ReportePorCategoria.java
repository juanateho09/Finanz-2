package co.finanz.service;

import co.finanz.model.Empresa;
import co.finanz.model.Transaccion;

import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reporte que agrupa el total de transacciones por categoría en un mes dado.
 * Implementa los pasos variables del patrón Template Method de {@link AnalizadorFinanciero}.
 */
public class ReportePorCategoria extends AnalizadorFinanciero {

    @Override
    protected String generarEncabezado(YearMonth mes) {
        return String.format("  REPORTE POR CATEGORÍA — %s",
                mes.format(FMT_MES).toUpperCase());
    }

    @Override
    protected Map<String, Double> calcularDatos(Empresa empresa, YearMonth mes) {
        Map<String, Double> datos = new LinkedHashMap<>();
        empresa.getTransacciones().stream()
                .filter(t -> YearMonth.from(t.getFecha()).equals(mes))
                .forEach(t -> {
                    String clave = t.getTipo() + " — " + t.getCategoria().getNombre();
                    datos.merge(clave, t.getMonto(), Double::sum);
                });
        return datos;
    }

    @Override
    protected String formatearResultados(Map<String, Double> datos) {
        if (datos.isEmpty()) {
            return "  Sin transacciones para este mes.";
        }
        StringBuilder sb = new StringBuilder();
        datos.forEach((etiqueta, valor) ->
                sb.append(String.format("  %-36s $%,12.2f%n", etiqueta, valor)));
        return sb.toString().stripTrailing();
    }
}
