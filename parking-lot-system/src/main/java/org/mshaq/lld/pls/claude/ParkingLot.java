package org.mshaq.lld.pls.claude;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mshaq.lld.pls.claude.Enum.TicketStatus;
import org.mshaq.lld.pls.claude.model.Ticket;
import org.mshaq.lld.pls.claude.model.Vehicle;
import org.mshaq.lld.pls.claude.payment.PaymentProcessor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLot {

    private static final Logger logger = LogManager.getLogger(ParkingLot.class);

    private static ParkingLot parkingLot;
    private List<ParkingLevel> parkingLevels;
    private Map<String, Ticket> activeTickets;
    private ParkingRateCalculator rateCalculator;

    private ParkingLot() {
        this.parkingLevels = new ArrayList<>();
        this.activeTickets = new ConcurrentHashMap<>();
        this.rateCalculator = new ParkingRateCalculator();
        logger.info("Created parking lot");
    }

    public static synchronized ParkingLot getInstance() {
        if (parkingLot == null) {
            parkingLot = new ParkingLot();
        }
        return parkingLot;

    }

    public void addParkingLevel(ParkingLevel parkingLevel) {
        logger.info("Adding {} to parking lot", parkingLevel.getLevelId());
        parkingLevels.add(parkingLevel);
    }

    public Optional<Ticket> parkVehicle(Vehicle vehicle) {
        String licensePlate = vehicle.getLicensePlate();
        logger.info("Attempting to park vehicle {} of type {}", licensePlate, vehicle.getVehicleType());
        for (ParkingLevel level : parkingLevels) {
            Optional<ParkingSpot> parkingSpotOptional = level.findParkingSpot(vehicle.getVehicleType());
            if (parkingSpotOptional.isEmpty()) {
                logger.warn("No available parking spot found for vehicle {}", licensePlate);
                return Optional.empty();
            }
            ParkingSpot parkingSpot = parkingSpotOptional.get();
            if (parkingSpot.parkVehicle(vehicle)) {
                Ticket ticket = new Ticket(vehicle, parkingSpot);
                activeTickets.put(ticket.getTicketId(), ticket);
                logger.info("Ticket {} issued for vehicle {}", ticket.getTicketId(), ticket.getVehicle().getLicensePlate());
                return Optional.of(ticket);
            }
        }
        return Optional.empty();
    }

    public boolean exitParking(Ticket ticket, PaymentProcessor paymentProcessor) {

        if (ticket == null || ticket.getTicketStatus() != TicketStatus.ACTIVE) {
            logger.warn("Invalid ticket or ticket status for exit");
            return false;
        }
        logger.info("Processing exit for ticket {} ", ticket.getTicketId());
        ticket.setExitTime(LocalDateTime.now());
        long durationInMinutes = ChronoUnit.MINUTES.between(
                ticket.getEntryTime(),
                ticket.getExitTime()
        );

        double parkingPrice = rateCalculator.calculateRate(ticket.getVehicle().getVehicleType(),
                durationInMinutes);

        if (paymentProcessor.processPayment(parkingPrice)) {
            ticket.setAmountPaid(parkingPrice);
            ticket.setTicketStatus(TicketStatus.PAID);
            ticket.getParkingSpot().remove();
            logger.info("Vehicle {} successfully exited", ticket.getVehicle().getLicensePlate());
            return true;
        }

        return false;
    }

}
