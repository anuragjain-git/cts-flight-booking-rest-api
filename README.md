# Flight Booking API – Spring Boot

## This readme contains explaination of all the complex terms and concepts used in this project.

This project is a RESTful Flight Booking system built with **Spring Boot**, **Spring Data JPA**, and **MySQL**. It offers endpoints for managing and querying flight data with various filters (like source, destination, class, price, duration, etc.).

---

## 📁 Structure Overview

- `entity/FlightData.java` – JPA Entity for flight data.
- `repository/FlightDataRepository.java` – Spring Data JPA repository interface.
- `service/FlightDataService.java` – Business logic layer.
- `controller/FlightDataController.java` – REST API endpoints.
  
---

**List of all the topics and subtopics** (features/concepts) covered in this project:

---

### ✅ Features / Topics Covered

- **JPA Core**
  - `@Entity`, `@Id`, `@Column` usage
  - `JpaRepository` for basic CRUD operations
  - Auto table generation and object-relational mapping

- **Spring Data JPA Queries**
  - **Derived Query Methods**
    - `findBySourceCityAndDestinationCity`
    - `findByPriceLessThan`, `findByPriceBetween`
    - `findTop5ByOrderByPriceAsc`, etc.
  - **Custom JPQL Queries**
    - `@Query` with JPQL (e.g., `findFlightsByRoute`)
    - Pattern matching (`LIKE`, time range filters)
  - **Native SQL Queries**
    - `@Query(..., nativeQuery = true)` for performance or complex joins
    - Queries with `LIMIT`, `ORDER BY`, etc.

- **Spring Boot Service Layer**
  - Business logic methods for filtering and updating
  - `@Transactional` methods for batch updates/deletes

- **Controller Layer**
  - REST APIs using `@RestController` and `@GetMapping`, `@PostMapping`, etc.
  - Handling of `Optional<FlightData>`
  - Use of `ResponseEntity.ok()`, `notFound()`, `noContent().build()`

---

## 🧠 Key Concepts & Examples

### ✅ 1. `ResponseEntity` and `Optional`

Used for flexible response handling in controllers:

```java
@GetMapping("/id/{id}")
public ResponseEntity<FlightData> getFlightById(@PathVariable Long id) {
    Optional<FlightData> flight = flightDataService.getFlightById(id);
    return flight.map(ResponseEntity::ok)
                 .orElse(ResponseEntity.notFound().build());
}
```

🔹 **Explanation**:

- `Optional<FlightData>`: Represents the presence or absence of a flight.
- `ResponseEntity.ok(flight)` = returns HTTP `200 OK` with the flight data.
- `ResponseEntity.notFound().build()` = returns HTTP `404 Not Found` if no flight exists.
- `ResponseEntity.noContent().build()` = returns HTTP `204 No Content` (used when operation succeeds but no data is returned).

---

### 📌 2. Spring Data JPA: Query Methods Without JPQL

You can write methods based on Spring naming conventions and JPA will auto-generate the SQL:

```java
List<FlightData> findBySourceCityAndDestinationCity(String source, String destination);
List<FlightData> findByPriceLessThan(Double maxPrice);
List<FlightData> findTop5ByOrderByPriceAsc();
```

➡️ No need for `@Query`. Spring understands the method name and builds the query.

---

### 🧾 3. JPQL Queries (`@Query` annotation)

Use JPQL when:

- You need custom filtering.
- You want to select specific fields.
- You need joins, aggregates, or more advanced logic.

```java
@Query("SELECT f FROM FlightData f WHERE f.sourceCity = :source AND f.destinationCity = :destination AND f.flightClass = :class")
List<FlightData> findFlightsByRoute(@Param("source") String source,
                                    @Param("destination") String destination,
                                    @Param("class") String flightClass);
```

**Another example** – using pattern matching:

```java
@Query("SELECT f FROM FlightData f WHERE f.departureTime LIKE :timePattern")
List<FlightData> findFlightsByDepartureTimePattern(@Param("timePattern") String timePattern);
```

---

### 🛠 4. Native SQL Queries

For performance or DB-specific queries:

```java
@Query(value = "SELECT * FROM flight_data WHERE price < :maxPrice AND stops <= :maxStops ORDER BY price ASC LIMIT :limit", nativeQuery = true)
List<FlightData> findBestDeals(@Param("maxPrice") Double maxPrice,
                               @Param("maxStops") Integer maxStops,
                               @Param("limit") Integer limit);
```

---

### 🧮 5. Count and Exists Methods

Spring Data provides automatic count and exists queries:

```java
Long countByAirline(String airline);
Boolean existsByFlight(String flightNumber);
```

---

### 🧹 6. Bulk Operations

Bulk deletion or updates are handled with `@Transactional`:

```java
@Transactional
void deleteByPriceGreaterThan(Double price);
```

```java
@Transactional
public void updateFlightPrices(String airline, Double discountPercentage) {
    List<FlightData> flights = flightDataRepository.findByAirline(airline);
    for (FlightData flight : flights) {
        flight.setPrice(flight.getPrice() * (1 - discountPercentage / 100));
    }
    flightDataRepository.saveAll(flights);
}
```

---

## 📖 Example API Usage

### Get flight by ID

```http
GET /flights/id/101
```

Returns:

```json
{
  "id": 101,
  "flight": "AI-101",
  "sourceCity": "Delhi",
  "destinationCity": "Mumbai",
  "price": 5200.0
}
```

---

### Search Flights by Route and Class

```http
GET /flights/search?source=Delhi&destination=Mumbai&class=Economy
```

Uses this JPQL method:

```java
@Query("SELECT f FROM FlightData f WHERE f.sourceCity = :source AND f.destinationCity = :destination AND f.flightClass = :class")
    List<FlightData> findFlightsByRoute(@Param("source") String source,
                                        @Param("destination") String destination,
                                        @Param("class") String flightClass);
```

---

## 📌 When to Use JPQL vs Derived Queries

| Use Case                                | JPQL (`@Query`) | Derived Method |
|-----------------------------------------|------------------|----------------|
| Complex filters with multiple fields    | ✅               | ❌             |
| LIKE, BETWEEN, subqueries               | ✅               | ❌             |
| Simple field = value lookups            | ❌               | ✅             |
| Top N sorting                           | ❌               | ✅ (e.g. `findTop5ByOrderByPriceAsc`) |

---

## 📦 Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- MySQL Driver
- Lombok (optional)

---
