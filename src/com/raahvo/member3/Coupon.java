package com.raahvo.member3;

import java.time.LocalDate;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 3 - Trip Planning & Packages
 * ============================================================================
 * Class: Coupon
 * Course Concepts:
 * - Date Comparison (LocalDate.isBefore)
 * - Financial Discount Validation Logic
 * 
 * Purpose:
 * Represents promotional discount codes with expiration dates, percentage rates,
 * maximum caps, and minimum booking order requirements.
 */
public class Coupon {
    private int couponId;
    private String code;                // e.g. "RAAHVO10", "FIRSTTRIP"
    private double discountPercentage;  // e.g. 10.0 for 10%
    private double maxDiscount;         // Maximum cap in ₹
    private double minBookingAmount;    // Minimum transaction requirement in ₹
    private LocalDate validUntil;       // Expiration date
    private boolean isActive;

    public Coupon(int couponId, String code, double discountPercentage, double maxDiscount,
                  double minBookingAmount, LocalDate validUntil, boolean isActive) {
        this.couponId = couponId;
        this.code = code.toUpperCase();
        this.discountPercentage = discountPercentage;
        this.maxDiscount = maxDiscount;
        this.minBookingAmount = minBookingAmount;
        this.validUntil = validUntil;
        this.isActive = isActive;
    }

    public int getCouponId() {
        return couponId;
    }

    public String getCode() {
        return code;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public double getMinBookingAmount() {
        return minBookingAmount;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    /**
     * Checks if coupon is active and has not expired against current system date
     */
    public boolean isActive() {
        return isActive && !validUntil.isBefore(LocalDate.now());
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Calculates the exact discount to apply, obeying minimum order rules and maximum discount caps
     * 
     * @param orderAmount Total order price before discount
     * @return Eligible discount in ₹
     */
    public double calculateDiscount(double orderAmount) {
        // Enforce active status and minimum booking threshold
        if (!isActive() || orderAmount < minBookingAmount) {
            return 0.0;
        }
        // Percentage calculation
        double calculated = (orderAmount * discountPercentage) / 100.0;
        // Cap at max discount limit
        return Math.min(calculated, maxDiscount);
    }

    @Override
    public String toString() {
        return String.format("%-12s | %.0f%% OFF (Up to ₹%.0f) | Min Order: ₹%.0f | Valid till: %s",
                code, discountPercentage, maxDiscount, minBookingAmount, validUntil);
    }
}
