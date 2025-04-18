package com.mshaq.machinecoding.model;

import com.mshaq.machinecoding.Enum.ParkingSpotType;
import com.mshaq.machinecoding.Enum.SpotStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String spotNumber;

    @Enumerated(value = EnumType.STRING)
    private ParkingSpotType spotType;

    @Enumerated(EnumType.STRING)
    private SpotStatus status = SpotStatus.AVAILABLE;

    @OneToOne(cascade = CascadeType.ALL)
    private Vehicle vehicle;

    @ManyToOne()
    private ParkingLevel level;


    public ParkingSpot(String spotNumber, ParkingSpotType spotType, SpotStatus status) {
        this.spotNumber = spotNumber;
        this.spotType = spotType;
        this.status = status;
    }
}
