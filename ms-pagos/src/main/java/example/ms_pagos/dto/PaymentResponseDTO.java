package example.ms_pagos.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import example.ms_pagos.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long id;

    private Long orderId;

    private BigDecimal amount;

    private String paymentMethod;

    private PaymentStatus status;

    private LocalDateTime paymentDate;
}