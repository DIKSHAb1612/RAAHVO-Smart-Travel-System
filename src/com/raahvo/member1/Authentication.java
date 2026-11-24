package com.raahvo.member1;

import com.raahvo.exception.AuthenticationException;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 1 - User & Destination Management
 * ============================================================================
 * Interface: Authentication
 * Course Concept: Abstraction & Interface Contracts (OOP)
 * 
 * Purpose:
 * Defines the strict contractual methods required for authentication and
 * session management in RAAHVO. Decouples the concrete login implementation
 * from the rest of the application.
 */
public interface Authentication {
    /**
     * Authenticates a user against stored credentials
     * @param email User's registered email
     * @param password Plain-text password entered at console
     * @return User object representing authenticated identity
     * @throws AuthenticationException if credentials fail or account is locked
     */
    User login(String email, String password) throws AuthenticationException;

    /**
     * Terminates the current active session
     */
    void logout();

    /**
     * Retrieves the currently logged in user
     * @return Active User or null if browsing as guest
     */
    User getCurrentUser();

    /**
     * Checks if a user is currently logged in
     * @return true if logged in, false if guest
     */
    boolean isAuthenticated();

    /**
     * Checks if the active user possesses administrative privileges
     * @return true if logged in as Admin, false otherwise
     */
    boolean isAdmin();
}
