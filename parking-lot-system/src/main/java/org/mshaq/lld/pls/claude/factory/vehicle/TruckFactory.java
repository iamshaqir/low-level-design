package org.mshaq.lld.pls.claude.factory.vehicle;

import org.mshaq.lld.pls.claude.model.Truck;
import org.mshaq.lld.pls.claude.model.Vehicle;

public class TruckFactory extends VehicleFactory {
    @Override
    public  Vehicle createVehicle(String licensePlate) {
        return new Truck(licensePlate);
    }
}
