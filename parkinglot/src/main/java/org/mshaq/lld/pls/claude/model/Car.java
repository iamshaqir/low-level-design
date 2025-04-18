package org.mshaq.lld.pls.claude.model;

import org.mshaq.lld.pls.claude.Enum.VehicleType;

public class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}
