package com.example.BackendProject.dto;

public class ConfirmPaymentRequest {
    
    private String paymentIntentId;
    private String orderId;
    private String paymentMethodId;
    
    // Constructores
    public ConfirmPaymentRequest() {}
    
    public ConfirmPaymentRequest(String paymentIntentId, String orderId, String paymentMethodId) {
        this.paymentIntentId = paymentIntentId;
        this.orderId = orderId;
        this.paymentMethodId = paymentMethodId;
    }
    
    // Getters y Setters
    public String getPaymentIntentId() { return paymentIntentId; }
    public void setPaymentIntentId(String paymentIntentId) { this.paymentIntentId = paymentIntentId; }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public String getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }
    
    // Validación
    public boolean isValid() {
        return paymentIntentId != null && !paymentIntentId.trim().isEmpty();
    }
}
