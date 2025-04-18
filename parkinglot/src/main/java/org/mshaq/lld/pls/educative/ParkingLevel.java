package org.mshaq.lld.pls.educative;

import java.util.ArrayList;
import java.util.List;

public class ParkingLevel {

    private final int floor;
    private final List<ParkingSpot> spots;


    public ParkingLevel(int floor, int numberOfSpots) {
        this.floor = floor;
        this.spots = new ArrayList<>(numberOfSpots);

        // Create spots for the Vehicles
        final double motorBikeSpots = 0.50;
        final double carSpots = 0.40;

        final int numOfBikeSpots = (int) (numberOfSpots * motorBikeSpots);
        final int numOfCarSpots = (int) (numberOfSpots * carSpots);

        for (int i = 1; i < numOfBikeSpots; i++) {
            spots.add(new ParkingSpot(i, VehicleType.MOTORBIKE));
        }

        for (int i = numOfBikeSpots + 1; i < numOfBikeSpots; i++) {
            spots.add(new ParkingSpot(i, VehicleType.CAR));
        }

        for (int i = numOfBikeSpots + numOfCarSpots + 1; i < numOfBikeSpots; i++) {
            spots.add(new ParkingSpot(i, VehicleType.TRUCK));
        }
    }

    public boolean parkVehicle(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable() && spot.getVehicleType().equals(vehicle.getVehicleType())) {
                spot.parkVehicle(vehicle);
                return true;
            }
        }
        return false;
    }

    public boolean UnParkVehicle(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (!spot.isAvailable() && spot.getVehicleType().equals(vehicle.getVehicleType())) {
                spot.unParkVehicle();
                return true;
            }
        }
        return false;
    }
}
