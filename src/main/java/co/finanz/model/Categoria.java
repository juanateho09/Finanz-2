package co.finanz.model;

import java.util.UUID;

/**
 * Categoría que clasifica una transacción financiera.
 *
 * <p>Clase abstracta que garantiza polimorfismo: {@link #getTipo()} y
 * {@link #esCompatible(Transaccion)} son implementados de forma distinta por
 * {@link CategoriaIngreso} y {@link CategoriaGasto}.</p>
 */
public abstract class Categoria {

    private final String id;
    private String nombre;
    private String descripcion;

    /**
     * Constructor base para todas las categorías.
     *
     * @param nombre      nombre de la categoría (ej: "Ventas", "Arriendo")
     * @param descripcion descripción de la categoría
     */
    protected Categoria(String nombre, String descripcion) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /**
     * Retorna el tipo de esta categoría.
     * {@link CategoriaIngreso} devuelve {@code "INGRESO"};
     * {@link CategoriaGasto} devuelve {@code "GASTO"}.
     *
     * @return tipo de la categoría
     */
    public abstract String getTipo();

    /**
     * Verifica si esta categoría acepta la transacción dada.
     * Una {@link CategoriaIngreso} solo acepta {@link Ingreso}s;
     * una {@link CategoriaGasto} solo acepta {@link Gasto}s.
     *
     * @param transaccion transacción a verificar
     * @return {@code true} si la transacción es compatible
     */
    public abstract boolean esCompatible(Transaccion transaccion);

    // ── Getters y setters ─────────────────────────────────────────────────────

    public String getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombre;
    }
}
