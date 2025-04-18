package org.mshaq.lld.pls.claude;

import org.mshaq.lld.pls.claude.Enum.VehicleType;

import java.util.HashMap;
import java.util.Map;

public class ParkingRateCalculator {

    private final Map<VehicleType, Double> baseRates;

    public ParkingRateCalculator() {
        this.baseRates = new HashMap<>();
        baseRates.put(VehicleType.MOTORCYCLE, 10.0);
        baseRates.put(VehicleType.CAR, 20.0);
        baseRates.put(VehicleType.TRUCK, 30.0);
    }

    public double calculateRate(VehicleType vehicleType, long durationInMinutes) {
        double hourlyRate = baseRates.get(vehicleType);
        double hours = durationInMinutes / 60.0;
        return Math.ceil(hours) * hourlyRate;
    }
}
