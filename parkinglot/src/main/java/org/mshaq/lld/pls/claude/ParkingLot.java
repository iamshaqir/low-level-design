package org.mshaq.lld.pls.claude;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mshaq.lld.pls.claude.Enum.ParkingSpotType;
import org.mshaq.lld.pls.claude.Enum.TicketStatus;
import org.mshaq.lld.pls.claude.Enum.VehicleType;
import org.mshaq.lld.pls.claude.factory.parkingspot.CompactParkingFactory;
import org.mshaq.lld.pls.claude.factory.parkingspot.LargeParkingFactory;
import org.mshaq.lld.pls.claude.factory.parkingspot.MotorcycleParkingFactory;
import org.mshaq.lld.pls.claude.factory.parkingspot.ParkingSpotFactory;
import org.mshaq.lld.pls.claude.factory.vehicle.VehicleFactory;
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

    private Map<VehicleType, VehicleFactory> factories;

    private ParkingLot() {
        this.parkingLevels = new ArrayList<>();
        this.activeTickets = new ConcurrentHashMap<>();
        this.rateCalculator = new ParkingRateCalculator();
        initializeVehicleFactories();
        logger.info("Created parking lot");
    }

    public static synchronized ParkingLot getInstance() {
        if (parkingLot == null) {
            parkingLot = new ParkingLot();
        }
        return parkingLot;

    }

    private void initializeVehicleFactories() {
        for (VehicleType value : VehicleType.values()) {
            factories.put(value, VehicleFactory.getFactory(value));
        }
    }

    public void addParkingLevel(ParkingLevel parkingLevel) {
        logger.info("Adding {} to parking lot", parkingLevel.getLevelId());
        parkingLevels.add(parkingLevel);
    }

    private Vehicle createVehicle(VehicleType type, String licensePlate) {
        VehicleFactory factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid vehicle type: " + type);
        }
        return factory.createVehicle(licensePlate);
    }

    public Optional<Ticket> parkVehicle(VehicleType type, String license) {
        Vehicle vehicle = createVehicle(type, license);
        return parkVehicle(vehicle);
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

    public boolean exitParking(Ticket ticket, ParkingSpotType spotType) {

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

        ParkingSpotFactory factory = getParkingSystemFactory(spotType);
        PaymentProcessor paymentProcessor = factory.createPaymentProcessor();
        if (paymentProcessor.processPayment(parkingPrice)) {
            ticket.setAmountPaid(parkingPrice);
            ticket.setTicketStatus(TicketStatus.PAID);
            ticket.getParkingSpot().remove();
            logger.info("Vehicle {} successfully exited", ticket.getVehicle().getLicensePlate());
            return true;
        }
        return false;
    }

    private ParkingSpotFactory getParkingSystemFactory(ParkingSpotType spotType) {
        return switch (spotType) {
            case COMPACT -> new CompactParkingFactory();
            case LARGE -> new LargeParkingFactory();
            case MOTORCYCLE -> new MotorcycleParkingFactory();
            default -> throw new IllegalArgumentException("Invalid spot type: " + spotType);
        };
    }

}
