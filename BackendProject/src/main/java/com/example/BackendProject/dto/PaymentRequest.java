package com.example.BackendProject.dto;

public class PaymentRequest {
    
    private Double amount;
    private String currency = "usd";
    private String description;
    private String orderId;
    private String customerEmail;
    private String customerName;
    private String returnUrl;
    private String cancelUrl;
    
    // Constructores
    public PaymentRequest() {}
    
    public PaymentRequest(Double amount, String currency, String description, String orderId, 
                         String customerEmail, String customerName, String returnUrl, String cancelUrl) {
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.returnUrl = returnUrl;
        this.cancelUrl = cancelUrl;
    }
    
    // Getters y Setters
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getReturnUrl() { return returnUrl; }
    public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }
    
    public String getCancelUrl() { return cancelUrl; }
    public void setCancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; }
    
    // Validaciones básicas
    public boolean isValid() {
        return amount != null && amount > 0 && 
               currency != null && !currency.trim().isEmpty() &&
               orderId != null && !orderId.trim().isEmpty();
    }
}
