package com.raahvo.member3;

import com.raahvo.common.DatabaseConnection;
import com.raahvo.common.InMemoryDatabase;
import com.raahvo.exception.CustomException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 3 - Trip Planning & Packages
 * ============================================================================
 * Class: CouponService
 * Course Concepts:
 * - Collections (HashMap for fast O(1) coupon code lookup)
 * - Custom Exceptions (INVALID_COUPON, COUPON_EXPIRED, MIN_AMOUNT_NOT_MET)
 * - JDBC (PreparedStatement for saving newly generated coupons)
 */
public class CouponService {
    private final InMemoryDatabase inMemoryDb;
    private final DatabaseConnection dbConn;

    public CouponService() {
        this.inMemoryDb = InMemoryDatabase.getInstance();
        this.dbConn = DatabaseConnection.getInstance();
    }

    /**
     * Retrieves all valid, active coupons available for customers
     */
    public List<Coupon> getAvailableCoupons() {
        List<Coupon> activeCoupons = new ArrayList<>();
        for (Coupon c : inMemoryDb.coupons.values()) {
            if (c.isActive()) {
                activeCoupons.add(c);
            }
        }
        return activeCoupons;
    }

    public Optional<Coupon> findCoupon(String code) {
        if (code == null) return Optional.empty();
        Coupon c = inMemoryDb.coupons.get(code.trim().toUpperCase());
        return (c != null && c.isActive()) ? Optional.of(c) : Optional.empty();
    }

    /**
     * Validates a coupon code and computes the final discount.
     * Throws granular domain exceptions explaining exact rejection reason.
     */
    public double validateAndCalculateDiscount(String code, double bookingTotal) throws CustomException {
        if (code == null || code.trim().isEmpty()) {
            return 0.0;
        }
        String cleanCode = code.trim().toUpperCase();
        
        // Course Concept: HashMap O(1) lookup
        Coupon coupon = inMemoryDb.coupons.get(cleanCode);

        if (coupon == null) {
            throw new CustomException("INVALID_COUPON", "Coupon code '" + cleanCode + "' does not exist.");
        }
        if (!coupon.isActive()) {
            throw new CustomException("COUPON_EXPIRED", "Coupon code '" + cleanCode + "' has expired or is inactive.");
        }
        if (bookingTotal < coupon.getMinBookingAmount()) {
            throw new CustomException("MIN_AMOUNT_NOT_MET", "Coupon '" + cleanCode + "' requires a minimum booking of ₹"
                    + coupon.getMinBookingAmount() + " (Current: ₹" + bookingTotal + ").");
        }

        return coupon.calculateDiscount(bookingTotal);
    }

    /**
     * Administrative Operation: Creates a new promo code with database sync
     */
    public void addCoupon(Coupon coupon) {
        inMemoryDb.coupons.put(coupon.getCode(), coupon);
        if (dbConn.isSqlServerConnected()) {
            String sql = "INSERT INTO Coupons (code, discount_percentage, max_discount, min_booking_amount, valid_until, is_active) VALUES (?, ?, ?, ?, ?, 1);";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, coupon.getCode());
                stmt.setDouble(2, coupon.getDiscountPercentage());
                stmt.setDouble(3, coupon.getMaxDiscount());
                stmt.setDouble(4, coupon.getMinBookingAmount());
                stmt.setDate(5, java.sql.Date.valueOf(coupon.getValidUntil()));
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Database sync warning: " + e.getMessage());
            }
        }
    }
}
