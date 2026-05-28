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
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO>
    createPayment(
            @Valid @RequestBody PaymentRequestDTO dto) {

        log.info("POST /api/pagos - Procesando pago para pedido ID: {}",
                dto.getOrderId());

        PaymentResponseDTO response =
                service.createPayment(dto);

        log.info("Pago creado correctamente con ID: {}",
                response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>>
    getAllPayments() {

        log.info("GET /api/pagos - Listando pagos");

        List<PaymentResponseDTO> payments =
                service.getAllPayments();

        log.info("Pagos encontrados: {}",
                payments.size());

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO>
    getPaymentById(@PathVariable Long id) {

        log.info("GET /api/pagos/{} - Buscando pago por ID",
                id);

        return ResponseEntity.ok(
                service.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponseDTO>>
    getPaymentsByOrderId(
            @PathVariable Long orderId) {

        log.info("GET /api/pagos/order/{} - Buscando pagos por pedido",
                orderId);

        return ResponseEntity.ok(
                service.getPaymentsByOrderId(orderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO>
    updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequestDTO dto) {

        log.info("PUT /api/pagos/{} - Actualizando pago",
                id);

        PaymentResponseDTO response =
                service.updatePayment(id, dto);

        log.info("Pago actualizado correctamente con ID: {}",
                response.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deletePayment(@PathVariable Long id) {

        log.info("DELETE /api/pagos/{} - Eliminando pago lógico",
                id);

        service.deletePayment(id);

        log.info("Pago eliminado correctamente con ID: {}",
                id);

        return ResponseEntity.noContent().build();
    }
}