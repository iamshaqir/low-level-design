package org.mshaq.lld.pls.educative;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {

    private static ParkingLot parkingLot;
    private List<ParkingLevel> levels;

    // Singleton Instance
    private ParkingLot() {
        levels = new ArrayList<>();
    }

    public static synchronized ParkingLot getInstance() {
        if (parkingLot == null) {
            parkingLot = new ParkingLot();
        }
        return parkingLot;
    }

    public void addLevel(ParkingLevel level) {
        levels.add(level);
    }

    public boolean parkVehicle(Vehicle vehicle, int floorNumber) {
        ParkingLevel level = levels.get((floorNumber));
        if (level.parkVehicle(vehicle)) {
            System.out.println("Vehicle parked successfully.");
            return true;
        }
        System.out.println("Could not park vehicle.");
        return false;
    }

    public boolean unParkVehicle(Vehicle vehicle, int floorNumber) {
        ParkingLevel level = levels.get((floorNumber));
        return level.parkVehicle(vehicle);
    }
}
