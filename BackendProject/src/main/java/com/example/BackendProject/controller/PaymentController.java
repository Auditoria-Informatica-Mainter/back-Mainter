package com.example.BackendProject.controller;

import com.example.BackendProject.dto.PaymentRequest;
import com.example.BackendProject.dto.PaymentResponse;
import com.example.BackendProject.dto.ConfirmPaymentRequest;
import com.example.BackendProject.service.StripeService;
import com.stripe.exception.StripeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    /**
     * Crear un PaymentIntent
     */
    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest request) {
        try {
            // Validar la solicitud
            if (!request.isValid()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Invalid payment request. Please check amount, currency and orderId."));
            }

            PaymentResponse response = stripeService.createPaymentIntent(request);
            return ResponseEntity.ok(response);
            
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(createErrorResponse("Stripe error: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("An unexpected error occurred"));
        }
    }

    /**
     * Confirmar un pago
     */
    @PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestBody ConfirmPaymentRequest request) {
        try {
            if (!request.isValid()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Invalid confirmation request. PaymentIntentId is required."));
            }

            PaymentResponse response = stripeService.confirmPayment(request);
            return ResponseEntity.ok(response);
            
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(createErrorResponse("Stripe error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("An unexpected error occurred"));
        }
    }

    /**
     * Obtener el estado de un pago
     */
    @GetMapping("/status/{paymentIntentId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String paymentIntentId) {
        try {
            if (paymentIntentId == null || paymentIntentId.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("PaymentIntentId is required"));
            }

            PaymentResponse response = stripeService.getPaymentStatus(paymentIntentId);
            return ResponseEntity.ok(response);
            
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse("Payment not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("An unexpected error occurred"));
        }
    }

    /**
     * Cancelar un pago
     */
    @PostMapping("/cancel/{paymentIntentId}")
    public ResponseEntity<?> cancelPayment(@PathVariable String paymentIntentId) {
        try {
            if (paymentIntentId == null || paymentIntentId.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("PaymentIntentId is required"));
            }

            PaymentResponse response = stripeService.cancelPayment(paymentIntentId);
            return ResponseEntity.ok(response);
            
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse("Cannot cancel payment: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("An unexpected error occurred"));
        }
    }

    /**
     * Obtener la clave pública de Stripe
     */
    @GetMapping("/config")
    public ResponseEntity<?> getStripeConfig() {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put("publishableKey", stripeService.getPublishableKey());
            config.put("success", true);
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Cannot retrieve Stripe configuration"));
        }
    }

    /**
     * Webhook para recibir eventos de Stripe
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        
        // TODO: Implementar validación de webhook y procesamiento de eventos
        // Por ahora, solo confirmamos que recibimos el webhook
        
        try {
            // Aquí deberías validar la firma del webhook
            // y procesar los diferentes tipos de eventos de Stripe
            
            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid webhook");
        }
    }

    /**
     * Crear una sesión de checkout de Stripe
     */
    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody PaymentRequest request) {
        try {
            // Validar la solicitud
            if (!request.isValid()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Invalid payment request. Please check amount, currency and orderId."));
            }

            // Crear la sesión de checkout usando el servicio de Stripe
            Map<String, Object> sessionData = stripeService.createCheckoutSession(request);
            return ResponseEntity.ok(sessionData);
            
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(createErrorResponse("Stripe error: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("An unexpected error occurred while creating checkout session"));
        }
    }

    /**
     * Método auxiliar para crear respuestas de error consistentes
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        error.put("success", false);
        error.put("timestamp", System.currentTimeMillis());
        return error;
    }
}