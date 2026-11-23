package com.raahvo.member4;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 4 - Booking & Payment
 * ============================================================================
 * Class: Passenger
 * Course Concept: Encapsulation (OOP)
 * 
 * Purpose:
 * Represents individual passengers linked to a booking. Stores legal identification,
 * assigned vehicle seat numbers, and demographic age/gender.
 */
public class Passenger {
    private int passengerId;
    private int bookingId;              // Foreign Key to Bookings table
    private String fullName;
    private int age;
    private String gender;              // Male, Female, Other
    private String seatNumber;          // Assigned seat code (e.g. "S-1", "A4")
    private String idProof;             // Aadhaar, Passport, Driving License, Voter ID

    public Passenger(int passengerId, int bookingId, String fullName, int age,
                     String gender, String seatNumber, String idProof) {
        this.passengerId = passengerId;
        this.bookingId = bookingId;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.seatNumber = seatNumber;
        this.idProof = idProof;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getIdProof() {
        return idProof;
    }

    @Override
    public String toString() {
        return String.format("%-20s | Age: %-3d | Gender: %-6s | Seat: %-4s | ID: %s",
                fullName, age, gender, (seatNumber != null ? seatNumber : "N/A"), idProof);
    }
}
