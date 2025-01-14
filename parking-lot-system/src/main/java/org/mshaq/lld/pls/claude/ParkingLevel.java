package org.mshaq.lld.pls.claude;

import org.mshaq.lld.pls.claude.Enum.ParkingSpotType;
import org.mshaq.lld.pls.claude.Enum.VehicleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParkingLevel {

    private String levelId;
    private List<ParkingSpot> parkingSpots;

    public ParkingLevel(String levelId, int spots) {
        this.levelId = levelId;
        parkingSpots = new ArrayList<>(spots);
    }

    public Optional<ParkingSpot> findParkingSpot(VehicleType vehicleType) {
        ParkingSpotType requiredSpotType = getParkingSpotType(vehicleType);
        return parkingSpots.stream()
                .filter(spot -> !spot.isOccupied()
                        && spot.getSpotType().equals(requiredSpotType))
                .findFirst();
    }

    private ParkingSpotType getParkingSpotType(VehicleType vehicleType) {
        return switch (vehicleType) {
            case MOTORCYCLE -> ParkingSpotType.MOTORCYCLE;
            case CAR -> ParkingSpotType.COMPACT;
            case TRUCK -> ParkingSpotType.LARGE;
            default -> throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        };
    }
}
