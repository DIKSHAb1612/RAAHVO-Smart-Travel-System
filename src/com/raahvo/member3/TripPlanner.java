package com.raahvo.member3;

import com.raahvo.common.ConsoleUI;
import com.raahvo.common.InMemoryDatabase;
import com.raahvo.exception.InsufficientBudgetException;
import com.raahvo.member1.Destination;
import com.raahvo.member2.Hotel;
import com.raahvo.member2.Transport;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 3 - Trip Planning & Packages
 * ============================================================================
 * Class: TripPlanner
 * Course Concepts:
 * - Complex Algorithmic Data Synthesis (Real Entity Integration)
 * - Custom Exceptions (InsufficientBudgetException)
 * - Collections & Stream API (.filter, .sorted, .limit, .mapToDouble)
 * - Financial Calculation & Multi-Tier Categorization
 * 
 * CORE UNIQUE DIFFERENTIATING FEATURE OF RAAHVO:
 * Unlike ordinary academic projects that return hardcoded static text,
 * TripPlanner queries live database records for transports, hotels, and sightseeing
 * spots connecting the user's specific starting city and destination.
 * 
 * It dynamically computes 3 customized options:
 * 1. BUDGET PLAN   : Low-cost Bus/Sleeper Train + Standard Hotel/Hostel + Free/low-cost sightseeing
 * 2. STANDARD PLAN : Express Train / Volvo Coach + 3/4-Star Resort + Premier sights
 * 3. PREMIUM PLAN  : Flight / Luxury Transport + 4/5-Star Palace Suite + Full sightseeing
 * 
 * For each tier, it calculates base tariffs, 5% Transport GST, 12% Hotel GST,
 * and estimated meal costs, comparing the grand total against the user's budget.
 */
public class TripPlanner {
    private final InMemoryDatabase inMemoryDb;

    public TripPlanner() {
        this.inMemoryDb = InMemoryDatabase.getInstance();
    }

    /**
     * Inner Data Structure holding a dynamically calculated plan option
     */
    public static class DynamicPlanOption {
        public String tierName;                         // Budget, Standard, Premium
        public Transport transport;
        public Hotel hotel;
        public List<Sightseeing> spots = new ArrayList<>();
        public BudgetCalculator.Breakdown breakdown;    // Itemized financial ledger
        public boolean fitsBudget;                      // Budget compliance flag

        /**
         * Formatted visual console presentation of the plan
         */
        public void printSummary(int index) {
            String badge = fitsBudget
                    ? ConsoleUI.GREEN + ConsoleUI.BOLD + "[✓ WITHIN BUDGET]" + ConsoleUI.RESET
                    : ConsoleUI.RED + ConsoleUI.BOLD + "[✗ EXCEEDS BUDGET by ₹" + String.format("%.2f", Math.abs(breakdown.balanceOrDeficit)) + "]" + ConsoleUI.RESET;

            System.out.println();
            ConsoleUI.printHorizontalLine(78, '=');
            System.out.printf("  OPTION %d: " + ConsoleUI.BOLD + ConsoleUI.CYAN + "%s PLAN" + ConsoleUI.RESET + "  %s\n",
                    index, tierName.toUpperCase(), badge);
            ConsoleUI.printHorizontalLine(78, '=');

            if (transport != null) {
                System.out.println("  ✈ Transport  : " + transport.getTransportType() + " - " + transport.getProviderName()
                        + " (Dep: " + transport.getDepartureTime() + ", Arr: " + transport.getArrivalTime() + ")");
            } else {
                System.out.println("  ✈ Transport  : Self-drive / Local State Intercity Service");
            }

            if (hotel != null) {
                System.out.println("  🏨 Stay       : " + hotel.getHotelName() + " (" + hotel.getStarRating() + "-Star) - "
                        + hotel.getAddress());
            }

            System.out.println("  🗺 Sightseeing: " + (spots.isEmpty() ? "Self-guided exploration" : spots.stream().map(Sightseeing::getSpotName).collect(Collectors.joining(", "))));

            System.out.println();
            System.out.printf("  • Transport Base (Both Ways/Pax) : ₹%-9.2f (GST 5%%: ₹%.2f)\n", breakdown.transportBase, breakdown.transportGst);
            System.out.printf("  • Hotel Stay Base                : ₹%-9.2f (GST 12%%: ₹%.2f)\n", breakdown.hotelBase, breakdown.hotelGst);
            System.out.printf("  • Sightseeing Entry Tickets      : ₹%-9.2f\n", breakdown.sightseeingTotal);
            System.out.printf("  • Estimated Dining & Commute     : ₹%-9.2f\n", breakdown.estimatedMealsAndLocalCommute);
            ConsoleUI.printHorizontalLine(78, '-');
            System.out.printf("  " + ConsoleUI.BOLD + "ESTIMATED GRAND TOTAL            : ₹%-9.2f" + ConsoleUI.RESET + " (Your Budget: ₹%.2f)\n",
                    breakdown.grandTotal, breakdown.userBudget);

            if (fitsBudget) {
                System.out.println(ConsoleUI.GREEN + "  ★ Savings Cushion                : +₹" + String.format("%.2f", breakdown.balanceOrDeficit) + ConsoleUI.RESET);
            } else {
                System.out.println(ConsoleUI.YELLOW + "  ⚠ Warning: Exceeds your budget. Consider selecting a lower tier or increasing funds." + ConsoleUI.RESET);
            }
        }
    }

