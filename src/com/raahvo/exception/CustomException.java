package com.raahvo.exception;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Custom Exception Hierarchy
 * ============================================================================
 * Class: CustomException
 * Course Concept: Exception Handling & Custom Checked Exceptions
 * 
 * Purpose:
 * Serves as the base checked domain exception for RAAHVO.
 * Extends java.lang.Exception, carrying machine-readable error codes and human-readable messages.
 */
public class CustomException extends Exception {
    private static final long serialVersionUID = 1L;
    
    // Machine-readable diagnostic error code (e.g. AUTH_ERR, BUDGET_DEFICIT)
    private final String errorCode;

    public CustomException(String message) {
        super(message);
        this.errorCode = "RAAHVO_ERR_GENERIC";
    }

    public CustomException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "[" + errorCode + "] " + getMessage();
    }
}
