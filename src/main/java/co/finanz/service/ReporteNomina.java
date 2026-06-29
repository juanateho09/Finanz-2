package co.finanz.service;

import co.finanz.model.DesprendiblePago;
import co.finanz.model.Empresa;

import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reporte de nómina mensual: muestra el salario neto pagado a cada empleado.
 * Implementa los pasos variables del patrón Template Method de {@link AnalizadorFinanciero}.
 */
public class ReporteNomina extends AnalizadorFinanciero {

    @Override
    protected String generarEncabezado(YearMonth mes) {
        return String.format("  REPORTE DE NÓMINA — %s",
                mes.format(FMT_MES).toUpperCase());
    }

    @Override
    protected Map<String, Double> calcularDatos(Empresa empresa, YearMonth mes) {
        Map<String, Double> datos = new LinkedHashMap<>();
        empresa.getEmpleados().forEach(emp ->
                emp.getHistorialPagos().stream()
                        .filter(d -> d.getPeriodo().equals(mes))
                        .forEach(d -> datos.put(emp.getNombreCompleto(), d.getSalarioNeto()))
        );
        return datos;
    }

    @Override
    protected String formatearResultados(Map<String, Double> datos) {
        if (datos.isEmpty()) {
            return "  Sin pagos de nómina para este mes.";
        }
        StringBuilder sb = new StringBuilder();
        double total = 0;
        for (Map.Entry<String, Double> entry : datos.entrySet()) {
            sb.append(String.format("  %-30s $%,12.2f%n",
                    entry.getKey(), entry.getValue()));
            total += entry.getValue();
        }
        sb.append(String.format("%n  %-30s $%,12.2f", "TOTAL NÓMINA", total));
        return sb.toString();
    }

    @Override
    protected String generarPie(Empresa empresa, YearMonth mes) {
        long totalEmpleados = empresa.getEmpleados().stream()
                .filter(e -> e.getHistorialPagos().stream()
                        .anyMatch(d -> d.getPeriodo().equals(mes)))
                .count();
        return String.format("Empleados pagados este mes: %d%n", totalEmpleados);
    }
}
