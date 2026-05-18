package example.ms_productos.service;

import java.util.List;

import example.ms_productos.dto.ProductRequestDTO;
import example.ms_productos.dto.ProductResponseDTO;

public interface ProductService {

    ProductResponseDTO createProduct(
            ProductRequestDTO dto);

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO getProductById(Long id);

    List<ProductResponseDTO> getProductsByCategory(
            String category);

    ProductResponseDTO updateProduct(
            Long id,
            ProductRequestDTO dto);

    void deleteProduct(Long id);
}