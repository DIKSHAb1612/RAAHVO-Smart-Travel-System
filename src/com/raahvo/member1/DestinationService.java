package com.raahvo.member1;

import com.raahvo.common.ConsoleUI;
import com.raahvo.common.DatabaseConnection;
import com.raahvo.common.InMemoryDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 1 - User & Destination Management
 * Evaluation 1 Class (7 of 7 for Member 1)
 * ============================================================================
 * Class: DestinationService
 * Course Concepts:
 * - Collections (ArrayList, TreeMap for natural key sorting)
 * - Lambda Expressions & Stream API (.filter, .sorted, .collect)
 * - Comparator (custom sorting by star rating descending)
 * - JDBC: PreparedStatement & ResultSet for real SQL Server querying
 */
public class DestinationService {
    private final InMemoryDatabase inMemoryDb;
    private final DatabaseConnection dbConn;

    public DestinationService() {
        this.inMemoryDb = InMemoryDatabase.getInstance();
        this.dbConn = DatabaseConnection.getInstance();
    }

    /**
     * Course Concept: JDBC PreparedStatement & ResultSet
     * Retrieves all available destinations from SQL Server if connected,
     * or falls back to the in-memory repository in offline demo mode.
     */
    public List<Destination> getAllDestinations() {
        if (dbConn.isSqlServerConnected()) {
            String sql = "SELECT destination_id, name, state, country, category, description, base_daily_cost, rating FROM Destinations ORDER BY name;";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                
                List<Destination> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new Destination(
                            rs.getInt("destination_id"),
                            rs.getString("name"),
                            rs.getString("state"),
                            rs.getString("country"),
                            rs.getString("category"),
                            rs.getString("description"),
                            rs.getDouble("base_daily_cost"),
                            rs.getDouble("rating")
                    ));
                }
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (SQLException e) {
                ConsoleUI.printWarning("SQL Server query error: " + e.getMessage() + ". Using in-memory fallback.");
            }
        }
        return new ArrayList<>(inMemoryDb.destinations);
    }

    public Optional<Destination> findById(int id) {
        return getAllDestinations().stream()
                .filter(d -> d.getDestinationId() == id)
                .findFirst();
    }

    public Optional<Destination> findByName(String name) {
        if (name == null) return Optional.empty();
        return getAllDestinations().stream()
                .filter(d -> d.getName().equalsIgnoreCase(name.trim()))
                .findFirst();
    }

    /**
     * Course Concept: Lambda Expressions & Stream API
     * Filters destinations by category (e.g. "Hill Station", "Beach")
     */
    public List<Destination> filterByCategory(String category) {
        return getAllDestinations().stream()
                .filter(d -> d.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Course Concept: Comparator Chaining & Stream API
     * Sorts destinations by rating in descending order (highest rated first)
     */
    public List<Destination> getPopularDestinations() {
        return getAllDestinations().stream()
                .sorted(Comparator.comparingDouble(Destination::getRating).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Course Concept: TreeMap
     * Groups destinations by base daily cost in naturally sorted ascending order
     */
    public Map<Double, List<Destination>> getDestinationsGroupedByPrice() {
        Map<Double, List<Destination>> priceMap = new TreeMap<>();
        for (Destination d : getAllDestinations()) {
            priceMap.computeIfAbsent(d.getBaseDailyCost(), k -> new ArrayList<>()).add(d);
        }
        return priceMap;
    }

    /**
     * Administrative Operation: Adds a new travel destination with SQL Server persistence
     */
    public boolean addDestination(String name, String state, String country,
                                  String category, String description, double baseCost, double rating) {
        int newId = inMemoryDb.destIdGen.getAndIncrement();
        Destination dest = new Destination(newId, name, state, country, category, description, baseCost, rating);

        // Persistent JDBC write via PreparedStatement
        if (dbConn.isSqlServerConnected()) {
            String sql = "INSERT INTO Destinations (name, state, country, category, description, base_daily_cost, rating) VALUES (?, ?, ?, ?, ?, ?, ?);";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, state);
                stmt.setString(3, country);
                stmt.setString(4, category);
                stmt.setString(5, description);
                stmt.setDouble(6, baseCost);
                stmt.setDouble(7, rating);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Database sync warning: " + e.getMessage());
            }
        }

        inMemoryDb.destinations.add(dest);
        return true;
    }

    /**
     * Administrative Operation: Deletes a destination from DB and memory
     */
    public boolean deleteDestination(int destinationId) {
        if (dbConn.isSqlServerConnected()) {
            String sql = "DELETE FROM Destinations WHERE destination_id = ?;";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, destinationId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Database sync warning: " + e.getMessage());
            }
        }
        return inMemoryDb.destinations.removeIf(d -> d.getDestinationId() == destinationId);
    }
}
