package example.ms_pagos.controller;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.ms_pagos.dto.PaymentRequestDTO;
import example.ms_pagos.dto.PaymentResponseDTO;
import example.ms_pagos.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO>
    createPayment(
            @Valid @RequestBody PaymentRequestDTO dto){

        log.info("POST /api/payments ejecutado");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createPayment(dto));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>>
    getAllPayments(){

        log.info("GET /api/payments ejecutado");

        return ResponseEntity.ok(
                service.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO>
    getPaymentById(@PathVariable Long id){

        log.info("GET /api/payments/{} ejecutado", id);

        return ResponseEntity.ok(
                service.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponseDTO>>
    getPaymentsByOrderId(
            @PathVariable Long orderId){

        log.info("GET pagos pedido {}",
                orderId);

        return ResponseEntity.ok(
                service.getPaymentsByOrderId(orderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO>
    updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequestDTO dto){

        log.info("PUT /api/payments/{} ejecutado", id);

        return ResponseEntity.ok(
                service.updatePayment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deletePayment(@PathVariable Long id){

        log.info("DELETE /api/payments/{} ejecutado", id);

        service.deletePayment(id);

        return ResponseEntity.noContent().build();
    }
}