    /**
     * Core Algorithm: Synthesizes real transports, hotels, and attractions into
     * 3 personalized travel options compared against the user's financial ceiling.
     * 
     * @param startingCity User's starting origin city
     * @param destination Destination selected
     * @param travellers Headcount of travellers
     * @param days Trip duration in days
     * @param userBudget Financial budget in ₹
     * @param preference Travel style preference
     * @return List of 3 computed DynamicPlanOptions
     * @throws InsufficientBudgetException if user's budget is critically lower than the minimum possible plan
     */
    public List<DynamicPlanOption> generateSmartPlans(String startingCity, Destination destination,
                                                      int travellers, int days, double userBudget,
                                                      String preference) throws InsufficientBudgetException {

        int destId = destination.getDestinationId();

        // 1. Fetch available transport from Starting City to Destination using Stream API
        List<Transport> availableTransports = inMemoryDb.transports.stream()
                .filter(t -> t.getSourceCity().equalsIgnoreCase(startingCity.trim())
                        && t.getDestinationCity().equalsIgnoreCase(destination.getName().trim()))
                .collect(Collectors.toList());

        // 2. Fetch available hotels in Destination, sorted ascending by price
        List<Hotel> availableHotels = inMemoryDb.hotels.stream()
                .filter(h -> h.getDestinationId() == destId)
                .sorted(Comparator.comparingDouble(Hotel::getBasePricePerNight))
                .collect(Collectors.toList());

        // 3. Fetch sightseeing spots linked to destination
        List<Sightseeing> spots = inMemoryDb.sightseeingSpots.stream()
                .filter(s -> s.getDestinationId() == destId)
                .collect(Collectors.toList());

        List<DynamicPlanOption> options = new ArrayList<>();

        // ====================================================================
        // TIER 1: BUDGET PLAN SYNTHESIS
        // ====================================================================
        DynamicPlanOption budgetOption = new DynamicPlanOption();
        budgetOption.tierName = "Budget";
        // Prefer bus or cheapest transport
        budgetOption.transport = availableTransports.stream()
                .filter(t -> "BUS".equalsIgnoreCase(t.getTransportType()) || "TRAIN".equalsIgnoreCase(t.getTransportType()))
                .min(Comparator.comparingDouble(Transport::getBasePrice))
                .orElse(availableTransports.isEmpty() ? null : availableTransports.get(0));

        budgetOption.hotel = availableHotels.isEmpty() ? null : availableHotels.get(0); // Most economical hotel
        budgetOption.spots = spots.stream().limit(2).collect(Collectors.toList());       // Top 2 essential spots

        // Mathematical Fare computation: round-trip (x2) for all travellers
        double transCost1 = (budgetOption.transport != null) ? (budgetOption.transport.getBasePrice() * travellers * 2) : (600.0 * travellers * 2);
        // Room calculation: 2 guests per room
        double hotelCost1 = (budgetOption.hotel != null) ? (budgetOption.hotel.getBasePricePerNight() * Math.ceil((double) travellers / 2) * days) : (1000.0 * days);
        double sightCost1 = budgetOption.spots.stream().mapToDouble(s -> s.getEntryFee() * travellers).sum();
        double dailyFood1 = 450.0; // Economical meals/local commute estimate per person per day

        budgetOption.breakdown = BudgetCalculator.computeBreakdown(transCost1, hotelCost1, sightCost1, travellers, days, dailyFood1, userBudget);
        budgetOption.fitsBudget = budgetOption.breakdown.isSurplus;
        options.add(budgetOption);

        // ====================================================================
        // TIER 2: STANDARD PLAN SYNTHESIS
        // ====================================================================
        DynamicPlanOption stdOption = new DynamicPlanOption();
        stdOption.tierName = "Standard";
        // Prefer AC Train or mid-tier coach
        stdOption.transport = availableTransports.stream()
                .filter(t -> "TRAIN".equalsIgnoreCase(t.getTransportType()))
                .findFirst()
                .orElse(availableTransports.size() > 1 ? availableTransports.get(1) : (availableTransports.isEmpty() ? null : availableTransports.get(0)));

        stdOption.hotel = availableHotels.size() > 1 ? availableHotels.get(1) : (availableHotels.isEmpty() ? null : availableHotels.get(0));
        stdOption.spots = spots.stream().limit(3).collect(Collectors.toList());

        double transCost2 = (stdOption.transport != null) ? (stdOption.transport.getBasePrice() * travellers * 2 * 1.3) : (1500.0 * travellers * 2);
        double hotelCost2 = (stdOption.hotel != null) ? (stdOption.hotel.getBasePricePerNight() * Math.ceil((double) travellers / 2) * days) : (2200.0 * days);
        double sightCost2 = stdOption.spots.stream().mapToDouble(s -> s.getEntryFee() * travellers).sum();
        double dailyFood2 = 800.0; // Comfortable dining per person per day

        stdOption.breakdown = BudgetCalculator.computeBreakdown(transCost2, hotelCost2, sightCost2, travellers, days, dailyFood2, userBudget);
        stdOption.fitsBudget = stdOption.breakdown.isSurplus;
        options.add(stdOption);

        // ====================================================================
        // TIER 3: PREMIUM PLAN SYNTHESIS
        // ====================================================================
        DynamicPlanOption premOption = new DynamicPlanOption();
        premOption.tierName = "Premium";
        // Prefer flight or premier transport
        premOption.transport = availableTransports.stream()
                .filter(t -> "FLIGHT".equalsIgnoreCase(t.getTransportType()))
                .findFirst()
                .orElse(availableTransports.stream().max(Comparator.comparingDouble(Transport::getBasePrice)).orElse(null));

        premOption.hotel = availableHotels.stream().max(Comparator.comparingDouble(Hotel::getBasePricePerNight)).orElse(null);
        premOption.spots = new ArrayList<>(spots); // Full itinerary sightseeing

        double transCost3 = (premOption.transport != null) ? (premOption.transport.getBasePrice() * travellers * 2 * 1.5) : (3500.0 * travellers * 2);
        double hotelCost3 = (premOption.hotel != null) ? (premOption.hotel.getBasePricePerNight() * Math.ceil((double) travellers / 2) * days) : (4500.0 * days);
        double sightCost3 = premOption.spots.stream().mapToDouble(s -> s.getEntryFee() * travellers).sum();
        double dailyFood3 = 1400.0; // Luxury dining & private cab estimate per person per day

        premOption.breakdown = BudgetCalculator.computeBreakdown(transCost3, hotelCost3, sightCost3, travellers, days, dailyFood3, userBudget);
        premOption.fitsBudget = premOption.breakdown.isSurplus;
        options.add(premOption);

        // Course Concept: Exception Handling (Deficit Threshold)
        // If even the most economical tier requires more than 140% of the user's budget, throw custom exception
        if (!budgetOption.fitsBudget && userBudget < (budgetOption.breakdown.grandTotal * 0.6)) {
            throw new InsufficientBudgetException(userBudget, budgetOption.breakdown.grandTotal,
                    "Allocated budget of ₹" + userBudget + " is significantly lower than minimum required ₹"
                            + String.format("%.2f", budgetOption.breakdown.grandTotal) + " for " + days + " days trip to " + destination.getName() + ".");
        }

        return options;
    }

