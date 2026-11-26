-- ============================================================================
-- RAAHVO - Comprehensive Seed Data for Microsoft SQL Server
-- Database Name: RAAHVO_DB
-- ============================================================================

USE RAAHVO_DB;
GO

-- 1. Seed Users (passwords are SHA-256 hashed or plain demo hashes)
INSERT INTO Users (name, email, phone, password_hash, role) VALUES
('System Administrator', 'admin@raahvo.com', '9876543210', 'admin123', 'ADMIN'),
('Aarav Sharma', 'aarav@gmail.com', '9811122233', 'pass123', 'CUSTOMER'),
('Ananya Verma', 'ananya@gmail.com', '9822233344', 'pass123', 'CUSTOMER'),
('Rohan Mehta', 'rohan@gmail.com', '9833344455', 'pass123', 'CUSTOMER');

-- Admins Table
INSERT INTO Admins (admin_id, department, privilege_level) VALUES
(1001, 'Head of Platform Operations', 5);

-- 2. Seed Destinations
INSERT INTO Destinations (name, state, country, category, description, base_daily_cost, rating) VALUES
('Manali', 'Himachal Pradesh', 'India', 'Hill Station', 'Nestled in the Beas River valley, famed for snow-capped peaks, Solang Valley adventures and Rohtang Pass.', 2200.00, 4.8),
('Goa', 'Goa', 'India', 'Beach', 'Tropical coastal paradise with golden sands, Portuguese heritage architecture, water sports and vibrant night markets.', 2800.00, 4.7),
('Jaipur', 'Rajasthan', 'India', 'Heritage', 'The historic Pink City, home to majestic Amber Fort, Hawa Mahal, City Palace, and rich royal culinary culture.', 1900.00, 4.6),
('Varanasi', 'Uttar Pradesh', 'India', 'Pilgrimage', 'Spiritual spiritual capital along the holy Ganges, known for ancient ghats, evening Ganga Aarti, and silk weaving.', 1400.00, 4.7),
('Rishikesh', 'Uttarakhand', 'India', 'Adventure', 'Yoga capital of the world and thrill-seekers hub featuring river rafting, bungee jumping, and serene Himalayan foothill trails.', 1800.00, 4.8),
('Munnar', 'Kerala', 'India', 'Nature', 'Sprawling tea gardens, mist-clad valleys, Eravikulam National Park, and romantic waterfalls in God''s Own Country.', 2300.00, 4.9);

-- 3. Seed Transport (Trains, Buses, Flights)
-- Delhi to Manali
INSERT INTO Transport (provider_name, transport_type, source_city, destination_city, departure_time, arrival_time, base_price, total_seats, available_seats) VALUES
('Himachal Volvo Royal Express', 'BUS', 'Delhi', 'Manali', '20:30', '08:30', 1250.00, 40, 36),
('Chandigarh-Manali Vande Bharat Connect', 'TRAIN', 'Delhi', 'Manali', '06:00', '15:30', 1650.00, 60, 52),
('Air India Regional CRJ', 'FLIGHT', 'Delhi', 'Manali', '07:15', '08:45', 4850.00, 30, 24);

-- Delhi to Goa
INSERT INTO Transport (provider_name, transport_type, source_city, destination_city, departure_time, arrival_time, base_price, total_seats, available_seats) VALUES
('Goa Express Superfast (12780)', 'TRAIN', 'Delhi', 'Goa', '15:15', '19:40', 2100.00, 80, 70),
('IndiGo 6E-2041 Direct', 'FLIGHT', 'Delhi', 'Goa', '09:20', '11:55', 4200.00, 50, 41),
('IntrCity SmartBus Premium', 'BUS', 'Delhi', 'Goa', '14:00', '22:00', 2400.00, 35, 30);

