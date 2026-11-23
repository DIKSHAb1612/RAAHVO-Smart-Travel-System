package com.raahvo.member3;

import com.raahvo.member1.Destination;
import com.raahvo.member2.Hotel;
import com.raahvo.member2.Transport;

import java.util.ArrayList;
import java.util.List;

/**
 * Member 3: Trip entity representing a customized calculated itinerary generated
 * by the Smart Trip Planner.
 */
public class Trip {
    private String tripId;
    private Destination destination;
    private String sourceCity;
    private int travellers;
    private int days;
    private double allocatedBudget;
    private Transport selectedTransport;
    private Hotel selectedHotel;
    private List<Sightseeing> selectedSightseeing;
    private double transportCost;
    private double hotelCost;
    private double sightseeingCost;
    private double estimatedFoodMiscCost;
    private double totalEstimatedCost;

    public Trip(String tripId, Destination destination, String sourceCity, int travellers, int days, double allocatedBudget) {
        this.tripId = tripId;
        this.destination = destination;
        this.sourceCity = sourceCity;
        this.travellers = travellers;
        this.days = days;
        this.allocatedBudget = allocatedBudget;
        this.selectedSightseeing = new ArrayList<>();
    }

    public String getTripId() {
        return tripId;
    }

    public Destination getDestination() {
        return destination;
    }

    public String getSourceCity() {
        return sourceCity;
    }

    public int getTravellers() {
        return travellers;
    }

    public int getDays() {
        return days;
    }

    public double getAllocatedBudget() {
        return allocatedBudget;
    }

    public Transport getSelectedTransport() {
        return selectedTransport;
    }

    public void setSelectedTransport(Transport selectedTransport, double cost) {
        this.selectedTransport = selectedTransport;
        this.transportCost = cost;
    }

    public Hotel getSelectedHotel() {
        return selectedHotel;
    }

    public void setSelectedHotel(Hotel selectedHotel, double cost) {
        this.selectedHotel = selectedHotel;
        this.hotelCost = cost;
    }

    public List<Sightseeing> getSelectedSightseeing() {
        return selectedSightseeing;
    }

    public void addSightseeing(Sightseeing spot) {
        this.selectedSightseeing.add(spot);
        this.sightseeingCost += (spot.getEntryFee() * travellers);
    }

    public double getTransportCost() {
        return transportCost;
    }

    public double getHotelCost() {
        return hotelCost;
    }

    public double getSightseeingCost() {
        return sightseeingCost;
    }

    public double getEstimatedFoodMiscCost() {
        return estimatedFoodMiscCost;
    }

    public void setEstimatedFoodMiscCost(double estimatedFoodMiscCost) {
        this.estimatedFoodMiscCost = estimatedFoodMiscCost;
    }

    public double getTotalEstimatedCost() {
        return transportCost + hotelCost + sightseeingCost + estimatedFoodMiscCost;
    }

    public double getBudgetDifference() {
        return allocatedBudget - getTotalEstimatedCost();
    }

    public boolean isWithinBudget() {
        return getBudgetDifference() >= 0;
    }
}
