package com.example.BackendProject.service;

import com.example.BackendProject.dto.PaymentRequest;
import com.example.BackendProject.dto.PaymentResponse;
import com.example.BackendProject.dto.ConfirmPaymentRequest;
import com.example.BackendProject.entity.StripePayment;
import com.example.BackendProject.repository.StripePaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Customer;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.CustomerCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;    @Value("${stripe.publishable.key}")
    private String stripePublishableKey;

    @Value("${frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Autowired
    private StripePaymentRepository stripePaymentRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    /**
     * Crea un PaymentIntent en Stripe
     */
    public PaymentResponse createPaymentIntent(PaymentRequest request) throws StripeException {
        // Validar la solicitud
        if (!request.isValid()) {
            throw new IllegalArgumentException("Invalid payment request");
        }

        // Crear o recuperar customer si se proporciona email
        String customerId = null;
        if (StringUtils.hasText(request.getCustomerEmail())) {
            customerId = createOrGetCustomer(request.getCustomerEmail(), request.getCustomerName());
        }

        // Construir parámetros para el PaymentIntent
        PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
                .setAmount((long) (request.getAmount() * 100)) // Stripe usa centavos
                .setCurrency(request.getCurrency().toLowerCase())
                .setDescription(request.getDescription())
                .putMetadata("orderId", request.getOrderId());

        // Agregar customer si existe
        if (customerId != null) {
            paramsBuilder.setCustomer(customerId);
        }

        // Configurar métodos de pago permitidos
        paramsBuilder.addPaymentMethodType("card");

        // Configurar URLs de retorno si se proporcionan
        if (StringUtils.hasText(request.getReturnUrl())) {
            paramsBuilder.setReceiptEmail(request.getCustomerEmail());
        }

        PaymentIntentCreateParams params = paramsBuilder.build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Guardar en la base de datos
        savePaymentToDatabase(paymentIntent, request);

        // Construir respuesta
        PaymentResponse response = new PaymentResponse();
        response.setClientSecret(paymentIntent.getClientSecret());
        response.setPaymentIntentId(paymentIntent.getId());
        response.setStatus(paymentIntent.getStatus());
        response.setAmount(paymentIntent.getAmount() / 100.0);
        response.setCurrency(paymentIntent.getCurrency());
        response.setOrderId(request.getOrderId());
        response.setPublishableKey(stripePublishableKey);
        response.setSuccess(true);
        response.setMessage("Payment intent created successfully");

        return response;
    }

    /**
     * Confirma un PaymentIntent
     */
    public PaymentResponse confirmPayment(ConfirmPaymentRequest request) throws StripeException {
        if (!request.isValid()) {
            throw new IllegalArgumentException("Invalid confirmation request");
        }

        PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getPaymentIntentId());

        // Si se proporciona un método de pago, configurarlo
        if (StringUtils.hasText(request.getPaymentMethodId())) {
            PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder()
                    .setPaymentMethod(request.getPaymentMethodId())
                    .build();
            paymentIntent = paymentIntent.confirm(confirmParams);
        }

        // Actualizar en la base de datos
        updatePaymentInDatabase(paymentIntent);

        PaymentResponse response = new PaymentResponse();
        response.setPaymentIntentId(paymentIntent.getId());
        response.setStatus(paymentIntent.getStatus());
        response.setAmount(paymentIntent.getAmount() / 100.0);
        response.setCurrency(paymentIntent.getCurrency());
        response.setOrderId(request.getOrderId());
        response.setSuccess("succeeded".equals(paymentIntent.getStatus()));
        response.setMessage(getStatusMessage(paymentIntent.getStatus()));

        return response;
    }

    /**
     * Obtiene el estado de un PaymentIntent
     */
    public PaymentResponse getPaymentStatus(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

        // Actualizar en la base de datos por si hay cambios
        updatePaymentInDatabase(paymentIntent);

        PaymentResponse response = new PaymentResponse();
        response.setPaymentIntentId(paymentIntent.getId());
        response.setStatus(paymentIntent.getStatus());
        response.setAmount(paymentIntent.getAmount() / 100.0);
        response.setCurrency(paymentIntent.getCurrency());
        response.setSuccess("succeeded".equals(paymentIntent.getStatus()));
        response.setMessage(getStatusMessage(paymentIntent.getStatus()));

        // Obtener orderId de los metadatos
        if (paymentIntent.getMetadata() != null) {
            response.setOrderId(paymentIntent.getMetadata().get("orderId"));
        }

        return response;
    }

    /**
     * Cancela un PaymentIntent
     */
    public PaymentResponse cancelPayment(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        PaymentIntent canceledPaymentIntent = paymentIntent.cancel();

        // Actualizar en la base de datos
        updatePaymentInDatabase(canceledPaymentIntent);

        PaymentResponse response = new PaymentResponse();
        response.setPaymentIntentId(canceledPaymentIntent.getId());
        response.setStatus(canceledPaymentIntent.getStatus());
        response.setAmount(canceledPaymentIntent.getAmount() / 100.0);
        response.setCurrency(canceledPaymentIntent.getCurrency());
        response.setSuccess("canceled".equals(canceledPaymentIntent.getStatus()));
        response.setMessage("Payment canceled successfully");

        return response;
    }

    /**
     * Guarda un pago en la base de datos
     */
    private void savePaymentToDatabase(PaymentIntent paymentIntent, PaymentRequest request) {
        try {
            if (!stripePaymentRepository.existsByPaymentIntentId(paymentIntent.getId())) {
                StripePayment stripePayment = new StripePayment(
                    paymentIntent.getId(),
                    request.getOrderId(),
                    BigDecimal.valueOf(paymentIntent.getAmount() / 100.0),
                    paymentIntent.getCurrency(),
                    paymentIntent.getStatus(),
                    request.getCustomerEmail(),
                    request.getCustomerName(),
                    request.getDescription()
                );
                stripePaymentRepository.save(stripePayment);
            }
        } catch (Exception e) {
            // Log error but don't fail the payment process
            System.err.println("Error saving payment to database: " + e.getMessage());
        }
    }

    /**
     * Actualiza un pago en la base de datos
     */
    private void updatePaymentInDatabase(PaymentIntent paymentIntent) {
        try {
            stripePaymentRepository.findByPaymentIntentId(paymentIntent.getId())
                .ifPresent(payment -> {
                    payment.setStatus(paymentIntent.getStatus());
                    payment.setAmount(BigDecimal.valueOf(paymentIntent.getAmount() / 100.0));
                    stripePaymentRepository.save(payment);
                });
        } catch (Exception e) {
            // Log error but don't fail the payment process
            System.err.println("Error updating payment in database: " + e.getMessage());
        }
    }

    /**
     * Crea o recupera un customer en Stripe
     */
    private String createOrGetCustomer(String email, String name) throws StripeException {
        CustomerCreateParams.Builder paramsBuilder = CustomerCreateParams.builder()
                .setEmail(email);

        if (StringUtils.hasText(name)) {
            paramsBuilder.setName(name);
        }

        Customer customer = Customer.create(paramsBuilder.build());
        return customer.getId();
    }

    /**
     * Obtiene un mensaje descriptivo basado en el estado del pago
     */
    private String getStatusMessage(String status) {
        switch (status) {
            case "requires_payment_method":
                return "Payment method required";
            case "requires_confirmation":
                return "Payment requires confirmation";
            case "requires_action":
                return "Payment requires additional action";
            case "processing":
                return "Payment is being processed";
            case "requires_capture":
                return "Payment requires capture";
            case "canceled":
                return "Payment was canceled";
            case "succeeded":
                return "Payment completed successfully";
            default:
                return "Payment status: " + status;
        }
    }

    /**
     * Obtiene la clave pública de Stripe
     */
    public String getPublishableKey() {
        return stripePublishableKey;
    }

    /**
     * Crea una sesión de checkout de Stripe
     */
    public Map<String, Object> createCheckoutSession(PaymentRequest request) throws StripeException {
        // Validar la solicitud
        if (!request.isValid()) {
            throw new IllegalArgumentException("Invalid payment request");
        }

        // Crear los parámetros para la sesión de checkout
        Map<String, Object> params = new HashMap<>();
        
        // Configurar modo de pago
        params.put("mode", "payment");
        
        // Configurar elementos de línea (line items)
        List<Map<String, Object>> lineItems = new ArrayList<>();
        Map<String, Object> lineItem = new HashMap<>();
        
        // Configurar datos del precio
        Map<String, Object> priceData = new HashMap<>();
        priceData.put("currency", request.getCurrency().toLowerCase());
        priceData.put("unit_amount", (long) (request.getAmount() * 100)); // Stripe usa centavos
        
        // Configurar datos del producto
        Map<String, Object> productData = new HashMap<>();
        productData.put("name", request.getDescription() != null ? request.getDescription() : "Producto");
        priceData.put("product_data", productData);
        
        lineItem.put("price_data", priceData);
        lineItem.put("quantity", 1);
        lineItems.add(lineItem);        params.put("line_items", lineItems);
        
        // ✅ SOLUCIÓN: URLs dinámicas que funcionan en cualquier puerto/dominio
        // Usando la URL base del request del frontend
        String frontendUrl = request.getReturnUrl() != null ? request.getReturnUrl() : "http://localhost:4200";
        
        // URLs que redirigen de vuelta al frontend con parámetros
        String successUrl = frontendUrl + "/pedidos?payment=success&session_id={CHECKOUT_SESSION_ID}&order_id=" + request.getOrderId();
        String cancelUrl = frontendUrl + "/pedidos?payment=cancelled&order_id=" + request.getOrderId();
        
        params.put("success_url", successUrl);
        params.put("cancel_url", cancelUrl);
        
        // Configurar metadata para tracking
        Map<String, String> metadata = new HashMap<>();
        metadata.put("orderId", request.getOrderId());
        if (request.getCustomerEmail() != null) {
            metadata.put("customerEmail", request.getCustomerEmail());
        }
        params.put("metadata", metadata);
        
        System.out.println("=== STRIPE CHECKOUT SESSION (URLs DINÁMICAS) ===");
        System.out.println("Order ID: " + request.getOrderId());
        System.out.println("Amount: " + (request.getAmount() * 100) + " centavos");
        System.out.println("Success URL: " + successUrl);
        System.out.println("Cancel URL: " + cancelUrl);
        System.out.println("Frontend URL detectada: " + frontendUrl);
        
        // Configurar email del cliente si se proporciona
        if (StringUtils.hasText(request.getCustomerEmail())) {
            params.put("customer_email", request.getCustomerEmail());
        }
        
        // Crear la sesión de checkout
        com.stripe.model.checkout.Session session = com.stripe.model.checkout.Session.create(params);
        
        // Preparar respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put("url", session.getUrl());
        response.put("publishableKey", stripePublishableKey);
        response.put("success", true);
        response.put("message", "Checkout session created successfully");
        
        return response;
    }
    
    // ============================================
    // MÉTODOS DE VALIDACIÓN PARA EL FRONTEND
    // ============================================
    
    @Autowired
    private PedidoService pedidoService;
    
    /**
     * Verifica el estado de un pago usando session ID de Stripe
     * Este método es para que el frontend pueda verificar si un pago fue exitoso
     */
    public Map<String, Object> verifyPaymentBySession(String sessionId) throws StripeException {
        System.out.println("🔍 Verificando estado de pago para session: " + sessionId);
        
        // Obtener la sesión de Stripe
        com.stripe.model.checkout.Session session = com.stripe.model.checkout.Session.retrieve(sessionId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("session_id", sessionId);
        response.put("payment_status", session.getPaymentStatus());
        response.put("amount_total", session.getAmountTotal());
        response.put("currency", session.getCurrency());
        response.put("is_paid", "paid".equals(session.getPaymentStatus()));
        
        // Obtener metadata del pedido
        if (session.getMetadata() != null) {
            response.put("order_id", session.getMetadata().get("orderId"));
            response.put("customer_email", session.getMetadata().get("customerEmail"));
        }
        
        System.out.println("📊 Payment Status: " + session.getPaymentStatus());
        System.out.println("💰 Amount: " + session.getAmountTotal() + " centavos");
        System.out.println("🆔 Order ID: " + session.getMetadata().get("orderId"));
        
        return response;
    }
    
    /**
     * Valida un pago exitoso y actualiza el estado del pedido
     * Este es el método principal que el frontend debe llamar después de un pago exitoso
     */
    public Map<String, Object> validateAndUpdateOrder(String sessionId, String orderId) throws StripeException {
        System.out.println("✅ Validando pago y actualizando pedido");
        System.out.println("🎫 Session ID: " + sessionId);
        System.out.println("📋 Order ID: " + orderId);
        
        // Verificar el pago con Stripe
        com.stripe.model.checkout.Session session = com.stripe.model.checkout.Session.retrieve(sessionId);
        
        if (!"paid".equals(session.getPaymentStatus())) {
            throw new IllegalStateException("El pago no está confirmado en Stripe. Estado: " + session.getPaymentStatus());
        }
        
        // Obtener pedido ID de metadata si no se proporcionó
        String actualOrderId = orderId;
        if (actualOrderId == null && session.getMetadata() != null) {
            actualOrderId = session.getMetadata().get("orderId");
        }
        
        if (actualOrderId == null) {
            throw new IllegalArgumentException("No se pudo obtener el ID del pedido");
        }
        
        try {
            // Actualizar el estado del pedido a pagado (true)
            System.out.println("🔄 Actualizando pedido #" + actualOrderId + " a estado PAGADO");
            pedidoService.cambiarEstadoPedido(Long.parseLong(actualOrderId), true);
            System.out.println("✅ Pedido actualizado exitosamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error actualizando pedido: " + e.getMessage());
            throw new RuntimeException("Error actualizando estado del pedido", e);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Pago validado y pedido actualizado exitosamente");
        response.put("order_id", actualOrderId);
        response.put("payment_amount", session.getAmountTotal() / 100.0); // Convertir de centavos
        response.put("session_id", sessionId);
        response.put("payment_status", session.getPaymentStatus());
        response.put("currency", session.getCurrency());
        response.put("updated_at", System.currentTimeMillis());
        
        return response;
    }
    
    /**
     * Obtiene el estado completo de una sesión de Stripe
     * Útil para debugging y obtener información detallada
     */
    public Map<String, Object> getSessionStatus(String sessionId) throws StripeException {
        System.out.println("📋 Obteniendo estado completo de sesión: " + sessionId);
        
        com.stripe.model.checkout.Session session = com.stripe.model.checkout.Session.retrieve(sessionId);
        
        Map<String, Object> response = new HashMap<>();
        
        // Información básica de la sesión
        response.put("success", true);
        response.put("session_id", sessionId);
        response.put("payment_status", session.getPaymentStatus());
        response.put("status", session.getStatus());
        response.put("amount_total", session.getAmountTotal());
        response.put("amount_subtotal", session.getAmountSubtotal());
        response.put("currency", session.getCurrency());
        response.put("customer_email", session.getCustomerEmail());
        response.put("mode", session.getMode());
        response.put("url", session.getUrl());
        
        // URLs de redirección
        response.put("success_url", session.getSuccessUrl());
        response.put("cancel_url", session.getCancelUrl());
        
        // Metadata
        if (session.getMetadata() != null) {
            response.put("metadata", session.getMetadata());
            response.put("order_id", session.getMetadata().get("orderId"));
        }
        
        // Información del cliente
        if (session.getCustomerDetails() != null) {
            Map<String, Object> customerDetails = new HashMap<>();
            customerDetails.put("email", session.getCustomerDetails().getEmail());
            customerDetails.put("name", session.getCustomerDetails().getName());
            response.put("customer_details", customerDetails);
        }
        
        // Estados útiles para el frontend
        response.put("is_paid", "paid".equals(session.getPaymentStatus()));
        response.put("is_complete", "complete".equals(session.getStatus()));
        response.put("expires_at", session.getExpiresAt());
        response.put("created", session.getCreated());
        
        return response;
    }}
