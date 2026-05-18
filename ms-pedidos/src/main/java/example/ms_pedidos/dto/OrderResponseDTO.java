package example.ms_pedidos.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import example.ms_pedidos.model.OrderStatus;
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
public class OrderResponseDTO {

    private Long id;

    private Long userId;

    private LocalDateTime orderDate;

    private BigDecimal total;

    private OrderStatus status;
}