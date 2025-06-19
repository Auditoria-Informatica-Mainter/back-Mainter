package com.example.BackendProject.controller;

import com.example.BackendProject.entity.StripePayment;
import com.example.BackendProject.repository.StripePaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments/management")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class PaymentManagementController {

    @Autowired
    private StripePaymentRepository stripePaymentRepository;

    /**
     * Obtener todos los pagos
     */
    @GetMapping("/all")
    public ResponseEntity<List<StripePayment>> getAllPayments() {
        List<StripePayment> payments = stripePaymentRepository.findAll();
        return ResponseEntity.ok(payments);
    }

    /**
     * Obtener pagos por orden
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<StripePayment>> getPaymentsByOrder(@PathVariable String orderId) {
        List<StripePayment> payments = stripePaymentRepository.findByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Obtener pagos por estado
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<StripePayment>> getPaymentsByStatus(@PathVariable String status) {
        List<StripePayment> payments = stripePaymentRepository.findByStatus(status);
        return ResponseEntity.ok(payments);
    }

    /**
     * Obtener pagos por email de cliente
     */
    @GetMapping("/customer/{email}")
    public ResponseEntity<List<StripePayment>> getPaymentsByCustomer(@PathVariable String email) {
        List<StripePayment> payments = stripePaymentRepository.findByCustomerEmail(email);
        return ResponseEntity.ok(payments);
    }

    /**
     * Obtener un pago específico por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        Optional<StripePayment> payment = stripePaymentRepository.findById(id);
        
        if (payment.isPresent()) {
            return ResponseEntity.ok(payment.get());
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Payment not found");
            error.put("success", false);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtener un pago por PaymentIntent ID
     */
    @GetMapping("/intent/{paymentIntentId}")
    public ResponseEntity<?> getPaymentByIntentId(@PathVariable String paymentIntentId) {
        Optional<StripePayment> payment = stripePaymentRepository.findByPaymentIntentId(paymentIntentId);
        
        if (payment.isPresent()) {
            return ResponseEntity.ok(payment.get());
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Payment not found");
            error.put("success", false);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtener estadísticas de pagos
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getPaymentStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total de pagos
        long totalPayments = stripePaymentRepository.count();
        stats.put("totalPayments", totalPayments);
        
        // Pagos exitosos
        List<StripePayment> successfulPayments = stripePaymentRepository.findByStatus("succeeded");
        stats.put("successfulPayments", successfulPayments.size());
        
        // Pagos pendientes
        List<StripePayment> pendingPayments = stripePaymentRepository.findByStatus("requires_payment_method");
        stats.put("pendingPayments", pendingPayments.size());
        
        // Pagos cancelados
        List<StripePayment> canceledPayments = stripePaymentRepository.findByStatus("canceled");
        stats.put("canceledPayments", canceledPayments.size());
        
        // Monto total de pagos exitosos
        double totalAmount = successfulPayments.stream()
                .mapToDouble(payment -> payment.getAmount().doubleValue())
                .sum();
        stats.put("totalSuccessfulAmount", totalAmount);
        
        stats.put("success", true);
        
        return ResponseEntity.ok(stats);
    }

    /**
     * Obtener pagos exitosos por orden
     */
    @GetMapping("/successful/order/{orderId}")
    public ResponseEntity<List<StripePayment>> getSuccessfulPaymentsByOrder(@PathVariable String orderId) {
        List<StripePayment> payments = stripePaymentRepository.findSuccessfulPaymentsByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }
}
