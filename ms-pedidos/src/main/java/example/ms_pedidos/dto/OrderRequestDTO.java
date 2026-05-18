package example.ms_pedidos.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    @NotNull(message = "El userId es obligatorio")
    private Long userId;

    @NotNull(message = "El total es obligatorio")
    @Positive(message = "El total debe ser positivo")
    private BigDecimal total;
}