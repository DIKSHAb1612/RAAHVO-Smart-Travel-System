package com.raahvo.member2;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 2 - Transport & Hotel Management
 * ============================================================================
 * Class: Bus
 * Course Concepts:
 * - Inheritance (extends Transport)
 * - Runtime Polymorphism (overrides calculateFare() & getTransportDetails())
 * 
 * Purpose:
 * Specializes Transport for intercity buses (e.g. Volvo Multi-Axle, Sleeper, Electric).
 * Adjusts ticket price based on AC amenities and sleeper configurations.
 */
public class Bus extends Transport {
    private String busNumber;
    private String busType;   // Volvo Multi-Axle, Sleeper, Semi-Sleeper, AC Seater
    private boolean isAc;
    private boolean hasWifi;

    public Bus(int transportId, String providerName, String sourceCity, String destinationCity,
               String departureTime, String arrivalTime, double basePrice, int totalSeats,
               int availableSeats, String busNumber, String busType, boolean isAc, boolean hasWifi) {
        super(transportId, providerName, "BUS", sourceCity, destinationCity, departureTime,
                arrivalTime, basePrice, totalSeats, availableSeats);
        this.busNumber = busNumber;
        this.busType = busType;
        this.isAc = isAc;
        this.hasWifi = hasWifi;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getBusType() {
        return busType;
    }

    public boolean isAc() {
        return isAc;
    }

    public boolean hasWifi() {
        return hasWifi;
    }

    /**
     * Course Concept: Method Overriding (Runtime Polymorphism)
     * Calculates bus fare:
     * - AC surcharge: +25%
     * - Sleeper berth surcharge: +20%
     */
    @Override
    public double calculateFare(int seats, String travelClass) {
        double multiplier = isAc ? 1.25 : 1.0;
        if (busType != null && busType.toUpperCase().contains("SLEEPER")) {
            multiplier += 0.20;
        }
        return getBasePrice() * seats * multiplier;
    }

    @Override
    public String getTransportDetails() {
        return String.format("[BUS]   %-12s | %-24s (%s) | %s -> %s | Dep: %s Arr: %s | Base: ₹%.2f | Avail: %d",
                busNumber, getProviderName(), busType, getSourceCity(), getDestinationCity(),
                getDepartureTime(), getArrivalTime(), getBasePrice(), getAvailableSeats());
    }
}
