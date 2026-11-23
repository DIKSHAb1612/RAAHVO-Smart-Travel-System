package com.raahvo.member2;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 2 - Transport & Hotel Management
 * ============================================================================
 * Abstract Class: Transport
 * Course Concepts:
 * - Abstraction: Abstract class defining common vehicle contract
 * - Polymorphism: Abstract calculateFare() method overridden by subclasses
 * - Multithreading & Synchronization: Thread-safe synchronized seat reservation
 * 
 * Purpose:
 * Serves as the foundation for multi-modal travel options (Trains, Buses, Flights).
 * Holds common attributes such as routes, timings, prices, and seat counts.
 */
public abstract class Transport {
    private int transportId;
    private String providerName;      // e.g., Indian Railways, Air India, Zingbus
    private String transportType;     // TRAIN, BUS, FLIGHT
    private String sourceCity;
    private String destinationCity;
    private String departureTime;     // 24-hr format (e.g., "06:00")
    private String arrivalTime;       // 24-hr format (e.g., "15:30")
    private double basePrice;
    private int totalSeats;
    private int availableSeats;

    public Transport(int transportId, String providerName, String transportType,
                     String sourceCity, String destinationCity, String departureTime,
                     String arrivalTime, double basePrice, int totalSeats, int availableSeats) {
        this.transportId = transportId;
        this.providerName = providerName;
        this.transportType = transportType.toUpperCase();
        this.sourceCity = sourceCity;
        this.destinationCity = destinationCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.basePrice = basePrice;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    /**
     * Course Concept: Runtime Polymorphism
     * Abstract method that must be specialized by Train, Bus, and Flight.
     * Calculates final fare based on passenger count and chosen travel class/tier.
     * 
     * @param seats Number of passengers/seats requested
     * @param travelClass Class multiplier (e.g., Sleeper, AC 3-Tier, Business, Economy)
     * @return Total calculated base fare
     */
    public abstract double calculateFare(int seats, String travelClass);

    /**
     * Course Concept: Polymorphism
     * Formats transport specifics for concise console listings.
     */
    public abstract String getTransportDetails();

    // ========================================================================
    // GETTERS & SETTERS
    // ========================================================================

    public int getTransportId() {
        return transportId;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getTransportType() {
        return transportType;
    }

    public String getSourceCity() {
        return sourceCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public synchronized void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    /**
     * Course Concept: Multithreading & Synchronization
     * Critical Section: The 'synchronized' keyword enforces an intrinsic monitor lock
     * on the Transport object. When multiple booking threads execute concurrently,
     * this guarantees that only one thread can inspect and decrement available seats
     * at a time, completely preventing race conditions and double-booking bugs!
     * 
     * @param count Number of seats to deduct
     * @return true if seats were atomically reserved, false if insufficient seats
     */
    public synchronized boolean deductSeats(int count) {
        if (this.availableSeats >= count) {
            this.availableSeats -= count;
            return true;
        }
        return false;
    }

    /**
     * Course Concept: Thread-Safe Seat Rollback
     * Safely releases seats back to the inventory upon booking cancellation.
     */
    public synchronized void releaseSeats(int count) {
        this.availableSeats = Math.min(this.totalSeats, this.availableSeats + count);
    }
}
