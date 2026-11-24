package com.raahvo.member1;

import com.raahvo.common.ConsoleUI;
import com.raahvo.common.DatabaseConnection;
import com.raahvo.common.InMemoryDatabase;
import com.raahvo.exception.AuthenticationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 1 - User & Destination Management
 * ============================================================================
 * Class: Login
 * Course Concepts:
 * - Interface Implementation (implements Authentication)
 * - Collections (HashMap for tracking failed attempts / brute-force protection)
 * - JDBC (PreparedStatement for parameterized credential querying)
 * - Session Management (tracking active authenticated user)
 */
public class Login implements Authentication {
    private final InMemoryDatabase inMemoryDb;
    private final DatabaseConnection dbConn;
    
    // Tracks current active logged-in user (null = Guest mode)
    private User currentUser = null;
    
    // Map tracking consecutive failed login attempts by email (Brute-force protection)
    private final Map<String, Integer> failedAttempts = new HashMap<>();

    public Login() {
        this.inMemoryDb = InMemoryDatabase.getInstance();
        this.dbConn = DatabaseConnection.getInstance();
    }

    /**
     * Authenticates user against SQL Server or In-Memory repository.
     * Enforces a 5-attempt brute force security threshold.
     */
    @Override
    public User login(String email, String password) throws AuthenticationException {
        if (email == null || password == null) {
            throw new AuthenticationException("Email and password credentials cannot be null.");
        }
        String normalizedEmail = email.trim().toLowerCase();

        // Security Check: Lock account if 5 consecutive failed attempts are reached
        int attempts = failedAttempts.getOrDefault(normalizedEmail, 0);
        if (attempts >= 5) {
            throw new AuthenticationException("Account temporarily locked due to 5 consecutive failed login attempts.");
        }

        User matchedUser = null;

        // Mode 1: Query Microsoft SQL Server via JDBC PreparedStatement
        if (dbConn.isSqlServerConnected()) {
            String sql = "SELECT user_id, name, email, phone, password_hash, role FROM Users WHERE LOWER(email) = ? AND password_hash = ?;";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, normalizedEmail);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("user_id");
                        String name = rs.getString("name");
                        String role = rs.getString("role");
                        String phone = rs.getString("phone");
                        // Instantiate appropriate subclass based on role
                        if ("ADMIN".equalsIgnoreCase(role)) {
                            matchedUser = new Admin(id, name, normalizedEmail, phone, password, "Operations", 5);
                        } else {
                            matchedUser = new User(id, name, normalizedEmail, phone, password, role);
                        }
                    }
                }
            } catch (SQLException e) {
                ConsoleUI.printWarning("SQL Server query error during login: " + e.getMessage());
            }
        }

        // Mode 2: Fallback to synchronized In-Memory Store
        if (matchedUser == null) {
            for (User u : inMemoryDb.users.values()) {
                if (u.getEmail().equalsIgnoreCase(normalizedEmail) && u.getPasswordHash().equals(password)) {
                    matchedUser = u;
                    break;
                }
            }
        }

        // Handle authentication failure
        if (matchedUser == null) {
            failedAttempts.put(normalizedEmail, attempts + 1);
            throw new AuthenticationException("Invalid email or password! Attempts remaining: " + (5 - (attempts + 1)));
        }

        // Successful login: reset failed attempts counter & set active session
        failedAttempts.remove(normalizedEmail);
        this.currentUser = matchedUser;
        return this.currentUser;
    }

    @Override
    public void logout() {
        // Clear active session
        this.currentUser = null;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean isAuthenticated() {
        return currentUser != null;
    }

    @Override
    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
}
