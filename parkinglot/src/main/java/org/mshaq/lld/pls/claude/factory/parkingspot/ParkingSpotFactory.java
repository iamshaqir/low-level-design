package org.mshaq.lld.pls.claude.factory.parkingspot;

import org.mshaq.lld.pls.claude.ParkingSpot;
import org.mshaq.lld.pls.claude.payment.PaymentProcessor;

public interface ParkingSpotFactory {

    ParkingSpot createParkingSpot(String spotId, String levelId);

    PaymentProcessor createPaymentProcessor();

}
