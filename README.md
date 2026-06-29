# Finanz — Software de Gestión Financiera para PYMES

Proyecto final de **Programación II** — Universidad Nacional de Colombia, 2026.

## Descripción

Finanz es una aplicación de escritorio que permite a las pequeñas y medianas empresas gestionar su flujo de caja, nómina, presupuestos y reportes financieros desde una interfaz amigable.

## Equipo

| Nombre | Rol |
|---|---|
| Juan José Atehorutua | Desarrollador principal |
| Esteban Valencia González | Co-sustentador |

## Tecnologías

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 25 (OpenJDK 25.0.2) |
| Interfaz gráfica | JavaFX 25 + FXML |
| Persistencia | JSON via Gson 2.11.0 |
| Build | Apache Maven |
| Pruebas | JUnit 5 |
| IDE | IntelliJ IDEA |

## Módulos implementados

- **Flujo de Caja** — Registro y gestión de ingresos y gastos con categorías dinámicas
- **Nómina** — Gestión de empleados, contratos y desprendibles de pago
- **Presupuestos** — Límites mensuales por categoría con alertas automáticas
- **Reportes** — Balance general, reportes por categoría y gráficas de flujo de caja
- **Persistencia** — Guardado y carga automática del estado en JSON

## Arquitectura y Patrones de Diseño

| Patrón | Clase | Propósito |
|---|---|---|
| **Facade** | `Empresa` | Único punto de entrada para los controllers; oculta toda la complejidad interna |
| **Strategy** | `TipoContrato` / `EstrategiaSalario` | Cada tipo de contrato calcula salario y deducciones de forma distinta. Los tipos son **configurables en tiempo de ejecución** (no hardcodeados) |
| **Observer** | `Presupuesto` / `ObservadorPresupuesto` | Notifica automáticamente a la UI cuando un gasto supera el límite presupuestado |
| **Template Method** | `AnalizadorFinanciero` | Define la estructura fija de generación de reportes; cada subclase implementa el cálculo específico |

## Jerarquía de Clases

```
Empresa  «Facade»
├── List<Empleado>
│   └── Contrato
│       └── TipoContrato  «Strategy»   ← configurable en runtime
├── List<Transaccion>  «abstract»
│   ├── Ingreso
│   └── Gasto
├── List<Categoria>  «abstract»
│   ├── CategoriaIngreso
│   └── CategoriaGasto
├── List<Presupuesto>  «Observer»
├── List<TipoContrato>  ← catálogo de tipos (3 por defecto + los que cree el usuario)
├── GestorPersistencia
└── AnalizadorFinanciero  «Template Method»  «abstract»
    ├── ReporteBalance
    ├── ReportePorCategoria
    └── ReporteNomina
```

## Estructura del Proyecto

```
src/
├── main/java/co/finanz/
│   ├── Main.java
│   ├── model/            ← Entidades de dominio
│   │   ├── Empresa.java             (Facade)
│   │   ├── Transaccion.java         (abstract)
│   │   ├── Ingreso.java
│   │   ├── Gasto.java
│   │   ├── Categoria.java           (abstract)
│   │   ├── CategoriaIngreso.java
│   │   ├── CategoriaGasto.java
│   │   ├── Empleado.java
│   │   ├── Contrato.java
│   │   ├── DesprendiblePago.java
│   │   └── Presupuesto.java
│   ├── service/          ← Lógica de negocio
│   │   ├── AnalizadorFinanciero.java  (abstract — Template Method)
│   │   ├── ReporteBalance.java
│   │   ├── ReportePorCategoria.java
│   │   ├── ReporteNomina.java
│   │   └── GestorPersistencia.java
│   ├── strategy/         ← Patrón Strategy (cálculo salarial)
│   │   ├── EstrategiaSalario.java   (interface)
│   │   └── TipoContrato.java        (implementación configurable)
│   ├── observer/         ← Patrón Observer (alertas de presupuesto)
│   │   └── ObservadorPresupuesto.java  (interface)
│   ├── controller/       ← Controllers JavaFX (solo UI, sin lógica de negocio)
│   │   ├── DashboardController.java
│   │   ├── TransaccionController.java
│   │   ├── EmpleadoController.java
│   │   ├── PresupuestoController.java
│   │   └── ReporteController.java
│   ├── util/
│   │   └── AlertaUtil.java
│   └── exception/        ← Excepciones personalizadas
│       ├── PresupuestoExcedidoException.java
│       ├── EmpleadoNoEncontradoException.java
│       ├── CategoriaNoEncontradaException.java
│       └── PersistenciaException.java
└── main/resources/co/finanz/
    ├── fxml/             ← Vistas declarativas (Scene Builder)
    └── css/              ← Estilos visuales
```

## Tipos de Contrato (Strategy extensible)

Los tipos de contrato **no están hardcodeados** como clases fijas. `TipoContrato` es una entidad configurable que implementa `EstrategiaSalario`. La aplicación incluye 3 tipos por defecto, pero el usuario puede crear los suyos:

| Tipo | % Salud | % Pensión | % Retención |
|---|---|---|---|
| Término Fijo | 4% | 4% | 0% |
| Término Indefinido | 4% | 4% | 0% |
| Prestación de Servicios | 0% | 0% | variable |
| *(definido por usuario)* | custom | custom | custom |

## Cómo ejecutar

```bash
# Compilar
mvn compile

# Ejecutar la aplicación
mvn javafx:run

# Correr los tests
mvn test
```
