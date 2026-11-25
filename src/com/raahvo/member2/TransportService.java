package com.raahvo.member2;

import com.raahvo.common.ConsoleUI;
import com.raahvo.common.DatabaseConnection;
import com.raahvo.common.InMemoryDatabase;
import com.raahvo.exception.SeatUnavailableException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 2 - Transport & Hotel Management
 * Evaluation 1 Class (7 of 7 for Member 2)
 * ============================================================================
 * Class: TransportService
 * Course Concepts:
 * - Multithreading & Synchronization (synchronized seat & room reservation)
 * - Lambda Expressions & Stream API (.filter, .sorted, .collect)
 * - Comparator (sorting transports by price ascending)
 * - Custom Exceptions (SeatUnavailableException)
 * - JDBC: PreparedStatement & ResultSet for Transport and Hotel retrieval
 */
public class TransportService {
    private final InMemoryDatabase inMemoryDb;
    private final DatabaseConnection dbConn;

    public TransportService() {
        this.inMemoryDb = InMemoryDatabase.getInstance();
        this.dbConn = DatabaseConnection.getInstance();
    }

    /**
     * Course Concept: JDBC PreparedStatement, ResultSet & Polymorphism
     * Retrieves transports between source and destination.
     * When SQL Server is connected, queries Transport table.
     * Instantiates appropriate Train, Bus, or Flight subclass dynamically.
     */
    public List<Transport> searchTransports(String source, String destination, String preferredType) {
        if (dbConn.isSqlServerConnected()) {
            String sql = "SELECT transport_id, provider_name, transport_type, source_city, destination_city, "
                    + "departure_time, arrival_time, base_price, total_seats, available_seats "
                    + "FROM Transport WHERE LOWER(source_city) = ? AND LOWER(destination_city) = ?;";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, source.trim().toLowerCase());
                stmt.setString(2, destination.trim().toLowerCase());
                try (ResultSet rs = stmt.executeQuery()) {
                    List<Transport> list = new ArrayList<>();
                    while (rs.next()) {
                        int id = rs.getInt("transport_id");
                        String provider = rs.getString("provider_name");
                        String type = rs.getString("transport_type");
                        String src = rs.getString("source_city");
                        String dest = rs.getString("destination_city");
                        String dep = rs.getString("departure_time");
                        String arr = rs.getString("arrival_time");
                        double price = rs.getDouble("base_price");
                        int total = rs.getInt("total_seats");
                        int avail = rs.getInt("available_seats");

                        if ("TRAIN".equalsIgnoreCase(type)) {
                            list.add(new Train(id, provider, src, dest, dep, arr, price, total, avail, "EXP-" + id, "AC / Sleeper"));
                        } else if ("BUS".equalsIgnoreCase(type)) {
                            list.add(new Bus(id, provider, src, dest, dep, arr, price, total, avail, "BUS-" + id, "Volvo AC", true, true));
                        } else {
                            list.add(new Flight(id, provider, src, dest, dep, arr, price, total, avail, "FLT-" + id, provider, 15, "Economy"));
                        }
                    }
                    if (!list.isEmpty()) {
                        return list.stream()
                                .filter(t -> (preferredType == null || preferredType.equalsIgnoreCase("ALL")
                                        || t.getTransportType().equalsIgnoreCase(preferredType.trim())))
                                .sorted(Comparator.comparingDouble(Transport::getBasePrice))
                                .collect(Collectors.toList());
                    }
                }
            } catch (SQLException e) {
                ConsoleUI.printWarning("SQL Server transport query error: " + e.getMessage() + ". Using in-memory store.");
            }
        }

        // Fallback or In-Memory Mode
        return inMemoryDb.transports.stream()
                .filter(t -> t.getSourceCity().equalsIgnoreCase(source.trim()))
                .filter(t -> t.getDestinationCity().equalsIgnoreCase(destination.trim()))
                .filter(t -> (preferredType == null || preferredType.equalsIgnoreCase("ALL")
                        || t.getTransportType().equalsIgnoreCase(preferredType.trim())))
                .sorted(Comparator.comparingDouble(Transport::getBasePrice))
                .collect(Collectors.toList());
    }

    public Optional<Transport> findTransportById(int transportId) {
        return inMemoryDb.transports.stream()
                .filter(t -> t.getTransportId() == transportId)
                .findFirst();
    }

    /**
     * Course Concept: Multithreading & Concurrency Lock
     * Synchronized method providing an atomic seat deduction lock across threads.
     */
    public synchronized boolean lockSeats(int transportId, int count) throws SeatUnavailableException {
        Transport transport = findTransportById(transportId)
                .orElseThrow(() -> new SeatUnavailableException("Transport vehicle ID " + transportId + " not found."));

        if (!transport.deductSeats(count)) {
            throw new SeatUnavailableException("Only " + transport.getAvailableSeats() + " seats available, requested " + count + ".");
        }

        if (dbConn.isSqlServerConnected()) {
            String sql = "UPDATE Transport SET available_seats = available_seats - ? WHERE transport_id = ? AND available_seats >= ?;";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, count);
                stmt.setInt(2, transportId);
                stmt.setInt(3, count);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Database sync warning: " + e.getMessage());
            }
        }
        return true;
    }

    /**
     * Synchronized Seat Rollback
     */
    public synchronized void releaseSeats(int transportId, int count) {
        findTransportById(transportId).ifPresent(t -> t.releaseSeats(count));
        if (dbConn.isSqlServerConnected()) {
            String sql = "UPDATE Transport SET available_seats = available_seats + ? WHERE transport_id = ?;";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, count);
                stmt.setInt(2, transportId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Database sync warning: " + e.getMessage());
            }
        }
    }

    /**
     * Course Concept: JDBC PreparedStatement for Hotel Retrieval
     * Searches hotels located in a destination, sorted by baseline price.
     */
    public List<Hotel> getHotelsByDestination(int destinationId) {
        if (dbConn.isSqlServerConnected()) {
            String sql = "SELECT hotel_id, destination_id, hotel_name, star_rating, address, amenities, base_price_per_night "
                    + "FROM Hotels WHERE destination_id = ? ORDER BY base_price_per_night;";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, destinationId);
                try (ResultSet rs = stmt.executeQuery()) {
                    List<Hotel> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new Hotel(
                                rs.getInt("hotel_id"),
                                rs.getInt("destination_id"),
                                rs.getString("hotel_name"),
                                rs.getInt("star_rating"),
                                rs.getString("address"),
                                rs.getString("amenities"),
                                rs.getDouble("base_price_per_night")
                        ));
                    }
                    if (!list.isEmpty()) {
                        return list;
                    }
                }
            } catch (SQLException e) {
                ConsoleUI.printWarning("SQL Server hotel query error: " + e.getMessage() + ". Using in-memory store.");
            }
        }

        return inMemoryDb.hotels.stream()
                .filter(h -> h.getDestinationId() == destinationId)
                .sorted(Comparator.comparingDouble(Hotel::getBasePricePerNight))
                .collect(Collectors.toList());
    }

    public Optional<Hotel> findHotelById(int hotelId) {
        return inMemoryDb.hotels.stream()
                .filter(h -> h.getHotelId() == hotelId)
                .findFirst();
    }

    /**
     * Retrieves currently available rooms within a hotel property
     */
    public List<Room> getAvailableRoomsByHotel(int hotelId) {
        if (dbConn.isSqlServerConnected()) {
            String sql = "SELECT room_id, hotel_id, room_number, room_type, price_per_night, max_occupancy, is_available "
                    + "FROM Rooms WHERE hotel_id = ? AND is_available = 1;";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, hotelId);
                try (ResultSet rs = stmt.executeQuery()) {
                    List<Room> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new Room(
                                rs.getInt("room_id"),
                                rs.getInt("hotel_id"),
                                rs.getString("room_number"),
                                rs.getString("room_type"),
                                rs.getDouble("price_per_night"),
                                rs.getInt("max_occupancy"),
                                rs.getBoolean("is_available")
                        ));
                    }
                    if (!list.isEmpty()) {
                        return list;
                    }
                }
            } catch (SQLException e) {
                ConsoleUI.printWarning("SQL Server room query error: " + e.getMessage() + ". Using in-memory store.");
            }
        }

        return inMemoryDb.rooms.stream()
                .filter(r -> r.getHotelId() == hotelId && r.isAvailable())
                .collect(Collectors.toList());
    }

    /**
     * Course Concept: Synchronized Room Allocation Lock
     */
    public synchronized boolean bookRoom(int roomId) throws SeatUnavailableException {
        Room targetRoom = inMemoryDb.rooms.stream()
                .filter(r -> r.getRoomId() == roomId)
                .findFirst()
                .orElseThrow(() -> new SeatUnavailableException("Room not found."));

        if (!targetRoom.bookRoom()) {
            throw new SeatUnavailableException("Room " + targetRoom.getRoomNumber() + " is already occupied!");
        }

        if (dbConn.isSqlServerConnected()) {
            String sql = "UPDATE Rooms SET is_available = 0 WHERE room_id = ?;";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, roomId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Database sync warning: " + e.getMessage());
            }
        }
        return true;
    }

    public List<Transport> getAllTransports() {
        return new ArrayList<>(inMemoryDb.transports);
    }

    public List<Hotel> getAllHotels() {
        return new ArrayList<>(inMemoryDb.hotels);
    }
}
