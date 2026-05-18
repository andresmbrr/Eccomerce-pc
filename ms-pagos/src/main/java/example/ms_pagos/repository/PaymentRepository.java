package example.ms_pagos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import example.ms_pagos.model.Payment;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderId(Long orderId);
}