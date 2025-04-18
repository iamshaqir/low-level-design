package org.mshaq.lld.pls.claude.factory.parkingspot;

import org.mshaq.lld.pls.claude.Enum.ParkingSpotType;
import org.mshaq.lld.pls.claude.ParkingSpot;

public class LargeParkingFactory extends AbstractParkingSpotFactory {
    @Override
    public ParkingSpot createParkingSpot(String spotId, String levelId) {
        return new ParkingSpot(levelId, spotId, ParkingSpotType.COMPACT);
    }
}
