package org.mshaq.lld.pls.claude;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mshaq.lld.pls.claude.Enum.ParkingSpotType;
import org.mshaq.lld.pls.claude.Enum.VehicleType;
import org.mshaq.lld.pls.claude.factory.parkingspot.CompactParkingFactory;
import org.mshaq.lld.pls.claude.factory.parkingspot.LargeParkingFactory;
import org.mshaq.lld.pls.claude.factory.parkingspot.MotorcycleParkingFactory;
import org.mshaq.lld.pls.claude.factory.parkingspot.ParkingSpotFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParkingLevel {

    private static final Logger logger = LogManager.getLogger(ParkingLevel.class);
    private String levelId;
    private List<ParkingSpot> parkingSpots;

    private Map<ParkingSpotType, ParkingSpotFactory> factories;

    public ParkingLevel(String levelId) {
        this.levelId = levelId;
        parkingSpots = new ArrayList<>();
        logger.info("Parking {} initialized", levelId);
        initializeParkingFactories();
    }

    private void initializeParkingFactories() {
        factories.put(ParkingSpotType.COMPACT, new CompactParkingFactory());
        factories.put(ParkingSpotType.LARGE, new LargeParkingFactory());
        factories.put(ParkingSpotType.MOTORCYCLE, new MotorcycleParkingFactory());
    }

    public void addParkingSpot(ParkingSpotType parkingSpotType, String spotId) {
        ParkingSpotFactory parkingSpotFactory = factories.get(parkingSpotType);
        if (parkingSpotFactory == null) {
            throw new IllegalArgumentException("Invalid Parking spot type: " + parkingSpotType);
        }
        ParkingSpot spot = parkingSpotFactory.createParkingSpot(spotId, this.levelId);
        parkingSpots.add(spot);
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