-- Mumbai to Jaipur
INSERT INTO Transport (provider_name, transport_type, source_city, destination_city, departure_time, arrival_time, base_price, total_seats, available_seats) VALUES
('Jaipur Duronto Express (12239)', 'TRAIN', 'Mumbai', 'Jaipur', '23:00', '14:35', 1850.00, 75, 68),
('SpiceJet SG-342', 'FLIGHT', 'Mumbai', 'Jaipur', '10:45', '12:30', 3600.00, 45, 38),
('Shrinath Travels Multi-Axle Volvo', 'BUS', 'Mumbai', 'Jaipur', '16:00', '11:00', 1600.00, 36, 29);

-- Delhi to Rishikesh
INSERT INTO Transport (provider_name, transport_type, source_city, destination_city, departure_time, arrival_time, base_price, total_seats, available_seats) VALUES
('Dehradun Shatabdi Express (12017)', 'TRAIN', 'Delhi', 'Rishikesh', '06:45', '11:30', 950.00, 90, 84),
('Zingbus Electric AC Coach', 'BUS', 'Delhi', 'Rishikesh', '07:30', '13:00', 650.00, 45, 40);

-- 4. Seed Hotels
INSERT INTO Hotels (destination_id, hotel_name, star_rating, address, amenities, base_price_per_night) VALUES
(1, 'Himalayan River Resort & Spa', 4, 'Naggar Road, Manali', 'WiFi, Mountain View, Heater, Multi-Cuisine Restaurant', 2600.00),
(1, 'Solang Valley Alpine Retreat', 3, 'Old Manali Village', 'WiFi, Bonfire, Cafe, Free Trek Guide', 1500.00),
(2, 'Calangute Palms Beach Resort', 4, 'Holiday Street, Calangute, Goa', 'Pool, Free Breakfast, Ocean View, Bar', 3200.00),
(2, 'Zostel Backpacker Hub Goa', 3, 'Anjuna Flea Market Road, Goa', 'WiFi, Shared Lounge, Cafe, Bike Rental', 1100.00),
(3, 'Royal Heritage Haveli Palace', 5, 'Khatipura, Jaipur', 'Swimming Pool, Royal Dining, Spa, Folk Music', 4800.00),
(3, 'Pink City Boutique Inn', 3, 'MI Road, Jaipur', 'WiFi, Rooftop Restaurant, Travel Desk', 1600.00),
(5, 'Ganga Kinare Riverside Retreat', 4, 'Veerbhadra Road, Rishikesh', 'Ganga View, Yoga Deck, Ayurvedic Spa', 2900.00);

-- 5. Seed Rooms
INSERT INTO Rooms (hotel_id, room_number, room_type, price_per_night, max_occupancy, is_available) VALUES
(3001, '101', 'Deluxe Room', 2600.00, 2, 1),
(3001, '102', 'Luxury Suite', 4200.00, 3, 1),
(3002, '201', 'Standard Double', 1500.00, 2, 1),
(3002, '202', 'Standard Single', 1000.00, 1, 1),
(3003, '301', 'Deluxe Room', 3200.00, 2, 1),
(3003, '302', 'Luxury Suite', 5500.00, 4, 1),
(3004, '401', 'Standard Double', 1100.00, 2, 1),
(3005, '501', 'Luxury Suite', 4800.00, 2, 1),
(3006, '601', 'Standard Double', 1600.00, 2, 1),
(3007, '701', 'Deluxe Room', 2900.00, 2, 1);

