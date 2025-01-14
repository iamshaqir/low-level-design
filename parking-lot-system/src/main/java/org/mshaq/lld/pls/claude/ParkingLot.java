package org.mshaq.lld.pls.claude;

import org.mshaq.lld.pls.claude.Enum.TicketStatus;
import org.mshaq.lld.pls.claude.model.Ticket;
import org.mshaq.lld.pls.claude.model.Vehicle;
import org.mshaq.lld.pls.claude.payment.PaymentProcessor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLot {

    private List<ParkingLevel> parkingLevels;
    private Map<String, Ticket> activeTickets;
    private ParkingRateCalculator rateCalculator;

    private ParkingLot() {
        this.parkingLevels = new ArrayList<>();
        this.activeTickets = new ConcurrentHashMap<>();
        this.rateCalculator = new ParkingRateCalculator();
    }

    public void addParkingLevel(ParkingLevel parkingLevel) {
        parkingLevels.add(parkingLevel);
    }

    public Optional<Ticket> parkVehicle(Vehicle vehicle) {
        for (ParkingLevel level : parkingLevels) {
            Optional<ParkingSpot> parkingSpotOptional = level.findParkingSpot(vehicle.getVehicleType());
            if (parkingSpotOptional.isPresent()) {
                ParkingSpot parkingSpot = parkingSpotOptional.get();
                if (parkingSpot.parkVehicle(vehicle)) {
                    Ticket ticket = new Ticket(vehicle, parkingSpot);
                    activeTickets.put(ticket.getTicketId(), ticket);
                    return Optional.of(ticket);
                }

            }
        }
        return Optional.empty();
    }

    public boolean exitParking(Ticket ticket, PaymentProcessor paymentProcessor) {

        if (ticket == null || ticket.getTicketStatus() != TicketStatus.ACTIVE) {
            return false;
        }
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
            return true;
        }

        return false;
    }

}
