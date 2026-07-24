package com.mubeen.mubeenmart.model;

/**
 * Order fulfillment statuses conforming to workflow: Pending -> Confirmed -> Shipped -> Delivered / Cancelled.
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public static OrderStatus fromString(String statusStr) {
        if (statusStr == null) return PENDING;
        try {
            return OrderStatus.valueOf(statusStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }
}
