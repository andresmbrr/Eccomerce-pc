package example.ms_carrito.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {

    private Long id;

    private Long productId;

    private Integer cantidad;
}