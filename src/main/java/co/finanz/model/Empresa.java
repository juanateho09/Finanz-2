package co.finanz.model;

import co.finanz.exception.CategoriaNoEncontradaException;
import co.finanz.exception.EmpleadoNoEncontradoException;
import co.finanz.service.AnalizadorFinanciero;
import co.finanz.service.ReporteBalance;
import co.finanz.service.ReportePorCategoria;
import co.finanz.service.ReporteNomina;
import co.finanz.strategy.TipoContrato;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Fachada principal del sistema Finanz.
 *
 * <p>Aplica el patrón <b>Facade</b>: es el único punto de entrada para los
 * controllers de la UI. Ningún controller debe instanciar ni manipular
 * directamente clases del modelo (Transaccion, Empleado, Categoria, etc.).</p>
 *
 * <p>Todos los métodos de negocio están aquí. Los controllers solo llaman a
 * métodos de {@code Empresa}.</p>
 */
public class Empresa {

    private String nombre;
    private final List<Empleado>    empleados;
    private final List<Transaccion> transacciones;
    private final List<Categoria>   categorias;
    private final List<Presupuesto> presupuestos;
    private final List<TipoContrato> tiposContrato;

    /**
     * Crea una nueva empresa con los tres tipos de contrato colombianos por defecto.
     *
     * @param nombre nombre de la empresa
     */
    public Empresa(String nombre) {
        this.nombre        = nombre;
        this.empleados     = new ArrayList<>();
        this.transacciones = new ArrayList<>();
        this.categorias    = new ArrayList<>();
        this.presupuestos  = new ArrayList<>();
        this.tiposContrato = new ArrayList<>();
        inicializarTiposContrato();
    }

