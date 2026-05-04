# AGENTS Guide

## Project snapshot
- Spring Boot 4 + Java 21 backend for calorie tracking (`pom.xml`, `src/main/java/com/vic/caloriestracker`).
- Current API surface includes health, food catalog, meal logging, and daily log endpoints; food reads return entities directly while meal/log endpoints use DTOs.
- Data access now flows `Controller -> Service -> Repository -> JPA/Hibernate -> Database`.

## Architecture and data flow
- Entry point: `src/main/java/com/vic/caloriestracker/CaloriesTrackerApplication.java`.
- HTTP controllers and DTOs live in `src/main/java/com/vic/caloriestracker/api/**`.
- Persistence model is in `src/main/java/com/vic/caloriestracker/entity/**` with JPA annotations.
- Repository interfaces are in `src/main/java/com/vic/caloriestracker/repository/**` and rely on Spring Data method naming.
- Service classes are in `src/main/java/com/vic/caloriestracker/service/**` and centralize meal/daily-log business logic.
- Startup seed path: `config/DatabaseSeeder.java` runs on app boot and inserts a demo `user` plus sample `foodItem` rows if the tables are empty.

## Key endpoints and behavior
- `GET /` returns `OK` (`api/health/HealthController.java`).
- `GET /api/foods` returns all food items; `POST /api/foods` creates a food item with validation.
- `GET /api/foods/search?q=...` uses `findByNameContainingIgnoreCase`.
- `GET /api/foods/{id}` returns 404 via `ResponseStatusException` if not found.
- `POST /api/meals` logs a meal from `LogMealRequest`; `GET /api/meals/today?userId=...` returns today's meal entries; `DELETE /api/meals/{id}` removes an entry and recalculates the daily log.
- `GET /api/logs/{date}?userId=...` returns the daily aggregate from `DailyLogResponse`.

## Conventions specific to this repo
- Class names are intentionally lowercase in entities/repositories (`foodItem`, `user`, `mealEntry`, `dailyLog`, `foodItemRepository`). Keep naming consistent unless doing a deliberate refactor across all usages.
- `mealEntry.userId`, `mealEntry.foodItem`, and `dailyLog.userId` are entity references (`@ManyToOne`), not primitive IDs.
- `dailyLog.date` and `mealEntry.loggedAt` are currently `int`-based; preserve compatibility unless migrating schema + API together.
- `foodItem.name` is unique; seeder assumes duplicate-protected inserts.
- Request/response DTOs live beside their controllers in `api/<domain>/` (for example `api/meal/LogMealRequest.java`, `MealEntryResponse.java`, and `api/log/DailyLogResponse.java`).
- Repository query methods should use nested property paths that match relations, for example `findByUserId_IdAndLoggedAt(Long userId, int loggedAt)` and `findByUserId_IdAndDate(Long userId, int date)`.

## Security and config realities
- `config/Security.java` currently permits all requests and disables CSRF; `spring-boot-starter-security` is present, but do not assume protected endpoints.
- Default datasource points to local PostgreSQL (`src/main/resources/application.properties`), with `spring.jpa.hibernate.ddl-auto=update` and SQL logging enabled; `pom.xml` also includes the PostgreSQL and H2 drivers.
- Treat credentials in `src/main/resources/application.properties` as sensitive; avoid reprinting/propagating secrets in commits or logs.

## Dev workflows (project-verified layout)
- Use Maven wrapper from project root (`./mvnw`) for consistent builds.
- Typical commands:
  - `./mvnw clean test`
  - `./mvnw spring-boot:run`
  - `./mvnw clean package`
- If startup or tests fail, first check local PostgreSQL reachability and credentials from `src/main/resources/application.properties` because the default datasource is not H2.

## High-leverage edit strategy for agents
- For new API features, follow existing placement: controller and any request/response DTOs in `api/<domain>/`, entity in `entity/`, repository query methods in `repository/`.
- When adding repository methods over relations, align method names with entity field names (e.g., current `findByUserId(Long userId)` targets a `user`-typed field and may need revisiting).
- Add migration-safe changes: this project relies on Hibernate auto-update, so incompatible column/type changes can break local DB state.
- Keep `DatabaseSeeder` in sync with `foodItem` required fields when entity shape changes.
- When changing meal logging or deletion, preserve the recalculation path through `MealEntryService` and `DailyLogService` so `/api/logs/{date}` stays consistent.

