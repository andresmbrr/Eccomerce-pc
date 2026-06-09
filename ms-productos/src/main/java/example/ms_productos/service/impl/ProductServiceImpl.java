package example.ms_productos.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_productos.client.CategoriaClient;
import example.ms_productos.dto.CategoriaResponseDTO;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoriaClient categoriaClient;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {

        log.info("Creando producto: {}", dto.getName());

        CategoriaResponseDTO categoria =
                categoriaClient.obtenerCategoriaPorId(dto.getCategoryId());

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(categoria.getNombre())
                .active(true)
                .build();

        Product saved = repository.save(product);

        log.info("Producto creado correctamente con ID: {}",
                saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {

        log.info("Listando productos activos");

        return repository.findAll()
                .stream()
                .filter(product ->
                        Boolean.TRUE.equals(product.getActive()))
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {

        log.info("Buscando producto con ID: {}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con ID: {}", id);
                    return new ProductNotFoundException(
                            "Producto no encontrado con ID: " + id);
                });

        return mapToDTO(product);
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(String category) {

        log.info("Buscando productos por categoría: {}", category);

        return repository.findByCategory(category)
                .stream()
                .filter(product ->
                        Boolean.TRUE.equals(product.getActive()))
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO updateProduct(
            Long id,
            ProductRequestDTO dto) {

        log.info("Actualizando producto con ID: {}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Producto no encontrado con ID: {}",
                            id);
                    return new ProductNotFoundException(
                            "Producto no encontrado con ID: " + id);
                });

        CategoriaResponseDTO categoria =
                categoriaClient.obtenerCategoriaPorId(dto.getCategoryId());

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(categoria.getNombre());

        if (dto.getActive() != null) {
            product.setActive(dto.getActive());
        }

        Product updated = repository.save(product);

        log.info("Producto actualizado correctamente con ID: {}",
                updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void deleteProduct(Long id) {

        log.info("Eliminando lógicamente producto con ID: {}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo eliminar. Producto no encontrado con ID: {}",
                            id);
                    return new ProductNotFoundException(
                            "Producto no encontrado con ID: " + id);
                });

        product.setActive(false);
        repository.save(product);

        log.info("Producto desactivado correctamente con ID: {}", id);
    }

    private ProductResponseDTO mapToDTO(Product product) {

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