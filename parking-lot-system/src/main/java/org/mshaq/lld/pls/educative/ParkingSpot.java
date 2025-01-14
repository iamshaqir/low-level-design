package org.mshaq.lld.pls.educative;

import java.util.List;

public class ParkingSpot {

    private final int spotNumber;
    private final VehicleType vehicleType;
    private Vehicle parkedVehicle;

    public ParkingSpot(int spotNumber, VehicleType vehicleType) {
        this.vehicleType = vehicleType;
        this.spotNumber = spotNumber;
    }

    public boolean isAvailable() {
        return vehicleType == null;
    }

    public void parkVehicle(Vehicle vehicle) {
        if (isAvailable() && vehicle.getVehicleType().equals(vehicleType)) {
            parkedVehicle = vehicle;
        } else {
            throw new IllegalArgumentException("Invalid vehicle type or spot already occupied!");
        }
    }

    public void unParkVehicle() {
        parkedVehicle = null;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }
}
