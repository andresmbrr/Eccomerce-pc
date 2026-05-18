package example.ms_carrito.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import example.ms_carrito.dto.StockResponseDTO;

@FeignClient(name = "ms-stock")
public interface StockClient {

    @GetMapping("/api/stock/product/{productId}")
    StockResponseDTO obtenerStockProducto(
            @PathVariable("productId") Long productId);
}