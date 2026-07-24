package com.mubeen.mubeenmart.service.impl;

import com.mubeen.mubeenmart.service.PaymentResult;
import com.mubeen.mubeenmart.service.PaymentStrategy;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mock Credit/Debit card payment confirmation strategy.
 */
public class MockCreditCardPayment implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(Long userId, BigDecimal amount, String reference) {
        String txnId = "CARD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PaymentResult(true, txnId, "CARD", "Mock Card payment of ₹" + amount + " authorized successfully");
    }
}
