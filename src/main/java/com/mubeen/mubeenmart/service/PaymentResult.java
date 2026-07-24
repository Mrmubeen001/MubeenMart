package com.mubeen.mubeenmart.service;

import java.time.LocalDateTime;

public class PaymentResult {

    private final boolean successful;
    private final String transactionId;
    private final String paymentMethod;
    private final String message;
    private final LocalDateTime timestamp;

    public PaymentResult(boolean successful, String transactionId, String paymentMethod, String message) {
        this.successful = successful;
        this.transactionId = transactionId;
        this.paymentMethod = paymentMethod;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
