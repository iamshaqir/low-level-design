package com.mshaq.machinecoding.model;

import com.mshaq.machinecoding.Enum.ParkingSpotType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class ParkingLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String levelName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "level", orphanRemoval = true)
    private List<ParkingSpot> parkingSpots = new ArrayList<>();

    public ParkingLevel(String levelName) {
        this.levelName = levelName;
    }
    public void addParkingSpot(ParkingSpot parkingSpot) {
        parkingSpots.add(parkingSpot);
        parkingSpot.setLevel(this);
    }

    public void removeParkingSpot(ParkingSpot parkingSpot) {
        if (parkingSpots.contains(parkingSpot)) {
            parkingSpots.remove(parkingSpot);
            parkingSpot.setLevel(null);
        }
    }

    public void addAllParkingSpots(Collection<ParkingSpot> spots) {
        if (!spots.isEmpty()) {
            parkingSpots.addAll(spots);
        }
        spots.forEach(spot -> spot.setLevel(this));
    }
}
