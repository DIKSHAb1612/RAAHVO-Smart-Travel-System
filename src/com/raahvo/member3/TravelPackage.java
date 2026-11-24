package com.raahvo.member3;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 3 - Trip Planning & Packages
 * ============================================================================
 * Class: TravelPackage
 * Course Concept: Encapsulation & Domain Modeling (OOP)
 * 
 * Purpose:
 * Represents pre-packaged curated holiday itineraries combining transport,
 * accommodation, and sightseeing for popular destinations.
 */
public class TravelPackage {
    private int packageId;
    private int destinationId;          // Foreign Key to Destinations table
    private String packageName;
    private String tier;                // Budget, Standard, Premium
    private int durationDays;
    private double pricePerPerson;
    private String inclusions;          // Textual summary of package inclusions
    private String description;

    public TravelPackage(int packageId, int destinationId, String packageName, String tier,
                         int durationDays, double pricePerPerson, String inclusions, String description) {
        this.packageId = packageId;
        this.destinationId = destinationId;
        this.packageName = packageName;
        this.tier = tier;
        this.durationDays = durationDays;
        this.pricePerPerson = pricePerPerson;
        this.inclusions = inclusions;
        this.description = description;
    }

    public int getPackageId() {
        return packageId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getTier() {
        return tier;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public double getPricePerPerson() {
        return pricePerPerson;
    }

    public String getInclusions() {
        return inclusions;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Scales package price linearly with traveller count
     */
    public double calculateTotalCost(int travellers) {
        return this.pricePerPerson * travellers;
    }

    @Override
    public String toString() {
        return String.format("[ID: %-4d] %-32s | Tier: %-8s | %d Days | ₹%-7.2f/person\n         Inclusions: %s",
                packageId, packageName, tier, durationDays, pricePerPerson, inclusions);
    }
}
