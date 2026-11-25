package com.raahvo.member2;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 2 - Transport & Hotel Management
 * ============================================================================
 * Class: Room
 * Course Concept: Multithreading Synchronization & Concurrency Control
 * 
 * Purpose:
 * Represents individual rooms inside a hotel property. Employs thread-safe
 * synchronized status transitions to prevent simultaneous double-booking of rooms.
 */
public class Room {
    private int roomId;
    private int hotelId;                // Foreign Key to Hotels table
    private String roomNumber;          // e.g. "101", "302"
    private String roomType;            // Standard Single, Deluxe Room, Luxury Suite
    private double pricePerNight;
    private int maxOccupancy;           // Max guests per room
    private boolean isAvailable;        // Thread-safe availability status

    public Room(int roomId, int hotelId, String roomNumber, String roomType,
                double pricePerNight, int maxOccupancy, boolean isAvailable) {
        this.roomId = roomId;
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.maxOccupancy = maxOccupancy;
        this.isAvailable = isAvailable;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Course Concept: Thread-Safety & Synchronized State Transition
     * Critical Section: Guarantees atomic check-and-set semantics so that two concurrent
     * guest threads cannot book the same room at the exact same millisecond.
     * 
     * @return true if room was successfully locked, false if already occupied
     */
    public synchronized boolean bookRoom() {
        if (this.isAvailable) {
            this.isAvailable = false;
            return true;
        }
        return false;
    }

    /**
     * Course Concept: Synchronized Room Release
     * Safely marks room as available upon checkout or reservation cancellation.
     */
    public synchronized void releaseRoom() {
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return String.format("Room #%-4s | %-16s | ₹%-7.2f/night | Max Occ: %d | Status: %s",
                roomNumber, roomType, pricePerNight, maxOccupancy, isAvailable ? "AVAILABLE" : "OCCUPIED");
    }
}
