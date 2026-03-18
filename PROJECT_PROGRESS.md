# Calories Tracker Project - Progress Documentation

## Project Overview
A Spring Boot application designed to track daily calorie intake and nutritional information for users. The application manages users, food items, meal entries, and daily logs with comprehensive nutritional tracking.

---

## Project Structure

```
Calories-Tracker/
├── pom.xml                          # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/vic/caloriestracker/
│   │   │   ├── CaloriesTrackerApplication.java    # Main Spring Boot application
│   │   │   ├── api/
│   │   │   │   ├── health/
│   │   │   │   │   └── HealthController.java      # Health check endpoint
│   │   │   │   └── root/
│   │   │   ├── food/
│   │   │   │   └── foodItem.java                  # Food item entity
│   │   │   ├── meal/
│   │   │   │   └── mealEntry.java                 # Meal entry entity
│   │   │   ├── log/
│   │   │   │   └── dailyLog.java                  # Daily log entity
│   │   │   └── user/
│   │   │       └── user.java                      # User entity
│   │   └── resources/
│   │       ├── application.properties             # Application configuration
│   │       ├── static/                            # Static files
│   │       └── templates/                         # View templates
│   └── test/
│       └── java/com/vic/caloriestracker/
│           └── CaloriesTrackerApplicationTests.java
└── target/                          # Build output
```

---

## Technology Stack

### Core Framework
- **Spring Boot** - Application framework
- **Java** - Programming language
- **Maven** - Build and dependency management

### Persistence & Database
- **Jakarta Persistence API (JPA)** - ORM framework
- **Hibernate** - JPA implementation
- **Database** - (To be configured)

### Key Dependencies
- `jakarta.persistence` - For entity management and relationships
- Spring Data JPA (inferred from structure)

---

## Entity Relationships

### Database Schema Design

```
┌─────────────────────┐
│      User           │
│─────────────────────│
│ id (PK)             │
│ email (UNIQUE)      │
│ passwordHash        │
│ name                │
│ caloriesGoal        │
│ createdAt           │
└─────────────────────┘
        ▲     ▲
        │     │
        │1:N  │1:N
        │     │
    ┌───┴─────┴──────┐
    │                │
    │                │
┌───┴──────────┐ ┌──┴──────────────┐
│  Daily Log   │ │  Meal Entry     │
├──────────────┤ ├─────────────────┤
│ id (PK)      │ │ id (PK)         │
│ userId (FK)  │ │ userId (FK)     │
│ date         │ │ foodItemId (FK) │
│ totalCal...  │ │ quantity        │
│ totalProtein │ │ mealType        │
└──────────────┘ │ loggedAt        │
                 └────────┬────────┘
                          │ N:1
                          │
                    ┌─────┴──────────┐
                    │   Food Item    │
                    ├────────────────┤
                    │ id (PK)        │
                    │ name (UNIQUE)  │
                    │ calories       │
                    │ protein        │
                    │ carbs          │
                    │ fats           │
                    │ servingSize    │
                    └────────────────┘
```

---

## Update History

### Update 1: Entity Classes Creation

**Date**: March 17, 2026

**Changes Made**:
- Created 5 core entity classes with JPA annotations
- Implemented basic field definitions with column constraints

#### 1.1 **foodItem.java**
```
Location: src/main/java/com/vic/caloriestracker/food/foodItem.java

Fields:
- id: Long (Primary Key, Auto-generated)
- name: String (Unique, Not Null)
- calories: int (Not Null)
- protein: int (Not Null)
- carbs: int (Not Null)
- fats: int (Not Null)
- servingSize: String (Not Null)

Constructors:
- Default constructor
- Full constructor with name, calories, protein, carbs, fats

Annotations Used:
@Entity - Marks as JPA entity
@Id - Primary key
@GeneratedValue - Auto-increment ID
@Column - Column constraints
```

#### 1.2 **user.java**
```
Location: src/main/java/com/vic/caloriestracker/user/user.java

Fields:
- id: Long (Primary Key, Auto-generated, Unique)
- email: String (Unique, Not Null)
- passwordHash: String (Not Null)
- name: String (Not Null)
- caloriesGoal: int (Not Null)
- createdAt: int (Not Null)

Constructors:
- Default constructor
- Full constructor with all fields except id

Annotations Used:
@Entity - Marks as JPA entity
@Id - Primary key
@GeneratedValue - Auto-increment ID
@Column - Column constraints (unique, nullable)
```

#### 1.3 **mealEntry.java**
```
Location: src/main/java/com/vic/caloriestracker/meal/mealEntry.java

Fields:
- id: Long (Primary Key, Auto-generated)
- userId: user (Foreign Key to User - @ManyToOne)
- foodItemId: foodItem (Foreign Key to FoodItem - @ManyToOne)
- quantity: int (Not Null)
- mealType: String (Not Null) - e.g., "breakfast", "lunch", "dinner"
- loggedAt: int (Not Null)

Constructors:
- Default constructor
- Full constructor with user, foodItem, quantity, mealType, loggedAt

Annotations Used:
@Entity - Marks as JPA entity
@Id - Primary key
@GeneratedValue - Auto-increment ID
@ManyToOne - Relationship to user and foodItem
@JoinColumn - Foreign key mapping
@Column - Column constraints

Relationships:
- Many meal entries can belong to one user
- Many meal entries can reference one food item
```

