package com.raahvo.member2;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 2 - Transport & Hotel Management
 * ============================================================================
 * Class: Flight
 * Course Concepts:
 * - Inheritance (extends Transport)
 * - Runtime Polymorphism (overrides calculateFare() & getTransportDetails())
 * 
 * Purpose:
 * Specializes Transport for commercial domestic aviation (e.g. IndiGo, Air India).
 * Applies cabin class multiplier rules (Economy, Premium Economy, Business Class).
 */
public class Flight extends Transport {
    private String flightNumber;        // e.g. "6E-2041", "AI-883"
    private String airline;             // IndiGo, Air India, SpiceJet
    private int baggageAllowanceKg;     // Checked baggage limit in kilograms
    private String cabinClass;          // Economy, Premium Economy, Business

    public Flight(int transportId, String providerName, String sourceCity, String destinationCity,
                  String departureTime, String arrivalTime, double basePrice, int totalSeats,
                  int availableSeats, String flightNumber, String airline, int baggageAllowanceKg, String cabinClass) {
        super(transportId, providerName, "FLIGHT", sourceCity, destinationCity, departureTime,
                arrivalTime, basePrice, totalSeats, availableSeats);
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.baggageAllowanceKg = baggageAllowanceKg;
        this.cabinClass = cabinClass;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public int getBaggageAllowanceKg() {
        return baggageAllowanceKg;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    /**
     * Course Concept: Method Overriding (Runtime Polymorphism)
     * Airline fare multipliers:
     * - Economy: 1.0x
     * - Premium Economy: 1.4x
     * - Business Class: 2.2x
     */
    @Override
    public double calculateFare(int seats, String travelClass) {
        double multiplier = 1.0;
        if (travelClass != null && (travelClass.equalsIgnoreCase("BUSINESS") || travelClass.equalsIgnoreCase("FIRST"))) {
            multiplier = 2.2;
        } else if (travelClass != null && travelClass.equalsIgnoreCase("PREMIUM ECONOMY")) {
            multiplier = 1.4;
        }
        return getBasePrice() * seats * multiplier;
    }

    @Override
    public String getTransportDetails() {
        return String.format("[FLIGHT]%-12s | %-24s | Route: %s -> %s | Dep: %s Arr: %s | Base: ₹%.2f | Avail: %d",
                flightNumber, airline, getSourceCity(), getDestinationCity(),
                getDepartureTime(), getArrivalTime(), getBasePrice(), getAvailableSeats());
    }
}
