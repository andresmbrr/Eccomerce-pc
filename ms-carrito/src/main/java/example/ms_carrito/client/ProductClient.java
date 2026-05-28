package example.ms_carrito.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import example.ms_carrito.dto.ProductResponseDTO;

@FeignClient(name = "ms-productos")
public interface ProductClient {

    @GetMapping("/api/productos/{id}")
    ProductResponseDTO getProductById(
            @PathVariable Long id);
}