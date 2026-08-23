# Arquitectura — modularApp

Monolito modular (single deployment, módulos de negocio con fronteras explícitas) sobre Spring Boot.

## Stack

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 26 (toolchain Gradle) |
| Framework | Spring Boot 4.1.0 (Gradle Kotlin DSL, single module) |
| Web | spring-boot-starter-web + validation |
| Persistencia | Spring Data JPA / Hibernate (`ddl-auto: create-drop`) |
| BD | H2 en memoria (modo MySQL), consola en `/h2-console` |
| Mapeo | MapStruct 1.6.3 |
| Otros | Lombok, Actuator, DevTools |

## Estructura de paquetes

```
com.monolito.modularizar
├── ModularizarApplication.java      (@SpringBootApplication)
├── exception/                       (compartido: ApiError, GlobalExceptionHandler)
├── inventory/                       (módulo)
│   ├── api/                         InventoryApi, InventoryController
│   │   └── dto/                     AdjustStockRequest, CreateItemRequest
│   └── internal/
│       ├── domain/                  Item
│       ├── mapper/                  ItemMapper
│       ├── persistence/             ItemEntity, ItemRepository
│       └── service/                 InventoryService
├── order/                           (módulo)
│   ├── api/                         OrderApi, OrderController
│   │   └── dto/                     OrderRequest, OrderResponse
│   └── internal/
│       ├── domain/                  Order, OrderLine, OrderStatusDomain
│       ├── mapper/                  OrderMapper
│       ├── persistence/             OrderEntity, OrderLineEntity,
│       │                            OrderRepository, OrderStatusPersistence
│       └── service/                 OrderService
└── invoice/                         (módulo)
    ├── api/                         InvoiceApi, InvoiceController
    │   └── dto/                     CreateInvoiceRequest, UpdateInvoiceRequest,
    │                                InvoiceResponse
    └── internal/
        ├── domain/                  Invoice, InvoiceStatus
        ├── entity/                  InvoiceEntity
        ├── mapper/                  InvoiceMapper
        ├── repository/              InvoiceRepository
        └── service/                 InvoiceService
```

## Reglas de frontera

- Cada módulo expone solo `api/` (contrato `*Api` + DTOs); todo lo `internal/` es privado del módulo.
- Comunicación entre módulos únicamente por sus APIs públicas; nunca accediendo a entidades/repositorios ajenos.
- Separación domain ↔ persistence dentro de cada módulo: entidades JPA (`*Entity`) no salen de `internal`; el dominio se mapea con MapStruct.
- Manejo de errores centralizado en `exception/` (compartido por todos los módulos).

## Flujo de una petición

```
HTTP → Controller (api) → Service (internal) → Repository (JPA/H2)
                     ↕ MapStruct            ↕
                    DTOs                 Domain ↔ Entity
```

## Notas

- Un solo artefacto Gradle (`settings.gradle.kts` sin subproyectos): las fronteras son convención de paquetes, no enforcement de compilación.
- Config en `src/main/resources/application.yml`; perfil dev con H2 mem y SQL visible.
