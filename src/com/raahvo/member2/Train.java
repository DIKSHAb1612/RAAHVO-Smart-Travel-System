package com.raahvo.member2;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 2 - Transport & Hotel Management
 * ============================================================================
 * Class: Train
 * Course Concepts:
 * - Inheritance (extends Transport)
 * - Runtime Polymorphism (overrides calculateFare() & getTransportDetails())
 * 
 * Purpose:
 * Specializes Transport for Indian Railways (e.g. Vande Bharat, Shatabdi, Express).
 * Applies multiplier rules based on coach tier (Sleeper, AC 3-Tier, 2-Tier, 1st AC).
 */
public class Train extends Transport {
    private String trainNumber; // 5-digit IRCTC train number (e.g. "22448")
    private String coachType;   // Sleeper, 3A, 2A, 1A, Chair Car

    public Train(int transportId, String providerName, String sourceCity, String destinationCity,
                 String departureTime, String arrivalTime, double basePrice, int totalSeats,
                 int availableSeats, String trainNumber, String coachType) {
        super(transportId, providerName, "TRAIN", sourceCity, destinationCity, departureTime,
                arrivalTime, basePrice, totalSeats, availableSeats);
        this.trainNumber = trainNumber;
        this.coachType = coachType;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getCoachType() {
        return coachType;
    }

    /**
     * Course Concept: Method Overriding (Runtime Polymorphism)
     * Calculates train ticket fare dynamically based on coach class multipliers:
     * - Standard / Sleeper: 1.0x
     * - AC 3-Tier (3A): 1.4x
     * - AC 2-Tier (2A): 1.8x
     * - First Class AC (1A): 2.4x
     */
    @Override
    public double calculateFare(int seats, String travelClass) {
        double multiplier = 1.0;
        if (travelClass != null) {
            String c = travelClass.toUpperCase();
            if (c.contains("3A") || c.contains("AC 3")) {
                multiplier = 1.4;
            } else if (c.contains("2A") || c.contains("AC 2")) {
                multiplier = 1.8;
            } else if (c.contains("1A") || c.contains("FIRST")) {
                multiplier = 2.4;
            }
        }
        return getBasePrice() * seats * multiplier;
    }

    /**
     * Course Concept: Method Overriding for polymorphic display
     */
    @Override
    public String getTransportDetails() {
        return String.format("[TRAIN] %-12s | %-24s | Route: %s -> %s | Dep: %s Arr: %s | Base: ₹%.2f | Avail: %d",
                trainNumber, getProviderName(), getSourceCity(), getDestinationCity(),
                getDepartureTime(), getArrivalTime(), getBasePrice(), getAvailableSeats());
    }
}
