package com.example.BackendProject.controller;

import com.example.BackendProject.dto.PaymentRequest;
import com.example.BackendProject.service.StripeService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
@Tag(name = "Stripe", description = "API para integración con Stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    /**
     * Crear una sesión de checkout de Stripe
     */
    @Operation(summary = "Crear sesión de checkout de Stripe")
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
     * Obtener la configuración pública de Stripe
     */
    @Operation(summary = "Obtener configuración pública de Stripe")
    @GetMapping("/config")
    public ResponseEntity<?> getStripeConfig() {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put("publishableKey", stripeService.getPublishableKey());
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Cannot retrieve Stripe configuration"));
        }
    }

    /**
     * Verificar el estado de un pago usando session ID de Stripe
     */
    @Operation(summary = "Verificar estado de pago por session ID")
    @GetMapping("/verify-payment/{sessionId}")
    public ResponseEntity<?> verifyPayment(@PathVariable String sessionId) {
        try {
            Map<String, Object> paymentStatus = stripeService.verifyPaymentBySession(sessionId);
            return ResponseEntity.ok(paymentStatus);
            
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(createErrorResponse("Stripe error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Error verifying payment: " + e.getMessage()));
        }
    }

    /**
     * Confirmar pago exitoso y actualizar pedido
     */
    @Operation(summary = "Confirmar pago y actualizar pedido")
    @PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, String> request) {
        try {
            String sessionId = request.get("sessionId");
            String orderId = request.get("orderId");
            
            if (sessionId == null || sessionId.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Session ID is required"));
            }
            
            Map<String, Object> confirmationResult = stripeService.validateAndUpdateOrder(sessionId, orderId);
            return ResponseEntity.ok(confirmationResult);
            
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(createErrorResponse("Stripe error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Error confirming payment: " + e.getMessage()));
        }
    }

    /**
     * Validar pago exitoso y actualizar estado del pedido
     */
    @Operation(summary = "Validar pago y actualizar pedido")
    @PostMapping("/validate-payment")
    public ResponseEntity<?> validatePayment(@RequestBody Map<String, String> request) {
        try {
            String sessionId = request.get("sessionId");
            String orderId = request.get("orderId");
            
            if (sessionId == null || sessionId.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Session ID is required"));
            }
            
            Map<String, Object> validationResult = stripeService.validateAndUpdateOrder(sessionId, orderId);
            return ResponseEntity.ok(validationResult);
            
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(createErrorResponse("Stripe error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Error validating payment: " + e.getMessage()));
        }
    }

    /**
     * Obtener estado completo de la sesión de Stripe
     */
    @Operation(summary = "Obtener estado de sesión de Stripe")
    @GetMapping("/session-status/{sessionId}")
    public ResponseEntity<?> getSessionStatus(@PathVariable String sessionId) {
        try {
            Map<String, Object> sessionStatus = stripeService.getSessionStatus(sessionId);
            return ResponseEntity.ok(sessionStatus);
            
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(createErrorResponse("Stripe error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Error getting session status: " + e.getMessage()));
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
