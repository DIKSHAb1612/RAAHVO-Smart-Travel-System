package com.raahvo.common;

import com.raahvo.member1.Admin;
import com.raahvo.member1.Destination;
import com.raahvo.member1.User;
import com.raahvo.member2.*;
import com.raahvo.member3.Coupon;
import com.raahvo.member3.Sightseeing;
import com.raahvo.member3.TravelPackage;
import com.raahvo.member4.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * InMemoryDatabase acts as a synchronized, thread-safe persistence layer.
 * Prepopulated with data matching database/seed_data.sql.
 * Used for offline university demonstrations and fast in-memory execution.
 */
public class InMemoryDatabase {
    private static InMemoryDatabase instance;

    // Collections demonstrating Java Collections Framework
    public final Map<Integer, User> users = new ConcurrentHashMap<>();
    public final Set<String> registeredEmails = Collections.synchronizedSet(new HashSet<>());
    public final List<Destination> destinations = Collections.synchronizedList(new ArrayList<>());
    public final List<Transport> transports = Collections.synchronizedList(new ArrayList<>());
    public final List<Hotel> hotels = Collections.synchronizedList(new ArrayList<>());
    public final List<Room> rooms = Collections.synchronizedList(new ArrayList<>());
    public final List<TravelPackage> packages = Collections.synchronizedList(new ArrayList<>());
    public final List<Sightseeing> sightseeingSpots = Collections.synchronizedList(new ArrayList<>());
    public final Map<String, Coupon> coupons = new ConcurrentHashMap<>();
    public final Map<String, Booking> bookingsByPnr = new ConcurrentHashMap<>();
    public final Map<Integer, List<Booking>> userBookings = new ConcurrentHashMap<>();
    public final List<Payment> payments = Collections.synchronizedList(new ArrayList<>());
    public final List<Cancellation> cancellations = Collections.synchronizedList(new ArrayList<>());

    // ID Generators
    public final AtomicInteger userIdGen = new AtomicInteger(1005);
    public final AtomicInteger bookingIdGen = new AtomicInteger(6002);
    public final AtomicInteger paymentIdGen = new AtomicInteger(7002);
    public final AtomicInteger cancelIdGen = new AtomicInteger(101);
    public final AtomicInteger destIdGen = new AtomicInteger(10);
    public final AtomicInteger transportIdGen = new AtomicInteger(2020);

    private InMemoryDatabase() {
        seedInitialData();
    }

    public static synchronized InMemoryDatabase getInstance() {
        if (instance == null) {
            instance = new InMemoryDatabase();
        }
        return instance;
    }

