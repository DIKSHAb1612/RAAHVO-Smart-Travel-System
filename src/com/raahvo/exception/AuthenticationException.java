package com.raahvo.exception;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Custom Exception Hierarchy
 * ============================================================================
 * Class: AuthenticationException
 * Course Concept: Security & Authentication Exception Handling
 * 
 * Purpose:
 * Thrown when credential verification fails, account brute-force lock is triggered,
 * or unauthorized operations are attempted.
 */
public class AuthenticationException extends CustomException {
    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super("AUTH_ERR", message);
    }
}
