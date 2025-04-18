package com.mshaq.machinecoding.service;

import com.mshaq.machinecoding.Enum.ParkingSpotType;
import com.mshaq.machinecoding.Enum.SpotStatus;
import com.mshaq.machinecoding.Enum.TicketStatus;
import com.mshaq.machinecoding.Enum.VehicleType;
import com.mshaq.machinecoding.dto.Success;
import com.mshaq.machinecoding.exceptions.ParkingSpotNotFoundException;
import com.mshaq.machinecoding.model.ParkingSpot;
import com.mshaq.machinecoding.model.Ticket;
import com.mshaq.machinecoding.model.Vehicle;
import com.mshaq.machinecoding.repository.ParkingSpotRepository;
import com.mshaq.machinecoding.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class ParkingLotService {

    private final ParkingLevelService parkingLevelService;

    private final ParkingSpotRepository parkingSpotRepository;

    private final TicketRepository ticketRepository;

    private final ParkingRateService rateService;

    public ParkingLotService(ParkingLevelService parkingLevelService,
                             ParkingSpotRepository parkingSpotRepository,
                             TicketRepository ticketRepository, ParkingRateService rateService) {
        this.parkingLevelService = parkingLevelService;
        this.parkingSpotRepository = parkingSpotRepository;
        this.ticketRepository = ticketRepository;
        this.rateService = rateService;
    }

    @Transactional
    public Ticket parkVehicle(Long levelId, Vehicle vehicle) {

        // fetch spots
        ParkingSpotType spotType = vehicle.getParkingSpotType();
        ParkingSpot parkingSpot = parkingLevelService.findParkingSpot(levelId, spotType).orElseThrow(() -> {
            log.warn("Parking spot {} in Level ID: {} is occupied", spotType, levelId);
            return new ParkingSpotNotFoundException("Parking spot occupied.");
        });

        // park vehicle
        parkingSpot.setVehicle(vehicle);
        parkingSpot.setStatus(SpotStatus.OCCUPIED);
        parkingSpotRepository.save(parkingSpot);
        log.info("Parked vehicle on Parking spot ID: {}", parkingSpot.getId());

        //create ticket
        Ticket ticket = new Ticket(levelId, vehicle, parkingSpot);
        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Generated ticket: {}", savedTicket);
        return savedTicket;
    }

    @Transactional
    public Success removeVehicle(Long ticketId) {

        Ticket validTicket = ticketRepository.findById(ticketId).orElseThrow(() -> {
            log.info("Invalid ticket ID: {}", ticketId);
            return new NoSuchElementException("Invalid Tciket");
        });

        if (validTicket.getStatus() != TicketStatus.ACTIVE) {
            throw new InvalidParameterException("Invalid ticket status for exit");
        }


        log.info("Processing exit for ticket {} ", validTicket.getId());
        validTicket.setExitTime(LocalDateTime.now());
        long durationInMinutes = ChronoUnit.MINUTES.between(validTicket.getEntryTime(), validTicket.getExitTime());
        ParkingSpot parkingSpot = validTicket.getParkingSpot();
        Double parkingPrice = rateService.calculateRate(parkingSpot.getSpotType(), durationInMinutes);
        validTicket.setAmountPaid(parkingPrice);
        Ticket savedTicket = ticketRepository.save(validTicket);

        parkingSpot.setStatus(SpotStatus.AVAILABLE);
        parkingSpot.setVehicle(null);
        parkingSpotRepository.save(parkingSpot);
        return new Success(String.format("Removed Vehicle from level %s", validTicket.getLevelId()), HttpStatusCode.valueOf(200).toString());
    }
}
