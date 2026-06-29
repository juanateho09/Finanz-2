package co.finanz.service;

import co.finanz.exception.PersistenciaException;
import co.finanz.model.*;
import co.finanz.strategy.TipoContrato;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Gestiona la persistencia del estado de {@link Empresa} en un archivo JSON.
 * Usa Gson con adaptadores personalizados para manejar:
 * <ul>
 *   <li>{@code LocalDate} y {@code YearMonth} (no soportados nativamente por Gson)</li>
 *   <li>{@code Transaccion} y {@code Categoria} (tipos abstractos con polimorfismo)</li>
 * </ul>
 */
public class GestorPersistencia {

    private static final String RUTA_ARCHIVO = "data/finanz_data.json";

    private final Gson gson;

    public GestorPersistencia() {
        this.gson = construirGson();
    }

    // ── API pública ───────────────────────────────────────────────────────────

    /**
     * Serializa el estado completo de la empresa al archivo JSON.
     *
     * @param empresa la empresa a guardar
     * @throws PersistenciaException si ocurre un error de escritura
     */
    public void guardar(Empresa empresa) {
        try {
            Path ruta = Path.of(RUTA_ARCHIVO);
            Files.createDirectories(ruta.getParent());
            String json = gson.toJson(empresa);
            Files.writeString(ruta, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar los datos en " + RUTA_ARCHIVO, e);
        }
    }

    /**
     * Deserializa el estado de la empresa desde el archivo JSON.
     * Si el archivo no existe, retorna una empresa nueva con nombre por defecto.
     *
     * @return la empresa cargada, o una nueva si el archivo no existe
     * @throws PersistenciaException si el archivo existe pero está corrupto
     */
    public Empresa cargar() {
        Path ruta = Path.of(RUTA_ARCHIVO);
        if (!Files.exists(ruta)) {
            return new Empresa("Mi Empresa");
        }
        try {
            String json = Files.readString(ruta, StandardCharsets.UTF_8);
            return gson.fromJson(json, Empresa.class);
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer el archivo " + RUTA_ARCHIVO, e);
        } catch (JsonSyntaxException e) {
            throw new PersistenciaException(
                    "El archivo de datos está corrupto: " + RUTA_ARCHIVO, e);
        }
    }

    // ── Configuración de Gson ─────────────────────────────────────────────────

    private Gson construirGson() {
        return new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class,    new LocalDateAdapter())
                .registerTypeAdapter(YearMonth.class,    new YearMonthAdapter())
                .registerTypeAdapter(Transaccion.class,  new TransaccionAdapter())
                .registerTypeAdapter(Categoria.class,    new CategoriaAdapter())
                .create();
    }

    // ── Adaptadores de tipos ──────────────────────────────────────────────────

    /** Serializa/deserializa LocalDate como "yyyy-MM-dd". */
    private static class LocalDateAdapter
            implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate src, Type t, JsonSerializationContext ctx) {
            return new JsonPrimitive(src.toString());
        }
        @Override
        public LocalDate deserialize(JsonElement json, Type t, JsonDeserializationContext ctx) {
            return LocalDate.parse(json.getAsString());
        }
    }

    /** Serializa/deserializa YearMonth como "yyyy-MM". */
    private static class YearMonthAdapter
            implements JsonSerializer<YearMonth>, JsonDeserializer<YearMonth> {
        @Override
        public JsonElement serialize(YearMonth src, Type t, JsonSerializationContext ctx) {
            return new JsonPrimitive(src.toString());
        }
        @Override
        public YearMonth deserialize(JsonElement json, Type t, JsonDeserializationContext ctx) {
            return YearMonth.parse(json.getAsString());
        }
    }

    /**
     * Agrega un campo {@code "tipo_clase"} al JSON de cada Transaccion
     * para poder reconstruir la subclase correcta al deserializar.
     */
    private static class TransaccionAdapter
            implements JsonSerializer<Transaccion>, JsonDeserializer<Transaccion> {
        private static final String CAMPO_TIPO = "tipo_clase";

        @Override
        public JsonElement serialize(Transaccion src, Type t, JsonSerializationContext ctx) {
            JsonObject obj = ctx.serialize(src, src.getClass()).getAsJsonObject();
            obj.addProperty(CAMPO_TIPO, src.getClass().getSimpleName());
            return obj;
        }

        @Override
        public Transaccion deserialize(JsonElement json, Type t, JsonDeserializationContext ctx)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String tipoClase = obj.get(CAMPO_TIPO).getAsString();
            return switch (tipoClase) {
                case "Ingreso" -> ctx.deserialize(json, Ingreso.class);
                case "Gasto"   -> ctx.deserialize(json, Gasto.class);
                default        -> throw new JsonParseException("Tipo de transacción desconocido: " + tipoClase);
            };
        }
    }

    /**
     * Agrega un campo {@code "tipo_clase"} al JSON de cada Categoria
     * para poder reconstruir la subclase correcta al deserializar.
     */
    private static class CategoriaAdapter
            implements JsonSerializer<Categoria>, JsonDeserializer<Categoria> {
        private static final String CAMPO_TIPO = "tipo_clase";

        @Override
        public JsonElement serialize(Categoria src, Type t, JsonSerializationContext ctx) {
            JsonObject obj = ctx.serialize(src, src.getClass()).getAsJsonObject();
            obj.addProperty(CAMPO_TIPO, src.getClass().getSimpleName());
            return obj;
        }

        @Override
        public Categoria deserialize(JsonElement json, Type t, JsonDeserializationContext ctx)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String tipoClase = obj.get(CAMPO_TIPO).getAsString();
            return switch (tipoClase) {
                case "CategoriaIngreso" -> ctx.deserialize(json, CategoriaIngreso.class);
                case "CategoriaGasto"   -> ctx.deserialize(json, CategoriaGasto.class);
                default -> throw new JsonParseException("Tipo de categoría desconocido: " + tipoClase);
            };
        }
    }
}
