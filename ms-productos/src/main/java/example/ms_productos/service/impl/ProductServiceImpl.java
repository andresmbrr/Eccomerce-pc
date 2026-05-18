package example.ms_productos.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_productos.dto.ProductRequestDTO;
import example.ms_productos.dto.ProductResponseDTO;
import example.ms_productos.exception.ProductNotFoundException;
import example.ms_productos.model.Product;
import example.ms_productos.repository.ProductRepository;
import example.ms_productos.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl
        implements ProductService {

    private final ProductRepository repository;

    @Override
    public ProductResponseDTO createProduct(
            ProductRequestDTO dto) {

        log.info("Creando producto {}",
                dto.getName());

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .active(true)
                .build();

        Product saved = repository.save(product);

        return mapToDTO(saved);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {

        log.info("Listando productos");

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO getProductById(
            Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Producto no encontrado"));

        return mapToDTO(product);
    }

    @Override
    public List<ProductResponseDTO>
    getProductsByCategory(String category) {

        return repository.findByCategory(category)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO updateProduct(
            Long id,
            ProductRequestDTO dto) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Producto no encontrado"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setActive(dto.getActive());

        Product updated = repository.save(product);

        return mapToDTO(updated);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Producto no encontrado"));

        repository.delete(product);
    }

    private ProductResponseDTO mapToDTO(
            Product product){

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .active(product.getActive())
                .build();
    }
}