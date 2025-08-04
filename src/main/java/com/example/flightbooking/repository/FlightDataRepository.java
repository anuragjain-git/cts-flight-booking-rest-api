package com.example.flightbooking.repository;

import com.example.flightbooking.entity.FlightData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FlightDataRepository extends JpaRepository<FlightData, Long> {
    List<FlightData> findBySourceCityAndDestinationCity(String source, String destination);

    List<FlightData> findByAirline(String airline);

    List<FlightData> findByFlightClass(String flightClass);

    List<FlightData> findByStops(String stops);

    List<FlightData> findByPriceLessThan(Double maxPrice);

    List<FlightData> findByPriceBetween(Double minPrice, Double maxPrice);

    List<FlightData> findByDurationLessThan(Double maxDuration);

    List<FlightData> findByDepartureTime(String departureTime);

    List<FlightData> findByArrivalTime(String arrivalTime);

    // 2. Custom JPQL Queries
    @Query("SELECT f FROM FlightData f WHERE f.sourceCity = :source AND f.destinationCity = :destination AND f.flightClass = :class")
    List<FlightData> findFlightsByRoute(@Param("source") String source,
                                        @Param("destination") String destination,
                                        @Param("class") String flightClass);

    @Query("SELECT f FROM FlightData f WHERE f.departureTime LIKE :timePattern")
    List<FlightData> findFlightsByDepartureTimePattern(@Param("timePattern") String timePattern);

    @Query("SELECT f FROM FlightData f WHERE f.departureTime >= :startTime AND f.departureTime <= :endTime")
    List<FlightData> findFlightsByDepartureTimeRange(@Param("startTime") String startTime,
                                                     @Param("endTime") String endTime);

    @Query("SELECT f FROM FlightData f WHERE f.stops = 0 ORDER BY f.price ASC")
    List<FlightData> findDirectFlightsOrderByPrice();

    @Query("SELECT DISTINCT f.airline FROM FlightData f ORDER BY f.airline")
    List<String> findAllAirlines();

    @Query("SELECT f FROM FlightData f WHERE f.price = (SELECT MIN(fd.price) FROM FlightData fd WHERE fd.sourceCity = f.sourceCity AND fd.destinationCity = f.destinationCity)")
    List<FlightData> findCheapestFlightsPerRoute();

    @Query("SELECT f FROM FlightData f WHERE f.duration <= :maxDuration AND f.stops <= :maxStops ORDER BY f.duration ASC")
    List<FlightData> findShortestFlights(@Param("maxDuration") Double maxDuration, @Param("maxStops") Integer maxStops);

    // 3. Native SQL Queries
    @Query(value = "SELECT * FROM flight_data WHERE price < :maxPrice AND stops <= :maxStops ORDER BY price ASC LIMIT :limit", nativeQuery = true)
    List<FlightData> findBestDeals(@Param("maxPrice") Double maxPrice,
                                   @Param("maxStops") Integer maxStops,
                                   @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM flight_data WHERE departure_time BETWEEN :startTime AND :endTime ORDER BY departure_time", nativeQuery = true)
    List<FlightData> findFlightsByTimeSlot(@Param("startTime") String startTime, @Param("endTime") String endTime);

    // 4. Advanced Query Methods
    List<FlightData> findTop5ByOrderByPriceAsc();

    List<FlightData> findTop10ByOrderByDurationAsc();

    @Query("SELECT f FROM FlightData f WHERE f.daysLeft BETWEEN :minDays AND :maxDays AND f.flightClass IN :classes")
    List<FlightData> findFlightsByDaysLeftAndClasses(@Param("minDays") Integer minDays,
                                                     @Param("maxDays") Integer maxDays,
                                                     @Param("classes") List<String> classes);

    @Query("SELECT f FROM FlightData f WHERE f.price <= :maxPrice AND f.duration <= :maxDuration")
    List<FlightData> findFlightsByPriceAndDuration(@Param("maxPrice") Double maxPrice,
                                                   @Param("maxDuration") Double maxDuration);

    // 5. Count and Exists Methods
    Long countByAirline(String airline);

    Boolean existsByFlight(String flight);

    Long countBySourceCityAndDestinationCity(String sourceCity, String destinationCity);

    Long countByFlightClass(String flightClass);

    Long countByStops(Integer stops);

    // 6. Delete Methods

    @Transactional
    void deleteByPriceGreaterThan(Double price);

}

