-- ============================================================================
-- RAAHVO - Smart Travel Booking & Trip Planning System
-- Target DBMS: MySQL (5.7 / 8.0+)
-- Database Name: RAAHVO_DB
-- ============================================================================

CREATE DATABASE IF NOT EXISTS RAAHVO_DB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE RAAHVO_DB;

-- ----------------------------------------------------------------------------
-- Table 1: Users (Member 1)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    password_hash VARCHAR(256) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) AUTO_INCREMENT = 1001;

-- ----------------------------------------------------------------------------
-- Table 2: Admins (Member 1)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Admins (
    admin_id INT PRIMARY KEY,
    department VARCHAR(80) NOT NULL DEFAULT 'Operations',
    privilege_level INT NOT NULL DEFAULT 1,
    CONSTRAINT FK_Admins_Users FOREIGN KEY (admin_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- ----------------------------------------------------------------------------
-- Table 3: Destinations (Member 1)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Destinations (
    destination_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(50) NOT NULL DEFAULT 'India',
    category VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    base_daily_cost DECIMAL(10, 2) NOT NULL,
    rating DECIMAL(3, 2) NOT NULL DEFAULT 4.5
) AUTO_INCREMENT = 1;

-- ----------------------------------------------------------------------------
-- Table 4: Transport (Member 2)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Transport (
    transport_id INT AUTO_INCREMENT PRIMARY KEY,
    provider_name VARCHAR(100) NOT NULL,
    transport_type VARCHAR(20) NOT NULL,
    source_city VARCHAR(100) NOT NULL,
    destination_city VARCHAR(100) NOT NULL,
    departure_time VARCHAR(20) NOT NULL,
    arrival_time VARCHAR(20) NOT NULL,
    base_price DECIMAL(10, 2) NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL
) AUTO_INCREMENT = 2001;

-- ----------------------------------------------------------------------------
-- Table 5: TransportSchedules (Member 2)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS TransportSchedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    transport_id INT NOT NULL,
    journey_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ON_TIME',
    CONSTRAINT FK_TransportSchedules_Transport FOREIGN KEY (transport_id) REFERENCES Transport(transport_id) ON DELETE CASCADE
) AUTO_INCREMENT = 1;

-- ----------------------------------------------------------------------------
-- Table 6: Seats (Member 2)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Seats (
    seat_id INT AUTO_INCREMENT PRIMARY KEY,
    transport_id INT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    seat_class VARCHAR(30) NOT NULL,
    is_booked TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT UQ_Transport_Seat UNIQUE (transport_id, seat_number),
    CONSTRAINT FK_Seats_Transport FOREIGN KEY (transport_id) REFERENCES Transport(transport_id) ON DELETE CASCADE
) AUTO_INCREMENT = 1;

-- ----------------------------------------------------------------------------
-- Table 7: Hotels (Member 2)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Hotels (
    hotel_id INT AUTO_INCREMENT PRIMARY KEY,
    destination_id INT NOT NULL,
    hotel_name VARCHAR(150) NOT NULL,
    star_rating INT NOT NULL,
    address VARCHAR(255) NOT NULL,
    amenities VARCHAR(255) NOT NULL,
    base_price_per_night DECIMAL(10, 2) NOT NULL,
    CONSTRAINT FK_Hotels_Destinations FOREIGN KEY (destination_id) REFERENCES Destinations(destination_id) ON DELETE CASCADE
) AUTO_INCREMENT = 3001;

-- ----------------------------------------------------------------------------
-- Table 8: Rooms (Member 2)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id INT NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    max_occupancy INT NOT NULL DEFAULT 2,
    is_available TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT UQ_Hotel_Room UNIQUE (hotel_id, room_number),
    CONSTRAINT FK_Rooms_Hotels FOREIGN KEY (hotel_id) REFERENCES Hotels(hotel_id) ON DELETE CASCADE
) AUTO_INCREMENT = 1;

-- ----------------------------------------------------------------------------
-- Table 9: Packages (Member 3)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Packages (
    package_id INT AUTO_INCREMENT PRIMARY KEY,
    destination_id INT NOT NULL,
    package_name VARCHAR(150) NOT NULL,
    tier VARCHAR(30) NOT NULL,
    duration_days INT NOT NULL,
    price_per_person DECIMAL(10, 2) NOT NULL,
    inclusions VARCHAR(500) NOT NULL,
    description VARCHAR(500) NOT NULL,
    CONSTRAINT FK_Packages_Destinations FOREIGN KEY (destination_id) REFERENCES Destinations(destination_id) ON DELETE CASCADE
) AUTO_INCREMENT = 4001;

-- ----------------------------------------------------------------------------
-- Table 10: Sightseeing (Member 3)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Sightseeing (
    spot_id INT AUTO_INCREMENT PRIMARY KEY,
    destination_id INT NOT NULL,
    spot_name VARCHAR(150) NOT NULL,
    entry_fee DECIMAL(10, 2) NOT NULL DEFAULT 0,
    duration_hours DECIMAL(4, 2) NOT NULL DEFAULT 2.0,
    spot_category VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    CONSTRAINT FK_Sightseeing_Destinations FOREIGN KEY (destination_id) REFERENCES Destinations(destination_id) ON DELETE CASCADE
) AUTO_INCREMENT = 5001;

-- ----------------------------------------------------------------------------
-- Table 11: Coupons (Member 3)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Coupons (
    coupon_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    discount_percentage DECIMAL(5, 2) NOT NULL,
    max_discount DECIMAL(10, 2) NOT NULL,
    min_booking_amount DECIMAL(10, 2) NOT NULL,
    valid_until DATE NOT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1
) AUTO_INCREMENT = 1;

-- ----------------------------------------------------------------------------
-- Table 12: Bookings (Member 4)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_pnr VARCHAR(50) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    booking_type VARCHAR(50) NOT NULL,
    booking_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    travel_date DATE NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    final_payable DECIMAL(10, 2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'CONFIRMED',
    CONSTRAINT FK_Bookings_Users FOREIGN KEY (user_id) REFERENCES Users(user_id)
) AUTO_INCREMENT = 6001;

-- ----------------------------------------------------------------------------
-- Table 13: Passengers (Member 4)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Passengers (
    passenger_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    seat_number VARCHAR(10) NULL,
    id_proof VARCHAR(50) NOT NULL,
    CONSTRAINT FK_Passengers_Bookings FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id) ON DELETE CASCADE
) AUTO_INCREMENT = 1;

-- ----------------------------------------------------------------------------
-- Table 14: Payments (Member 4)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    transaction_ref VARCHAR(100) NOT NULL UNIQUE,
    payment_method VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(30) NOT NULL DEFAULT 'SUCCESS',
    payment_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_Payments_Bookings FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id) ON DELETE CASCADE
) AUTO_INCREMENT = 7001;

-- ----------------------------------------------------------------------------
-- Table 15: Cancellations (Member 4)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Cancellations (
    cancellation_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL UNIQUE,
    reason VARCHAR(255) NOT NULL,
    cancellation_charge DECIMAL(10, 2) NOT NULL DEFAULT 0,
    refund_eligible DECIMAL(10, 2) NOT NULL,
    cancelled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_Cancellations_Bookings FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id)
) AUTO_INCREMENT = 1;
