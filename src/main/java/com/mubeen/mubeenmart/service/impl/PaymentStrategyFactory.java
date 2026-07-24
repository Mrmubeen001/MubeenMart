package com.mubeen.mubeenmart.service.impl;

import com.mubeen.mubeenmart.service.PaymentStrategy;

/**
 * Factory for creating/retrieving payment strategy instances.
 */
public final class PaymentStrategyFactory {

    private static final PaymentStrategy UPI = new MockUpiPayment();
    private static final PaymentStrategy CARD = new MockCreditCardPayment();
    private static final PaymentStrategy NET_BANKING = new MockNetBankingPayment();

    private PaymentStrategyFactory() {
    }

    public static PaymentStrategy getStrategy(String method) {
        if (method == null) return UPI;
        String normalized = method.trim().toUpperCase();
        if (normalized.contains("CARD")) {
            return CARD;
        } else if (normalized.contains("BANK") || normalized.contains("NET")) {
            return NET_BANKING;
        } else {
            return UPI;
        }
    }
}