    private void inicializarTiposContrato() {
        tiposContrato.add(TipoContrato.terminoFijo());
        tiposContrato.add(TipoContrato.terminoIndefinido());
        tiposContrato.add(TipoContrato.prestacionServicios());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MÓDULO 1 — TRANSACCIONES
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Registra un ingreso en el flujo de caja.
     *
     * @param monto       valor del ingreso (mayor que cero)
     * @param fecha       fecha del ingreso
     * @param descripcion descripción libre
     * @param categoriaId ID de la {@link CategoriaIngreso} a usar
     * @param fuente      origen del ingreso (ej: "Cliente XYZ")
     * @return el {@link Ingreso} creado y registrado
     * @throws CategoriaNoEncontradaException si no existe la categoría
     */
    public Ingreso registrarIngreso(double monto, LocalDate fecha,
                                    String descripcion, String categoriaId,
                                    String fuente) {
        CategoriaIngreso cat = buscarCategoriaIngreso(categoriaId);
        Ingreso ingreso = new Ingreso(monto, fecha, descripcion, cat, fuente);
        transacciones.add(ingreso);
        return ingreso;
    }

    /**
     * Registra un gasto en el flujo de caja y actualiza los presupuestos afectados.
     *
     * @param monto       valor del gasto (mayor que cero)
     * @param fecha       fecha del gasto
     * @param descripcion descripción libre
     * @param categoriaId ID de la {@link CategoriaGasto} a usar
     * @return el {@link Gasto} creado y registrado
     * @throws CategoriaNoEncontradaException si no existe la categoría
     */
    public Gasto registrarGasto(double monto, LocalDate fecha,
                                String descripcion, String categoriaId) {
        CategoriaGasto cat = buscarCategoriaGasto(categoriaId);
        Gasto gasto = new Gasto(monto, fecha, descripcion, cat, false);
        transacciones.add(gasto);
        actualizarPresupuesto(cat, monto, YearMonth.from(fecha));
        return gasto;
    }

    /**
     * Edita el monto y la descripción de una transacción existente.
     *
     * @param id         ID de la transacción
     * @param nuevoMonto nuevo valor monetario
     * @param nuevaDesc  nueva descripción
     * @throws NoSuchElementException si no existe la transacción
     */
    public void editarTransaccion(String id, double nuevoMonto, String nuevaDesc) {
        Transaccion t = buscarTransaccion(id);
        t.setMonto(nuevoMonto);
        t.setDescripcion(nuevaDesc);
    }

    /**
     * Elimina una transacción por su ID.
     *
     * @param id ID de la transacción a eliminar
     * @throws NoSuchElementException si no existe la transacción
     */
    public void eliminarTransaccion(String id) {
        Transaccion t = buscarTransaccion(id);
        transacciones.remove(t);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MÓDULO 2 — CATEGORÍAS
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Crea y registra una nueva categoría de ingreso.
     *
     * @param nombre      nombre de la categoría
     * @param descripcion descripción de la categoría
     * @return la {@link CategoriaIngreso} creada
     */
    public CategoriaIngreso crearCategoriaIngreso(String nombre, String descripcion) {
        CategoriaIngreso cat = new CategoriaIngreso(nombre, descripcion);
        categorias.add(cat);
        return cat;
    }

    /**
     * Crea y registra una nueva categoría de gasto.
     *
     * @param nombre      nombre de la categoría
     * @param descripcion descripción de la categoría
     * @return la {@link CategoriaGasto} creada
     */
    public CategoriaGasto crearCategoriaGasto(String nombre, String descripcion) {
        CategoriaGasto cat = new CategoriaGasto(nombre, descripcion);
        categorias.add(cat);
        return cat;
    }

    /**
     * Elimina una categoría por su ID.
     *
     * @param id ID de la categoría
     * @throws CategoriaNoEncontradaException si no existe
     */
    public void eliminarCategoria(String id) {
        Categoria cat = categorias.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CategoriaNoEncontradaException(id));
        categorias.remove(cat);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MÓDULO 3 — EMPLEADOS Y NÓMINA
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Registra un nuevo empleado en la empresa (sin contrato aún).
     *
     * @param nombre   primer nombre
     * @param apellido apellido
     * @param cedula   número de documento de identidad
     * @param cargo    puesto en la empresa
     * @return el {@link Empleado} creado
     */
    public Empleado registrarEmpleado(String nombre, String apellido,
                                      String cedula, String cargo) {
        Empleado emp = new Empleado(nombre, apellido, cedula, cargo);
        empleados.add(emp);
        return emp;
    }

    /**
     * Asigna o reemplaza el contrato de un empleado.
     *
     * @param empleadoId     ID del empleado
     * @param salarioBase    salario bruto acordado
     * @param fechaInicio    fecha de inicio del contrato
     * @param tipoContratoId ID del {@link TipoContrato} del catálogo
     * @throws EmpleadoNoEncontradoException si no existe el empleado
     * @throws NoSuchElementException        si no existe el tipo de contrato
     */
    public void asignarContrato(String empleadoId, double salarioBase,
                                LocalDate fechaInicio, String tipoContratoId) {
        Empleado emp = buscarEmpleado(empleadoId);
        TipoContrato tipo = tiposContrato.stream()
                .filter(t -> t.getId().equals(tipoContratoId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Tipo de contrato no encontrado: " + tipoContratoId));
        emp.setContrato(new Contrato(salarioBase, fechaInicio, tipo));
    }

    /**
     * Genera la nómina de un empleado para el periodo indicado.
     * Crea el {@link DesprendiblePago} y lo convierte automáticamente en un
     * {@link Gasto} de nómina para afectar el flujo de caja.
     *
     * @param empleadoId  ID del empleado
     * @param periodo     mes y año a pagar (ej: 2026-06)
     * @param categoriaId ID de la {@link CategoriaGasto} donde se contabiliza el gasto
     * @return el {@link DesprendiblePago} generado
     * @throws EmpleadoNoEncontradoException si no existe el empleado
     * @throws IllegalStateException         si el empleado no tiene contrato
     * @throws CategoriaNoEncontradaException si no existe la categoría
     */
    public DesprendiblePago generarNomina(String empleadoId,
                                          YearMonth periodo,
                                          String categoriaId) {
        Empleado emp = buscarEmpleado(empleadoId);
        if (!emp.tieneContrato()) {
            throw new IllegalStateException(
                    "El empleado '" + emp.getNombreCompleto() + "' no tiene contrato asignado.");
        }
        Contrato contrato = emp.getContrato();
        DesprendiblePago desprendible = new DesprendiblePago(
                emp.getId(),
                emp.getNombreCompleto(),
                periodo,
                contrato.getSalarioBase(),
                contrato.calcularDeducciones()
        );
        emp.agregarPago(desprendible);

        CategoriaGasto cat = buscarCategoriaGasto(categoriaId);
        Gasto gastoNomina = desprendible.convertirAGasto(cat);
        transacciones.add(gastoNomina);
        actualizarPresupuesto(cat, gastoNomina.getMonto(), periodo);

        return desprendible;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MÓDULO 4 — TIPOS DE CONTRATO (Strategy extensible)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Crea un tipo de contrato personalizado y lo agrega al catálogo.
     * Con esto el usuario puede definir nuevos tipos sin modificar el código.
     *
     * @param nombre              nombre del tipo (ej: "Pasantía Remunerada")
     * @param descripcion         descripción del tipo
     * @param porcentajeSalud     porcentaje de descuento para salud (ej: 0.04)
     * @param porcentajePension   porcentaje de descuento para pensión (ej: 0.04)
     * @param porcentajeRetencion porcentaje de retención en la fuente (ej: 0.00)
     * @param porcentajeAdicional descuento adicional libre (ej: libranza)
     * @return el {@link TipoContrato} creado y registrado
     */
    public TipoContrato crearTipoContrato(String nombre, String descripcion,
                                          double porcentajeSalud, double porcentajePension,
                                          double porcentajeRetencion, double porcentajeAdicional) {
        TipoContrato tipo = new TipoContrato(nombre, descripcion,
                porcentajeSalud, porcentajePension,
                porcentajeRetencion, porcentajeAdicional);
        tiposContrato.add(tipo);
        return tipo;
    }

    /**
     * Elimina un tipo de contrato del catálogo por su ID.
     *
     * @param id ID del tipo a eliminar
     * @throws NoSuchElementException si no existe
     */
    public void eliminarTipoContrato(String id) {
        TipoContrato tipo = tiposContrato.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Tipo de contrato no encontrado: " + id));
        tiposContrato.remove(tipo);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MÓDULO 5 — PRESUPUESTOS
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Crea un presupuesto mensual para una categoría de gasto.
     *
     * @param categoriaId ID de la {@link CategoriaGasto}
     * @param limite      monto máximo permitido
     * @param mes         mes al que aplica
     * @return el {@link Presupuesto} creado
     * @throws CategoriaNoEncontradaException si no existe la categoría
     */
    public Presupuesto crearPresupuesto(String categoriaId, double limite, YearMonth mes) {
        CategoriaGasto cat = buscarCategoriaGasto(categoriaId);
        Presupuesto presupuesto = new Presupuesto(cat, limite, mes);
        presupuestos.add(presupuesto);
        return presupuesto;
    }

    /**
     * Elimina un presupuesto por su ID.
     *
     * @param id ID del presupuesto
     */
    public void eliminarPresupuesto(String id) {
        presupuestos.removeIf(p -> p.getId().equals(id));
    }

    /**
     * Busca el presupuesto de una categoría para un mes dado.
     *
     * @param categoriaId ID de la categoría
     * @param mes         mes a consultar
     * @return {@link Optional} con el presupuesto, o vacío si no existe
     */
    public Optional<Presupuesto> getPresupuesto(String categoriaId, YearMonth mes) {
        return presupuestos.stream()
                .filter(p -> p.getCategoria().getId().equals(categoriaId)
                        && p.getMes().equals(mes))
                .findFirst();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MÓDULO 6 — REPORTES (Template Method via AnalizadorFinanciero)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Genera el reporte de balance general para un mes dado.
     *
     * @param mes mes a reportar
     * @return reporte formateado como texto
     */
    public String generarReporteBalance(YearMonth mes) {
        return new ReporteBalance().generarReporte(this, mes);
    }

    /**
     * Genera el reporte de transacciones agrupadas por categoría.
     *
     * @param mes mes a reportar
     * @return reporte formateado como texto
     */
    public String generarReportePorCategoria(YearMonth mes) {
        return new ReportePorCategoria().generarReporte(this, mes);
    }

    /**
     * Genera el reporte de nómina pagada en un mes dado.
     *
     * @param mes mes a reportar
     * @return reporte formateado como texto
     */
    public String generarReporteNomina(YearMonth mes) {
        return new ReporteNomina().generarReporte(this, mes);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MÓDULO 7 — CÁLCULOS FINANCIEROS
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Calcula el balance general: ingresos totales menos gastos totales.
     *
     * @return superávit (positivo) o déficit (negativo)
     */
    public double calcularBalance() {
        return calcularTotalIngresos() - calcularTotalGastos();
    }

    /**
     * Suma todos los ingresos registrados.
     *
     * @return total de ingresos
     */
    public double calcularTotalIngresos() {
        return transacciones.stream()
                .filter(t -> t instanceof Ingreso)
                .mapToDouble(Transaccion::getMonto)
                .sum();
    }

    /**
     * Suma todos los gastos registrados.
     *
     * @return total de gastos
     */
    public double calcularTotalGastos() {
        return transacciones.stream()
                .filter(t -> t instanceof Gasto)
                .mapToDouble(Transaccion::getMonto)
                .sum();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // GETTERS DE COLECCIONES (vistas inmutables)
    // ══════════════════════════════════════════════════════════════════════════

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Empleado>     getEmpleados()     { return Collections.unmodifiableList(empleados); }
    public List<Transaccion>  getTransacciones() { return Collections.unmodifiableList(transacciones); }
    public List<Categoria>    getCategorias()    { return Collections.unmodifiableList(categorias); }
    public List<Presupuesto>  getPresupuestos()  { return Collections.unmodifiableList(presupuestos); }
    public List<TipoContrato> getTiposContrato() { return Collections.unmodifiableList(tiposContrato); }

    /** Retorna solo los ingresos de la lista de transacciones. */
    public List<Ingreso> getIngresos() {
        return transacciones.stream()
                .filter(t -> t instanceof Ingreso)
                .map(t -> (Ingreso) t)
                .collect(Collectors.toList());
    }

    /** Retorna solo los gastos de la lista de transacciones. */
    public List<Gasto> getGastos() {
        return transacciones.stream()
                .filter(t -> t instanceof Gasto)
                .map(t -> (Gasto) t)
                .collect(Collectors.toList());
    }

    /** Retorna solo las categorías de ingreso. */
    public List<CategoriaIngreso> getCategoriasIngreso() {
        return categorias.stream()
                .filter(c -> c instanceof CategoriaIngreso)
                .map(c -> (CategoriaIngreso) c)
                .collect(Collectors.toList());
    }

    /** Retorna solo las categorías de gasto. */
    public List<CategoriaGasto> getCategoriasGasto() {
        return categorias.stream()
                .filter(c -> c instanceof CategoriaGasto)
                .map(c -> (CategoriaGasto) c)
                .collect(Collectors.toList());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // BÚSQUEDAS INTERNAS (solo Empresa las usa — encapsulan la lógica de lookup)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Busca un empleado por ID. Usado internamente por la fachada.
     *
     * @param id ID del empleado
     * @return el {@link Empleado} encontrado
     * @throws EmpleadoNoEncontradoException si no existe
     */
    public Empleado buscarEmpleado(String id) {
        return empleados.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EmpleadoNoEncontradoException(id));
    }

    private Transaccion buscarTransaccion(String id) {
        return transacciones.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Transacción no encontrada: " + id));
    }

    private CategoriaIngreso buscarCategoriaIngreso(String id) {
        return categorias.stream()
                .filter(c -> c.getId().equals(id) && c instanceof CategoriaIngreso)
                .map(c -> (CategoriaIngreso) c)
                .findFirst()
                .orElseThrow(() -> new CategoriaNoEncontradaException(id));
    }

    private CategoriaGasto buscarCategoriaGasto(String id) {
        return categorias.stream()
                .filter(c -> c.getId().equals(id) && c instanceof CategoriaGasto)
                .map(c -> (CategoriaGasto) c)
                .findFirst()
                .orElseThrow(() -> new CategoriaNoEncontradaException(id));
    }

    private void actualizarPresupuesto(CategoriaGasto cat, double monto, YearMonth mes) {
        getPresupuesto(cat.getId(), mes).ifPresent(p -> p.registrarGasto(monto));
    }
}
