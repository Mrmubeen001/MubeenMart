package com.mubeen.mubeenmart.dto;

import java.math.BigDecimal;

/**
 * DTO for Seller Sales Dashboard (O3 requirement).
 */
public class SalesSummaryDTO {

    private final int totalOrders;
    private final int totalItemsSold;
    private final BigDecimal totalRevenue;
    private final int activeListings;

    public SalesSummaryDTO(int totalOrders, int totalItemsSold, BigDecimal totalRevenue, int activeListings) {
        this.totalOrders = totalOrders;
        this.totalItemsSold = totalItemsSold;
        this.totalRevenue = totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
        this.activeListings = activeListings;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public int getTotalItemsSold() {
        return totalItemsSold;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public int getActiveListings() {
        return activeListings;
    }
}
