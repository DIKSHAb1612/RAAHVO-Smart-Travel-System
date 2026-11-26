-- ============================================================================
-- RAAHVO - Complete MySQL Master Setup Script (Schema + Seed Data)
-- Database Name: RAAHVO_DB
-- ============================================================================

SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS RAAHVO_DB;
CREATE DATABASE RAAHVO_DB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE RAAHVO_DB;

-- ----------------------------------------------------------------------------
-- Table 1: Users (Member 1)
-- ----------------------------------------------------------------------------
CREATE TABLE Users (
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
CREATE TABLE Admins (
    admin_id INT PRIMARY KEY,
    department VARCHAR(80) NOT NULL DEFAULT 'Operations',
    privilege_level INT NOT NULL DEFAULT 1,
    CONSTRAINT FK_Admins_Users FOREIGN KEY (admin_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- ----------------------------------------------------------------------------
-- Table 3: Destinations (Member 1)
-- ----------------------------------------------------------------------------
CREATE TABLE Destinations (
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
CREATE TABLE Transport (
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
CREATE TABLE TransportSchedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    transport_id INT NOT NULL,
    journey_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ON_TIME',
    CONSTRAINT FK_TransportSchedules_Transport FOREIGN KEY (transport_id) REFERENCES Transport(transport_id) ON DELETE CASCADE
) AUTO_INCREMENT = 1;

-- ----------------------------------------------------------------------------
-- Table 6: Seats (Member 2)
-- ----------------------------------------------------------------------------
CREATE TABLE Seats (
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
CREATE TABLE Hotels (
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
CREATE TABLE Rooms (
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
CREATE TABLE Packages (
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
CREATE TABLE Sightseeing (
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
CREATE TABLE Coupons (
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
CREATE TABLE Bookings (
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
CREATE TABLE Passengers (
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
CREATE TABLE Payments (
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
CREATE TABLE Cancellations (
    cancellation_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL UNIQUE,
    reason VARCHAR(255) NOT NULL,
    cancellation_charge DECIMAL(10, 2) NOT NULL DEFAULT 0,
    refund_eligible DECIMAL(10, 2) NOT NULL,
    cancelled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_Cancellations_Bookings FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id)
) AUTO_INCREMENT = 1;

-- ============================================================================
-- POPULATING SEED DATA
-- ============================================================================

-- 1. Users
INSERT INTO Users (user_id, name, email, phone, password_hash, role) VALUES
(1001, 'System Administrator', 'admin@raahvo.com', '9876543210', 'admin123', 'ADMIN'),
(1002, 'Aarav Sharma', 'aarav@gmail.com', '9811122233', 'pass123', 'CUSTOMER'),
(1003, 'Ananya Verma', 'ananya@gmail.com', '9822233344', 'pass123', 'CUSTOMER'),
(1004, 'Rohan Mehta', 'rohan@gmail.com', '9833344455', 'pass123', 'CUSTOMER');

-- 1b. Admins
INSERT INTO Admins (admin_id, department, privilege_level) VALUES
(1001, 'Head of Platform Operations', 5);

-- 2. Destinations
INSERT INTO Destinations (destination_id, name, state, country, category, description, base_daily_cost, rating) VALUES
(1, 'Manali', 'Himachal Pradesh', 'India', 'Hill Station', 'Nestled in the Beas River valley, famed for snow-capped peaks, Solang Valley adventures and Rohtang Pass.', 2200.00, 4.8),
(2, 'Goa', 'Goa', 'India', 'Beach', 'Tropical coastal paradise with golden sands, Portuguese heritage architecture, water sports and vibrant night markets.', 2800.00, 4.7),
(3, 'Jaipur', 'Rajasthan', 'India', 'Heritage', 'The historic Pink City, home to majestic Amber Fort, Hawa Mahal, City Palace, and rich royal culinary culture.', 1900.00, 4.6),
(4, 'Varanasi', 'Uttar Pradesh', 'India', 'Pilgrimage', 'Spiritual capital along the holy Ganges, known for ancient ghats, evening Ganga Aarti, and silk weaving.', 1400.00, 4.7),
(5, 'Rishikesh', 'Uttarakhand', 'India', 'Adventure', 'Yoga capital of the world and thrill-seekers hub featuring river rafting, bungee jumping, and serene Himalayan foothill trails.', 1800.00, 4.8),
(6, 'Munnar', 'Kerala', 'India', 'Nature', 'Sprawling tea gardens, mist-clad valleys, Eravikulam National Park, and romantic waterfalls in God\'s Own Country.', 2300.00, 4.9);

-- 3. Transport (Trains, Buses, Flights)
INSERT INTO Transport (transport_id, provider_name, transport_type, source_city, destination_city, departure_time, arrival_time, base_price, total_seats, available_seats) VALUES
(2001, 'Himachal Volvo Royal Express', 'BUS', 'Delhi', 'Manali', '20:30', '08:30', 1250.00, 40, 36),
(2002, 'Chandigarh-Manali Vande Bharat Connect', 'TRAIN', 'Delhi', 'Manali', '06:00', '15:30', 1650.00, 60, 52),
(2003, 'Air India Regional CRJ', 'FLIGHT', 'Delhi', 'Manali', '07:15', '08:45', 4850.00, 30, 24),
(2004, 'Goa Express Superfast (12780)', 'TRAIN', 'Delhi', 'Goa', '15:15', '19:40', 2100.00, 80, 70),
(2005, 'IndiGo 6E-2041 Direct', 'FLIGHT', 'Delhi', 'Goa', '09:20', '11:55', 4200.00, 50, 41),
(2006, 'IntrCity SmartBus Premium', 'BUS', 'Delhi', 'Goa', '14:00', '22:00', 2400.00, 35, 30),
(2007, 'Jaipur Duronto Express (12239)', 'TRAIN', 'Mumbai', 'Jaipur', '23:00', '14:35', 1850.00, 75, 68),
(2008, 'SpiceJet SG-342', 'FLIGHT', 'Mumbai', 'Jaipur', '10:45', '12:30', 3600.00, 45, 38),
(2009, 'Shrinath Travels Multi-Axle Volvo', 'BUS', 'Mumbai', 'Jaipur', '16:00', '11:00', 1600.00, 36, 29),
(2010, 'Dehradun Shatabdi Express (12017)', 'TRAIN', 'Delhi', 'Rishikesh', '06:45', '11:30', 950.00, 90, 84),
(2011, 'Zingbus Electric AC Coach', 'BUS', 'Delhi', 'Rishikesh', '07:30', '13:00', 650.00, 45, 40);

-- 4. Hotels
INSERT INTO Hotels (hotel_id, destination_id, hotel_name, star_rating, address, amenities, base_price_per_night) VALUES
(3001, 1, 'Himalayan River Resort & Spa', 4, 'Naggar Road, Manali', 'WiFi, Mountain View, Heater, Multi-Cuisine Restaurant', 2600.00),
(3002, 1, 'Solang Valley Alpine Retreat', 3, 'Old Manali Village', 'WiFi, Bonfire, Cafe, Free Trek Guide', 1500.00),
(3003, 2, 'Calangute Palms Beach Resort', 4, 'Holiday Street, Calangute, Goa', 'Pool, Free Breakfast, Ocean View, Bar', 3200.00),
(3004, 2, 'Zostel Backpacker Hub Goa', 3, 'Anjuna Flea Market Road, Goa', 'WiFi, Shared Lounge, Cafe, Bike Rental', 1100.00),
(3005, 3, 'Royal Heritage Haveli Palace', 5, 'Khatipura, Jaipur', 'Swimming Pool, Royal Dining, Spa, Folk Music', 4800.00),
(3006, 3, 'Pink City Boutique Inn', 3, 'MI Road, Jaipur', 'WiFi, Rooftop Restaurant, Travel Desk', 1600.00),
(3007, 5, 'Ganga Kinare Riverside Retreat', 4, 'Veerbhadra Road, Rishikesh', 'Ganga View, Yoga Deck, Ayurvedic Spa', 2900.00);

-- 5. Rooms
INSERT INTO Rooms (room_id, hotel_id, room_number, room_type, price_per_night, max_occupancy, is_available) VALUES
(1, 3001, '101', 'Deluxe Room', 2600.00, 2, 1),
(2, 3001, '102', 'Luxury Suite', 4200.00, 3, 1),
(3, 3002, '201', 'Standard Double', 1500.00, 2, 1),
(4, 3002, '202', 'Standard Single', 1000.00, 1, 1),
(5, 3003, '301', 'Deluxe Room', 3200.00, 2, 1),
(6, 3003, '302', 'Luxury Suite', 5500.00, 4, 1),
(7, 3004, '401', 'Standard Single', 1100.00, 1, 1),
(8, 3005, '501', 'Deluxe Room', 4800.00, 2, 1),
(9, 3006, '601', 'Standard Double', 1600.00, 2, 1),
(10, 3007, '701', 'Deluxe Room', 2900.00, 2, 1);

-- 6. Packages
INSERT INTO Packages (package_id, destination_id, package_name, tier, duration_days, price_per_person, inclusions, description) VALUES
(4001, 1, 'Manali Solang Valley Snow Escape', 'Budget', 3, 4999.00, 'Volvo Bus + Standard Hotel + Breakfast + Solang Day Trip', 'Ideal 3-day budget itinerary exploring snow viewpoints and local monasteries.'),
(4002, 1, 'Manali Alpine Luxury & Rohtang Expedition', 'Premium', 5, 14500.00, 'Flight + 4-Star Resort + All Meals + Private SUV & Rohtang Permit', 'Exclusive 5-day luxury getaway with guided trekking and spa treatments.'),
(4003, 2, 'Goa Sun, Sand & Portuguese Charm', 'Standard', 4, 8500.00, 'Train + 3-Star Beach Resort + Breakfast + North/South Goa Tours', 'Complete 4-day tropical vacation covering historic churches and beaches.'),
(4004, 3, 'Royal Jaipur Imperial Heritage Weekend', 'Standard', 3, 6200.00, 'AC Train + Heritage Hotel + Guided Fort Tour + Chokhi Dhani Entry', 'Cultural royal weekend discovering royal palaces and desert traditions.'),
(4005, 5, 'Rishikesh White Water Adventure & Camping', 'Budget', 2, 3800.00, 'AC Bus + Riverside Camp + 16km River Rafting + Bonfire & Cliff Jump', 'Adrenaline-packed 2-day river adventure with rapids and cliff diving.');

-- 7. Sightseeing
INSERT INTO Sightseeing (spot_id, destination_id, spot_name, entry_fee, duration_hours, spot_category, description) VALUES
(5001, 1, 'Solang Valley Adventure Point', 500.00, 4.0, 'Adventure', 'Paragliding, zorbing, and ropeway adventures surrounded by snow-capped peaks.'),
(5002, 1, 'Hadimba Temple & Cedar Forest', 50.00, 1.5, 'Heritage', 'Ancient wooden pagoda temple built in 1553 surrounded by towering deodar trees.'),
(5003, 2, 'Aguada Fort & Lighthouse', 100.00, 2.0, 'Heritage', '17th-century Portuguese fortress overlooking the vast Arabian Sea.'),
(5004, 2, 'Dudhsagar Waterfalls Safari', 650.00, 5.0, 'Nature', 'Four-tiered milky waterfall accessible via jeep safari through Bhagwan Mahaveer Sanctuary.'),
(5005, 3, 'Amber Fort & Palace', 200.00, 3.0, 'Heritage', 'Majestic hilltop fort with Sheesh Mahal mirror palace and panoramic lake views.'),
(5006, 3, 'Hawa Mahal (Palace of Winds)', 50.00, 1.0, 'Heritage', 'Iconic five-story pink sandstone palace with 953 intricately carved jharokhas.'),
(5007, 5, 'Triveni Ghat Evening Ganga Aarti', 0.00, 2.0, 'Pilgrimage', 'Mesmerizing spiritual prayer ceremony with floating oil lamps and Vedic chants.');

-- 8. Coupons
INSERT INTO Coupons (coupon_id, code, discount_percentage, max_discount, min_booking_amount, valid_until, is_active) VALUES
(1, 'RAAHVO10', 10.00, 1000.00, 3000.00, '2027-12-31', 1),
(2, 'FIRSTTRIP', 15.00, 1500.00, 2500.00, '2027-12-31', 1),
(3, 'EXPLORE20', 20.00, 2500.00, 6000.00, '2027-12-31', 1),
(4, 'STUDENTPASS', 12.00, 800.00, 2000.00, '2027-12-31', 1);

SET FOREIGN_KEY_CHECKS = 1;
