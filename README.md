# RAAHVO - Smart Travel Booking & Trip Planning System
> **Tagline:** *Every Route Has a Story.*  
> **Course:** Object-Oriented Programming with Java  
> **Architecture:** Pure Core Java Console Application (OOP + Collections + Concurrency + JDBC)  
> **Database:** MySQL / Microsoft SQL Server (`RAAHVO_DB`) + Synchronized In-Memory Fallback  
> **Team Size:** 4 Members  

---

## 1. Project Overview
**RAAHVO** is an enterprise-grade, console-based travel booking and intelligent itinerary planning application designed to eliminate financial uncertainty for budget-constrained travelers.

Unlike traditional fragmented travel portals, RAAHVO unifies **multi-modal transportation** (Trains, Buses, Flights), **hospitality accommodation**, and **sightseeing discovery** with a **Dynamic Smart Trip Planner**. The system asks for the traveler's starting city, destination, duration, party size, and total financial ceiling, dynamically calculating itemized expenses (base tariffs, lodging, daily meals, local commute, and GST taxes) and categorizing feasible itineraries into three clear tiers: **Budget**, **Standard**, and **Premium**.

---

## 2. Key Differentiating Features
1. **Smart Budget Trip Planner**: Analyzes real transportation tariffs, hotel night rates, and attraction fees to dynamically present Budget, Standard, and Premium packages with exact surplus or deficit feedback.
2. **Multi-Modal Transport Search**: Unified engine across Trains (Vande Bharat, Shatabdi), Volvo Buses, and Airlines (Air India, IndiGo) with polymorphic fare calculations.
3. **Hospitality & Room Management**: Destination-based hotel discovery with star ratings and thread-safe room occupancy locks.
4. **Promo Voucher & Discount Engine**: Real-time coupon validation (`RAAHVO10`, `FIRSTTRIP`, `EXPLORE20`, `STUDENTPASS`) with minimum booking thresholds and discount caps.
5. **Atomic JDBC Transactions**: Booking, passenger registration, payment processing, and seat updates wrapped in ACID transactions with automatic rollback on error.
6. **Thread-Safe Seat Allocation**: Synchronized locking algorithms preventing double-booking and race conditions during concurrent user access.
7. **Legal Invoice Generation**: Formatted timestamped receipts displayed on console and automatically exported to disk (`invoices/INVOICE_<PNR>.txt`).
8. **Dual-Mode Persistence**: Seamlessly switches between live Microsoft SQL Server via JDBC and a synchronized In-Memory Store for reliable offline evaluation.

---

## 3. Project Directory Structure
```
Raahvo/
├── compile.bat                  # One-click Windows compilation script
├── run.bat                      # One-click Windows application launcher
├── pom.xml                      # Maven project configuration (optional)
├── README.md                    # Project Documentation
├── config/
│   └── db.properties            # Configurable SQL Server connection parameters
├── database/
│   ├── schema.sql               # Normalized SQL Server DDL script (16 tables, 3NF)
│   ├── seed_data.sql            # Rich seed data for testing and demonstrations
│   ├── reset_database.sql       # Safe reset script for database
│   └── README_DATABASE_SETUP.md # Database setup and configuration guide
└── src/
    └── com/
        └── raahvo/
            ├── common/          # Shared infrastructure & utilities (Main, DB, Validator)
            ├── exception/       # Domain-specific custom checked exceptions
            ├── member1/         # User & Destination Management (7 Classes)
            ├── member2/         # Transport & Hotel Management (7 Classes)
            ├── member3/         # Trip Planning & Packages (7 Classes)
            └── member4/         # Booking & Payment Management (6 Classes)
```

---

## 4. Evaluation 1 Class Distribution Matrix (30 Classes)

The project implements a balanced workload across all 4 team members:

| Team Member | Module Assigned | Evaluation 1 Classes (30 Total) | Database Tables |
|-------------|-----------------|---------------------------------|-----------------|
| **Member 1** | User & Destination Management | `User`, `Admin`, `Authentication`, `Registration`, `Login`, `Destination`, `DestinationService` **(7 Classes)** | `Users`, `Admins`, `Destinations` |
| **Member 2** | Transport & Hotel Management | `Transport`, `Train`, `Bus`, `Flight`, `Hotel`, `Room`, `TransportService` **(7 Classes)** | `Transport`, `TransportSchedules`, `Hotels`, `Rooms` |
| **Member 3** | Trip Planning & Packages | `TripPlanner`, `Trip`, `TravelPackage`, `BudgetCalculator`, `Coupon`, `CouponService`, `Sightseeing` **(7 Classes)** | `Packages`, `Sightseeing`, `Coupons` |
| **Member 4** | Booking & Payment | `Booking`, `BookingService`, `Passenger`, `Payment`, `Cancellation`, `Invoice` **(6 Classes)** | `Bookings`, `Passengers`, `Payments`, `Cancellations` |
| **Common (All)**| Architecture & Utilities | `Main`, `DatabaseConnection`, `InputValidator` **(3 Classes)** | Core Driver & Connection Pool |

---

## 5. Technology Stack & Java Syllabus Mapping

