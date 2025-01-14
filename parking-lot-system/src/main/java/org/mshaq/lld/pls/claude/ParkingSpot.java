package org.mshaq.lld.pls.claude;

import org.mshaq.lld.pls.claude.Enum.ParkingSpotType;
import org.mshaq.lld.pls.claude.model.Vehicle;

public class ParkingSpot {
    private String levelId;
    private String spotId;
    private ParkingSpotType spotType;
    private boolean isOccupied;
    private Vehicle vehicle;

    // Driver will create parking spot with required fields to be passed
    public ParkingSpot(String levelId, String spotId, ParkingSpotType spotType) {
        this.levelId = levelId;
        this.spotId = spotId;
        this.spotType = spotType;
        this.isOccupied = false;
    }

    public boolean parkVehicle(Vehicle vehicle) {
        if (isOccupied) return false;
        this.vehicle = vehicle;
        this.isOccupied = true;
        return true;
    }

    public boolean remove() {
        if (!isOccupied) return false;
        this.vehicle = null;
        this.isOccupied = false;
        return true;
    }

    public String getSpotId() {
        return spotId;
    }

    public ParkingSpotType getSpotType() {
        return spotType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }
}
