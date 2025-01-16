package org.mshaq.lld.pls.claude.factory.vehicle;

import org.mshaq.lld.pls.claude.model.Car;
import org.mshaq.lld.pls.claude.model.Vehicle;

public class CarFactory extends VehicleFactory {
    @Override
    public  Vehicle createVehicle(String licensePlate) {
        return new Car(licensePlate);
    }
}