-- 6. Seed Packages
INSERT INTO Packages (destination_id, package_name, tier, duration_days, price_per_person, inclusions, description) VALUES
(1, 'Manali Explorer Budget Trip', 'Budget', 3, 5499.00, 'Volvo Bus + Standard Hotel + Breakfast + Local Solang Valley Tour', 'Ideal for students and backpackers seeking snow and adventure without exceeding budget.'),
(1, 'Manali Alpine Experience', 'Standard', 4, 8999.00, 'AC Train/Cab + 3-Star Resort + All Meals + Rohtang Pass & Atal Tunnel Tour', 'Balanced leisure getaway for couples and friends with comfortable transport and resort stay.'),
(1, 'Manali Royal Himalayan Luxury', 'Premium', 5, 14999.00, 'Flight/Luxury SUV + 4-Star Mountain Suite + Spa + Private Rohtang Expedition', 'Ultra-luxurious Himalayan retreat featuring private chauffeur, fine dining, and heated suites.'),
(2, 'Goa Sun & Sand Getaway', 'Budget', 3, 4999.00, 'Sleeper Train + Boutique Hotel + North Goa Beach Tour', 'Perfect weekend break covering Baga, Calangute and Fort Aguada on an economical budget.'),
(2, 'Goa Coastal Grandeur', 'Standard', 4, 9499.00, 'Flight/AC Train + 4-Star Beach Resort + Scuba Diving + Sunset Cruise', 'Immersive coastal holiday with water sports, heritage churches, and beachside barbecue.'),
(3, 'Jaipur Royal Heritage Tour', 'Budget', 2, 3499.00, 'Superfast Train + Haveli Stay + Amber Fort & City Palace Guide', 'Compact cultural heritage expedition covering majestic forts and handicraft bazaars.');

-- 7. Seed Sightseeing
INSERT INTO Sightseeing (destination_id, spot_name, entry_fee, duration_hours, spot_category, description) VALUES
(1, 'Solang Valley Snow Point', 250.00, 4.0, 'Adventure', 'Hub for paragliding, zorbing, skiing and snow scooter adventures.'),
(1, 'Hadimba Temple & Cedar Forest', 50.00, 1.5, 'Heritage', 'Ancient wooden pagoda temple built in 1553 amid dense Dhungri cedar forests.'),
(1, 'Rohtang Pass & Atal Tunnel', 500.00, 6.0, 'Nature', 'Panoramic 13,058 ft mountain pass offering snow slopes and high-altitude vistas.'),
(2, 'Fort Aguada & Lighthouse', 100.00, 2.5, 'Heritage', '17th-century Portuguese fortress overlooking Sinquerim Beach and Arabian Sea.'),
(2, 'Dudhsagar Waterfalls Trek', 400.00, 5.0, 'Nature', 'Four-tiered cascading white waterfall surrounded by lush Bhagwan Mahaveer Sanctuary.'),
(3, 'Amber Fort Palace', 200.00, 3.5, 'Heritage', 'UNESCO World Heritage fort celebrated for Sheesh Mahal and Rajput architecture.'),
(3, 'Hawa Mahal Palace of Winds', 100.00, 1.5, 'Heritage', 'Five-story pink sandstone palace with 953 honeycomb windows.');

-- 8. Seed Coupons
INSERT INTO Coupons (code, discount_percentage, max_discount, min_booking_amount, valid_until, is_active) VALUES
('RAAHVO10', 10.00, 1000.00, 2000.00, '2027-12-31', 1),
('FIRSTTRIP', 15.00, 1500.00, 3000.00, '2027-12-31', 1),
('EXPLORE20', 20.00, 2500.00, 6000.00, '2027-12-31', 1),
('STUDENTPASS', 12.00, 800.00, 1500.00, '2027-12-31', 1);

-- 9. Seed Sample Booking for Aarav Sharma (user 1002)
INSERT INTO Bookings (booking_pnr, user_id, booking_type, booking_date, travel_date, total_amount, discount_amount, final_payable, status) VALUES
('RAAHVO-MNL-84920', 1002, 'PACKAGE', GETDATE(), '2026-12-20', 10998.00, 1000.00, 9998.00, 'CONFIRMED');

INSERT INTO Passengers (booking_id, full_name, age, gender, seat_number, id_proof) VALUES
(6001, 'Aarav Sharma', 22, 'Male', 'A1', 'AADHAAR-8472'),
(6001, 'Ananya Verma', 21, 'Female', 'A2', 'AADHAAR-9931');

INSERT INTO Payments (booking_id, transaction_ref, payment_method, amount, payment_status) VALUES
(6001, 'TXN-RAAHVO-98210384', 'UPI', 9998.00, 'SUCCESS');
GO
