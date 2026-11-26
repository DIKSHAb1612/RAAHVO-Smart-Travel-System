-- ============================================================================
-- RAAHVO - Comprehensive Seed Data for MySQL
-- Database Name: RAAHVO_DB
-- ============================================================================

USE RAAHVO_DB;

-- Disable Foreign Key Checks during batch data seeding to prevent constraint ordering errors
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Seed Users
INSERT INTO Users (user_id, name, email, phone, password_hash, role) VALUES
(1001, 'System Administrator', 'admin@raahvo.com', '9876543210', 'admin123', 'ADMIN'),
(1002, 'Aarav Sharma', 'aarav@gmail.com', '9811122233', 'pass123', 'CUSTOMER'),
(1003, 'Ananya Verma', 'ananya@gmail.com', '9822233344', 'pass123', 'CUSTOMER'),
(1004, 'Rohan Mehta', 'rohan@gmail.com', '9833344455', 'pass123', 'CUSTOMER')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 1b. Seed Admins
INSERT INTO Admins (admin_id, department, privilege_level) VALUES
(1001, 'Head of Platform Operations', 5)
ON DUPLICATE KEY UPDATE department=VALUES(department);

-- 2. Seed Destinations
INSERT INTO Destinations (destination_id, name, state, country, category, description, base_daily_cost, rating) VALUES
(1, 'Manali', 'Himachal Pradesh', 'India', 'Hill Station', 'Nestled in the Beas River valley, famed for snow-capped peaks, Solang Valley adventures and Rohtang Pass.', 2200.00, 4.8),
(2, 'Goa', 'Goa', 'India', 'Beach', 'Tropical coastal paradise with golden sands, Portuguese heritage architecture, water sports and vibrant night markets.', 2800.00, 4.7),
(3, 'Jaipur', 'Rajasthan', 'India', 'Heritage', 'The historic Pink City, home to majestic Amber Fort, Hawa Mahal, City Palace, and rich royal culinary culture.', 1900.00, 4.6),
(4, 'Varanasi', 'Uttar Pradesh', 'India', 'Pilgrimage', 'Spiritual capital along the holy Ganges, known for ancient ghats, evening Ganga Aarti, and silk weaving.', 1400.00, 4.7),
(5, 'Rishikesh', 'Uttarakhand', 'India', 'Adventure', 'Yoga capital of the world and thrill-seekers hub featuring river rafting, bungee jumping, and serene Himalayan foothill trails.', 1800.00, 4.8),
(6, 'Munnar', 'Kerala', 'India', 'Nature', 'Sprawling tea gardens, mist-clad valleys, Eravikulam National Park, and romantic waterfalls in God\'s Own Country.', 2300.00, 4.9)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 3. Seed Transport (Trains, Buses, Flights)
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
(2011, 'Zingbus Electric AC Coach', 'BUS', 'Delhi', 'Rishikesh', '07:30', '13:00', 650.00, 45, 40)
ON DUPLICATE KEY UPDATE provider_name=VALUES(provider_name);

-- 4. Seed Hotels
INSERT INTO Hotels (hotel_id, destination_id, hotel_name, star_rating, address, amenities, base_price_per_night) VALUES
(3001, 1, 'Himalayan River Resort & Spa', 4, 'Naggar Road, Manali', 'WiFi, Mountain View, Heater, Multi-Cuisine Restaurant', 2600.00),
(3002, 1, 'Solang Valley Alpine Retreat', 3, 'Old Manali Village', 'WiFi, Bonfire, Cafe, Free Trek Guide', 1500.00),
(3003, 2, 'Calangute Palms Beach Resort', 4, 'Holiday Street, Calangute, Goa', 'Pool, Free Breakfast, Ocean View, Bar', 3200.00),
(3004, 2, 'Zostel Backpacker Hub Goa', 3, 'Anjuna Flea Market Road, Goa', 'WiFi, Shared Lounge, Cafe, Bike Rental', 1100.00),
(3005, 3, 'Royal Heritage Haveli Palace', 5, 'Khatipura, Jaipur', 'Swimming Pool, Royal Dining, Spa, Folk Music', 4800.00),
(3006, 3, 'Pink City Boutique Inn', 3, 'MI Road, Jaipur', 'WiFi, Rooftop Restaurant, Travel Desk', 1600.00),
(3007, 5, 'Ganga Kinare Riverside Retreat', 4, 'Veerbhadra Road, Rishikesh', 'Ganga View, Yoga Deck, Ayurvedic Spa', 2900.00)
ON DUPLICATE KEY UPDATE hotel_name=VALUES(hotel_name);

