package com.mshaq.machinecoding.model.impl;

import com.mshaq.machinecoding.Enum.ParkingSpotType;
import com.mshaq.machinecoding.Enum.VehicleType;
import com.mshaq.machinecoding.model.Vehicle;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Car extends Vehicle {
    public Car(String licensePlate, ParkingSpotType parkingSpotType) {
        super(licensePlate, VehicleType.CAR, parkingSpotType);
    }
}
