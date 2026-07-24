package com.mubeen.mubeenmart.service.impl;

import com.mubeen.mubeenmart.service.PaymentResult;
import com.mubeen.mubeenmart.service.PaymentStrategy;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mock UPI payment confirmation strategy.
 */
public class MockUpiPayment implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(Long userId, BigDecimal amount, String reference) {
        String txnId = "UPI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PaymentResult(true, txnId, "UPI", "Mock UPI payment of ₹" + amount + " confirmed successfully");
    }
}
