package com.mshaq.machinecoding.controller;

import com.mshaq.machinecoding.dto.Success;
import com.mshaq.machinecoding.model.Ticket;
import com.mshaq.machinecoding.model.Vehicle;
import com.mshaq.machinecoding.service.ParkingLotService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parkinglot")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PostMapping("{levelId}/park")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket parkVehicle(@PathVariable Long levelId, @RequestBody Vehicle vehicle) {
        return parkingLotService.parkVehicle(levelId, vehicle);
    }

    @DeleteMapping("/{ticketId}")
    public Success removeVehicle(@PathVariable Long ticketId) {
        return parkingLotService.removeVehicle(ticketId);
    }
}
