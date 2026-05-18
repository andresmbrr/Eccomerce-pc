package example.ms_pagos.service.Impl;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_pagos.dto.PaymentRequestDTO;
import example.ms_pagos.dto.PaymentResponseDTO;
import example.ms_pagos.exception.ResourceNotFoundException;
import example.ms_pagos.model.Payment;
import example.ms_pagos.model.PaymentStatus;
import example.ms_pagos.repository.PaymentRepository;
import example.ms_pagos.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl
        implements PaymentService {

    private final PaymentRepository repository;

    @Override
    public PaymentResponseDTO createPayment(
            PaymentRequestDTO dto) {

        log.info("Procesando pago para pedido {}",
                dto.getOrderId());

        Payment payment = Payment.builder()
                .orderId(dto.getOrderId())
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .status(PaymentStatus.APPROVED)
                .paymentDate(LocalDateTime.now())
                .build();

        Payment saved = repository.save(payment);

        log.info("Pago registrado ID {}", saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {

        log.info("Listando pagos");

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {

        log.info("Buscando pago ID {}", id);

        Payment payment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pago no encontrado"));

        return mapToDTO(payment);
    }

    @Override
    public List<PaymentResponseDTO>
    getPaymentsByOrderId(Long orderId) {

        log.info("Buscando pagos del pedido {}",
                orderId);

        return repository.findByOrderId(orderId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public PaymentResponseDTO updatePayment(
            Long id,
            PaymentRequestDTO dto) {

        log.info("Actualizando pago ID {}", id);

        Payment payment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pago no encontrado"));

        payment.setOrderId(dto.getOrderId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());

        Payment updated = repository.save(payment);

        log.info("Pago actualizado ID {}",
                updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void deletePayment(Long id) {

        log.info("Eliminando pago ID {}", id);

        Payment payment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pago no encontrado"));

        repository.delete(payment);

        log.info("Pago eliminado ID {}", id);
    }

    private PaymentResponseDTO mapToDTO(
            Payment payment){

        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}