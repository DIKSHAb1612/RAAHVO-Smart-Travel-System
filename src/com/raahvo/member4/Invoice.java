package com.raahvo.member4;

import com.raahvo.common.ConsoleUI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 4 - Booking & Payment
 * Evaluation 1 Class (6 of 6 for Member 4)
 * ============================================================================
 * Class: Invoice
 * Course Concepts:
 * - String and StringBuilder Manipulation (dynamic text receipt construction)
 * - Java File Handling (BufferedWriter, FileWriter, automatic directory creation)
 * - Exception Handling (try-with-resources for file writer auto-close)
 * 
 * Purpose:
 * Generates an official customer travel receipt containing Booking ID (PNR),
 * destination, transport, hotel, passenger details, gross fare, coupon deductions,
 * net payable, and payment references.
 * Automatically saves the receipt to disk as a text file in `invoices/INVOICE_<PNR>.txt`.
 */
public class Invoice {
    private final Booking booking;
    private final Payment payment;

    public Invoice(Booking booking, Payment payment) {
        this.booking = booking;
        this.payment = payment;
    }

    /**
     * Course Concept: StringBuilder
     * Efficiently builds multi-line formatted invoice document
     */
    public String generateInvoiceContent() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

        sb.append("===============================================================================\n");
        sb.append("                                R A A H V O                                    \n");
        sb.append("               Smart Travel Booking & Budget Trip Planning System               \n");
        sb.append("                       \"Every Route Has a Story.\"                              \n");
        sb.append("===============================================================================\n");
        sb.append(String.format(" INVOICE REF  : %-25s ISSUE DATE  : %s\n",
                "INV-" + booking.getBookingPnr(), booking.getBookingDate().format(dtf)));
        sb.append(String.format(" BOOKING ID   : %-25s STATUS      : %s\n",
                booking.getBookingPnr(), booking.getStatus()));
        sb.append(String.format(" BOOKING TYPE : %-25s TRAVEL DATE : %s\n",
                booking.getBookingType(), booking.getTravelDate().format(df)));
        sb.append("-------------------------------------------------------------------------------\n");
        sb.append(" ITINERARY SPECIFICS:\n");
        sb.append(String.format("   Destination : %s\n", booking.getDestinationName()));
        sb.append(String.format("   Transport   : %s\n", booking.getTransportSummary()));
        sb.append(String.format("   Hotel / Stay: %s\n", booking.getHotelSummary()));
        sb.append("-------------------------------------------------------------------------------\n");
        sb.append(" PASSENGER DETAILS:\n");
        sb.append(String.format(" %-4s | %-26s | %-5s | %-8s | %-12s\n", "S.No", "Passenger Name", "Age", "Gender", "Seat / ID"));
        sb.append("-------------------------------------------------------------------------------\n");

        int idx = 1;
        for (Passenger p : booking.getPassengers()) {
            sb.append(String.format(" %-4d | %-26s | %-5d | %-8s | %-12s\n",
                    idx++, p.getFullName(), p.getAge(), p.getGender(),
                    (p.getSeatNumber() != null ? p.getSeatNumber() : p.getIdProof())));
        }

        sb.append("-------------------------------------------------------------------------------\n");
        sb.append(" PAYMENT SUMMARY:\n");
        sb.append(String.format("   Gross Booking Fare       : ₹ %10.2f\n", booking.getTotalAmount()));
        sb.append(String.format("   Discount / Coupon Applied: -₹%10.2f\n", booking.getDiscountAmount()));
        sb.append("   --------------------------------------------\n");
        sb.append(String.format("   TOTAL PAID AMOUNT        : ₹ %10.2f\n", booking.getFinalPayable()));
        if (payment != null) {
            sb.append(String.format("   Payment Method           : %s\n", payment.getPaymentMethod()));
            sb.append(String.format("   Transaction Reference    : %s\n", payment.getTransactionRef()));
            sb.append(String.format("   Payment Status           : %s\n", payment.getPaymentStatus()));
        }
        sb.append("===============================================================================\n");
        sb.append("      Thank you for choosing RAAHVO! Wishing you an unforgettable journey!     \n");
        sb.append("         Support: support@raahvo.com | Emergency Helpline: 1800-RAAHVO-24      \n");
        sb.append("===============================================================================\n");

        return sb.toString();
    }

    public void printToConsole() {
        System.out.println(ConsoleUI.CYAN + generateInvoiceContent() + ConsoleUI.RESET);
    }

    /**
     * Course Concept: Java File Handling (I/O)
     * Writes invoice content to persistent text file in `invoices/` directory.
     * Uses try-with-resources to automatically close the file stream even if an exception occurs.
     * 
     * @return true if file write succeeded, false otherwise
     */
    public boolean saveToFile() {
        File dir = new File("invoices");
        // Create directory if it does not already exist
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, "INVOICE_" + booking.getBookingPnr() + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(generateInvoiceContent());
            ConsoleUI.printSuccess("Official Invoice exported to: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            ConsoleUI.printError("Failed to save invoice file: " + e.getMessage());
            return false;
        }
    }
}