| Java Concept | Implementation in RAAHVO |
|--------------|--------------------------|
| **Encapsulation** | Private variables with defensive getters/setters across all model entities (`User`, `Destination`, `Booking`, etc.). |
| **Inheritance** | `Train`, `Bus`, and `Flight` extend abstract `Transport`; `Admin` extends `User`; custom exceptions extend `CustomException`. |
| **Polymorphism** | Overridden `calculateFare(int seats, String travelClass)` across `Train`, `Bus`, and `Flight` providing dynamic rate calculations. |
| **Abstraction** | Abstract class `Transport`, interfaces `Authentication` and `GenericRepository<T, ID>`. |
| **Collections** | `ArrayList` (destinations, transports), `HashSet` (email deduplication), `HashMap` (PNR & coupons), `TreeMap` (chronological booking history), `PriorityQueue` (package ranking). |
| **Exception Handling** | Granular custom checked exceptions (`CustomException`, `InsufficientBudgetException`, `SeatUnavailableException`, etc.). |
| **Multithreading** | `synchronized` seat/room locks, background loading animations, and concurrent stress simulation via `ExecutorService`. |
| **JDBC & SQL Server** | Singleton connection manager, `PreparedStatement` parameterized queries, and multi-statement JDBC ACID transactions. |
| **Generics** | `GenericRepository<T, ID>` interface providing typed repository abstractions. |
| **Lambda & Streams** | Filtering destinations by category, transports by route, and mapping costs via Java Stream API. |
| **File Handling** | Generating customer travel receipts and exporting to `invoices/INVOICE_<PNR>.txt`. |
| **Comparable / Comparator** | `Comparable<Destination>` for alphabetical sorting; custom `Comparator` for popularity and price sorting. |

---

## 6. How to Setup and Run

### Prerequisites
- Java Development Kit (JDK 17 or higher, verified on JDK 26).
- Microsoft SQL Server (Optional: application automatically runs in synchronized offline mode if SQL Server is not running).

### Option A: One-Click Execution (Windows)
1. Double-click `compile.bat` to compile all source files into the `bin/` directory.
2. Double-click `run.bat` to launch the application.

### Option B: Terminal / Command Prompt (Manual)
```powershell
# 1. Navigate to the project root
cd c:\Users\PC\Desktop\Raahvo

# 2. Compile all Java sources
Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName } > sources.txt
javac -d bin @sources.txt
Remove-Item sources.txt

# 3. Run the application
java -cp bin com.raahvo.common.Main
```

### Option C: Using Maven (If preferred)
```bash
mvn compile
mvn exec:java
```

---

## 7. Microsoft SQL Server Setup (Optional for Live DB Mode)
If you wish to run against a live Microsoft SQL Server instance:
1. Open **SQL Server Management Studio (SSMS)** or **Azure Data Studio**.
2. Connect to your local SQL Server instance.
3. Open and execute [`database/schema.sql`](file:///c:/Users/PC/Desktop/Raahvo/database/schema.sql) to create `RAAHVO_DB` and all 16 normalized tables.
4. Open and execute [`database/seed_data.sql`](file:///c:/Users/PC/Desktop/Raahvo/database/seed_data.sql) to populate initial data.
5. Update your credentials in [`config/db.properties`](file:///c:/Users/PC/Desktop/Raahvo/config/db.properties):
   ```properties
   db.mode=SQL_SERVER
   db.url=jdbc:sqlserver://localhost:1433;databaseName=RAAHVO_DB;encrypt=true;trustServerCertificate=true;
   db.user=sa
   db.password=YourStrongPassword!
   ```

---

## 8. Demo Walkthrough Script for Evaluations

### Test Scenario 1: Smart Trip Planner (Main Unique Feature)
1. Select Option **1** from the Main Menu.
2. Enter Starting City: `Delhi`
3. Select Destination: `1` (Manali)
4. Travellers: `2`, Days: `3`, Budget: `15000`, Preference: `Budget`
5. Observe the algorithm generating **Budget Plan (₹13,590)** marked with `[✓ WITHIN BUDGET (+₹1,410 cushion)]`, and comparing with **Standard Plan (₹24,145)** and **Premium Plan (₹49,291)**.

### Test Scenario 2: Multi-Modal Transport & Polymorphism
1. Select Option **3** from the Main Menu.
2. Source: `Delhi`, Destination: `Manali`, Mode: `ALL`.
3. Notice Volvo Bus (HP-01-A-4422), Vande Bharat Express (22448), and Air India Regional (AI-883) retrieved with distinct polymorphic fare structures.

### Test Scenario 3: Database Connectivity Diagnostic & Fallback Verification
1. Select Option **8** from the Main Menu.
2. Observe the automated diagnostic report evaluating target JDBC driver, connection URL, credentials, and live connection status or resilient offline mode.

### Test Scenario 4: User Authentication & Booking History
- Default Admin Credentials: `admin@raahvo.com` / `admin123`
- Default Customer Credentials: `aarav@gmail.com` / `pass123`

---

## 9. Team Members & Module Ownership

| Member | Name | Module Assigned | Key Classes Implemented |
|---|---|---|---|
| **Member 1** | Diksha | User & Destination Management | `User`, `Admin`, `Authentication`, `Registration`, `Login`, `Destination`, `DestinationService` |
| **Member 2** | Manya | Transport & Hotel Management | `Transport`, `Train`, `Bus`, `Flight`, `Hotel`, `Room`, `TransportService` |
| **Member 3** | Krishu | Trip Planning & Packages | `TripPlanner`, `Trip`, `TravelPackage`, `BudgetCalculator`, `Coupon`, `CouponService`, `Sightseeing` |
| **Member 4** | Khushbu | Booking & Payment Management | `Booking`, `BookingService`, `Passenger`, `Payment`, `Cancellation`, `Invoice` |
