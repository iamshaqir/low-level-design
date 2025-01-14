package org.mshaq.lld.pls.claude.model;

import org.mshaq.lld.pls.claude.Enum.VehicleType;

public class MotorCycle extends Vehicle {
    public MotorCycle(String licensePlate) {
        super(licensePlate, VehicleType.MOTORCYCLE);
    }
}
