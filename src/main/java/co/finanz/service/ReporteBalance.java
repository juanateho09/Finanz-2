package co.finanz.service;

import co.finanz.model.Empresa;

import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reporte de balance general: ingresos totales, gastos totales y superávit/déficit.
 * Implementa los pasos variables del patrón Template Method de {@link AnalizadorFinanciero}.
 */
public class ReporteBalance extends AnalizadorFinanciero {

    @Override
    protected String generarEncabezado(YearMonth mes) {
        return String.format("  BALANCE GENERAL — %s",
                mes.format(FMT_MES).toUpperCase());
    }

    @Override
    protected Map<String, Double> calcularDatos(Empresa empresa, YearMonth mes) {
        double ingresos = empresa.getIngresos().stream()
                .filter(i -> YearMonth.from(i.getFecha()).equals(mes))
                .mapToDouble(i -> i.getMonto())
                .sum();
        double gastos = empresa.getGastos().stream()
                .filter(g -> YearMonth.from(g.getFecha()).equals(mes))
                .mapToDouble(g -> g.getMonto())
                .sum();

        Map<String, Double> datos = new LinkedHashMap<>();
        datos.put("Total Ingresos", ingresos);
        datos.put("Total Gastos",   gastos);
        datos.put("Balance",        ingresos - gastos);
        return datos;
    }

    @Override
    protected String formatearResultados(Map<String, Double> datos) {
        StringBuilder sb = new StringBuilder();
        datos.forEach((etiqueta, valor) -> {
            String signo = etiqueta.equals("Balance") && valor < 0 ? "DÉFICIT  " : "         ";
            sb.append(String.format("  %-20s %s $%,12.2f%n", etiqueta, signo, valor));
        });
        return sb.toString().stripTrailing();
    }
}
