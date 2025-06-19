package com.example.BackendProject.repository;

import com.example.BackendProject.entity.StripePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StripePaymentRepository extends JpaRepository<StripePayment, Long> {
    
    Optional<StripePayment> findByPaymentIntentId(String paymentIntentId);
    
    List<StripePayment> findByOrderId(String orderId);
    
    List<StripePayment> findByStatus(String status);
    
    List<StripePayment> findByCustomerEmail(String customerEmail);
    
    @Query("SELECT sp FROM StripePayment sp WHERE sp.status = :status AND sp.customerEmail = :email")
    List<StripePayment> findByStatusAndCustomerEmail(@Param("status") String status, @Param("email") String email);
    
    @Query("SELECT sp FROM StripePayment sp WHERE sp.orderId = :orderId AND sp.status IN ('succeeded', 'processing')")
    List<StripePayment> findSuccessfulPaymentsByOrderId(@Param("orderId") String orderId);
    
    boolean existsByPaymentIntentId(String paymentIntentId);
}
