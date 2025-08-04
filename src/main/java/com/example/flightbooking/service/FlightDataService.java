package com.example.flightbooking.service;

import com.example.flightbooking.entity.FlightData;
import com.example.flightbooking.repository.FlightDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightDataService {
    @Autowired
    private FlightDataRepository flightDataRepository;

    // Basic CRUD Operations
    public FlightData saveFlight(FlightData flightData) {
        return flightDataRepository.save(flightData);
    }

    public List<FlightData> saveAllFlights(List<FlightData> flights) {
        return flightDataRepository.saveAll(flights);
    }

    public Optional<FlightData> getFlightById(Long id) {
        return flightDataRepository.findById(id);
    }

    public List<FlightData> getAllFlights() {
        return flightDataRepository.findAll();
    }

    public void deleteFlightById(Long id) {
        flightDataRepository.deleteById(id);
    }

    public FlightData updateFlight(FlightData flightData) {
        return flightDataRepository.save(flightData);
    }

    // Business Logic Methods

    public List<FlightData> searchBySourceAndDestination(String source, String destination) {
        return flightDataRepository.findBySourceCityAndDestinationCity(source, destination);
    }

    public List<FlightData> searchFlightsByMultipleCriteria(
            String sourceCity,
            String destinationCity,
            String flightClass,
            Double maxPrice,
            String stops,
            Double maxDuration
    ) {
        List<FlightData> flights = flightDataRepository.findBySourceCityAndDestinationCity(sourceCity, destinationCity);

        return flights.stream()
                .filter(f -> flightClass == null || f.getFlightClass().equalsIgnoreCase(flightClass))
                .filter(f -> maxPrice == null || f.getPrice() <= maxPrice)
                .filter(f -> stops == null || f.getStops().equalsIgnoreCase(stops))
                .filter(f -> maxDuration == null || f.getDuration() <= maxDuration)
                .collect(Collectors.toList());
    }

    public List<FlightData> getFlightsByAirline(String airline) {
        return flightDataRepository.findByAirline(airline);
    }

    public List<FlightData> getFlightsByClass(String flightClass) {
        return flightDataRepository.findByFlightClass(flightClass);
    }

    public List<FlightData> getDirectFlights() {
        return flightDataRepository.findByStops("zero");
    }

    public List<FlightData> getFlightsUnderPrice(Double maxPrice) {
        return flightDataRepository.findByPriceLessThan(maxPrice);
    }

    public List<FlightData> getFlightsByPriceRange(Double minPrice, Double maxPrice) {
        return flightDataRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<FlightData> getFlightsByDuration(Double maxDuration) {
        return flightDataRepository.findByDurationLessThan(maxDuration);
    }

    public List<FlightData> getFlightsByDepartureTime(String departureTime) {
        return flightDataRepository.findByDepartureTime(departureTime);
    }

    public List<FlightData> getFlightsByArrivalTime(String arrivalTime) {
        return flightDataRepository.findByArrivalTime(arrivalTime);
    }

    public List<FlightData> getFlightsByRoute(String source, String destination, String flightClass) {
        return flightDataRepository.findFlightsByRoute(source, destination, flightClass);
    }

    public List<FlightData> getFlightsByTimeSlot(String startTime, String endTime) {
        return flightDataRepository.findFlightsByTimeSlot(startTime, endTime);
    }

    public List<FlightData> getFlightsByDepartureTimeRange(String startTime, String endTime) {
        return flightDataRepository.findFlightsByDepartureTimeRange(startTime, endTime);
    }

    public List<FlightData> getFlightsByTimePattern(String timePattern) {
        return flightDataRepository.findFlightsByDepartureTimePattern(timePattern + "%");
    }

    public List<FlightData> getCheapestDirectFlights() {
        return flightDataRepository.findDirectFlightsOrderByPrice();
    }

    public List<FlightData> getShortestFlights(Double maxDuration, Integer maxStops) {
        return flightDataRepository.findShortestFlights(maxDuration, maxStops);
    }

    public List<String> getAllAirlines() {
        return flightDataRepository.findAllAirlines();
    }

    public List<FlightData> getBestDeals(Double maxPrice, Integer maxStops, Integer limit) {
        return flightDataRepository.findBestDeals(maxPrice, maxStops, limit);
    }

    public List<FlightData> getTop5CheapestFlights() {
        return flightDataRepository.findTop5ByOrderByPriceAsc();
    }

    public List<FlightData> getTop10ShortestFlights() {
        return flightDataRepository.findTop10ByOrderByDurationAsc();
    }

    public List<FlightData> getFlightsByPriceAndDuration(Double maxPrice, Double maxDuration) {
        return flightDataRepository.findFlightsByPriceAndDuration(maxPrice, maxDuration);
    }

    public List<FlightData> getFlightsByDaysLeftAndClasses(Integer minDays, Integer maxDays, List<String> classes) {
        return flightDataRepository.findFlightsByDaysLeftAndClasses(minDays, maxDays, classes);
    }

    // Count and Exists
    public Long getFlightCountByAirline(String airline) {
        return flightDataRepository.countByAirline(airline);
    }

    public Long getFlightCountByRoute(String sourceCity, String destinationCity) {
        return flightDataRepository.countBySourceCityAndDestinationCity(sourceCity, destinationCity);
    }

    public Long getFlightCountByClass(String flightClass) {
        return flightDataRepository.countByFlightClass(flightClass);
    }

    public Long getFlightCountByStops(Integer stops) {
        return flightDataRepository.countByStops(stops);
    }

    public Boolean checkFlightExists(String flightNumber) {
        return flightDataRepository.existsByFlight(flightNumber);
    }

    // Bulk Operations

    @Transactional
    public void cleanupExpensiveFlights(Double priceThreshold) {
        flightDataRepository.deleteByPriceGreaterThan(priceThreshold);
    }

    @Transactional
    public void updateFlightPrices(String airline, Double discountPercentage) {
        List<FlightData> flights = flightDataRepository.findByAirline(airline);
        for (FlightData flight : flights) {
            double newPrice = flight.getPrice() * (1 - discountPercentage / 100);
            flight.setPrice(newPrice);
        }
        flightDataRepository.saveAll(flights);
    }
}
