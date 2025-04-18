package org.mshaq.lld.pls.claude;

import org.mshaq.lld.pls.claude.Enum.ParkingSpotType;
import org.mshaq.lld.pls.claude.Enum.VehicleType;
import org.mshaq.lld.pls.claude.model.Car;
import org.mshaq.lld.pls.claude.model.Motorcycle;
import org.mshaq.lld.pls.claude.model.Ticket;
import org.mshaq.lld.pls.claude.model.Vehicle;
import org.mshaq.lld.pls.claude.payment.CashPaymentProcessor;
import org.mshaq.lld.pls.claude.payment.PaymentProcessor;

import java.util.List;
import java.util.Optional;

public class DriverManager {

    public static void main(String[] args) {

        // Get parking lot instance
        ParkingLot parkingLot = ParkingLot.getInstance();

        // Create a new parking level
        ParkingLevel level = new ParkingLevel("L1");

        // Add parking spots using factories
        level.addParkingSpot(ParkingSpotType.COMPACT, "A1");
        level.addParkingSpot(ParkingSpotType.MOTORCYCLE, "A2");
        level.addParkingSpot(ParkingSpotType.LARGE, "A3");

        parkingLot.addParkingLevel(level);

        // Park a vehicle using factory
        Optional<Ticket> ticketOptional = parkingLot.parkVehicle(VehicleType.CAR, "ABC123");

        // Process exit using factory-created payment processor
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            boolean success = parkingLot.exitParking(ticket, ParkingSpotType.COMPACT);
            if (success) {
                System.out.println("Vehicle successfully exited");
            }
        }
    }
}
