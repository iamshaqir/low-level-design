package org.mshaq.lld.pls.claude;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mshaq.lld.pls.claude.Enum.ParkingSpotType;
import org.mshaq.lld.pls.claude.model.Vehicle;

public class ParkingSpot {

    private static final Logger logger = LogManager.getLogger(ParkingSpot.class);
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
        if (isOccupied) {
            logger.warn("Attempt to park vehicle {} in occupied spot {}", vehicle.getLicensePlate(), spotId);
            return false;
        }
        this.vehicle = vehicle;
        this.isOccupied = true;
        logger.info("Vehicle {} parked in spot {}", vehicle.getLicensePlate(), spotId);
        return true;
    }

    public boolean remove() {
        if (!isOccupied) {
            logger.warn("Attempt to remove vehicle from empty spot {}", spotId);
            return false;
        }
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