-- 5. Seed Rooms
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
(10, 3007, '701', 'Deluxe Room', 2900.00, 2, 1)
ON DUPLICATE KEY UPDATE price_per_night=VALUES(price_per_night);

-- 6. Seed Packages
INSERT INTO Packages (package_id, destination_id, package_name, tier, duration_days, price_per_person, inclusions, description) VALUES
(4001, 1, 'Manali Solang Valley Snow Escape', 'Budget', 3, 4999.00, 'Volvo Bus + Standard Hotel + Breakfast + Solang Day Trip', 'Ideal 3-day budget itinerary exploring snow viewpoints and local monasteries.'),
(4002, 1, 'Manali Alpine Luxury & Rohtang Expedition', 'Premium', 5, 14500.00, 'Flight + 4-Star Resort + All Meals + Private SUV & Rohtang Permit', 'Exclusive 5-day luxury getaway with guided trekking and spa treatments.'),
(4003, 2, 'Goa Sun, Sand & Portuguese Charm', 'Standard', 4, 8500.00, 'Train + 3-Star Beach Resort + Breakfast + North/South Goa Tours', 'Complete 4-day tropical vacation covering historic churches and beaches.'),
(4004, 3, 'Royal Jaipur Imperial Heritage Weekend', 'Standard', 3, 6200.00, 'AC Train + Heritage Hotel + Guided Fort Tour + Chokhi Dhani Entry', 'Cultural royal weekend discovering royal palaces and desert traditions.'),
(4005, 5, 'Rishikesh White Water Adventure & Camping', 'Budget', 2, 3800.00, 'AC Bus + Riverside Camp + 16km River Rafting + Bonfire & Cliff Jump', 'Adrenaline-packed 2-day river adventure with rapids and cliff diving.')
ON DUPLICATE KEY UPDATE package_name=VALUES(package_name);

-- 7. Seed Sightseeing Attractions
INSERT INTO Sightseeing (spot_id, destination_id, spot_name, entry_fee, duration_hours, spot_category, description) VALUES
(5001, 1, 'Solang Valley Adventure Point', 500.00, 4.0, 'Adventure', 'Paragliding, zorbing, and ropeway adventures surrounded by snow-capped peaks.'),
(5002, 1, 'Hadimba Temple & Cedar Forest', 50.00, 1.5, 'Heritage', 'Ancient wooden pagoda temple built in 1553 surrounded by towering deodar trees.'),
(5003, 2, 'Aguada Fort & Lighthouse', 100.00, 2.0, 'Heritage', '17th-century Portuguese fortress overlooking the vast Arabian Sea.'),
(5004, 2, 'Dudhsagar Waterfalls Safari', 650.00, 5.0, 'Nature', 'Four-tiered milky waterfall accessible via jeep safari through Bhagwan Mahaveer Sanctuary.'),
(5005, 3, 'Amber Fort & Palace', 200.00, 3.0, 'Heritage', 'Majestic hilltop fort with Sheesh Mahal mirror palace and panoramic lake views.'),
(5006, 3, 'Hawa Mahal (Palace of Winds)', 50.00, 1.0, 'Heritage', 'Iconic five-story pink sandstone palace with 953 intricately carved jharokhas.'),
(5007, 5, 'Triveni Ghat Evening Ganga Aarti', 0.00, 2.0, 'Pilgrimage', 'Mesmerizing spiritual prayer ceremony with floating oil lamps and Vedic chants.')
ON DUPLICATE KEY UPDATE spot_name=VALUES(spot_name);

-- 8. Seed Promotional Coupons
INSERT INTO Coupons (coupon_id, code, discount_percentage, max_discount, min_booking_amount, valid_until, is_active) VALUES
(1, 'RAAHVO10', 10.00, 1000.00, 3000.00, '2027-12-31', 1),
(2, 'FIRSTTRIP', 15.00, 1500.00, 2500.00, '2027-12-31', 1),
(3, 'EXPLORE20', 20.00, 2500.00, 6000.00, '2027-12-31', 1),
(4, 'STUDENTPASS', 12.00, 800.00, 2000.00, '2027-12-31', 1)
ON DUPLICATE KEY UPDATE is_active=1;

-- Re-enable Foreign Key Checks
SET FOREIGN_KEY_CHECKS = 1;
