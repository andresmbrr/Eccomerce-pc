package example.ms_pagos.service;



import java.util.List;

import example.ms_pagos.dto.PaymentRequestDTO;
import example.ms_pagos.dto.PaymentResponseDTO;

public interface PaymentService {

    PaymentResponseDTO createPayment(
            PaymentRequestDTO dto);

    List<PaymentResponseDTO> getAllPayments();

    PaymentResponseDTO getPaymentById(Long id);

    List<PaymentResponseDTO> getPaymentsByOrderId(
            Long orderId);

    PaymentResponseDTO updatePayment(
            Long id,
            PaymentRequestDTO dto);

    void deletePayment(Long id);
}