    /**
     * Retrieves all curated packages for a destination, ordered by base price
     */
    public List<TravelPackage> getPackagesByDestination(int destinationId) {
        return inMemoryDb.packages.stream()
                .filter(p -> p.getDestinationId() == destinationId)
                .sorted(Comparator.comparingDouble(TravelPackage::getPricePerPerson))
                .collect(Collectors.toList());
    }

    /**
     * Displays a terminal comparison table of all packages
     */
    public void displayPackageComparison(int destinationId, int travellers) {
        List<TravelPackage> packages = getPackagesByDestination(destinationId);
        if (packages.isEmpty()) {
            ConsoleUI.printWarning("No curated packages found for this destination.");
            return;
        }

        ConsoleUI.printSectionHeader("CURATED PACKAGES COMPARISON (FOR " + travellers + " TRAVELLER" + (travellers > 1 ? "S" : "") + ")");
        System.out.printf("%-4s | %-28s | %-8s | %-6s | %-12s | %-14s\n", "ID", "Package Name", "Tier", "Days", "Per Person", "Total (" + travellers + " Pax)");
        ConsoleUI.printHorizontalLine(84, '-');

        for (TravelPackage p : packages) {
            double total = p.calculateTotalCost(travellers);
            System.out.printf("%-4d | %-28s | %-8s | %-6d | ₹%-10.2f | ₹%-12.2f\n",
                    p.getPackageId(), p.getPackageName(), p.getTier(), p.getDurationDays(), p.getPricePerPerson(), total);
            System.out.println("     Inclusions: " + p.getInclusions());
        }
        ConsoleUI.printHorizontalLine(84, '-');
    }
}
