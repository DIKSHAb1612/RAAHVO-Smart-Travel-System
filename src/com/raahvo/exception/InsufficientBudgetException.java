package com.raahvo.exception;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Custom Exception Hierarchy
 * ============================================================================
 * Class: InsufficientBudgetException
 * Course Concept: Custom Exception with Domain State (OOP)
 * 
 * Purpose:
 * Thrown by TripPlanner when a traveler's planned budget ceiling is critically lower
 * than the minimum required baseline expenses for that itinerary.
 * Encapsulates requested budget, required minimum cost, and deficit amount.
 */
public class InsufficientBudgetException extends CustomException {
    private static final long serialVersionUID = 1L;
    
    private final double requestedBudget;
    private final double estimatedCost;

    public InsufficientBudgetException(double requestedBudget, double estimatedCost, String message) {
        super("BUDGET_DEFICIT", message);
        this.requestedBudget = requestedBudget;
        this.estimatedCost = estimatedCost;
    }

    public double getRequestedBudget() {
        return requestedBudget;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    /**
     * Calculates the exact shortfall in funds
     */
    public double getDeficit() {
        return Math.max(0, estimatedCost - requestedBudget);
    }
}
