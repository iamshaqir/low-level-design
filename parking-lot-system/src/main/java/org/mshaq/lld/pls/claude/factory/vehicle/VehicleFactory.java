package org.mshaq.lld.pls.claude.factory.vehicle;

import org.mshaq.lld.pls.claude.Enum.VehicleType;
import org.mshaq.lld.pls.claude.model.Vehicle;

public abstract class VehicleFactory {
    public abstract Vehicle createVehicle(String licensePlate);

    public static VehicleFactory getFactory(VehicleType type) {
        return switch (type) {
            case MOTORCYCLE -> new MotorcycleFactory();
            case CAR -> new CarFactory();
            case TRUCK -> new TruckFactory();
            default -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
        };
    }
}
