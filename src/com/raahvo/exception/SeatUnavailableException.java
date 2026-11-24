package com.raahvo.exception;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Custom Exception Hierarchy
 * ============================================================================
 * Class: SeatUnavailableException
 * Course Concept: Custom Exception for Concurrency & Inventory Depletion
 * 
 * Purpose:
 * Thrown when requested seats on a transport vehicle or rooms in a hotel
 * are fully booked or locked by a concurrent thread.
 */
public class SeatUnavailableException extends CustomException {
    private static final long serialVersionUID = 1L;

    public SeatUnavailableException(String message) {
        super("SEAT_UNAVAILABLE", message);
    }
}
