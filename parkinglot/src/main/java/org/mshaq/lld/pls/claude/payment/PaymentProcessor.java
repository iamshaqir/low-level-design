package org.mshaq.lld.pls.claude.payment;

public interface PaymentProcessor {
    boolean processPayment(double amount);
}
