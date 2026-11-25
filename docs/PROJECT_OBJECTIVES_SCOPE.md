# RAAHVO - PROJECT OBJECTIVES AND SCOPE

## 1. Project Objectives

### Primary Objectives
1. **Intelligent Budget Trip Planning**: Implement an algorithmic recommendation engine that takes starting location, destination, group size, duration, and financial ceiling to dynamically compute three balanced tiers (Budget, Standard, Premium) with full itemized cost breakdowns (transport, hotel, meals, sightseeing, GST).
2. **Multi-Modal Transport Search & Reservation**: Provide integrated search, fare calculation (with runtime polymorphism), and seat locking across Trains, Buses, and Flights.
3. **Hospitality & Room Inventory Management**: Enable hotel discovery indexed by destination, star rating, and room availability with thread-safe room allocation.
4. **Resilient Transaction Management**: Execute bookings, passenger registrations, and payment simulations within ACID-compliant JDBC database transactions.
5. **Rigorous OOP & Java Syllabus Integration**: Naturally incorporate Encapsulation, Inheritance, Polymorphism, Abstraction, Collections (`ArrayList`, `LinkedList`, `HashSet`, `HashMap`, `TreeMap`, `PriorityQueue`), Multithreading, Generics, Lambda Expressions, and File I/O.

## 2. Project Scope

### In-Scope (Phase 1, 2 & 3)
- **Console Interface**: Polished ANSI-colored CLI compatible with Windows Terminal, VS Code Terminal, PowerShell, and Unix shells.
- **User & Admin Management**: Sign-up, credential verification, brute-force attempt throttling, profile updates, and role-based permissions.
- **Destinations**: Real-world curated destinations across India (Hill Stations, Beaches, Heritage, Pilgrimage, Adventure, Nature).
- **Coupons & Discounts**: Validation, minimum booking thresholds, and percentage discounts with caps.
- **Invoicing & Persistence**: Timestamped text invoice generation exported to `invoices/INVOICE_<PNR>.txt`, and dual-mode persistence (Microsoft SQL Server JDBC + synchronized In-Memory fallback store).
- **Concurrency Safety**: Multi-threaded seat booking simulation demonstrating synchronized locks to eliminate double-booking bugs.

### Out-of-Scope (Boundaries)
- Graphical user interfaces (GUI, Swing, JavaFX, Web, or Mobile).
- Live bank payment gateway integration (Strictly simulated to maintain zero-credential security compliance).
- Real GPS/satellite live tracking.
