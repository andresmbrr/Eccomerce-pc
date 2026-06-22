package example.ms_pagos.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_pagos.client.OrderClient;
import example.ms_pagos.dto.OrderResponseDTO;
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
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final OrderClient orderClient;

    @Override
    public PaymentResponseDTO createPayment(
            PaymentRequestDTO dto) {

        log.info("Procesando pago para pedido ID: {}",
                dto.getOrderId());

        OrderResponseDTO order =
                orderClient.getOrderById(dto.getOrderId());

        log.info("Pedido obtenido desde ms-pedidos. ID: {}, total: {}",
                order.getId(),
                order.getTotal());

        Payment payment = Payment.builder()
                .orderId(dto.getOrderId())
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .status(PaymentStatus.APPROVED)
                .paymentDate(LocalDateTime.now())
                .active(true)
                .build();

        Payment saved = repository.save(payment);

        log.info("Pago registrado correctamente con ID: {}",
                saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {

        log.info("Listando pagos activos");

        List<PaymentResponseDTO> payments =
                repository.findAll()
                        .stream()
                        .filter(payment ->
                                Boolean.TRUE.equals(payment.getActive()))
                        .map(this::mapToDTO)
                        .toList();

        log.info("Pagos activos encontrados: {}",
                payments.size());

        return payments;
    }

    @Override
    public PaymentResponseDTO getPaymentById(
            Long id) {

        log.info("Buscando pago con ID: {}",
                id);

        Payment payment = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Pago no encontrado con ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Pago no encontrado con ID: " + id);
                });

        return mapToDTO(payment);
    }

    @Override
    public List<PaymentResponseDTO>
    getPaymentsByOrderId(Long orderId) {

        log.info("Buscando pagos del pedido ID: {}",
                orderId);

        List<PaymentResponseDTO> payments =
                repository.findByOrderId(orderId)
                        .stream()
                        .filter(payment ->
                                Boolean.TRUE.equals(payment.getActive()))
                        .map(this::mapToDTO)
                        .toList();

        log.info("Pagos encontrados para pedido {}: {}",
                orderId,
                payments.size());

        return payments;
    }

    @Override
    public PaymentResponseDTO updatePayment(
            Long id,
            PaymentRequestDTO dto) {

        log.info("Actualizando pago con ID: {}",
                id);

        Payment payment = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("No se pudo actualizar. Pago no encontrado con ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Pago no encontrado con ID: " + id);
                });

        OrderResponseDTO order =
                orderClient.getOrderById(dto.getOrderId());

        log.info("Pedido validado desde ms-pedidos. ID: {}",
                order.getId());

        payment.setOrderId(dto.getOrderId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());

        Payment updated = repository.save(payment);

        log.info("Pago actualizado correctamente con ID: {}",
                updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void deletePayment(Long id) {

        log.info("Eliminando lógicamente pago con ID: {}",
                id);

        Payment payment = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("No se pudo eliminar. Pago no encontrado con ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Pago no encontrado con ID: " + id);
                });

        payment.setActive(false);

        repository.save(payment);

        log.info("Pago desactivado correctamente con ID: {}",
                id);
    }

    private PaymentResponseDTO mapToDTO(
            Payment payment) {

        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .active(payment.getActive())
                .build();
    }
}
