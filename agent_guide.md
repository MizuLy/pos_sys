# Agent Guide — pos_sys

This guide is for AI agents (and developers) working on this project. Read it before
modifying code. It documents the tech stack, structure, conventions, and known quirks.

## 1. Project Overview

`pos_sys` is a Point-of-Sale (POS) **REST API backend** built with Spring Boot. It is a
learning/demonstration project that mixes several data-access styles on purpose:

- **JPA + Repository** (Product, Table) — full `JpaRepository` CRUD
- **Raw JDBC + `JdbcTemplate`** (Category, Cashier) — hand-written SQL

There is **no service layer, no authentication, and no global exception handler**. It is a
pure backend; the frontend is expected to live elsewhere (a React app served on
`localhost:3000` per the CORS config).

## 2. Tech Stack

| Concern | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot `4.0.8-SNAPSHOT` |
| Build | Maven (wrapper `mvnw` / `mvnw.cmd` provided) |
| Web | `spring-boot-starter-webmvc` |
| Validation | `spring-boot-starter-validation` (`@Valid`, JSR-303) |
| Persistence | JPA/Hibernate (`spring-boot-starter-data-jpa`) + `spring-boot-starter-jdbc` |
| Database | MySQL 8 (`mysql-connector-j`, runtime scope) |
| API Docs | `springdoc-openapi-starter-webmvc-ui` v3.0.2 (Swagger UI) |
| Boilerplate | Lombok (`@Data`, etc.) — configured as an annotation processor in `pom.xml` |
| Dev | `spring-boot-devtools` (runtime, optional) |
| Tests | `spring-boot-starter-webmvc-test`, `spring-boot-starter-validation-test` (JUnit 5) |

Base package: `com.example.pos_sys`

## 3. Prerequisites & How to Run

**Database:** The app expects MySQL on **port 3308** with:

- URL: `jdbc:mysql://localhost:3308/db_2_5`
- User: `root` / Password: `rootpass`
- `spring.jpa.hibernate.ddl-auto=update` → Hibernate creates/updates entity tables automatically.

A MySQL + phpMyAdmin Docker stack is provided in `Docker/docker-compose.yml`
(MySQL on `3308`, phpMyAdmin on `http://localhost:8082`). NOTE: the compose file creates
database `lessons_db`, but the app connects to `db_2_5` — you must create the `db_2_5`
database yourself (e.g. via phpMyAdmin) before first run.

**Run the app:**

```bash
./mvnw spring-boot:run        # or on Windows: mvnw.cmd spring-boot:run
```

- Default port: **8080**
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

**Tests:** `./mvnw test` (only a `contextLoads` smoke test exists — it requires the DB
to be up because `@SpringBootTest` boots the full context).

## 4. Project Structure

```
src/main/java/com/example/pos_sys/
├── PosSysApplication.java          # @SpringBootApplication entry point
├── config/
│   └── WebConfig.java              # CORS: allows http://localhost:3000 and 127.0.0.1:3000
├── controllers/
│   ├── CategoryController.java     # /api/category   — JdbcTemplate (raw SQL)
│   ├── ProductController.java      # /api/product    — JPA + DTOs + Mapper
│   ├── TableController.java        # /api/tables     — JPA, entity used directly as DTO
│   └── CashierController.java      # /api/cashier    — JdbcTemplate, body as Map<String,Object>
├── dtos/products/
│   ├── ProductRequestDTO.java      # Input validation for product create/update
│   └── ProductResponseDTO.java     # Output shape for product responses
├── enums/
│   └── TableEnum.java              # AVAILABLE | OCCUPIED
├── mappers/
│   └── ProductMapper.java          # Entity <-> DTO conversion + category FK resolution
├── models/
│   ├── Category.java               # tb_categories
│   ├── Product.java                # tb_products
│   └── Table.java                  # tb_table (note: name collides with java.sql.Table)
└── repositories/
    ├── CategoryRepository.java     # JpaRepository<Category, Long>
    ├── ProductRepository.java      # JpaRepository<Product, Long>
    └── TableRepository.java        # JpaRepository<Table, Long>
```

Root-level files: `index.html`, `test.html` are throwaway front-end mockups for manual
API testing, not part of the app.

## 5. Database Schema

Tables created automatically by Hibernate (`ddl-auto=update`):

**`tb_categories`**
| column | type | constraints |
|---|---|---|
| id | BIGINT | PK, auto-increment |
| category_name | VARCHAR(255) | @NotBlank, max 50 |

**`tb_products`**
| column | type | constraints |
|---|---|---|
| id | BIGINT | PK, auto-increment |
| product_name | VARCHAR(100) | not null |
| category_id | BIGINT | FK → tb_categories.id, not null |
| price | DECIMAL(12) | not null |

**`tb_table`** (JPA table name is `tb_table` even though the class is `Table`)
| column | type | constraints |
|---|---|---|
| id | BIGINT | PK, auto-increment |
| table_name | VARCHAR(50) | not empty |
| status | VARCHAR(20) | enum string, default `AVAILABLE` |

**`tb_cashiers`** — NOT a JPA entity; table must exist in DB manually. Referenced only by
raw SQL in `CashierController`. Expected columns: `id`, `fullname`, `phone`, `username`.

## 6. API Reference

All controllers return `Map<String, Object>` (JSON), not typed response bodies.

