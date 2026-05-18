package example.ms_stock.dto;

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
public class StockResponseDTO {

    private Long id;

    private Long productId;

    private Integer quantity;

    private Boolean available;
}