#### 1.4 **dailyLog.java**
```
Location: src/main/java/com/vic/caloriestracker/log/dailyLog.java

Fields:
- id: Long (Primary Key, Auto-generated)
- userId: user (Foreign Key to User - @ManyToOne)
- date: int (Not Null) - Format: YYYYMMDD
- totalCalories: int (Not Null)
- totalProtein: int (Not Null)

Constructors:
- Default constructor
- Full constructor with userId, date, totalCalories, totalProtein

Annotations Used:
@Entity - Marks as JPA entity
@Id - Primary key
@GeneratedValue - Auto-increment ID
@ManyToOne - Relationship to user
@JoinColumn - Foreign key mapping
@Column - Column constraints

Relationships:
- Many daily logs can belong to one user
- Aggregates meal entries for a specific date per user
```

#### 1.5 **HealthController.java**
```
Location: src/main/java/com/vic/caloriestracker/api/health/HealthController.java

Purpose: Health check endpoint for application monitoring

Status: Basic structure in place
```

---

### Update 2: Adding @ManyToOne Relationships

**Date**: March 17, 2026

**Changes Made**:
- Added `@ManyToOne` annotations to establish proper JPA entity relationships
- Added `@JoinColumn` annotations to define foreign key mappings
- Updated import statements for Jakarta Persistence API

#### 2.1 **mealEntry.java Changes**
```
Field Changes:
- userId: changed from Long to user (object type)
- foodItemId: changed from Long to foodItem (object type)

Constructor Update:
- Before: public mealEntry(Long userId, Long foodItemId, int quantity, String mealType, int loggedAt)
- After: public mealEntry(user userId, foodItem foodItemId, int quantity, String mealType, int loggedAt)

Annotations Added:
@ManyToOne
@JoinColumn(name = "userId", nullable = false)
private user userId;

@ManyToOne
@JoinColumn(name = "foodItemId", nullable = false)
private foodItem foodItem;

Rationale:
- Stores actual entity objects instead of just IDs
- Enables Hibernate to manage relationships automatically
- Allows cascade operations and lazy/eager loading strategies
```

#### 2.2 **dailyLog.java Changes**
```
Field Changes:
- userId: changed from Long to user (object type)

Annotations Added:
@ManyToOne
@JoinColumn(name = "userId", nullable = false)
private user userId;

Constructor Update:
- Parameters now accept user object instead of Long
- this.userId = userId now correctly assigns user object

Rationale:
- Aligns with JPA best practices for relationship mapping
- Enables proper cascade operations
- Allows automatic foreign key management by Hibernate
```

#### 2.3 **Key Technical Points**
```
Type System Correction:
- Issue: Field declared as 'user' type but constructor had Long parameter
- Solution: Changed constructor parameter type to match field type
- Pattern: @ManyToOne relationships store entity objects, not primitive IDs

Jakarta Persistence Imports:
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

Benefits of @ManyToOne:
1. Automatic foreign key constraint management
2. Lazy/eager loading of related entities
3. Cascade operations (delete, update)
4. Proper relationship navigation in code
5. Reduced boilerplate for managing relationships
```

---

## Current Status

### Completed
✅ Entity model design
✅ Primary key definitions
✅ Column constraints (unique, nullable)
✅ Relationship mapping with @ManyToOne
✅ Foreign key configuration with @JoinColumn
✅ Constructor definitions for all entities
✅ Type system alignment for relationships

### In Progress / To Do
- [ ] Repository interfaces (Spring Data JPA)
- [ ] Service layer implementation
- [ ] REST API controllers for CRUD operations
- [ ] Input validation (DTOs)
- [ ] Error handling and exception management
- [ ] Database configuration (application.properties)
- [ ] Testing suite implementation
- [ ] Logging and monitoring
- [ ] Security implementation (password hashing, authentication)
- [ ] Unit and integration tests

---

## Data Flow

```
User Request
    ↓
Controller (REST Endpoint)
    ↓
Service Layer (Business Logic)
    ↓
Repository (Data Access)
    ↓
JPA/Hibernate (ORM)
    ↓
Database (Persistence)
```

---

## Key Annotations Used

| Annotation | Purpose | Used In |
|-----------|---------|---------|
| `@Entity` | Marks class as JPA entity | All entity classes |
| `@Id` | Designates primary key | id field in all entities |
| `@GeneratedValue` | Auto-increment strategy | id field in all entities |
| `@Column` | Defines column properties | All data fields |
| `@ManyToOne` | Defines N:1 relationship | userId in mealEntry, dailyLog |
| `@JoinColumn` | Maps foreign key | userId, foodItemId in relationships |

---

## Notes & Lessons Learned

1. **Type System Consistency**: When using @ManyToOne relationships, ensure constructor parameters match the field type (object, not primitive ID)

2. **Foreign Key Mapping**: @JoinColumn(name = "columnName") explicitly defines the foreign key column name in the database

3. **Entity Independence**: Food item and User are independent entities; they're referenced by other entities

4. **Date Representation**: Using int for date (YYYYMMDD format) requires careful handling; consider LocalDate in future updates

5. **Nullable Fields**: Critical foreign keys should have `nullable = false` to maintain referential integrity

---

## Next Steps

1. Create Repository interfaces extending Spring Data JPA
2. Implement Service layer with business logic
3. Build REST API controllers with endpoints:
   - POST /api/users - Create user
   - POST /api/food-items - Add food item
   - POST /api/meal-entries - Log meal
   - GET /api/daily-logs/{userId}/{date} - Get daily summary
   - GET /api/users/{userId} - Get user details

4. Add request/response DTOs for API
5. Implement comprehensive error handling
6. Add unit and integration tests
7. Configure database connection
8. Add authentication/authorization

---

## Build & Deployment Info

**Build Tool**: Maven
**Build Command**: `mvn clean install`
**Run Command**: `mvn spring-boot:run`

---

**Last Updated**: March 17, 2026
**Project Status**: Entity modeling phase complete
**Next Phase**: Repository & Service layer implementation

