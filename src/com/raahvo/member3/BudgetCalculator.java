package com.raahvo.member3;

import com.raahvo.common.ConsoleUI;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 3 - Trip Planning & Packages
 * ============================================================================
 * Class: BudgetCalculator
 * Course Concepts:
 * - Mathematical Ledger Formatting & Business Rule Computation
 * - Static Nested Classes (Breakdown)
 * - Financial Tax Regulations (GST rates: 5% Transport, 12% Hotel)
 * 
 * Purpose:
 * Provides transparent, itemized cost auditing for itineraries.
 * Computes base costs, mandatory taxes, daily meal buffers, and cost-reduction tips.
 */
public class BudgetCalculator {
    // Government Tax Regulations
    public static final double TRANSPORT_GST_RATE = 0.05; // 5% GST on commercial travel
    public static final double HOTEL_GST_RATE = 0.12;    // 12% GST on hospitality lodging

    /**
     * Nested Value Object representing an audited itinerary financial ledger
     */
    public static class Breakdown {
        public double transportBase;
        public double transportGst;
        public double hotelBase;
        public double hotelGst;
        public double sightseeingTotal;
        public double estimatedMealsAndLocalCommute;
        public double subtotal;
        public double totalTaxes;
        public double grandTotal;
        public double userBudget;
        public double balanceOrDeficit;
        public boolean isSurplus;

        /**
         * Renders a clean terminal cost breakdown table
         */
        public void printFormattedReport() {
            ConsoleUI.printSectionHeader("DETAILED BUDGET & EXPENSE BREAKDOWN");
            System.out.printf("  %-38s : ₹%10.2f\n", "1. Transportation Base Fare", transportBase);
            System.out.printf("  %-38s : ₹%10.2f\n", "   Transport GST (5%%)", transportGst);
            System.out.printf("  %-38s : ₹%10.2f\n", "2. Accommodation Total Stay", hotelBase);
            System.out.printf("  %-38s : ₹%10.2f\n", "   Hotel Hospitality GST (12%%)", hotelGst);
            System.out.printf("  %-38s : ₹%10.2f\n", "3. Sightseeing & Attraction Fees", sightseeingTotal);
            System.out.printf("  %-38s : ₹%10.2f\n", "4. Estimated Meals & Local Commute", estimatedMealsAndLocalCommute);
            ConsoleUI.printHorizontalLine(60, '-');
            System.out.printf("  %-38s : ₹%10.2f\n", "SUBTOTAL (Base Expenses)", subtotal);
            System.out.printf("  %-38s : ₹%10.2f\n", "TOTAL TAXES & CESS", totalTaxes);
            ConsoleUI.printHorizontalLine(60, '=');
            System.out.printf("  " + ConsoleUI.BOLD + "%-38s : ₹%10.2f" + ConsoleUI.RESET + "\n", "ESTIMATED GRAND TOTAL", grandTotal);
            System.out.printf("  %-38s : ₹%10.2f\n", "USER ALLOCATED BUDGET", userBudget);
            ConsoleUI.printHorizontalLine(60, '-');

            if (isSurplus) {
                System.out.printf("  " + ConsoleUI.GREEN + ConsoleUI.BOLD + "%-38s : +₹%10.2f (Surplus)" + ConsoleUI.RESET + "\n",
                        "BUDGET STATUS", balanceOrDeficit);
            } else {
                System.out.printf("  " + ConsoleUI.RED + ConsoleUI.BOLD + "%-38s : -₹%10.2f (DEFICIT)" + ConsoleUI.RESET + "\n",
                        "BUDGET STATUS", Math.abs(balanceOrDeficit));
            }
            ConsoleUI.printHorizontalLine(60, '-');
        }
    }

    /**
     * Pure calculation function generating an audited Breakdown instance
     */
    public static Breakdown computeBreakdown(double transportBase, double hotelBase,
                                            double sightseeingTotal, int travellers,
                                            int days, double dailyFoodEstimate, double userBudget) {
        Breakdown b = new Breakdown();
        b.transportBase = transportBase;
        b.transportGst = transportBase * TRANSPORT_GST_RATE;

        b.hotelBase = hotelBase;
        b.hotelGst = hotelBase * HOTEL_GST_RATE;

        b.sightseeingTotal = sightseeingTotal;
        b.estimatedMealsAndLocalCommute = dailyFoodEstimate * travellers * days;

        b.subtotal = b.transportBase + b.hotelBase + b.sightseeingTotal + b.estimatedMealsAndLocalCommute;
        b.totalTaxes = b.transportGst + b.hotelGst;
        b.grandTotal = b.subtotal + b.totalTaxes;
        b.userBudget = userBudget;

        b.balanceOrDeficit = userBudget - b.grandTotal;
        b.isSurplus = b.balanceOrDeficit >= 0;
        return b;
    }

    /**
     * Generates intelligent, actionable cost-cutting advice if user faces a budget deficit
     */
    public static void printCostOptimizationTips(Breakdown b) {
        if (!b.isSurplus) {
            ConsoleUI.printWarning("Your planned itinerary exceeds your allocated budget by ₹" + String.format("%.2f", Math.abs(b.balanceOrDeficit)) + "!");
            System.out.println(ConsoleUI.YELLOW + "  Smart Optimization Recommendations:" + ConsoleUI.RESET);
            System.out.println("  1. Switch flight to a Superfast Train or Sleeper Bus to save up to 60% on transport.");
            System.out.println("  2. Opt for standard/boutique rooms instead of luxury suites to save ₹1,000 - ₹2,500/night.");
            System.out.println("  3. Apply an active discount coupon (e.g. RAAHVO10 or FIRSTTRIP) at checkout.");
            System.out.println("  4. Reduce trip duration by 1 day or explore our pre-calculated 'Budget' package.");
        } else {
            ConsoleUI.printSuccess("Great news! Your itinerary is comfortably within budget with a ₹"
                    + String.format("%.2f", b.balanceOrDeficit) + " surplus cushion for shopping & souvenirs!");
        }
    }
}
