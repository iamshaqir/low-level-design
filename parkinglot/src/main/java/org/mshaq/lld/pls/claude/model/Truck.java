package org.mshaq.lld.pls.claude.model;

import org.mshaq.lld.pls.claude.Enum.VehicleType;

public class Truck extends Vehicle {
    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }
}
