package example.ms_pagos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import example.ms_pagos.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;

    private Long userId;

    private LocalDateTime orderDate;

    private BigDecimal total;

    private OrderStatus status;

    private Boolean active;
}