package com.mubeen.mubeenmart.service.impl;

import com.mubeen.mubeenmart.service.PaymentResult;
import com.mubeen.mubeenmart.service.PaymentStrategy;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mock Net Banking payment confirmation strategy.
 */
public class MockNetBankingPayment implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(Long userId, BigDecimal amount, String reference) {
        String txnId = "NB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PaymentResult(true, txnId, "NET_BANKING", "Mock Net Banking payment of ₹" + amount + " settled successfully");
    }
}
