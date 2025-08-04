package com.example.flightbooking.controller;

import com.example.flightbooking.entity.FlightData;
import com.example.flightbooking.service.FlightDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flights") // optional base path
public class FlightDataController {
    @Autowired
    private FlightDataService flightDataService;

    // Create a single flight
    @PostMapping("/flights")
    public ResponseEntity<FlightData> createFlight(@RequestBody FlightData flightData) {
        FlightData savedFlight = flightDataService.saveFlight(flightData);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFlight);
    }

    // Create multiple flights
    @PostMapping("/flights/bulk")
    public ResponseEntity<List<FlightData>> createMultipleFlights(@RequestBody List<FlightData> flights) {
        List<FlightData> savedFlights = flightDataService.saveAllFlights(flights);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFlights);
    }

    // Get All Flights
    @GetMapping
    public List<FlightData> getAllFlights() {
        return flightDataService.getAllFlights();
    }

    // Get Flights by id
    /**
     * Retrieves a flight by its ID using the FlightDataService.
     *
     * - Optional<FlightData>: The service returns an Optional, which may or may not contain a flight.
     *   This is used to avoid null checks and handle the absence of data in a clean way.
     *
     * - ResponseEntity<FlightData>:
     *     - Wraps the response with both data and HTTP status code.
     *     - If the flight is found, returns 200 OK with the flight data.
     *     - If not found, returns 404 Not Found with no body.
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<FlightData> getFlightById(@PathVariable Long id) {
        Optional<FlightData> flight = flightDataService.getFlightById(id);
        return flight.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Delete flight by ID
    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightDataService.deleteFlightById(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }

    // Update existing flight
    @PutMapping("/flights")
    public ResponseEntity<FlightData> updateFlight(@RequestBody FlightData flightData) {
        FlightData updated = flightDataService.updateFlight(flightData);
        return ResponseEntity.ok(updated);
    }

    // Search flights by source and destination



    @GetMapping("/search")
    public List<FlightData> searchFlights(@RequestParam String source, @RequestParam String destination) {
        return flightDataService.searchBySourceAndDestination(source, destination);
    }

    // Search flights by multiple criteria (sourceCity and destinationCity required)
    @GetMapping("/search/advance")
    public ResponseEntity<List<FlightData>> searchFlights(
            @RequestParam String sourceCity,
            @RequestParam String destinationCity,
            @RequestParam(required = false) String flightClass,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String stops,
            @RequestParam(required = false) Double maxDuration
    ) {
        List<FlightData> flights = flightDataService.searchFlightsByMultipleCriteria(
                sourceCity, destinationCity, flightClass, maxPrice, stops, maxDuration);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/airline/{airline}")
    public ResponseEntity<List<FlightData>> getFlightsByAirline(@PathVariable String airline) {
        List<FlightData> flights = flightDataService.getFlightsByAirline(airline);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/class/{flightClass}")
    public ResponseEntity<List<FlightData>> getFlightsByClass(@PathVariable String flightClass) {
        List<FlightData> flights = flightDataService.getFlightsByClass(flightClass);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/direct")
    public ResponseEntity<List<FlightData>> getDirectFlights() {
        List<FlightData> flights = flightDataService.getDirectFlights();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/price/under/{maxPrice}")
    public ResponseEntity<List<FlightData>> getFlightsUnderPrice(@PathVariable Double maxPrice) {
        List<FlightData> flights = flightDataService.getFlightsUnderPrice(maxPrice);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/price/range")
    public ResponseEntity<List<FlightData>> getFlightsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<FlightData> flights = flightDataService.getFlightsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/duration/under/{maxDuration}")
    public ResponseEntity<List<FlightData>> getFlightsByDuration(@PathVariable Double maxDuration) {
        List<FlightData> flights = flightDataService.getFlightsByDuration(maxDuration);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/departure-time/{time}")
    public ResponseEntity<List<FlightData>> getFlightsByDepartureTime(@PathVariable String time) {
        List<FlightData> flights = flightDataService.getFlightsByDepartureTime(time);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/arrival-time/{time}")
    public ResponseEntity<List<FlightData>> getFlightsByArrivalTime(@PathVariable String time) {
        List<FlightData> flights = flightDataService.getFlightsByArrivalTime(time);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/route")
    public ResponseEntity<List<FlightData>> getFlightsByRoute(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(required = false) String flightClass) {
        List<FlightData> flights;
        if (flightClass != null && !flightClass.isEmpty()) {
            flights = flightDataService.getFlightsByRoute(source, destination, flightClass);
        } else {
            flights = flightDataService.searchBySourceAndDestination(source, destination);
        }
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/time-slot")
    public ResponseEntity<List<FlightData>> getFlightsByTimeSlot(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        List<FlightData> flights = flightDataService.getFlightsByTimeSlot(startTime, endTime);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/departure-range")
    public ResponseEntity<List<FlightData>> getFlightsByDepartureTimeRange(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        List<FlightData> flights = flightDataService.getFlightsByDepartureTimeRange(startTime, endTime);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/time-pattern/{pattern}")
    public ResponseEntity<List<FlightData>> getFlightsByTimePattern(@PathVariable String pattern) {
        List<FlightData> flights = flightDataService.getFlightsByTimePattern(pattern);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/cheapest-direct")
    public ResponseEntity<List<FlightData>> getCheapestDirectFlights() {
        List<FlightData> flights = flightDataService.getCheapestDirectFlights();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/shortest")
    public ResponseEntity<List<FlightData>> getShortestFlights(
            @RequestParam(defaultValue = "10.0") Double maxDuration,
            @RequestParam(defaultValue = "1") Integer maxStops) {
        List<FlightData> flights = flightDataService.getShortestFlights(maxDuration, maxStops);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/airlines")
    public ResponseEntity<List<String>> getAllAirlines() {
        List<String> airlines = flightDataService.getAllAirlines();
        return ResponseEntity.ok(airlines);
    }

    @GetMapping("/deals")
    public ResponseEntity<List<FlightData>> getBestDeals(
            @RequestParam(defaultValue = "10000.0") Double maxPrice,
            @RequestParam(defaultValue = "1") Integer maxStops,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<FlightData> flights = flightDataService.getBestDeals(maxPrice, maxStops, limit);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/top5-cheapest")
    public ResponseEntity<List<FlightData>> getTop5CheapestFlights() {
        List<FlightData> flights = flightDataService.getTop5CheapestFlights();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/top10-shortest")
    public ResponseEntity<List<FlightData>> getTop10ShortestFlights() {
        List<FlightData> flights = flightDataService.getTop10ShortestFlights();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/price-duration")
    public ResponseEntity<List<FlightData>> getFlightsByPriceAndDuration(
            @RequestParam Double maxPrice,
            @RequestParam Double maxDuration) {
        List<FlightData> flights = flightDataService.getFlightsByPriceAndDuration(maxPrice, maxDuration);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/filter/days-classes")
    public ResponseEntity<List<FlightData>> getFlightsByDaysLeftAndClasses(
            @RequestParam Integer minDays,
            @RequestParam Integer maxDays,
            @RequestParam String classes) {
        List<String> classList = Arrays.asList(classes.split(","));
        List<FlightData> flights = flightDataService.getFlightsByDaysLeftAndClasses(minDays, maxDays, classList);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/count/airline/{airline}")
    public ResponseEntity<Long> getFlightCountByAirline(@PathVariable String airline) {
        Long count = flightDataService.getFlightCountByAirline(airline);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/route")
    public ResponseEntity<Long> getFlightCountByRoute(
            @RequestParam String source,
            @RequestParam String destination) {
        Long count = flightDataService.getFlightCountByRoute(source, destination);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/class/{flightClass}")
    public ResponseEntity<Long> getFlightCountByClass(@PathVariable String flightClass) {
        Long count = flightDataService.getFlightCountByClass(flightClass);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/stops/{stops}")
    public ResponseEntity<Long> getFlightCountByStops(@PathVariable Integer stops) {
        Long count = flightDataService.getFlightCountByStops(stops);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists/{flightNumber}")
    public ResponseEntity<Boolean> checkFlightExists(@PathVariable String flightNumber) {
        Boolean exists = flightDataService.checkFlightExists(flightNumber);
        return ResponseEntity.ok(exists);
    }

    // Bulk Operations

    @DeleteMapping("/cleanup/expensive/{priceThreshold}")
    public ResponseEntity<String> cleanupExpensiveFlights(@PathVariable Double priceThreshold) {
        flightDataService.cleanupExpensiveFlights(priceThreshold);
        return ResponseEntity.ok("Expensive flights cleaned successfully");
    }

    @PutMapping("/update-prices/{airline}")
    public ResponseEntity<String> updateFlightPrices(
            @PathVariable String airline,
            @RequestParam Double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            return ResponseEntity.badRequest().body("Discount percentage must be between 0 and 100");
        }
        flightDataService.updateFlightPrices(airline, discountPercentage);
        return ResponseEntity.ok("Prices updated for airline " + airline);
    }
}