### Category — `/api/category` (JdbcTemplate)
- `GET` → list all categories → `{status: 200, data: [...]}`
- `POST` → insert, body `{category_name}` → `{message: "Create success"}`
- `PUT /{id}` → update, body `{category_name}` → `{message: "Update success"}`
- `DELETE /{id}` → `{message: "Delete success"}`

### Product — `/api/product` (JPA + DTOs + Mapper)
- `GET` → list all → `{data: [...]}` (⚠️ **no `status` key — see Quirks**)
- `GET /{id}` → one → `{status: "success", data: {...}}` or `{status: "Error", message: "..."}`
- `POST` → body `{product_name, category_id, price}` → `{message: "...", data: {...}}`
- `PUT /{id}` → same body → `{message: "...", data: {...}}`
- `DELETE /{id}` → `{message: "..."}`

Product response shape (`ProductResponseDTO`): `{id, product_name, category_id, category_name, price}`

Product request validation (`ProductRequestDTO`): `product_name` @NotBlank ≤100 chars,
`category_id` @NotNull, `price` @NotNull, `@Digits(integer=10, fraction=2)`, `> 0.0`.

### Table — `/api/tables` (JPA, entity used directly)
- `GET` → `{status: "success", data: [...]}`
- `GET /{id}` → `{status: 200, data: {...}}` or `{status: 404, message: "Table not found"}`
- `POST` → body `{table_name, status?}` → `{message: "...", data: {...}}`
- `PUT /{id}` → same body → `{message: "...", data: {...}}` (404 → `{message: "Table not found"}`)
- `DELETE /{id}` → `{message: "..."}`

### Cashier — `/api/cashier` (JdbcTemplate, free-form Map body)
- `GET` → list all → `{status: "success", data: [...]}`
- `POST` → body `{fullname, phone, username}` → `{message: "Create success"}`
- `PUT /{id}` → body `{fullname, phone, username}` → `{message: "Update success"}`
- `DELETE /{id}` → `{message: "Delete success"}`

## 7. Conventions (follow these when editing)

1. **Controllers return `Map<String, Object>`.** Success shapes use a `data` key; CRUD
   messages use a `message` key. Do not introduce a shared response class unless the user
   explicitly asks.
2. **Two data-access styles coexist.** Keep the existing style per controller:
   - Product/Table → constructor-inject `JpaRepository` beans, use optional
     `findById()` + `existsById()` guards, and `Map.of(...)` for small responses.
   - Category/Cashier → constructor-inject `JdbcTemplate`, write raw SQL with `?`
     placeholders, and `HashMap`/`Map.of` for responses.
3. **Validation:** annotate DTOs/entities with `jakarta.validation` constraints and add
   `@Valid` on `@RequestBody` parameters. `@NotBlank`/`@NotNull`/`@Size`/`@Digits`/
   `@DecimalMin` are already in use.
4. **Naming:** DB columns and entity fields use **snake_case** (e.g. `product_name`,
   `category_id`). Match this style in new fields.
5. **Lombok:** use `@Data` on models/DTOs. For `@ManyToOne` associations, exclude the
   association from `@ToString`/`@EqualsAndHashCode` (see `Product`).
6. **FK resolution:** when a request DTO carries a foreign key, resolve it through the
   repository in a `Mapper` component and throw `EntityNotFoundException` on missing rows
   (see `ProductMapper.resolveCategory`).
7. **Swagger:** tag each controller with `@Tag(name = "...", description = "...")`.
8. **CORS:** registered in `config/WebConfig` for the React dev origins. Extend
   `allowedOrigins` if the frontend runs elsewhere.

## 8. Known Quirks & Caveats

- **`ProductController.getAll()` bug:** it does `res.put("data", "success")` then
  `res.put("data", data)` — the first put is dead code, and the response has **no
  `status` key** (unlike every other list endpoint).
- **Inconsistent error shapes:** status is sometimes an `int` (`404`) and sometimes a
  string (`"Error"`, `"success"`). Table's `getAll` returns `"success"` but `getOne`
  returns `200`. Don't assume a single shape.
- **No global exception handling.** Validation errors and unexpected exceptions bubble up
  as Spring default responses, not the app's `{status, message}` shape. There is no
  `@RestControllerAdvice`.
- **`tb_cashiers` is not an entity.** Hibernate will not create it. Creating/altering it is
  manual SQL only.
- **Entity field naming** is snake_case, which deviates from standard Java camelCase —
  preserve it rather than "fixing" it unless asked.
- **DB name mismatch:** app connects to `db_2_5`, docker compose seeds `lessons_db`.
- **Spring Boot 4.0.8-SNAPSHOT** — a snapshot release from the Spring snapshots repo
  (`repo.spring.io/snapshot`). Keep versions aligned with the parent POM.

## 9. Recommended Practices for Agents

- When adding a new feature, mirror the nearest existing controller (JPA vs JDBC style)
  and reuse `ProductMapper` patterns if a DTO needs FK resolution.
- Verify changes with `./mvnw test` and, when possible, a manual curl against the running
  app. The DB must be up first (see section 3).
- Never add secrets to `application.properties`; credentials are dev-only local defaults.
- Do not restructure the package layout or introduce a service/exception layer without
  asking the user — this is a deliberate teaching-style project.
