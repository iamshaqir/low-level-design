package com.mshaq.machinecoding.model.impl;

import com.mshaq.machinecoding.Enum.ParkingSpotType;
import com.mshaq.machinecoding.Enum.VehicleType;
import com.mshaq.machinecoding.model.Vehicle;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Truck extends Vehicle {
    public Truck(String licensePlate, ParkingSpotType parkingSpotType) {
        super(licensePlate, VehicleType.TRUCK, parkingSpotType);
    }
}
