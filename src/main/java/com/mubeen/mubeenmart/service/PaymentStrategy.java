package com.mubeen.mubeenmart.service;

import java.math.BigDecimal;

/**
 * Strategy interface for mock payment confirmation channels.
 * Fulfills Section 12 design pattern requirement:
 * "Strategy (swappable notification/payment-mock channels)".
 */
public interface PaymentStrategy {

    /**
     * Executes mock payment confirmation for an order total.
     *
     * @param userId buyer user ID
     * @param amount order amount
     * @param reference payment reference / method details
     * @return payment processing result
     */
    PaymentResult processPayment(Long userId, BigDecimal amount, String reference);
}
