package com.example.BackendProject.dto;

public class PaymentResponse {
    
    private String clientSecret;
    private String paymentIntentId;
    private String status;
    private Double amount;
    private String currency;
    private String orderId;
    private String publishableKey;
    private String message;
    private Boolean success;
    
    // Constructores
    public PaymentResponse() {}
    
    public PaymentResponse(String clientSecret, String paymentIntentId, String status, Double amount, 
                          String currency, String orderId, String publishableKey, String message, Boolean success) {
        this.clientSecret = clientSecret;
        this.paymentIntentId = paymentIntentId;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
        this.orderId = orderId;
        this.publishableKey = publishableKey;
        this.message = message;
        this.success = success;
    }
    
    // Getters y Setters
    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
    
    public String getPaymentIntentId() { return paymentIntentId; }
    public void setPaymentIntentId(String paymentIntentId) { this.paymentIntentId = paymentIntentId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public String getPublishableKey() { return publishableKey; }
    public void setPublishableKey(String publishableKey) { this.publishableKey = publishableKey; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
}
