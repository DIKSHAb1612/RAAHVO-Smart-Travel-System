-- ============================================================================
-- RAAHVO - Smart Travel Booking & Trip Planning System
-- Target DBMS: Microsoft SQL Server (Transact-SQL)
-- Database Name: RAAHVO_DB
-- SAFE SCHEMA CREATION SCRIPT (Non-Destructive)
-- ============================================================================
-- NOTE: This script checks if tables exist before creating them.
-- It will NEVER drop tables or delete existing user/booking data.
-- To completely wipe and reset the database, use: reset_database.sql
-- ============================================================================

-- 1. Create Database if it does not exist
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'RAAHVO_DB')
BEGIN
    CREATE DATABASE RAAHVO_DB;
END
GO

USE RAAHVO_DB;
GO

-- ----------------------------------------------------------------------------
-- Table 1: Users (Member 1)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Users', 'U') IS NULL
BEGIN
    CREATE TABLE Users (
        user_id INT IDENTITY(1001, 1) PRIMARY KEY,
        name NVARCHAR(100) NOT NULL,
        email NVARCHAR(120) NOT NULL UNIQUE,
        phone NVARCHAR(20) NOT NULL,
        password_hash NVARCHAR(256) NOT NULL,
        role NVARCHAR(20) NOT NULL DEFAULT 'CUSTOMER' CHECK (role IN ('CUSTOMER', 'ADMIN')),
        created_at DATETIME NOT NULL DEFAULT GETDATE()
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 2: Admins (Member 1)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Admins', 'U') IS NULL
BEGIN
    CREATE TABLE Admins (
        admin_id INT PRIMARY KEY,
        department NVARCHAR(80) NOT NULL DEFAULT 'Operations',
        privilege_level INT NOT NULL DEFAULT 1 CHECK (privilege_level BETWEEN 1 AND 5),
        CONSTRAINT FK_Admins_Users FOREIGN KEY (admin_id) REFERENCES Users(user_id) ON DELETE CASCADE
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 3: Destinations (Member 1)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Destinations', 'U') IS NULL
BEGIN
    CREATE TABLE Destinations (
        destination_id INT IDENTITY(1, 1) PRIMARY KEY,
        name NVARCHAR(100) NOT NULL UNIQUE,
        state NVARCHAR(100) NOT NULL,
        country NVARCHAR(50) NOT NULL DEFAULT 'India',
        category NVARCHAR(50) NOT NULL CHECK (category IN ('Hill Station', 'Beach', 'Heritage', 'Adventure', 'Pilgrimage', 'Nature')),
        description NVARCHAR(500) NOT NULL,
        base_daily_cost DECIMAL(10, 2) NOT NULL CHECK (base_daily_cost > 0),
        rating DECIMAL(3, 2) NOT NULL DEFAULT 4.5 CHECK (rating BETWEEN 1.0 AND 5.0)
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 4: Transport (Member 2)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Transport', 'U') IS NULL
BEGIN
    CREATE TABLE Transport (
        transport_id INT IDENTITY(2001, 1) PRIMARY KEY,
        provider_name NVARCHAR(100) NOT NULL,
        transport_type NVARCHAR(20) NOT NULL CHECK (transport_type IN ('TRAIN', 'BUS', 'FLIGHT')),
        source_city NVARCHAR(100) NOT NULL,
        destination_city NVARCHAR(100) NOT NULL,
        departure_time NVARCHAR(20) NOT NULL,
        arrival_time NVARCHAR(20) NOT NULL,
        base_price DECIMAL(10, 2) NOT NULL CHECK (base_price > 0),
        total_seats INT NOT NULL CHECK (total_seats > 0),
        available_seats INT NOT NULL CHECK (available_seats >= 0)
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 5: TransportSchedules (Member 2)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('TransportSchedules', 'U') IS NULL
BEGIN
    CREATE TABLE TransportSchedules (
        schedule_id INT IDENTITY(1, 1) PRIMARY KEY,
        transport_id INT NOT NULL,
        journey_date DATE NOT NULL,
        status NVARCHAR(20) NOT NULL DEFAULT 'ON_TIME' CHECK (status IN ('ON_TIME', 'DELAYED', 'CANCELLED')),
        CONSTRAINT FK_TransportSchedules_Transport FOREIGN KEY (transport_id) REFERENCES Transport(transport_id) ON DELETE CASCADE
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 6: Seats (Member 2 - Future Milestone)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Seats', 'U') IS NULL
BEGIN
    CREATE TABLE Seats (
        seat_id INT IDENTITY(1, 1) PRIMARY KEY,
        transport_id INT NOT NULL,
        seat_number NVARCHAR(10) NOT NULL,
        seat_class NVARCHAR(30) NOT NULL,
        is_booked BIT NOT NULL DEFAULT 0,
        CONSTRAINT UQ_Transport_Seat UNIQUE (transport_id, seat_number),
        CONSTRAINT FK_Seats_Transport FOREIGN KEY (transport_id) REFERENCES Transport(transport_id) ON DELETE CASCADE
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 7: Hotels (Member 2)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Hotels', 'U') IS NULL
BEGIN
    CREATE TABLE Hotels (
        hotel_id INT IDENTITY(3001, 1) PRIMARY KEY,
        destination_id INT NOT NULL,
        hotel_name NVARCHAR(150) NOT NULL,
        star_rating INT NOT NULL CHECK (star_rating BETWEEN 1 AND 5),
        address NVARCHAR(255) NOT NULL,
        amenities NVARCHAR(255) NOT NULL,
        base_price_per_night DECIMAL(10, 2) NOT NULL CHECK (base_price_per_night > 0),
        CONSTRAINT FK_Hotels_Destinations FOREIGN KEY (destination_id) REFERENCES Destinations(destination_id) ON DELETE CASCADE
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 8: Rooms (Member 2)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Rooms', 'U') IS NULL
BEGIN
    CREATE TABLE Rooms (
        room_id INT IDENTITY(1, 1) PRIMARY KEY,
        hotel_id INT NOT NULL,
        room_number NVARCHAR(10) NOT NULL,
        room_type NVARCHAR(50) NOT NULL CHECK (room_type IN ('Standard Single', 'Standard Double', 'Deluxe Room', 'Luxury Suite', 'Executive Family')),
        price_per_night DECIMAL(10, 2) NOT NULL CHECK (price_per_night > 0),
        max_occupancy INT NOT NULL DEFAULT 2 CHECK (max_occupancy BETWEEN 1 AND 6),
        is_available BIT NOT NULL DEFAULT 1,
        CONSTRAINT UQ_Hotel_Room UNIQUE (hotel_id, room_number),
        CONSTRAINT FK_Rooms_Hotels FOREIGN KEY (hotel_id) REFERENCES Hotels(hotel_id) ON DELETE CASCADE
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 9: Packages (Member 3)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Packages', 'U') IS NULL
BEGIN
    CREATE TABLE Packages (
        package_id INT IDENTITY(4001, 1) PRIMARY KEY,
        destination_id INT NOT NULL,
        package_name NVARCHAR(150) NOT NULL,
        tier NVARCHAR(30) NOT NULL CHECK (tier IN ('Budget', 'Standard', 'Premium')),
        duration_days INT NOT NULL CHECK (duration_days > 0),
        price_per_person DECIMAL(10, 2) NOT NULL CHECK (price_per_person > 0),
        inclusions NVARCHAR(500) NOT NULL,
        description NVARCHAR(500) NOT NULL,
        CONSTRAINT FK_Packages_Destinations FOREIGN KEY (destination_id) REFERENCES Destinations(destination_id) ON DELETE CASCADE
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 10: Sightseeing (Member 3)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Sightseeing', 'U') IS NULL
BEGIN
    CREATE TABLE Sightseeing (
        spot_id INT IDENTITY(5001, 1) PRIMARY KEY,
        destination_id INT NOT NULL,
        spot_name NVARCHAR(150) NOT NULL,
        entry_fee DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK (entry_fee >= 0),
        duration_hours DECIMAL(4, 2) NOT NULL DEFAULT 2.0 CHECK (duration_hours > 0),
        spot_category NVARCHAR(50) NOT NULL,
        description NVARCHAR(500) NOT NULL,
        CONSTRAINT FK_Sightseeing_Destinations FOREIGN KEY (destination_id) REFERENCES Destinations(destination_id) ON DELETE CASCADE
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 11: Coupons (Member 3)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Coupons', 'U') IS NULL
BEGIN
    CREATE TABLE Coupons (
        coupon_id INT IDENTITY(1, 1) PRIMARY KEY,
        code NVARCHAR(50) NOT NULL UNIQUE,
        discount_percentage DECIMAL(5, 2) NOT NULL CHECK (discount_percentage BETWEEN 1 AND 100),
        max_discount DECIMAL(10, 2) NOT NULL CHECK (max_discount > 0),
        min_booking_amount DECIMAL(10, 2) NOT NULL CHECK (min_booking_amount >= 0),
        valid_until DATE NOT NULL,
        is_active BIT NOT NULL DEFAULT 1
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 12: Bookings (Member 4)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Bookings', 'U') IS NULL
BEGIN
    CREATE TABLE Bookings (
        booking_id INT IDENTITY(6001, 1) PRIMARY KEY,
        booking_pnr NVARCHAR(50) NOT NULL UNIQUE,
        user_id INT NOT NULL,
        booking_type NVARCHAR(50) NOT NULL,
        booking_date DATETIME NOT NULL DEFAULT GETDATE(),
        travel_date DATE NOT NULL,
        total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
        discount_amount DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK (discount_amount >= 0),
        final_payable DECIMAL(10, 2) NOT NULL CHECK (final_payable >= 0),
        status NVARCHAR(30) NOT NULL DEFAULT 'CONFIRMED' CHECK (status IN ('CONFIRMED', 'CANCELLED', 'COMPLETED', 'PENDING')),
        CONSTRAINT FK_Bookings_Users FOREIGN KEY (user_id) REFERENCES Users(user_id)
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 13: Passengers (Member 4)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Passengers', 'U') IS NULL
BEGIN
    CREATE TABLE Passengers (
        passenger_id INT IDENTITY(1, 1) PRIMARY KEY,
        booking_id INT NOT NULL,
        full_name NVARCHAR(100) NOT NULL,
        age INT NOT NULL CHECK (age BETWEEN 1 AND 120),
        gender NVARCHAR(10) NOT NULL CHECK (gender IN ('Male', 'Female', 'Other')),
        seat_number NVARCHAR(10) NULL,
        id_proof NVARCHAR(50) NOT NULL,
        CONSTRAINT FK_Passengers_Bookings FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id) ON DELETE CASCADE
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 14: Payments (Member 4)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Payments', 'U') IS NULL
BEGIN
    CREATE TABLE Payments (
        payment_id INT IDENTITY(7001, 1) PRIMARY KEY,
        booking_id INT NOT NULL,
        transaction_ref NVARCHAR(100) NOT NULL UNIQUE,
        payment_method NVARCHAR(50) NOT NULL CHECK (payment_method IN ('UPI', 'CREDIT_CARD', 'DEBIT_CARD', 'NET_BANKING', 'WALLET', 'CASH_ON_ARRIVAL')),
        amount DECIMAL(10, 2) NOT NULL CHECK (amount >= 0),
        payment_status NVARCHAR(30) NOT NULL DEFAULT 'SUCCESS' CHECK (payment_status IN ('SUCCESS', 'FAILED', 'REFUNDED')),
        payment_timestamp DATETIME NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_Payments_Bookings FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id) ON DELETE CASCADE
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 15: Cancellations (Member 4)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Cancellations', 'U') IS NULL
BEGIN
    CREATE TABLE Cancellations (
        cancellation_id INT IDENTITY(1, 1) PRIMARY KEY,
        booking_id INT NOT NULL UNIQUE,
        reason NVARCHAR(255) NOT NULL,
        cancellation_charge DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK (cancellation_charge >= 0),
        refund_eligible DECIMAL(10, 2) NOT NULL CHECK (refund_eligible >= 0),
        cancelled_at DATETIME NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_Cancellations_Bookings FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id)
    );
END
GO

-- ----------------------------------------------------------------------------
-- Table 16: Refunds (Member 4 - Future Milestone)
-- ----------------------------------------------------------------------------
IF OBJECT_ID('Refunds', 'U') IS NULL
BEGIN
    CREATE TABLE Refunds (
        refund_id INT IDENTITY(1, 1) PRIMARY KEY,
        cancellation_id INT NOT NULL,
        booking_id INT NOT NULL,
        refund_amount DECIMAL(10, 2) NOT NULL CHECK (refund_amount >= 0),
        status NVARCHAR(30) NOT NULL DEFAULT 'PROCESSED' CHECK (status IN ('PENDING', 'PROCESSED', 'FAILED')),
        processed_at DATETIME NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_Refunds_Cancellations FOREIGN KEY (cancellation_id) REFERENCES Cancellations(cancellation_id),
        CONSTRAINT FK_Refunds_Bookings FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id)
    );
END
GO

-- ----------------------------------------------------------------------------
-- Indexes for Performance & Search Optimization
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT name FROM sys.indexes WHERE name = 'IX_Transport_Route')
    CREATE NONCLUSTERED INDEX IX_Transport_Route ON Transport(source_city, destination_city);

IF NOT EXISTS (SELECT name FROM sys.indexes WHERE name = 'IX_Hotels_Destination')
    CREATE NONCLUSTERED INDEX IX_Hotels_Destination ON Hotels(destination_id);

IF NOT EXISTS (SELECT name FROM sys.indexes WHERE name = 'IX_Bookings_User')
    CREATE NONCLUSTERED INDEX IX_Bookings_User ON Bookings(user_id);

IF NOT EXISTS (SELECT name FROM sys.indexes WHERE name = 'IX_Bookings_PNR')
    CREATE NONCLUSTERED INDEX IX_Bookings_PNR ON Bookings(booking_pnr);
GO
