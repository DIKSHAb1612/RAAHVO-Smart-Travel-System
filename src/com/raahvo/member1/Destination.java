package com.raahvo.member1;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 1 - User & Destination Management
 * ============================================================================
 * Class: Destination
 * Course Concept: Comparable Interface & Natural Ordering (Java Collections)
 * 
 * Purpose:
 * Represents travel destinations across India with categories, ratings,
 * and base daily living costs. Implements Comparable<Destination> to allow
 * default alphabetical sorting by destination name.
 */
public class Destination implements Comparable<Destination> {
    private int destinationId;
    private String name;
    private String state;
    private String country;
    private String category; // Hill Station, Beach, Heritage, Adventure, Pilgrimage, Nature
    private String description;
    private double baseDailyCost; // Estimated base daily living cost per person
    private double rating;        // Star rating (1.0 to 5.0)

    public Destination(int destinationId, String name, String state, String country,
                       String category, String description, double baseDailyCost, double rating) {
        this.destinationId = destinationId;
        this.name = name;
        this.state = state;
        this.country = country;
        this.category = category;
        this.description = description;
        this.baseDailyCost = baseDailyCost;
        this.rating = rating;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBaseDailyCost() {
        return baseDailyCost;
    }

    public void setBaseDailyCost(double baseDailyCost) {
        this.baseDailyCost = baseDailyCost;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Course Concept: Comparable<Destination>
     * Defines natural ordering alphabetically by destination name.
     */
    @Override
    public int compareTo(Destination other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    /**
     * Formatted string output for terminal display tables
     */
    @Override
    public String toString() {
        return String.format("%-4d | %-14s | %-16s | %-12s | ₹%-7.2f/day | ★ %.1f",
                destinationId, name, state, category, baseDailyCost, rating);
    }
}
