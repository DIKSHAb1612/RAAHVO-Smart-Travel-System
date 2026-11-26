package com.raahvo.common;

/**
 * ConsoleUI provides consistent ANSI styling, banners, formatted tables,
 * alert boxes, and visual presentation across the entire RAAHVO application.
 */
public class ConsoleUI {
    // ANSI Colors
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String PURPLE = "\u001B[35m";
    public static final String BLUE = "\u001B[34m";
    public static final String WHITE = "\u001B[37m";

    // Backgrounds
    public static final String BG_CYAN = "\u001B[46;30m";
    public static final String BG_BLUE = "\u001B[44;37m";

    public static void printBanner() {
        System.out.println(CYAN + BOLD);
        System.out.println("  ================================================================================");
        System.out.println("     ____         _           _        _   _  __     __   ___  ");
        System.out.println("    |  _ \\       / \\         / \\      | | | | \\ \\   / /  / _ \\ ");
        System.out.println("    | |_) |     / _ \\       / _ \\     | |_| |  \\ \\ / /  | | | |");
        System.out.println("    |  _ <     / ___ \\     / ___ \\    |  _  |   \\ V /   | | | |");
        System.out.println("    |_| \\_\\   /_/   \\_\\   /_/   \\_\\   |_| |_|    \\_/     \\___/ ");
        System.out.println("  ================================================================================");
        System.out.println("            SMART TRAVEL BOOKING & BUDGET TRIP PLANNING SYSTEM            ");
        System.out.println(PURPLE + BOLD + "                    \"Every Route Has a Story.\"                            " + RESET);
        System.out.println(CYAN + "  ================================================================================" + RESET);
    }

    public static void printSectionHeader(String title) {
        System.out.println();
        System.out.println(CYAN + BOLD + ">>> " + title.toUpperCase() + " <<<" + RESET);
        printHorizontalLine(78, '-');
    }

    public static void printHorizontalLine(int length, char ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ch);
        }
        System.out.println(CYAN + sb.toString() + RESET);
    }

    public static void printSuccess(String message) {
        System.out.println(GREEN + BOLD + "[SUCCESS] " + RESET + GREEN + message + RESET);
    }

    public static void printWarning(String message) {
        System.out.println(YELLOW + BOLD + "[WARNING] " + RESET + YELLOW + message + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED + BOLD + "[ERROR] " + RESET + RED + message + RESET);
    }

    public static void printInfo(String message) {
        System.out.println(BLUE + BOLD + "[INFO] " + RESET + WHITE + message + RESET);
    }

    public static void printHighlight(String message) {
        System.out.println(PURPLE + BOLD + "[★] " + message + RESET);
    }

    /**
     * Multithreaded simulated loading animation for smooth console UX
     */
    public static void showLoadingSpinner(String taskName, int durationMs) {
        char[] spinnerChars = new char[]{'|', '/', '-', '\\'};
        long endTime = System.currentTimeMillis() + durationMs;
        int i = 0;
        System.out.print(CYAN + "  " + taskName + " " + RESET);
        while (System.currentTimeMillis() < endTime) {
            System.out.print(spinnerChars[i % spinnerChars.length]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            System.out.print("\b");
            i++;
        }
        System.out.println(GREEN + "[DONE]" + RESET);
    }

    /**
     * Standalone UI demo runner for testing UI components independently
     */
    public static void main(String[] args) {
        printBanner();
        printSectionHeader("CONSOLE UI COMPONENT PREVIEW & VIVA DEMO");
        printSuccess("This is a green [SUCCESS] alert.");
        printWarning("This is a yellow [WARNING] alert.");
        printError("This is a red [ERROR] alert.");
        printInfo("This is a blue/white [INFO] badge.");
        printHighlight("This is a purple [★] highlight banner.");
        showLoadingSpinner("Simulating multithreaded loading spinner...", 1200);
        printHorizontalLine(78, '=');
        System.out.println("  UI components functioning properly!");
    }
}
