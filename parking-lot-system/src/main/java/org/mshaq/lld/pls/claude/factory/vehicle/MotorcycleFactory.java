package org.mshaq.lld.pls.claude.factory.vehicle;

import org.mshaq.lld.pls.claude.model.Motorcycle;
import org.mshaq.lld.pls.claude.model.Vehicle;

public class MotorcycleFactory extends VehicleFactory {
    @Override
    public Vehicle createVehicle(String licensePlate) {
        return new Motorcycle(licensePlate);
    }
}
