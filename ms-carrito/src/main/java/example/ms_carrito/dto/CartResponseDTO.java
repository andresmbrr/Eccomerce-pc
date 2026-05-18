package example.ms_carrito.dto;

import java.math.BigDecimal;

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
public class CartResponseDTO {

    private Long id;

    private Long userId;

    private Long productId;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal subtotal;
}