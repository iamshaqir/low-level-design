package org.mshaq.lld.pls.claude;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mshaq.lld.pls.claude.Enum.ParkingSpotType;
import org.mshaq.lld.pls.claude.Enum.VehicleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParkingLevel {

    private static final Logger logger = LogManager.getLogger(ParkingLevel.class);
    private String levelId;
    private List<ParkingSpot> parkingSpots;

    public ParkingLevel(String levelId, List<ParkingSpot> spots) {
        this.levelId = levelId;
        parkingSpots = new ArrayList<>(spots);
        logger.info("Parking {} initialized", levelId);
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

    public String getLevelId() {
        return levelId;
    }
}
