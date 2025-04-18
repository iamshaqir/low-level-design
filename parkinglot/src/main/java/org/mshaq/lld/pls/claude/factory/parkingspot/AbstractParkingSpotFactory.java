package org.mshaq.lld.pls.claude.factory.parkingspot;

import org.mshaq.lld.pls.claude.payment.CashPaymentProcessor;
import org.mshaq.lld.pls.claude.payment.PaymentProcessor;

public abstract class AbstractParkingSpotFactory implements ParkingSpotFactory {
    @Override
    public PaymentProcessor createPaymentProcessor() {
        return new CashPaymentProcessor();
    }
}