    private void seedInitialData() {
        // Seed Users
        Admin admin = new Admin(1001, "System Administrator", "admin@raahvo.com", "9876543210", "admin123", "Operations", 5);
        User u1 = new User(1002, "Aarav Sharma", "aarav@gmail.com", "9811122233", "pass123", "CUSTOMER");
        User u2 = new User(1003, "Ananya Verma", "ananya@gmail.com", "9822233344", "pass123", "CUSTOMER");
        User u3 = new User(1004, "Rohan Mehta", "rohan@gmail.com", "9833344455", "pass123", "CUSTOMER");

        users.put(admin.getUserId(), admin);
        users.put(u1.getUserId(), u1);
        users.put(u2.getUserId(), u2);
        users.put(u3.getUserId(), u3);

        registeredEmails.add(admin.getEmail().toLowerCase());
        registeredEmails.add(u1.getEmail().toLowerCase());
        registeredEmails.add(u2.getEmail().toLowerCase());
        registeredEmails.add(u3.getEmail().toLowerCase());

        // Seed Destinations
        destinations.add(new Destination(1, "Manali", "Himachal Pradesh", "India", "Hill Station",
                "Nestled in the Beas River valley, famed for snow-capped peaks and Solang Valley adventures.", 2200.0, 4.8));
        destinations.add(new Destination(2, "Goa", "Goa", "India", "Beach",
                "Tropical coastal paradise with golden sands, Portuguese heritage, and water sports.", 2800.0, 4.7));
        destinations.add(new Destination(3, "Jaipur", "Rajasthan", "India", "Heritage",
                "The historic Pink City, home to majestic Amber Fort, Hawa Mahal, and royal culture.", 1900.0, 4.6));
        destinations.add(new Destination(4, "Varanasi", "Uttar Pradesh", "India", "Pilgrimage",
                "Spiritual capital along the holy Ganges, known for ancient ghats and Ganga Aarti.", 1400.0, 4.7));
        destinations.add(new Destination(5, "Rishikesh", "Uttarakhand", "India", "Adventure",
                "Yoga capital and thrill-seekers hub featuring river rafting and Himalayan foothill trails.", 1800.0, 4.8));
        destinations.add(new Destination(6, "Munnar", "Kerala", "India", "Nature",
                "Sprawling tea gardens, mist-clad valleys, and romantic waterfalls in God's Own Country.", 2300.0, 4.9));

        // Seed Transports
        transports.add(new Bus(2001, "Himachal Volvo Royal", "Delhi", "Manali", "20:30", "08:30", 1250.0, 40, 36, "HP-01-A-4422", "Volvo Multi-Axle", true, true));
        transports.add(new Train(2002, "Vande Bharat Express", "Delhi", "Manali", "06:00", "15:30", 1650.0, 60, 52, "22448", "Chair Car / Executive"));
        transports.add(new Flight(2003, "Air India Regional", "Delhi", "Manali", "07:15", "08:45", 4850.0, 30, 24, "AI-883", "Air India", 15, "Economy"));

        transports.add(new Train(2004, "Goa Express Superfast", "Delhi", "Goa", "15:15", "19:40", 2100.0, 80, 70, "12780", "3A AC 3-Tier"));
        transports.add(new Flight(2005, "IndiGo Non-Stop", "Delhi", "Goa", "09:20", "11:55", 4200.0, 50, 41, "6E-2041", "IndiGo", 15, "Economy"));
        transports.add(new Bus(2006, "IntrCity SmartBus", "Delhi", "Goa", "14:00", "22:00", 2400.0, 35, 30, "DL-01-B-9988", "Luxury Sleeper AC", true, true));

        transports.add(new Train(2007, "Jaipur Duronto Express", "Mumbai", "Jaipur", "23:00", "14:35", 1850.0, 75, 68, "12239", "2A AC 2-Tier"));
        transports.add(new Flight(2008, "SpiceJet Direct", "Mumbai", "Jaipur", "10:45", "12:30", 3600.0, 45, 38, "SG-342", "SpiceJet", 15, "Economy"));
        transports.add(new Bus(2009, "Shrinath Travels Multi-Axle", "Mumbai", "Jaipur", "16:00", "11:00", 1600.0, 36, 29, "RJ-14-C-1122", "Volvo Sleeper", true, false));

        transports.add(new Train(2010, "Dehradun Shatabdi", "Delhi", "Rishikesh", "06:45", "11:30", 950.0, 90, 84, "12017", "AC Chair Car"));
        transports.add(new Bus(2011, "Zingbus Electric Coach", "Delhi", "Rishikesh", "07:30", "13:00", 650.0, 45, 40, "UK-07-E-3300", "AC Seater", true, true));

        // Seed Hotels
        hotels.add(new Hotel(3001, 1, "Himalayan River Resort & Spa", 4, "Naggar Road, Manali", "WiFi, Mountain View, Heater, Restaurant", 2600.0));
        hotels.add(new Hotel(3002, 1, "Solang Alpine Retreat", 3, "Old Manali Village", "WiFi, Bonfire, Cafe, Free Trek Guide", 1500.0));
        hotels.add(new Hotel(3003, 2, "Calangute Palms Beach Resort", 4, "Calangute Beach Road, Goa", "Pool, Free Breakfast, Ocean View, Bar", 3200.0));
        hotels.add(new Hotel(3004, 2, "Zostel Backpacker Hub Goa", 3, "Anjuna Flea Market Road, Goa", "WiFi, Shared Lounge, Cafe, Bike Rental", 1100.0));
        hotels.add(new Hotel(3005, 3, "Royal Heritage Haveli Palace", 5, "Khatipura, Jaipur", "Swimming Pool, Royal Dining, Spa, Folk Music", 4800.0));
        hotels.add(new Hotel(3006, 3, "Pink City Boutique Inn", 3, "MI Road, Jaipur", "WiFi, Rooftop Restaurant, Travel Desk", 1600.0));
        hotels.add(new Hotel(3007, 5, "Ganga Kinare Riverside Retreat", 4, "Veerbhadra Road, Rishikesh", "Ganga View, Yoga Deck, Ayurvedic Spa", 2900.0));

        // Seed Rooms
        rooms.add(new Room(1, 3001, "101", "Deluxe Room", 2600.0, 2, true));
        rooms.add(new Room(2, 3001, "102", "Luxury Suite", 4200.0, 3, true));
        rooms.add(new Room(3, 3002, "201", "Standard Double", 1500.0, 2, true));
        rooms.add(new Room(4, 3002, "202", "Standard Single", 1000.0, 1, true));
        rooms.add(new Room(5, 3003, "301", "Deluxe Room", 3200.0, 2, true));
        rooms.add(new Room(6, 3003, "302", "Luxury Suite", 5500.0, 4, true));
        rooms.add(new Room(7, 3004, "401", "Standard Double", 1100.0, 2, true));
        rooms.add(new Room(8, 3005, "501", "Royal Suite", 4800.0, 2, true));
        rooms.add(new Room(9, 3006, "601", "Standard Double", 1600.0, 2, true));
        rooms.add(new Room(10, 3007, "701", "Deluxe Room", 2900.0, 2, true));

        // Seed Packages
        packages.add(new TravelPackage(4001, 1, "Manali Explorer Budget Trip", "Budget", 3, 5499.0,
                "Volvo Bus + Standard Hotel + Breakfast + Solang Valley Tour",
                "Ideal for students and backpackers seeking snow and adventure."));
        packages.add(new TravelPackage(4002, 1, "Manali Alpine Experience", "Standard", 4, 8999.0,
                "AC Train/Cab + 3-Star Resort + All Meals + Rohtang Pass Tour",
                "Balanced leisure getaway for couples and friends with comfortable transport and stay."));
        packages.add(new TravelPackage(4003, 1, "Manali Royal Himalayan Luxury", "Premium", 5, 14999.0,
                "Flight/Luxury SUV + 4-Star Mountain Suite + Spa + Private Rohtang Expedition",
                "Ultra-luxurious Himalayan retreat featuring private chauffeur, fine dining, and heated suites."));
        packages.add(new TravelPackage(4004, 2, "Goa Sun & Sand Getaway", "Budget", 3, 4999.0,
                "Sleeper Train + Boutique Hotel + North Goa Beach Tour",
                "Perfect weekend break covering Baga, Calangute, and Fort Aguada on an economical budget."));
        packages.add(new TravelPackage(4005, 2, "Goa Coastal Grandeur", "Standard", 4, 9499.0,
                "Flight/AC Train + 4-Star Beach Resort + Scuba Diving + Sunset Cruise",
                "Immersive coastal holiday with water sports, heritage churches, and beachside barbecue."));
        packages.add(new TravelPackage(4006, 3, "Jaipur Royal Heritage Tour", "Budget", 2, 3499.0,
                "Superfast Train + Haveli Stay + Amber Fort & City Palace Guide",
                "Compact cultural heritage expedition covering majestic forts and handicraft bazaars."));

        // Seed Sightseeing
        sightseeingSpots.add(new Sightseeing(5001, 1, "Solang Valley Snow Point", 250.0, 4.0, "Adventure", "Hub for paragliding, zorbing, and skiing."));
        sightseeingSpots.add(new Sightseeing(5002, 1, "Hadimba Temple & Forest", 50.0, 1.5, "Heritage", "Ancient wooden pagoda temple built in 1553 amid cedar forests."));
        sightseeingSpots.add(new Sightseeing(5003, 1, "Rohtang Pass & Atal Tunnel", 500.0, 6.0, "Nature", "Panoramic 13,058 ft mountain pass offering snow slopes."));
        sightseeingSpots.add(new Sightseeing(5004, 2, "Fort Aguada & Lighthouse", 100.0, 2.5, "Heritage", "17th-century Portuguese fortress overlooking Arabian Sea."));
        sightseeingSpots.add(new Sightseeing(5005, 2, "Dudhsagar Waterfalls", 400.0, 5.0, "Nature", "Four-tiered cascading waterfall inside national sanctuary."));
        sightseeingSpots.add(new Sightseeing(5006, 3, "Amber Fort Palace", 200.0, 3.5, "Heritage", "UNESCO World Heritage fort celebrated for Sheesh Mahal."));
        sightseeingSpots.add(new Sightseeing(5007, 3, "Hawa Mahal", 100.0, 1.5, "Heritage", "Five-story pink sandstone palace with 953 honeycomb windows."));

        // Seed Coupons
        coupons.put("RAAHVO10", new Coupon(1, "RAAHVO10", 10.0, 1000.0, 2000.0, LocalDate.now().plusMonths(6), true));
        coupons.put("FIRSTTRIP", new Coupon(2, "FIRSTTRIP", 15.0, 1500.0, 3000.0, LocalDate.now().plusMonths(6), true));
        coupons.put("EXPLORE20", new Coupon(3, "EXPLORE20", 20.0, 2500.0, 6000.0, LocalDate.now().plusMonths(6), true));
        coupons.put("STUDENTPASS", new Coupon(4, "STUDENTPASS", 12.0, 800.0, 1500.0, LocalDate.now().plusMonths(6), true));

        // Seed Sample Booking for Aarav Sharma (user 1002)
        Booking sampleBooking = new Booking(6001, "RAAHVO-MNL-84920", 1002, "PACKAGE",
                LocalDate.now().plusDays(20), 10998.0, 1000.0, 9998.0, "CONFIRMED");
        sampleBooking.addPassenger(new Passenger(1, 6001, "Aarav Sharma", 22, "Male", "A1", "AADHAAR-8472"));
        sampleBooking.addPassenger(new Passenger(2, 6001, "Ananya Verma", 21, "Female", "A2", "AADHAAR-9931"));

        bookingsByPnr.put(sampleBooking.getBookingPnr(), sampleBooking);
        userBookings.computeIfAbsent(1002, k -> new ArrayList<>()).add(sampleBooking);

        payments.add(new Payment(7001, 6001, "TXN-RAAHVO-98210384", "UPI", 9998.0, "SUCCESS"));
    }
}
