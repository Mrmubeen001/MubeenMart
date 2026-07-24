package com.mubeen.mubeenmart.dto;

public class OrderRequestDTO {
    private String shippingAddress;
    private String paymentMethod; // MOCK_UPI, MOCK_CARD, MOCK_NET_BANKING

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(String shippingAddress, String paymentMethod) {
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
