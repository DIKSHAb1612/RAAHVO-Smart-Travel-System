package com.raahvo.member4;

import com.raahvo.common.ConsoleUI;
import com.raahvo.common.DatabaseConnection;
import com.raahvo.common.InMemoryDatabase;
import com.raahvo.exception.CustomException;
import com.raahvo.exception.InvalidBookingException;
import com.raahvo.member2.TransportService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 4 - Booking & Payment
 * ============================================================================
 * Class: BookingService
 * Course Concepts:
 * - JDBC Transactions (ACID properties, commit, rollback)
 * - Multithreading Synchronization (synchronized reservation execution)
 * - Unique Random PNR Generation (String manipulation)
 * - Custom Exceptions (InvalidBookingException, CustomException)
 * - Batch Processing (PreparedStatement.addBatch() & executeBatch())
 * 
 * Purpose:
 * Coordinates the full reservation workflow: validates passengers, checks dates,
 * generates unique PNRs, locks seat/room inventories, and commits multi-table
 * records into Microsoft SQL Server inside an atomic transaction.
 */
public class BookingService {
    private final InMemoryDatabase inMemoryDb;
    private final DatabaseConnection dbConn;
    private final TransportService transportService;

    public BookingService() {
        this.inMemoryDb = InMemoryDatabase.getInstance();
        this.dbConn = DatabaseConnection.getInstance();
        this.transportService = new TransportService();
    }

    /**
     * Generates a collision-resistant unique 14-character PNR code
     * Format: RAAHVO-<3_LETTER_PREFIX>-<5_DIGIT_RANDOM>
     * Example: "RAAHVO-TRA-84920"
     */
    public String generatePNR(String prefix) {
        String code = prefix.toUpperCase().replaceAll("[^A-Z]", "");
        if (code.length() > 3) code = code.substring(0, 3);
        int randomNum = 10000 + new Random().nextInt(90000);
        return "RAAHVO-" + code + "-" + randomNum;
    }

    /**
     * Course Concept: Multi-Table JDBC Transactions (ACID) & Thread Safety
     * 
     * Executes atomic reservation creation:
     * 1. Validates business constraints (travel date, passengers, non-zero fare).
     * 2. Atomically locks transport seats or hotel rooms via synchronized locks.
     * 3. Opens a JDBC transaction with conn.setAutoCommit(false).
     * 4. Inserts into Bookings, batch inserts into Passengers, and inserts Payment.
     * 5. If any operation fails, catches SQLException and triggers conn.rollback().
     * 6. Releases locked seats on failure to maintain absolute consistency.
     */
    public synchronized Booking createBooking(int userId, String bookingType, LocalDate travelDate,
                                             double totalAmount, double discountAmount,
                                             List<Passenger> passengers, String paymentMethod,
                                             Integer transportIdToLock, Integer roomIdToLock) throws CustomException {
        return createBooking(userId, bookingType, travelDate, totalAmount, discountAmount,
                passengers, paymentMethod, transportIdToLock, roomIdToLock,
                "Standard Travel Route", "Direct Transport Option", "Standard Hotel Room");
    }

