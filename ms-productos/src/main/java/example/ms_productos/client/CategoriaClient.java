package example.ms_productos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import example.ms_productos.dto.CategoriaResponseDTO;

@FeignClient(name = "ms-categorias")
public interface CategoriaClient {

    @GetMapping("/api/categorias/{id}")
    CategoriaResponseDTO obtenerCategoriaPorId(
            @PathVariable("id") Long id);
}