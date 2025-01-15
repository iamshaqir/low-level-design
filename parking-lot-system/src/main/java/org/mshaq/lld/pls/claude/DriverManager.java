package org.mshaq.lld.pls.claude;

import org.mshaq.lld.pls.claude.Enum.ParkingSpotType;
import org.mshaq.lld.pls.claude.Enum.VehicleType;
import org.mshaq.lld.pls.claude.model.Car;
import org.mshaq.lld.pls.claude.model.MotorCycle;
import org.mshaq.lld.pls.claude.model.Ticket;
import org.mshaq.lld.pls.claude.model.Vehicle;
import org.mshaq.lld.pls.claude.payment.CashPaymentProcessor;
import org.mshaq.lld.pls.claude.payment.PaymentProcessor;

import java.util.List;
import java.util.Optional;

public class DriverManager {

    public static void main(String[] args) {

        ParkingLot parkingLot = ParkingLot.getInstance();
        List<ParkingSpot> level1Spots = List.of(new ParkingSpot("Level-1", "SPOT-1", ParkingSpotType.MOTORCYCLE),
                new ParkingSpot("Level-1", "SPOT-2", ParkingSpotType.COMPACT),
                new ParkingSpot("Level-1", "SPOT-3", ParkingSpotType.LARGE));

        List<ParkingSpot> level2Spots = List.of(new ParkingSpot("Level-2", "SPOT-1", ParkingSpotType.MOTORCYCLE),
                new ParkingSpot("Level-2", "SPOT-2", ParkingSpotType.COMPACT),
                new ParkingSpot("Level-2", "SPOT-3", ParkingSpotType.LARGE));

        parkingLot.addParkingLevel(new ParkingLevel("LEVEL-1", level1Spots));
        parkingLot.addParkingLevel(new ParkingLevel("LEVEL-2", level2Spots));

        Vehicle car = new Car("TS08ZV7548");
        Vehicle motorCycle = new MotorCycle("TS09FT7576");

        Optional<Ticket> carTicket = parkingLot.parkVehicle(car);
        Optional<Ticket> motorCycleTicket = parkingLot.parkVehicle(motorCycle);


        PaymentProcessor cashPayment = new CashPaymentProcessor();
        boolean status = parkingLot.exitParking(carTicket.orElseThrow(RuntimeException::new), cashPayment);
        boolean exitParking = parkingLot.exitParking(motorCycleTicket.orElseThrow(RuntimeException::new), cashPayment);
    }
}
