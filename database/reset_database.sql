-- ============================================================================
-- RAAHVO - Smart Travel Booking & Trip Planning System
-- WARNING: DESTRUCTIVE DATABASE RESET SCRIPT
-- ============================================================================
-- CAUTION: Running this script will DROP ALL TABLES in RAAHVO_DB and
-- PERMANENTLY DELETE ALL existing user accounts, bookings, and payment data.
-- To create tables safely without deleting data, use: schema.sql
-- ============================================================================

USE RAAHVO_DB;
GO

PRINT '>>> WARNING: Initiating Destructive Reset of RAAHVO_DB...';
GO

-- Drop foreign keys and tables in reverse dependency order
IF OBJECT_ID('Refunds', 'U') IS NOT NULL DROP TABLE Refunds;
IF OBJECT_ID('Cancellations', 'U') IS NOT NULL DROP TABLE Cancellations;
IF OBJECT_ID('Payments', 'U') IS NOT NULL DROP TABLE Payments;
IF OBJECT_ID('Passengers', 'U') IS NOT NULL DROP TABLE Passengers;
IF OBJECT_ID('Bookings', 'U') IS NOT NULL DROP TABLE Bookings;
IF OBJECT_ID('Coupons', 'U') IS NOT NULL DROP TABLE Coupons;
IF OBJECT_ID('Sightseeing', 'U') IS NOT NULL DROP TABLE Sightseeing;
IF OBJECT_ID('Packages', 'U') IS NOT NULL DROP TABLE Packages;
IF OBJECT_ID('Rooms', 'U') IS NOT NULL DROP TABLE Rooms;
IF OBJECT_ID('Hotels', 'U') IS NOT NULL DROP TABLE Hotels;
IF OBJECT_ID('Seats', 'U') IS NOT NULL DROP TABLE Seats;
IF OBJECT_ID('TransportSchedules', 'U') IS NOT NULL DROP TABLE TransportSchedules;
IF OBJECT_ID('Transport', 'U') IS NOT NULL DROP TABLE Transport;
IF OBJECT_ID('Destinations', 'U') IS NOT NULL DROP TABLE Destinations;
IF OBJECT_ID('Admins', 'U') IS NOT NULL DROP TABLE Admins;
IF OBJECT_ID('Users', 'U') IS NOT NULL DROP TABLE Users;
GO

PRINT '>>> Destructive reset complete: All tables dropped.';
PRINT '>>> Now execute schema.sql and seed_data.sql to initialize clean tables.';
GO
