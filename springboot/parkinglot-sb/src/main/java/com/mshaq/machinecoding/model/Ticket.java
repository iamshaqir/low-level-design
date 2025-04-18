package com.mshaq.machinecoding.model;

import com.mshaq.machinecoding.Enum.ParkingSpotType;
import com.mshaq.machinecoding.Enum.TicketStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long levelId;

    @OneToOne
    private Vehicle vehicle;

    @OneToOne
    private ParkingSpot parkingSpot;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    @Enumerated(value = EnumType.STRING)
    private TicketStatus status = TicketStatus.ACTIVE;

    private Double amountPaid;

    public Ticket(Long levelId, Vehicle vehicle, ParkingSpot parkingSpot) {
        this.levelId = levelId;
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = LocalDateTime.now();
    }
}
