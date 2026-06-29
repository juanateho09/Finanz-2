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

## Arquitectura

El proyecto aplica los siguientes patrones de diseño de la GoF:

- **Facade** — `Empresa` es el único punto de entrada para los controllers
- **Strategy** — `Contrato` delega el cálculo salarial según el tipo de contrato
- **Observer** — `Presupuesto` notifica automáticamente cuando se excede un límite
- **Template Method** — `AnalizadorFinanciero` define la estructura fija de cada reporte

## Cómo ejecutar

```bash
# Compilar
mvn compile

# Ejecutar la aplicación
mvn javafx:run

# Correr los tests
mvn test
```

## Estructura del proyecto

```
src/
├── main/java/co/finanz/
│   ├── model/        ← Clases de dominio (POO)
│   ├── service/      ← Lógica de análisis y persistencia
│   ├── controller/   ← Controllers de JavaFX (solo UI)
│   ├── util/         ← Utilidades comunes
│   └── exception/    ← Excepciones personalizadas
└── main/resources/co/finanz/
    ├── fxml/         ← Vistas declarativas
    └── css/          ← Estilos visuales
```
