# AGENTS Guide

## Project snapshot
- Spring Boot 4 + Java 21 backend for calorie tracking (`pom.xml`, `src/main/java/com/vic/caloriestracker`).
- Current API surface is minimal: health endpoint and food catalog read endpoints.
- Data access is direct `Controller -> JpaRepository` (no service layer yet).

## Architecture and data flow
- Entry point: `src/main/java/com/vic/caloriestracker/CaloriesTrackerApplication.java`.
- HTTP controllers live in `src/main/java/com/vic/caloriestracker/api/**`.
- Persistence model is in `src/main/java/com/vic/caloriestracker/entity/**` with JPA annotations.
- Repository interfaces are in `src/main/java/com/vic/caloriestracker/repository/**` and rely on Spring Data method naming.
- Startup seed path: `config/DatabaseSeeder.java` runs on app boot and inserts sample `foodItem` rows if table is empty.

## Key endpoints and behavior
- `GET /` returns `OK` (`api/health/HealthController.java`).
- `GET /api/foods` returns all food items.
- `GET /api/foods/search?q=...` uses `findByNameContainingIgnoreCase`.
- `GET /api/foods/{id}` throws `RuntimeException` if not found (no global exception handler yet).

## Conventions specific to this repo
- Class names are intentionally lowercase in entities/repositories (`foodItem`, `user`, `mealEntry`, `dailyLog`, `foodItemRepository`). Keep naming consistent unless doing a deliberate refactor across all usages.
- `mealEntry.userId` and `dailyLog.userId` are entity references (`@ManyToOne user`), not primitive IDs.
- `dailyLog.date` and `mealEntry.loggedAt` are currently `int`-based; preserve compatibility unless migrating schema + API together.
- `foodItem.name` is unique; seeder assumes duplicate-protected inserts.

## Security and config realities
- `config/Security.java` currently permits all requests and disables CSRF; do not assume protected endpoints.
- Default datasource points to local PostgreSQL (`application.properties`), with `spring.jpa.hibernate.ddl-auto=update` and SQL logging enabled.
- Treat credentials in `src/main/resources/application.properties` as sensitive; avoid reprinting/propagating secrets in commits or logs.

## Dev workflows (project-verified layout)
- Use Maven wrapper from project root (`./mvnw`) for consistent builds.
- Typical commands:
  - `./mvnw clean test`
  - `./mvnw spring-boot:run`
  - `./mvnw clean package`
- If startup fails, first check DB reachability and credentials from `application.properties` because tests/app context may depend on datasource settings.

## High-leverage edit strategy for agents
- For new API features, follow existing placement: controller in `api/<domain>/`, entity in `entity/`, repository query methods in `repository/`.
- When adding repository methods over relations, align method names with entity field names (e.g., current `findByUserId(Long userId)` targets a `user`-typed field and may need revisiting).
- Add migration-safe changes: this project relies on Hibernate auto-update, so incompatible column/type changes can break local DB state.
- Keep `DatabaseSeeder` in sync with `foodItem` required fields when entity shape changes.

