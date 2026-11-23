package com.raahvo.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Common Infrastructure
 * ============================================================================
 * Class: InputValidator
 * Course Concepts:
 * - Regular Expressions (Pattern & Matcher for email & mobile validation)
 * - Java Time API (LocalDate, DateTimeFormatter, DateTimeParseException)
 * - Defensive Programming (Crash-Proof Console Scanner Input)
 * - Wrapper Classes & Parsing (Integer.parseInt, Double.parseDouble)
 * 
 * Purpose:
 * Prevents java.util.InputMismatchException and console application crashes.
 * Enforces business rules (e.g. mobile must start with 6-9, dates cannot be past).
 */
public class InputValidator {
    // Standard RFC-compliant email regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    
    // Indian 10-digit mobile number pattern (starts with 6, 7, 8, or 9)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    
    // ISO standard travel date formatter
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Safely reads an integer within bounds [min, max] without crashing on non-numeric input
     */
    public static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(ConsoleUI.WHITE + prompt + ConsoleUI.RESET);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    ConsoleUI.printWarning("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                ConsoleUI.printError("Invalid number format. Please enter an integer.");
            }
        }
    }

    /**
     * Safely reads a floating-point double amount >= min
     */
    public static double readDouble(Scanner scanner, String prompt, double min) {
        while (true) {
            System.out.print(ConsoleUI.WHITE + prompt + ConsoleUI.RESET);
            String line = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value < min) {
                    ConsoleUI.printWarning("Value must be at least " + min + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                ConsoleUI.printError("Invalid decimal amount. Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a mandatory non-empty text string
     */
    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(ConsoleUI.WHITE + prompt + ConsoleUI.RESET);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                ConsoleUI.printWarning("Field cannot be empty. Please enter a value.");
                continue;
            }
            return input;
        }
    }

    /**
     * Validates email format using regular expressions
     */
    public static String readEmail(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(ConsoleUI.WHITE + prompt + ConsoleUI.RESET);
            String email = scanner.nextLine().trim();
            if (EMAIL_PATTERN.matcher(email).matches()) {
                return email;
            }
            ConsoleUI.printError("Invalid email format! Example: user@example.com");
        }
    }

    /**
     * Validates 10-digit mobile number format using regular expressions
     */
    public static String readPhone(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(ConsoleUI.WHITE + prompt + ConsoleUI.RESET);
            String phone = scanner.nextLine().trim();
            if (PHONE_PATTERN.matcher(phone).matches()) {
                return phone;
            }
            ConsoleUI.printError("Invalid phone number! Must be a 10-digit mobile number starting with 6-9.");
        }
    }

    /**
     * Reads and validates that a journey date is in format YYYY-MM-DD and not in the past
     */
    public static LocalDate readFutureDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(ConsoleUI.WHITE + prompt + " (YYYY-MM-DD): " + ConsoleUI.RESET);
            String input = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
                // Enforce business rule: travel date cannot be in the past
                if (date.isBefore(LocalDate.now())) {
                    ConsoleUI.printWarning("Journey date cannot be in the past! Please enter today or a future date.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                ConsoleUI.printError("Invalid date format! Please use YYYY-MM-DD (e.g., 2026-12-25).");
            }
        }
    }

    /**
     * Reads a binary Yes/No confirmation from the user
     */
    public static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(ConsoleUI.WHITE + prompt + " (Y/N): " + ConsoleUI.RESET);
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y") || input.equals("YES")) {
                return true;
            }
            if (input.equals("N") || input.equals("NO")) {
                return false;
            }
            ConsoleUI.printWarning("Please enter 'Y' for Yes or 'N' for No.");
        }
    }
}
