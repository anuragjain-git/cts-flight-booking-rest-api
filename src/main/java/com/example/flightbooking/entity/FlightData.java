package com.example.flightbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "airlines_flights_data")
public class FlightData {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "airline")
    private String airline;

    @Column(name = "flight", unique = true)
    private String flight;

    @Column(name = "source_city")
    private String sourceCity;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "stops")
    private String stops;

    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column(name = "destination_city")
    private String destinationCity;

    @Column(name = "class")
    private String flightClass;

    @Column(name = "duration")
    private Double duration;

    @Column(name = "days_left")
    private Integer daysLeft;

    @Column(name = "price")
    private Double price;

    // Default constructor for JPA
    public FlightData() {
    }

    // Parameterized constructor
    public FlightData(String airline, String flight, String sourceCity,
                      String departureTime, String stops, String arrivalTime,
                      String destinationCity, String flightClass, Double duration,
                      Integer daysLeft, Double price) {
        this.airline = airline;
        this.flight = flight;
        this.sourceCity = sourceCity;
        this.departureTime = departureTime;
        this.stops = stops;
        this.arrivalTime = arrivalTime;
        this.destinationCity = destinationCity;
        this.flightClass = flightClass;
        this.duration = duration;
        this.daysLeft = daysLeft;
        this.price = price;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public String getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Integer getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(Integer daysLeft) {
        this.daysLeft = daysLeft;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

