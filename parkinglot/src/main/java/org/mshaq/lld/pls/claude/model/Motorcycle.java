package org.mshaq.lld.pls.claude.model;

import org.mshaq.lld.pls.claude.Enum.VehicleType;

public class Motorcycle extends Vehicle{
    public Motorcycle(String licensePlate) {
        super(licensePlate, VehicleType.MOTORCYCLE);
    }
}
