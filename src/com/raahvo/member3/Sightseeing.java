package com.raahvo.member3;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 3 - Trip Planning & Packages
 * ============================================================================
 * Class: Sightseeing
 * Course Concept: Encapsulation & Entity Modeling
 * 
 * Purpose:
 * Represents tourist attractions, adventure spots, and heritage points.
 * Holds entry ticketing fees and visit duration.
 */
public class Sightseeing {
    private int spotId;
    private int destinationId;          // Foreign Key to Destinations table
    private String spotName;
    private double entryFee;            // Per person entry ticket in ₹ (0 = Free)
    private double durationHours;       // Estimated visit duration
    private String spotCategory;        // Adventure, Heritage, Nature, Pilgrimage
    private String description;

    public Sightseeing(int spotId, int destinationId, String spotName, double entryFee,
                       double durationHours, String spotCategory, String description) {
        this.spotId = spotId;
        this.destinationId = destinationId;
        this.spotName = spotName;
        this.entryFee = entryFee;
        this.durationHours = durationHours;
        this.spotCategory = spotCategory;
        this.description = description;
    }

    public int getSpotId() {
        return spotId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public String getSpotName() {
        return spotName;
    }

    public double getEntryFee() {
        return entryFee;
    }

    public double getDurationHours() {
        return durationHours;
    }

    public String getSpotCategory() {
        return spotCategory;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%-28s | Entry: ₹%-5.2f | Duration: %.1fh | Category: %-12s",
                spotName, entryFee, durationHours, spotCategory);
    }
}
