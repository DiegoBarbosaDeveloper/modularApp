# Arquitectura del proyecto `modularizar` (rama `layer`)

## 1. Resumen

Este proyecto es un **monolito con arquitectura por capas** (layered architecture): una sola unidad de despliegue (un único módulo Gradle que produce un solo JAR ejecutable), donde el código se organiza por **responsabilidad técnica**, no por feature de negocio.

- **Stack**: Spring Boot 4.1.0 · Java 26 · Gradle (Kotlin DSL) · Spring Web MVC · Spring Data JPA (Hibernate) · H2 en memoria · Lombok · Bean Validation · Actuator · DevTools
- **Artefacto**: un solo `build.gradle.kts` en la raíz (`settings.gradle.kts` declara únicamente `rootProject.name = "modularizar"`). No hay multi-módulo Gradle.
- **Base package**: `com.monolito.modularizar`

## 2. Estructura de paquetes

```
src/main/java/com/monolito/modularizar/
├── ModularizarApplication.java   (@SpringBootApplication, punto de entrada)
├── controller/                   (capa web / API REST)
│   ├── InventoryController.java      → /api/inventory
│   └── OrderController.java          → /api/orders
├── service/                      (lógica de negocio y transacciones)
│   ├── InventoryService.java
│   └── OrderService.java
├── repository/                   (acceso a datos, Spring Data JPA)
│   ├── ItemRepository.java
│   ├── OrderRepository.java
│   └── InvoiceRepository.java
├── domain/                       (entidades JPA y modelo persistente)
│   ├── Item.java                     (entidad, tabla items)
│   ├── Order.java                    (entidad, tabla orders)
│   ├── OrderLine.java                (@Embeddable, tabla order_lines)
│   ├── Invoice.java                  (entidad, tabla invoices)
│   └── OrderStatus.java              (enum de estado de la orden)
├── dto/                          (objetos de entrada/salida de la API)
│   ├── CreateItemRequest.java
│   └── AdjustStockRequest.java
└── exception/                    (manejo global de errores)
    ├── GlobalExceptionHandler.java   (@RestControllerAdvice)
    └── ApiError.java                 (cuerpo estándar de error)

src/main/resources/
└── application.yml               (datasource H2, JPA, consola H2)

src/test/java/com/monolito/modularizar/
├── ModularizarApplicationTests.java  (carga de contexto)
├── InventoryServiceTest.java         (integración: crear item y ajustar stock)
└── OrderServiceTest.java             (integración: crear orden con factura)
```

## 3. Las capas y sus responsabilidades

El flujo de dependencias es estrictamente hacia abajo:

```
HTTP Request
     ↓
Controller  → recibe HTTP, valida entrada (@Valid), delega, devuelve respuesta
     ↓
Service     → lógica de negocio, reglas, transacciones (@Transactional)
     ↓
Repository  → acceso a datos (JpaRepository)
     ↓
H2 (base de datos en memoria)
```

### Controller (`controller/`)
- `@RestController` + `@RequestMapping`; no contiene lógica de negocio.
- Valida los DTOs de entrada con `@Valid` (Bean Validation).
- Endpoints expuestos:
  - `GET /api/inventory` — lista ítems
  - `GET /api/inventory/{id}` — obtiene un ítem
  - `POST /api/inventory` — crea ítem (valida `CreateItemRequest`)
  - `PATCH /api/inventory/{id}/stock` — ajusta stock (`AdjustStockRequest`)
  - `POST /api/orders` — crea una orden a partir de líneas `"itemId:cantidad"`
- Los controllers devuelven directamente entidades del dominio (p. ej. `Item`, `Order`). No hay DTOs de salida.

### Service (`service/`)
- Anotados con `@Service`; métodos de escritura con `@Transactional`.
- **InventoryService**: CRUD de ítems, unicidad de SKU, ajuste de stock (delega la regla de stock negativo a la entidad `Item.adjustStock()`).
- **OrderService**: orquesta la creación de órdenes:
  - Parsea las líneas `"itemId:cantidad"`.
  - Consulta cada ítem vía `InventoryService` (comunicación entre servicios, no entre repositorios).
  - Valida stock disponible y descuenta stock por línea.
  - Genera la `Invoice` (número `FAC-<timestamp>`) y la `Order` con estado `PENDING`.
