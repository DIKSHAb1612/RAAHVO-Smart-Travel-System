package com.raahvo.member4;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 4 - Booking & Payment
 * Evaluation 1 Class (1 of 6 for Member 4)
 * ============================================================================
 * Class: Booking
 * Course Concept: Encapsulation & Entity Aggregation (OOP)
 * 
 * Purpose:
 * Represents confirmed travel reservations across all booking types (Transport,
 * Hotel, Curated Package, or Custom Trip). Holds unique PNR, financial totals,
 * discount deductions, travel dates, itinerary details, and passenger entities.
 */
public class Booking {
    private int bookingId;
    private String bookingPnr;          // Unique Passenger Name Record (e.g. "RAAHVO-DEL-98421")
    private int userId;                 // Foreign Key to Users table
    private String bookingType;         // TRANSPORT, HOTEL, PACKAGE, CUSTOM_TRIP
    private LocalDateTime bookingDate;  // Timestamp of reservation execution
    private LocalDate travelDate;       // Date of scheduled journey
    private double totalAmount;         // Gross fare before vouchers
    private double discountAmount;      // Total coupon deduction in ₹
    private double finalPayable;        // Net amount paid by traveler
    private String status;              // CONFIRMED, CANCELLED, COMPLETED, PENDING
    private List<Passenger> passengers; // Aggregate list of traveler tickets

    // Descriptive metadata for clear invoice presentation
    private String destinationName = "Multiple / Custom Itinerary";
    private String transportSummary = "Not Specified";
    private String hotelSummary = "Not Specified";

    public Booking(int bookingId, String bookingPnr, int userId, String bookingType,
                   LocalDate travelDate, double totalAmount, double discountAmount,
                   double finalPayable, String status) {
        this.bookingId = bookingId;
        this.bookingPnr = bookingPnr;
        this.userId = userId;
        this.bookingType = bookingType;
        this.bookingDate = LocalDateTime.now();
        this.travelDate = travelDate;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalPayable = finalPayable;
        this.status = status;
        this.passengers = new ArrayList<>();
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingPnr() {
        return bookingPnr;
    }

    public int getUserId() {
        return userId;
    }

    public String getBookingType() {
        return bookingType;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getFinalPayable() {
        return finalPayable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getTransportSummary() {
        return transportSummary;
    }

    public void setTransportSummary(String transportSummary) {
        this.transportSummary = transportSummary;
    }

    public String getHotelSummary() {
        return hotelSummary;
    }

    public void setHotelSummary(String hotelSummary) {
        this.hotelSummary = hotelSummary;
    }

    @Override
    public String toString() {
        return String.format("PNR: %-18s | Type: %-11s | Travel: %s | Amount: ₹%-8.2f | Status: %s",
                bookingPnr, bookingType, travelDate, finalPayable, status);
    }
}
