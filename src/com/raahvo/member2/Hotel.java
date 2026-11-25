package com.raahvo.member2;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 2 - Transport & Hotel Management
 * ============================================================================
 * Class: Hotel
 * Course Concept: Encapsulation (OOP)
 * 
 * Purpose:
 * Represents lodging properties linked to destinations by destinationId.
 * Holds star rating, address, amenities, and baseline nightly rates.
 */
public class Hotel {
    private int hotelId;
    private int destinationId;          // Foreign Key to Destinations table
    private String hotelName;
    private int starRating;             // 1 to 5 Stars
    private String address;
    private String amenities;           // WiFi, Mountain View, Pool, Free Breakfast, etc.
    private double basePricePerNight;

    public Hotel(int hotelId, int destinationId, String hotelName, int starRating,
                 String address, String amenities, double basePricePerNight) {
        this.hotelId = hotelId;
        this.destinationId = destinationId;
        this.hotelName = hotelName;
        this.starRating = starRating;
        this.address = address;
        this.amenities = amenities;
        this.basePricePerNight = basePricePerNight;
    }

    public int getHotelId() {
        return hotelId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public int getStarRating() {
        return starRating;
    }

    public String getAddress() {
        return address;
    }

    public String getAmenities() {
        return amenities;
    }

    public double getBasePricePerNight() {
        return basePricePerNight;
    }

    public void setBasePricePerNight(double basePricePerNight) {
        this.basePricePerNight = basePricePerNight;
    }

    @Override
    public String toString() {
        return String.format("%-4d | %-30s | %d-Star | ₹%-7.2f/night | %s",
                hotelId, hotelName, starRating, basePricePerNight, amenities);
    }
}