    public synchronized Booking createBooking(int userId, String bookingType, LocalDate travelDate,
                                             double totalAmount, double discountAmount,
                                             List<Passenger> passengers, String paymentMethod,
                                             Integer transportIdToLock, Integer roomIdToLock,
                                             String destinationName, String transportSummary, String hotelSummary) throws CustomException {

        // Step 1: Business validation
        if (travelDate == null || travelDate.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Travel date cannot be in the past.");
        }
        if (passengers == null || passengers.isEmpty()) {
            throw new InvalidBookingException("At least one passenger must be registered.");
        }
        if (totalAmount <= 0) {
            throw new InvalidBookingException("Total booking amount must be greater than zero.");
        }

        double finalPayable = Math.max(0, totalAmount - discountAmount);
        String pnr = generatePNR(bookingType);
        int bookingId = inMemoryDb.bookingIdGen.getAndIncrement();

        Booking booking = new Booking(bookingId, pnr, userId, bookingType, travelDate, totalAmount, discountAmount, finalPayable, "CONFIRMED");
        if (destinationName != null) booking.setDestinationName(destinationName);
        if (transportSummary != null) booking.setTransportSummary(transportSummary);
        if (hotelSummary != null) booking.setHotelSummary(hotelSummary);

        for (Passenger p : passengers) {
            p.setBookingId(bookingId);
            booking.addPassenger(p);
        }

        // Step 2: Thread-Safe inventory locking
        if (transportIdToLock != null) {
            transportService.lockSeats(transportIdToLock, passengers.size());
        }
        if (roomIdToLock != null) {
            transportService.bookRoom(roomIdToLock);
        }

        // Step 3: Course Concept -> Multi-Table ACID Transaction in SQL Server
        if (dbConn.isSqlServerConnected()) {
            try {
                // Begin Transaction: Disable automatic commit
                dbConn.beginTransaction();
                Connection conn = dbConn.getConnection();

                // 3a. Insert into Bookings table
                String bookingSql = "INSERT INTO Bookings (booking_pnr, user_id, booking_type, booking_date, travel_date, total_amount, discount_amount, final_payable, status) "
                        + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, 'CONFIRMED');";
                try (PreparedStatement stmt = conn.prepareStatement(bookingSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, pnr);
                    stmt.setInt(2, userId);
                    stmt.setString(3, bookingType);
                    stmt.setDate(4, java.sql.Date.valueOf(travelDate));
                    stmt.setDouble(5, totalAmount);
                    stmt.setDouble(6, discountAmount);
                    stmt.setDouble(7, finalPayable);
                    stmt.executeUpdate();

                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            booking.setBookingId(rs.getInt(1));
                        }
                    }
                }

                // 3b. Course Concept: Batch Processing for Passenger records
                String passSql = "INSERT INTO Passengers (booking_id, full_name, age, gender, seat_number, id_proof) VALUES (?, ?, ?, ?, ?, ?);";
                try (PreparedStatement stmt = conn.prepareStatement(passSql)) {
                    for (Passenger p : passengers) {
                        stmt.setInt(1, booking.getBookingId());
                        stmt.setString(2, p.getFullName());
                        stmt.setInt(3, p.getAge());
                        stmt.setString(4, p.getGender());
                        stmt.setString(5, p.getSeatNumber());
                        stmt.setString(6, p.getIdProof());
                        stmt.addBatch(); // Batching statements for high performance
                    }
                    stmt.executeBatch(); // Single round-trip database execution
                }

                // 3c. Insert into Payments audit table
                String txnRef = "TXN-" + System.currentTimeMillis();
                String paySql = "INSERT INTO Payments (booking_id, transaction_ref, payment_method, amount, payment_status, payment_timestamp) VALUES (?, ?, ?, ?, 'SUCCESS', CURRENT_TIMESTAMP);";
                try (PreparedStatement stmt = conn.prepareStatement(paySql)) {
                    stmt.setInt(1, booking.getBookingId());
                    stmt.setString(2, txnRef);
                    stmt.setString(3, paymentMethod);
                    stmt.setDouble(4, finalPayable);
                    stmt.executeUpdate();
                }

