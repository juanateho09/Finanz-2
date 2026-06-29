package co.finanz.strategy;

/**
 * Contrato del patrón Strategy para el cálculo salarial.
 * Permite que {@link TipoContrato} varíe sus algoritmos de cálculo
 * sin modificar la clase {@code Contrato} que los usa.
 */
public interface EstrategiaSalario {

    /**
     * Calcula el total de deducciones que se aplican al salario base.
     *
     * @param salarioBase salario bruto acordado en el contrato
     * @return suma de todas las deducciones (salud + pensión + retención + adicional)
     */
    double calcularDeducciones(double salarioBase);

    /**
     * Calcula el salario neto que el empleado recibe en mano.
     *
     * @param salarioBase salario bruto acordado en el contrato
     * @return salario neto después de todas las deducciones
     */
    double calcularSalarioNeto(double salarioBase);

    /**
     * Retorna el nombre descriptivo de este tipo de contrato.
     *
     * @return nombre del tipo (ej: "Término Fijo")
     */
    String getNombre();
}
