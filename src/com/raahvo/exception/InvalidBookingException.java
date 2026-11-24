package com.raahvo.exception;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Custom Exception Hierarchy
 * ============================================================================
 * Class: InvalidBookingException
 * Course Concept: Exception Handling for Input & Business Validation
 * 
 * Purpose:
 * Thrown when booking validation fails (e.g. past travel dates, negative passenger counts,
 * or empty passenger lists).
 */
public class InvalidBookingException extends CustomException {
    private static final long serialVersionUID = 1L;

    public InvalidBookingException(String message) {
        super("BOOKING_INVALID", message);
    }
}
