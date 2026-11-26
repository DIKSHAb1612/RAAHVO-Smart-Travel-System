# RAAHVO – Microsoft SQL Server Database Setup Guide
**Smart Travel Booking & Budget Trip Planning System**
*University Course: Programming in Java | Evaluation 1 (CE-3)*

---

## 1. Overview & Dual Persistence Architecture

RAAHVO is engineered with an **enterprise-grade Dual Persistence Engine**:
1. **Live Microsoft SQL Server Database (JDBC Mode)**:
   - Connects to Microsoft SQL Server on port `1433`.
   - Uses `com.microsoft.sqlserver.jdbc.SQLServerDriver`.
   - Executes parameterized queries via `PreparedStatement` and processes tabular results via `ResultSet`.
   - Manages connection lifecycle with resilient `try-with-resources` resource cleanup.
2. **Synchronized In-Memory Mock Store (Offline Demonstration Mode)**:
   - If SQL Server is not installed or unreachable, RAAHVO automatically detects this and falls back to an in-memory repository without crashing.
   - Prepopulated with identical seed data to guarantee 100% feature demonstration during university viva evaluations.

---

## 2. Prerequisites
- **Database Engine**: Microsoft SQL Server (2019, 2022, or SQL Server Express).
- **GUI Management Tool**: SQL Server Management Studio (SSMS) or Azure Data Studio.
- **Java Development Kit**: JDK 17, 21, or 26.
- **JDBC Driver**: `mssql-jdbc-12.x.x.jre11.jar` (or compatible version).

---

## 3. Step-by-Step SQL Server Setup

### Step 3.1: Enable TCP/IP in SQL Server Configuration Manager
1. Press `Win + R`, type `compmgmt.msc` or open **SQL Server Configuration Manager**.
2. Navigate to **SQL Server Network Configuration** > **Protocols for SQLEXPRESS** (or `MSSQLSERVER`).
3. Ensure **TCP/IP** is set to **Enabled**.
4. Double-click **TCP/IP**, switch to the **IP Addresses** tab:
   - Scroll down to **IPAll**.
   - Set **TCP Port** to `1433`.
   - Clear **TCP Dynamic Ports** (leave blank).
5. Click **Apply** and restart the **SQL Server (SQLEXPRESS / MSSQLSERVER)** service in Windows Services (`services.msc`).

### Step 3.2: Enable Mixed Mode Authentication (SQL Server and Windows Authentication)
1. Open **SQL Server Management Studio (SSMS)**.
2. Connect to your local server using **Windows Authentication**.
3. Right-click the server node in Object Explorer > **Properties** > **Security**.
4. Under *Server authentication*, select **SQL Server and Windows Authentication mode**.
5. Under **Security** > **Logins**, right-click `sa` > **Properties**:
   - Set a strong password (e.g. `StrongPassword123!` or `sa`).
   - Switch to **Status** page: ensure **Login** is set to **Enabled**.
6. Restart the SQL Server service.

### Step 3.3: Execute Schema & Seed Data Scripts
In SSMS, open and execute the following SQL scripts located in the `database/` folder in order:

1. **Create Tables & Foreign Keys**:
   - Open and execute [`database/schema.sql`](file:///c:/Users/PC/Desktop/Raahvo/database/schema.sql).
   - This creates the `RAAHVO_DB` database and 16 normalized tables:
     - `Users`, `Destinations`, `Transport`, `Hotels`, `Rooms`, `Seats`
     - `TravelPackages`, `SightseeingSpots`, `Coupons`, `Bookings`
     - `Passengers`, `Payments`, `Cancellations`, `Refunds`
     - `AuditLogs`, `Reviews`
2. **Insert Initial Mock Data**:
   - Open and execute [`database/seed_data.sql`](file:///c:/Users/PC/Desktop/Raahvo/database/seed_data.sql).
   - This populates test users (Admin, Customers), destinations (Manali, Goa, Jaipur, Varanasi, Rishikesh, Munnar), trains, buses, flights, partner hotels, rooms, coupons, and sample bookings.

---

## 4. Configuring Application Database Properties

Configuration settings are stored in [`config/db.properties`](file:///c:/Users/PC/Desktop/Raahvo/config/db.properties):

```properties
# RAAHVO Database Connection Configuration
db.mode=AUTO
db.url=jdbc:sqlserver://localhost:1433;databaseName=RAAHVO_DB;encrypt=true;trustServerCertificate=true;loginTimeout=3;
db.user=sa
db.password=dikshA_B1612
db.driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

- **`db.mode=AUTO`**: Tries live SQL Server first; gracefully falls back to in-memory store if offline.
- **`db.mode=SQLSERVER`**: Forces SQL Server connectivity.
- **`db.mode=MOCK`**: Forces in-memory demo mode.

---

## 5. Verification & Testing

### Running the Diagnostic Tool
1. Launch RAAHVO using `run.bat` or:
   ```cmd
   java -cp bin com.raahvo.common.Main
   ```
2. Select **Option 8** from the Main Menu:
   ```
   8. Test Database Connectivity (SQL Server JDBC / In-Memory Mode)
   ```
3. The system prints a live connectivity report verifying whether queries are running against live SQL Server or the synchronized mock store.

---

## 6. Sample SQL Queries for Viva Verification

You can run these queries directly in SSMS to verify data during evaluation:

```sql
-- 1. Check all destinations and average daily cost
SELECT destination_id, name, state, category, base_daily_cost, rating 
FROM Destinations 
ORDER BY rating DESC;

-- 2. Check live transport availability
SELECT transport_id, provider_name, transport_type, source_city, destination_city, base_price, available_seats 
FROM Transport;

-- 3. Check bookings and passenger count
SELECT b.booking_pnr, b.booking_type, b.travel_date, b.final_payable, b.booking_status, COUNT(p.passenger_id) AS passenger_count
FROM Bookings b
LEFT JOIN Passengers p ON b.booking_id = p.booking_id
GROUP BY b.booking_pnr, b.booking_type, b.travel_date, b.final_payable, b.booking_status;

-- 4. Check payment transactions
SELECT payment_id, transaction_ref, payment_method, amount, payment_status, payment_date
FROM Payments;
```