                // Transaction Success: Commit all changes atomically
                dbConn.commit();
                ConsoleUI.printSuccess("Booking and payment successfully committed to SQL Server Database!");

            } catch (SQLException e) {
                // Transaction Failure: Roll back all operations to preserve database consistency!
                dbConn.rollback();
                
                // Roll back locked seat count if database transaction fails
                if (transportIdToLock != null) {
                    transportService.releaseSeats(transportIdToLock, passengers.size());
                }
                throw new CustomException("DB_TRANSACTION_FAILED", "Transaction failed and was rolled back: " + e.getMessage());
            }
        }

        // Step 4: Synchronize In-Memory Cache for fast runtime lookup
        inMemoryDb.bookingsByPnr.put(pnr, booking);
        inMemoryDb.userBookings.computeIfAbsent(userId, k -> new ArrayList<>()).add(booking);

        int paymentId = inMemoryDb.paymentIdGen.getAndIncrement();
        String txnRef = "TXN-" + System.currentTimeMillis();
        Payment payment = new Payment(paymentId, booking.getBookingId(), txnRef, paymentMethod, finalPayable, "SUCCESS");
        inMemoryDb.payments.add(payment);

        return booking;
    }

    public Optional<Booking> getBookingByPnr(String pnr) {
        if (pnr == null) return Optional.empty();
        return Optional.ofNullable(inMemoryDb.bookingsByPnr.get(pnr.trim().toUpperCase()));
    }

    public List<Booking> getUserBookings(int userId) {
        return inMemoryDb.userBookings.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * Booking Cancellation Workflow with policy penalty and simulated refund
     */
    public synchronized Cancellation cancelBooking(String pnr, String reason) throws CustomException {
        Booking booking = getBookingByPnr(pnr)
                .orElseThrow(() -> new CustomException("BOOKING_NOT_FOUND", "No booking found with PNR: " + pnr));

        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            throw new CustomException("ALREADY_CANCELLED", "This booking is already cancelled.");
        }

        // Policy: 15% cancellation fee if cancelled > 2 days ahead, 30% if <= 2 days
        double chargeRate = 0.15;
        if (booking.getTravelDate().minusDays(2).isBefore(LocalDate.now())) {
            chargeRate = 0.30;
        }

        double cancellationCharge = booking.getFinalPayable() * chargeRate;
        double refundAmount = Math.max(0, booking.getFinalPayable() - cancellationCharge);

        booking.setStatus("CANCELLED");

        int cancelId = inMemoryDb.cancelIdGen.getAndIncrement();
        Cancellation cancellation = new Cancellation(cancelId, booking.getBookingId(), reason, cancellationCharge, refundAmount);
        inMemoryDb.cancellations.add(cancellation);

        // Update SQL Server database record
        if (dbConn.isSqlServerConnected()) {
            String sql = "UPDATE Bookings SET status = 'CANCELLED' WHERE booking_pnr = ?;";
            try (Connection conn = dbConn.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, pnr);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("DB sync warning: " + e.getMessage());
            }
        }

        return cancellation;
    }

    /**
     * Course Concept: TreeMap
     * Groups and sorts a user's bookings chronologically in natural ascending date order.
     */
    public Map<LocalDate, List<Booking>> getBookingsGroupedByDate(int userId) {
        Map<LocalDate, List<Booking>> treeMap = new TreeMap<>();
        List<Booking> list = getUserBookings(userId);
        for (Booking b : list) {
            treeMap.computeIfAbsent(b.getTravelDate(), k -> new ArrayList<>()).add(b);
        }
        return treeMap;
    }

    /**
     * Renders a clean formatted table of user bookings
     */
    public void displayUserBookingHistory(int userId) {
        List<Booking> bookings = getUserBookings(userId);
        if (bookings.isEmpty()) {
            ConsoleUI.printInfo("No previous travel bookings found for your account.");
            return;
        }

        ConsoleUI.printSectionHeader("YOUR COMPLETE BOOKING HISTORY (" + bookings.size() + " RESERVATIONS)");
        System.out.printf("%-18s | %-12s | %-12s | %-10s | %-12s | %-8s\n",
                "PNR", "Type", "Travel Date", "Amount", "Status", "Pax");
        ConsoleUI.printHorizontalLine(84, '-');

        for (Booking b : bookings) {
            String statusColor = "CONFIRMED".equalsIgnoreCase(b.getStatus()) ? ConsoleUI.GREEN
                    : ("CANCELLED".equalsIgnoreCase(b.getStatus()) ? ConsoleUI.RED : ConsoleUI.YELLOW);

            System.out.printf("%-18s | %-12s | %-12s | ₹%-9.2f | " + statusColor + "%-12s" + ConsoleUI.RESET + " | %-8d\n",
                    b.getBookingPnr(), b.getBookingType(), b.getTravelDate(), b.getFinalPayable(), b.getStatus(), b.getPassengers().size());
        }
        ConsoleUI.printHorizontalLine(84, '-');
    }

    /**
     * Retrieves all bookings across the platform for Admin audits
     */
    public List<Booking> getAllPlatformBookings() {
        return new ArrayList<>(inMemoryDb.bookingsByPnr.values());
    }
}
