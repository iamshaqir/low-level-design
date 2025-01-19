package com.mshaq.machinecoding.model;

import com.mshaq.machinecoding.Enum.ParkingSpotType;
import com.mshaq.machinecoding.Enum.VehicleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "VEHICLE_TYPE_D")
public abstract class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licensePlate;

    @Enumerated(value = EnumType.STRING)
    private VehicleType vehicleType;

    @Enumerated(value = EnumType.STRING)
    private ParkingSpotType parkingSpotType;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "vehicle")
    private ParkingSpot parkingSpot;

    public Vehicle(String licensePlate, VehicleType type, ParkingSpotType parkingSpotType) {
        this.licensePlate = licensePlate;
        this.parkingSpotType = parkingSpotType;
        this.vehicleType = type;
    }
}