- Es el único punto donde dos capacidades de negocio (inventario y órdenes) se tocan.

### Repository (`repository/`)
- Interfaces que extienden `JpaRepository<Entity, Long>`; queries derivadas por nombre (`findBySku`).
- Solo son usados por la capa service; los controllers nunca acceden a repositorios.

### Domain (`domain/`)
- Entidades JPA anotadas con Lombok (`@Getter/@Setter/@Builder/@NoArgsConstructor/@AllArgsConstructor`).
- `Order` → `Invoice` es `@OneToOne` con `CascadeType.ALL`; `Order.lines` es `@ElementCollection` de `OrderLine` (value object embebible, guarda snapshot de precio unitario).
- Contiene lógica de dominio simple: `Item.adjustStock(delta)` lanza excepción si el stock resultante sería negativo.

### DTO (`dto/`)
- Objetos de **entrada** con reglas de validación declarativas (`@NotBlank`, `@Size`, `@Pattern`, `@PositiveOrZero`) con mensajes en español.
- Se convierten a parámetros simples antes de llamar al service (el service no conoce los DTOs).

### Exception (`exception/`)
- `GlobalExceptionHandler` (`@RestControllerAdvice`) centraliza el manejo de errores:
  - `MethodArgumentNotValidException` → 400 con lista de errores de campo.
  - `IllegalArgumentException` → 400 con el mensaje del error de negocio.
- Respuesta de error estandarizada en `ApiError` (`timestamp`, `status`, `error`, `message`, `details`).

## 4. Configuración e infraestructura

- **Base de datos**: H2 en memoria (`jdbc:h2:mem:modularizar`, modo MySQL), `ddl-auto: create-drop` (el esquema se genera desde las entidades), SQL visible en consola, consola H2 habilitada en `/h2-console`.
- **Persistencia**: Hibernate genera tablas `items`, `orders`, `order_lines`, `invoices`. No hay migraciones (Flyway/Liquibase).
- **Actuator** incluido para monitoreo básico; **DevTools** para recarga en desarrollo.
- **Inyección de dependencias**: por constructor en todos los beans (sin `@Autowired` en campos).

## 5. Pruebas

- Pruebas de integración con `@SpringBootTest` (levantan el contexto completo contra H2 en memoria):
  - `InventoryServiceTest`: crea un ítem y verifica el descuento de stock.
  - `OrderServiceTest`: crea una orden y verifica total, factura y estado.
- Se prueban los services directamente (no hay pruebas de controller/WebMvc ni tests unitarios aislados con mocks).

## 6. Reglas implícitas de la arquitectura actual

1. Una capa solo depende de la inmediatamente inferior (controller → service → repository).
2. Los controllers no tocan repositorios.
3. La comunicación entre capacidades de negocio (órdenes ↔ inventario) pasa por servicios, nunca por repositorios ajenos.
4. Las entidades JPA salen hasta el controller (no hay capa de mapeo entidad ↔ DTO de salida).
5. Un solo módulo Gradle: todas las fronteras son convenciones de paquetes, no enforced por el build.

## 7. Observaciones y puntos de mejora potenciales

- **Entidades como respuesta HTTP**: exponer entidades JPA directamente acopla el contrato de la API al modelo de datos (riesgo de lazy-loading / serialización cíclica con `Order.invoice`). Crearía DTOs de salida + mapper (p. ej. MapStruct).
- **Sin manejo de concurrencia de stock**: el chequeo "hay stock suficiente" y el descuento no son atómicos ante peticiones concurrentes (haría falta locking pesimista o update condicional).
- **Dinero como `double`**: para totales y precios, `BigDecimal` evitaría errores de redondeo.
- **Sin migraciones de esquema**: `create-drop` borra datos en cada reinicio; Flyway sería el siguiente paso natural.
- **Camino a monolito modular**: si el proyecto crece, la migración natural sería agrupar por bounded context (`inventory/`, `orders/`) con paquetes `api/` explícitos, o dividir en módulos Gradle (`module-inventory`, `module-orders`, `common`), haciendo cumplir en compilación las fronteras que hoy son solo convención.
