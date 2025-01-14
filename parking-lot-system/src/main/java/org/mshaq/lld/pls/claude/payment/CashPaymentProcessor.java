package org.mshaq.lld.pls.claude.payment;

public class CashPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        return false;
    }
}
