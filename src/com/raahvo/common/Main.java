package com.raahvo.common;

import com.raahvo.exception.AuthenticationException;
import com.raahvo.exception.CustomException;
import com.raahvo.exception.InsufficientBudgetException;
import com.raahvo.member1.*;
import com.raahvo.member2.*;
import com.raahvo.member3.*;
import com.raahvo.member4.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Application Entry Point & Controller
 * ============================================================================
 * Class: Main
 * Course Concepts:
 * - State Machine & Menu Orchestration (Console UI)
 * - Multithreading & Concurrency Stress Testing (ExecutorService, Thread Pools)
 * - Module Integration: Connects all 4 Team Members' services
 * - Graceful Exception Handling (try-catch across all user actions)
 * 
 * Team Distribution (Evaluation 1 - 30 Classes):
 * - Member 1 (7 Classes): User, Admin, Authentication, Registration, Login, Destination, DestinationService
 * - Member 2 (7 Classes): Transport, Train, Bus, Flight, Hotel, Room, TransportService
 * - Member 3 (7 Classes): TripPlanner, Trip, TravelPackage, BudgetCalculator, Coupon, CouponService, Sightseeing
 * - Member 4 (6 Classes): Booking, BookingService, Passenger, Payment, Cancellation, Invoice
 * - Common   (3 Classes): Main, DatabaseConnection, InputValidator
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    // ========================================================================
    // SERVICE INSTANTIATION ACROSS ALL 4 TEAM MODULES
    // ========================================================================
    // Member 1 Services
    private static final Authentication auth = new Login();
    private static final Registration registration = new Registration();
    private static final DestinationService destinationService = new DestinationService();

    // Member 2 Services
    private static final TransportService transportService = new TransportService();

    // Member 3 Services
    private static final TripPlanner tripPlanner = new TripPlanner();
    private static final CouponService couponService = new CouponService();

    // Member 4 Services
    private static final BookingService bookingService = new BookingService();

    // Shared Common Infrastructure
    private static final DatabaseConnection dbConn = DatabaseConnection.getInstance();

    /**
     * Main application lifecycle loop
     */
    public static void main(String[] args) {
        // Display ASCII Banner & Tagline
        ConsoleUI.printBanner();
        
        // Report database connectivity status
        System.out.println(ConsoleUI.BLUE + "  Persistence Status: " + ConsoleUI.BOLD + dbConn.getDbMode() + ConsoleUI.RESET);
        if (dbConn.isSqlServerConnected()) {
            ConsoleUI.printSuccess("Live " + dbConn.getDatabaseName() + " database connection active.");
        } else {
            ConsoleUI.printInfo("Running with synchronized in-memory mock store (Demo-Ready Offline Mode).");
        }

        // Main navigation loop
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputValidator.readInt(scanner, "Enter your choice (0-8): ", 0, 8);
            switch (choice) {
                case 1:
                    handleSmartTripPlanner();
                    break;
                case 2:
                    handleExploreDestinations();
                    break;
                case 3:
                    handleSearchAndBookTransport();
                    break;
                case 4:
                    handleSearchAndBookHotels();
                    break;
                case 5:
                    handleExplorePackages();
                    break;
                case 6:
                    handleUserAuthenticationAndDashboard();
                    break;
                case 7:
                    handleUserRegistration();
                    break;
                case 8:
                    dbConn.runDiagnosticTest();
                    break;
                case 0:
                    boolean confirmExit = InputValidator.readYesNo(scanner, "Are you sure you want to exit RAAHVO?");
                    if (confirmExit) {
                        running = false;
                        System.out.println(ConsoleUI.PURPLE + "\nThank you for travelling with RAAHVO. Every Route Has a Story. Safe travels!" + ConsoleUI.RESET);
                    }
                    break;
            }
        }
    }

    /**
     * Prints the primary application navigation menu
     */
    private static void printMainMenu() {
        System.out.println();
        ConsoleUI.printSectionHeader("MAIN NAVIGATION MENU - EVALUATION 1");
        if (auth.isAuthenticated()) {
            System.out.println(ConsoleUI.GREEN + "  [Logged in as: " + auth.getCurrentUser().getName() + " (" + auth.getCurrentUser().getRole() + ")]" + ConsoleUI.RESET);
        } else {
            System.out.println(ConsoleUI.YELLOW + "  [Browsing as: Guest Traveler]" + ConsoleUI.RESET);
        }
        System.out.println("  1. " + ConsoleUI.PURPLE + ConsoleUI.BOLD + "★ SMART TRIP PLANNER (Budget-Based Trip Calculation)" + ConsoleUI.RESET);
        System.out.println("  2. Explore Destinations & Estimated Costs");
        System.out.println("  3. Search & Book Transportation (Trains, Buses, Flights)");
        System.out.println("  4. Search & Book Hotels & Rooms");
        System.out.println("  5. Browse Curated Travel Packages");
        System.out.println("  6. " + (auth.isAuthenticated() ? "My Account & Bookings" : "User Login"));
        System.out.println("  7. Register New Account");
        System.out.println("  8. Test Database Connectivity (JDBC Database / In-Memory Mode)");
        System.out.println("  0. Exit Application");
        ConsoleUI.printHorizontalLine(78, '-');
    }

    // =========================================================================
    // FEATURE 1: SMART TRIP PLANNER (Main Unique Differentiating Feature)
    // Managed by: Member 3 (Planning Engine)
    // =========================================================================
    private static void handleSmartTripPlanner() {
        ConsoleUI.printSectionHeader("SMART BUDGET TRIP PLANNER");
        System.out.println("Tell us your travel vision, and our algorithm will tailor the best route, stay, and activities!");

        String startLocation = InputValidator.readNonEmptyString(scanner, "Enter Starting City (e.g., Delhi, Mumbai): ");

        System.out.println("\nAvailable Destinations:");
        List<Destination> destinations = destinationService.getAllDestinations();
        for (int i = 0; i < destinations.size(); i++) {
            Destination d = destinations.get(i);
            System.out.printf("  %d. %-15s (%-12s, %s) - Base: ₹%.0f/day\n", (i + 1), d.getName(), d.getCategory(), d.getState(), d.getBaseDailyCost());
        }

        int destChoice = InputValidator.readInt(scanner, "Select destination number (1-" + destinations.size() + "): ", 1, destinations.size());
        Destination selectedDest = destinations.get(destChoice - 1);

        int travellers = InputValidator.readInt(scanner, "Enter number of travellers (1-20): ", 1, 20);
        int days = InputValidator.readInt(scanner, "Enter trip duration in days (1-30): ", 1, 30);
        double budget = InputValidator.readDouble(scanner, "Enter your total planned budget in ₹: ", 500);

        String preference = InputValidator.readNonEmptyString(scanner, "Travel style preference (Budget, Standard, Luxury): ");

        // UI Loading Spinner
        ConsoleUI.showLoadingSpinner("Analyzing routes, hotel tariffs, and attraction fees...", 1200);

        try {
            // Core Algorithm Execution: Dynamically synthesizes real DB tariffs
            List<TripPlanner.DynamicPlanOption> options = tripPlanner.generateSmartPlans(startLocation, selectedDest, travellers, days, budget, preference);

            for (int i = 0; i < options.size(); i++) {
                options.get(i).printSummary(i + 1);
            }

            boolean anyFits = options.stream().anyMatch(o -> o.fitsBudget);
            if (!anyFits) {
                System.out.println();
                ConsoleUI.printWarning("All generated itineraries exceed your planned budget of ₹" + String.format("%.2f", budget));
                System.out.println(ConsoleUI.YELLOW + "  ★ Smart Recommendation: Consider booking the 'Budget' tier with standard transit," + ConsoleUI.RESET);
                System.out.println(ConsoleUI.YELLOW + "    or adjust your duration to " + Math.max(1, (int)(days * budget / options.get(0).breakdown.grandTotal)) + " days to stay strictly within budget." + ConsoleUI.RESET);
            } else {
                System.out.println(ConsoleUI.GREEN + "\n  ★ Great news! At least one itinerary fits completely within your ₹" + String.format("%.2f", budget) + " budget." + ConsoleUI.RESET);
            }

            System.out.println();
            boolean bookNow = InputValidator.readYesNo(scanner, "Would you like to book one of these generated itineraries right now?");
            if (bookNow) {
                int optionChoice = InputValidator.readInt(scanner, "Select option to book (1-" + options.size() + "): ", 1, options.size());
                TripPlanner.DynamicPlanOption chosen = options.get(optionChoice - 1);
                String transportSummary = (chosen.transport != null)
                        ? chosen.transport.getTransportType() + " - " + chosen.transport.getProviderName()
                        : "Intercity Rail / Road Transit";
                String hotelSummary = (chosen.hotel != null)
                        ? chosen.hotel.getHotelName() + " (" + chosen.hotel.getStarRating() + "-Star)"
                        : "Recommended Hotel in " + selectedDest.getName();

                executeBookingWorkflow("SMART_PLAN_" + chosen.tierName.toUpperCase(),
                        chosen.breakdown.grandTotal, travellers,
                        (chosen.transport != null ? chosen.transport.getTransportId() : null),
                        null,
                        selectedDest.getName(), transportSummary, hotelSummary);
            }

        } catch (InsufficientBudgetException e) {
            // Custom Exception handling for extreme budget deficit
            ConsoleUI.printError("BUDGET DEFICIT: " + e.getMessage());
            double deficit = e.getDeficit();
            System.out.println(ConsoleUI.RED + "  • Deficit Amount : ₹" + String.format("%.2f", deficit)
                    + " (Min Required: ₹" + String.format("%.2f", e.getEstimatedCost())
                    + " | Your Budget: ₹" + String.format("%.2f", e.getRequestedBudget()) + ")" + ConsoleUI.RESET);

            System.out.println("\n" + ConsoleUI.CYAN + ConsoleUI.BOLD + "--- SMART AFFORDABLE ALTERNATIVES & RECOMMENDATIONS ---" + ConsoleUI.RESET);
            int suggestedDays = Math.max(1, (int) (days * e.getRequestedBudget() / e.getEstimatedCost()));
            System.out.println("  1. " + ConsoleUI.BOLD + "Adjust Trip Duration:" + ConsoleUI.RESET
                    + " A " + suggestedDays + "-day trip to " + selectedDest.getName() + " fits within your ₹" + String.format("%.2f", e.getRequestedBudget()) + " budget.");
            System.out.println("  2. " + ConsoleUI.BOLD + "Transit & Stay Shift:" + ConsoleUI.RESET
                    + " Choose off-peak Sleeper Class train or State Express bus and standard rooms to save up to 40%.");
            
            // Smart Destination Suggestions
            System.out.println("  3. " + ConsoleUI.BOLD + "Budget-Friendly Alternate Destinations:" + ConsoleUI.RESET);
            List<Destination> budgetAlts = destinationService.getAllDestinations().stream()
                    .filter(d -> d.getDestinationId() != selectedDest.getDestinationId()
                            && d.getBaseDailyCost() <= selectedDest.getBaseDailyCost() * 0.85)
                    .sorted(Comparator.comparingDouble(Destination::getBaseDailyCost))
                    .limit(3)
                    .collect(java.util.stream.Collectors.toList());

            if (!budgetAlts.isEmpty()) {
                for (Destination alt : budgetAlts) {
                    System.out.printf("     • %-12s (%-14s) - Base: ₹%.0f/day (Category: %s)\n",
                            alt.getName(), alt.getState(), alt.getBaseDailyCost(), alt.getCategory());
                }
            } else {
                System.out.println("     • Explore nearby historical circuits with lower accommodation tariffs.");
            }
        }
    }

    // =========================================================================
    // FEATURE 2: EXPLORE DESTINATIONS
    // Managed by: Member 1
    // =========================================================================
    private static void handleExploreDestinations() {
        ConsoleUI.printSectionHeader("EXPLORE TOP DESTINATIONS");
        System.out.println("  1. View All Destinations (Alphabetical)");
        System.out.println("  2. View Popular Destinations (By Star Rating)");
        System.out.println("  3. Filter Destinations by Category (Hill Station, Beach, etc.)");
        int sub = InputValidator.readInt(scanner, "Choice: ", 1, 3);

        List<Destination> list = new ArrayList<>();
        if (sub == 1) {
            list = destinationService.getAllDestinations();
            Collections.sort(list); // Course Concept: Demonstrates Comparable<Destination>
        } else if (sub == 2) {
            list = destinationService.getPopularDestinations(); // Course Concept: Demonstrates Comparator & Stream API
        } else {
            String cat = InputValidator.readNonEmptyString(scanner, "Enter category (Hill Station, Beach, Heritage, Adventure, Pilgrimage, Nature): ");
            list = destinationService.filterByCategory(cat);
        }

        if (list.isEmpty()) {
            ConsoleUI.printWarning("No destinations found matching your criteria.");
            return;
        }

        System.out.printf("\n%-4s | %-14s | %-16s | %-12s | %-12s | %s\n", "ID", "Name", "State", "Category", "Est. Daily", "Rating");
        ConsoleUI.printHorizontalLine(84, '-');
        for (Destination d : list) {
            System.out.println(d.toString());
        }
    }

    // =========================================================================
    // FEATURE 3: TRANSPORTATION SEARCH & BOOKING
    // Managed by: Member 2
    // =========================================================================
    private static void handleSearchAndBookTransport() {
        ConsoleUI.printSectionHeader("SEARCH TRANSPORTATION (TRAINS, BUSES, FLIGHTS)");
        String src = InputValidator.readNonEmptyString(scanner, "Enter Source City (e.g. Delhi, Mumbai): ");
        String dest = InputValidator.readNonEmptyString(scanner, "Enter Destination City (e.g. Manali, Goa, Jaipur): ");
        String type = InputValidator.readNonEmptyString(scanner, "Preferred Mode (TRAIN, BUS, FLIGHT, or ALL): ");

        ConsoleUI.showLoadingSpinner("Fetching live schedules...", 800);
        List<Transport> results = transportService.searchTransports(src, dest, type);

        if (results.isEmpty()) {
            ConsoleUI.printWarning("No direct transport found between " + src + " and " + dest + ".");
            return;
        }

        System.out.println();
        for (int i = 0; i < results.size(); i++) {
            Transport t = results.get(i);
            System.out.printf("  [%d] %s\n", (i + 1), t.getTransportDetails());
        }

        boolean book = InputValidator.readYesNo(scanner, "Book one of these transport options?");
        if (book) {
            int sel = InputValidator.readInt(scanner, "Select transport number (or 0 to cancel): ", 0, results.size());
            if (sel == 0) return;
            Transport chosen = results.get(sel - 1);
            int pax = InputValidator.readInt(scanner, "Enter number of passengers: ", 1, chosen.getAvailableSeats());
            String travelClass = InputValidator.readNonEmptyString(scanner, "Enter class preference (e.g. Economy, 3A, Sleeper): ");

            // Course Concept: Runtime Polymorphism on calculateFare()
            double totalFare = chosen.calculateFare(pax, travelClass);
            String transportSummary = chosen.getTransportType() + " - " + chosen.getProviderName()
                    + " (" + chosen.getSourceCity() + " to " + chosen.getDestinationCity() + " | Dep: " + chosen.getDepartureTime() + ")";
            executeBookingWorkflow("TRANSPORT_" + chosen.getTransportType(), totalFare, pax, chosen.getTransportId(), null,
                    chosen.getDestinationCity(), transportSummary, "N/A (Transport Only Booking)");
        }
    }

    // =========================================================================
    // FEATURE 4: HOTEL SEARCH & BOOKING
    // Managed by: Member 2
    // =========================================================================
    private static void handleSearchAndBookHotels() {
        ConsoleUI.printSectionHeader("SEARCH HOTELS & ROOMS");
        String destName = InputValidator.readNonEmptyString(scanner, "Enter Destination Name (e.g. Manali, Goa, Jaipur): ");
        Optional<Destination> destOpt = destinationService.findByName(destName);

        if (!destOpt.isPresent()) {
            ConsoleUI.printWarning("Destination '" + destName + "' not recognized in system.");
            return;
        }

        Destination dest = destOpt.get();
        List<Hotel> hotels = transportService.getHotelsByDestination(dest.getDestinationId());

        if (hotels.isEmpty()) {
            ConsoleUI.printWarning("No registered hotels found in " + dest.getName() + ".");
            return;
        }

        System.out.printf("\n%-4s | %-30s | %-8s | %-12s | %s\n", "ID", "Hotel Name", "Rating", "Base Rate", "Amenities");
        ConsoleUI.printHorizontalLine(84, '-');
        for (Hotel h : hotels) {
            System.out.println(h.toString());
        }

        int hotelId = InputValidator.readInt(scanner, "\nEnter Hotel ID to view rooms (or 0 to return): ", 0, 99999);
        if (hotelId == 0) return;
        List<Room> rooms = transportService.getAvailableRoomsByHotel(hotelId);

        if (rooms.isEmpty()) {
            ConsoleUI.printWarning("No rooms currently available in this hotel.");
            return;
        }

        System.out.println("\nAvailable Rooms in Hotel #" + hotelId + ":");
        for (int i = 0; i < rooms.size(); i++) {
            System.out.printf("  [%d] %s\n", (i + 1), rooms.get(i).toString());
        }

        boolean book = InputValidator.readYesNo(scanner, "Book a room in this hotel?");
        if (book) {
            int roomChoice = InputValidator.readInt(scanner, "Select room number (or 0 to cancel): ", 0, rooms.size());
            if (roomChoice == 0) return;
            Room chosenRoom = rooms.get(roomChoice - 1);
            int nights = InputValidator.readInt(scanner, "Enter number of nights: ", 1, 30);
            int guests = InputValidator.readInt(scanner, "Enter number of guests: ", 1, chosenRoom.getMaxOccupancy());

            double totalFare = chosenRoom.getPricePerNight() * nights;
            Optional<Hotel> hOpt = transportService.findHotelById(hotelId);
            String hotelName = hOpt.isPresent() ? hOpt.get().getHotelName() : ("Hotel #" + hotelId);
            String hotelSummary = hotelName + " - Room " + chosenRoom.getRoomNumber() + " (" + chosenRoom.getRoomType() + ", " + nights + " nights)";
            executeBookingWorkflow("HOTEL_STAY", totalFare, guests, null, chosenRoom.getRoomId(),
                    dest.getName(), "N/A (Hotel Stay Booking Only)", hotelSummary);
        }
    }

    // =========================================================================
    // FEATURE 5: EXPLORE & COMPARE PACKAGES
    // Managed by: Member 3
    // =========================================================================
    private static void handleExplorePackages() {
        ConsoleUI.printSectionHeader("CURATED TRAVEL PACKAGES");
        String destName = InputValidator.readNonEmptyString(scanner, "Enter Destination Name (e.g. Manali, Goa, Jaipur): ");
        Optional<Destination> destOpt = destinationService.findByName(destName);

        if (!destOpt.isPresent()) {
            ConsoleUI.printWarning("Destination not found.");
            return;
        }

        int travellers = InputValidator.readInt(scanner, "Enter number of travellers: ", 1, 20);
        tripPlanner.displayPackageComparison(destOpt.get().getDestinationId(), travellers);

        List<TravelPackage> pkgs = tripPlanner.getPackagesByDestination(destOpt.get().getDestinationId());
        if (pkgs.isEmpty()) return;

        boolean book = InputValidator.readYesNo(scanner, "Book one of these packages?");
        if (book) {
            int pkgId = InputValidator.readInt(scanner, "Enter Package ID to book (or 0 to cancel): ", 0, 99999);
            if (pkgId == 0) return;
            TravelPackage chosen = pkgs.stream().filter(p -> p.getPackageId() == pkgId).findFirst().orElse(null);
            if (chosen != null) {
                double total = chosen.calculateTotalCost(travellers);
                String transportSummary = chosen.getPackageName() + " (" + chosen.getTier() + " Package Transit)";
                String hotelSummary = chosen.getInclusions() + " (" + chosen.getDurationDays() + " Days)";
                executeBookingWorkflow("PACKAGE_" + chosen.getTier().toUpperCase(), total, travellers, null, null,
                        destOpt.get().getName(), transportSummary, hotelSummary);
            } else {
                ConsoleUI.printError("Invalid Package ID selected.");
            }
        }
    }

    // =========================================================================
    // UNIVERSAL BOOKING WORKFLOW (Checkout, Coupons, Payment, Invoicing)
    // Managed by: Member 4
    // =========================================================================
    private static void executeBookingWorkflow(String bookingType, double totalAmount, int paxCount,
                                               Integer transportId, Integer roomId,
                                               String destinationName, String transportSummary, String hotelSummary) {
        ConsoleUI.printSectionHeader("CONFIRMATION & PAYMENT CHECKOUT");

        // Ensure user is authenticated or authenticate inline
        if (!auth.isAuthenticated()) {
            ConsoleUI.printWarning("You must be logged in to confirm a booking.");
            System.out.println("  1. Login with existing account");
            System.out.println("  2. Quick guest registration");
            int choice = InputValidator.readInt(scanner, "Choice: ", 1, 2);
            if (choice == 1) {
                if (!promptLogin()) return;
            } else {
                if (!promptQuickRegistration()) return;
            }
        }

        LocalDate travelDate = InputValidator.readFutureDate(scanner, "Enter scheduled travel date");

        // Collect passenger details
        List<Passenger> passengers = new ArrayList<>();
        System.out.println("\nPlease enter passenger details:");
        for (int i = 1; i <= paxCount; i++) {
            System.out.println("--- Passenger " + i + " ---");
            String name = InputValidator.readNonEmptyString(scanner, "Full Name: ");
            int age = InputValidator.readInt(scanner, "Age: ", 1, 100);
            String gender = InputValidator.readNonEmptyString(scanner, "Gender (Male/Female/Other): ");
            String idProof = InputValidator.readNonEmptyString(scanner, "Govt ID (Aadhaar/Passport/Driving License): ");
            passengers.add(new Passenger(0, 0, name, age, gender, "S-" + i, idProof));
        }

        // Coupon Handling (Member 3 Service)
        double discount = 0.0;
        boolean applyCoupon = InputValidator.readYesNo(scanner, "Do you have a promo discount coupon?");
        if (applyCoupon) {
            System.out.println("\nActive Coupons for you:");
            for (Coupon c : couponService.getAvailableCoupons()) {
                System.out.println("  • " + c.toString());
            }
            String code = InputValidator.readNonEmptyString(scanner, "Enter Coupon Code: ");
            try {
                discount = couponService.validateAndCalculateDiscount(code, totalAmount);
                ConsoleUI.printSuccess("Coupon applied! Discount of ₹" + String.format("%.2f", discount) + " granted!");
            } catch (CustomException e) {
                ConsoleUI.printWarning("Coupon rejected: " + e.getMessage());
            }
        }

        double finalPayable = Math.max(0, totalAmount - discount);
        System.out.println();
        ConsoleUI.printHorizontalLine(60, '-');
        System.out.printf("  Gross Booking Total : ₹%10.2f\n", totalAmount);
        System.out.printf("  Discount Applied    : -₹%10.2f\n", discount);
        System.out.printf("  " + ConsoleUI.BOLD + "FINAL PAYABLE       : ₹%10.2f" + ConsoleUI.RESET + "\n", finalPayable);
        ConsoleUI.printHorizontalLine(60, '-');

        // Simulated Payment Gateway (Member 4)
        System.out.println("\nSelect Simulated Payment Method:");
        System.out.println("  1. UPI (GooglePay / PhonePe / Paytm / BHIM)");
        System.out.println("  2. Credit Card");
        System.out.println("  3. Debit Card");
        System.out.println("  4. Net Banking");
        System.out.println("  5. Cash on Arrival / Travel Desk");
        int payMethodChoice = InputValidator.readInt(scanner, "Choice (1-5): ", 1, 5);
        String[] methods = {"UPI", "CREDIT_CARD", "DEBIT_CARD", "NET_BANKING", "CASH_ON_ARRIVAL"};
        String chosenMethod = methods[payMethodChoice - 1];

        // Generate simulated transaction reference
        String txnPrefix = chosenMethod.equals("CASH_ON_ARRIVAL") ? "CASH" : chosenMethod.substring(0, Math.min(4, chosenMethod.length()));
        String txnRef = "TXN-" + txnPrefix + "-" + (100000 + (int)(Math.random() * 900000));

        ConsoleUI.showLoadingSpinner("Processing payment authorization (" + chosenMethod + ")...", 1200);

        try {
            // Atomic transaction execution across Bookings, Passengers, Payments, and Seat/Room locks
            Booking booking = bookingService.createBooking(auth.getCurrentUser().getUserId(),
                    bookingType, travelDate, totalAmount, discount, passengers, chosenMethod, transportId, roomId,
                    destinationName, transportSummary, hotelSummary);

            ConsoleUI.printSuccess("BOOKING RESERVATION CONFIRMED SUCCESSFULLY!");
            ConsoleUI.printHighlight("Your Unique Booking Reference PNR: " + booking.getBookingPnr());
            System.out.println(ConsoleUI.GREEN + "  Payment Status  : SUCCESS (Verified)" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.GREEN + "  Transaction Ref : " + txnRef + " (Mode: " + chosenMethod + ")" + ConsoleUI.RESET);

            // Generate and export official invoice to disk
            Payment p = new Payment(0, booking.getBookingId(), txnRef, chosenMethod, finalPayable, "SUCCESS");
            Invoice invoice = new Invoice(booking, p);
            invoice.printToConsole();
            invoice.saveToFile();

        } catch (CustomException e) {
            ConsoleUI.printError("Booking failed: " + e.getMessage());
        }
    }

    // =========================================================================
    // FEATURE 6: USER LOGIN & DASHBOARD
    // Managed by: Member 1 & Member 4
    // =========================================================================
    private static void handleUserAuthenticationAndDashboard() {
        if (!auth.isAuthenticated()) {
            promptLogin();
        }

        if (!auth.isAuthenticated()) return;

        User user = auth.getCurrentUser();
        boolean inDashboard = true;
        while (inDashboard) {
            ConsoleUI.printSectionHeader("USER DASHBOARD - " + user.getName().toUpperCase());
            System.out.println("  1. View My Booking History & Invoices");
            System.out.println("  2. Cancel a Booking");
            System.out.println("  3. Logout");
            System.out.println("  0. Back to Main Menu");

            int choice = InputValidator.readInt(scanner, "Choice (0-3): ", 0, 3);
            switch (choice) {
                case 1:
                    bookingService.displayUserBookingHistory(user.getUserId());
                    break;
                case 2:
                    handleCancelBooking();
                    break;
                case 3:
                    auth.logout();
                    ConsoleUI.printSuccess("Logged out successfully.");
                    inDashboard = false;
                    break;
                case 0:
                    inDashboard = false;
                    break;
            }
        }
    }

    private static void handleCancelBooking() {
        ConsoleUI.printSectionHeader("CANCEL BOOKING");
        String pnr = InputValidator.readNonEmptyString(scanner, "Enter Booking PNR to cancel: ");
        String reason = InputValidator.readNonEmptyString(scanner, "Reason for cancellation: ");

        try {
            Cancellation cancellation = bookingService.cancelBooking(pnr, reason);
            ConsoleUI.printSuccess("Booking " + pnr + " has been cancelled.");
            System.out.println(ConsoleUI.YELLOW + "  • Cancellation Deduction (Policy): ₹" + String.format("%.2f", cancellation.getCancellationCharge()) + ConsoleUI.RESET);
            System.out.println(ConsoleUI.GREEN + "  • Refund Eligible: ₹" + String.format("%.2f", cancellation.getRefundEligible()) + " (Simulated refund initiated)" + ConsoleUI.RESET);
        } catch (CustomException e) {
            ConsoleUI.printError("Cancellation failed: " + e.getMessage());
        }
    }

    private static boolean promptLogin() {
        ConsoleUI.printSectionHeader("USER LOGIN");
        String email = InputValidator.readEmail(scanner, "Email: ");
        String password = InputValidator.readNonEmptyString(scanner, "Password: ");
        try {
            User u = auth.login(email, password);
            ConsoleUI.printSuccess("Welcome back, " + u.getName() + "!");
            return true;
        } catch (AuthenticationException e) {
            ConsoleUI.printError("Authentication failed: " + e.getMessage());
            return false;
        }
    }

    private static boolean promptQuickRegistration() {
        ConsoleUI.printSectionHeader("QUICK REGISTRATION");
        String name = InputValidator.readNonEmptyString(scanner, "Full Name: ");
        String email = InputValidator.readEmail(scanner, "Email: ");
        String phone = InputValidator.readPhone(scanner, "Mobile Number (10 digits): ");
        String password = InputValidator.readNonEmptyString(scanner, "Create Password: ");

        try {
            User u = registration.registerUser(name, email, phone, password);
            auth.login(email, password);
            ConsoleUI.printSuccess("Registration complete! Welcome to RAAHVO, " + u.getName() + "!");
            return true;
        } catch (AuthenticationException e) {
            ConsoleUI.printError("Registration failed: " + e.getMessage());
            return false;
        }
    }

    private static void handleUserRegistration() {
        promptQuickRegistration();
    }
}
