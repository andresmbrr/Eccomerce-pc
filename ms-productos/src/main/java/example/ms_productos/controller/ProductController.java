package example.ms_productos.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.ms_productos.dto.ProductRequestDTO;
import example.ms_productos.dto.ProductResponseDTO;
import example.ms_productos.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductResponseDTO>
    createProduct(
            @Valid @RequestBody ProductRequestDTO dto){

        log.info("POST /api/productos - Creando producto: {}", dto.getName());

        ProductResponseDTO response = service.createProduct(dto);

        log.info("Producto creado correctamente con ID: {}", response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>>
    getAllProducts(){

        log.info("GET /api/productos - Listando productos");

        List<ProductResponseDTO> products = service.getAllProducts();

        log.info("Cantidad de productos encontrados: {}", products.size());

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO>
    getProductById(@PathVariable Long id){

        log.info("GET /api/productos/{} - Buscando producto por ID", id);

        ProductResponseDTO response = service.getProductById(id);

        log.info("Producto encontrado: {}", response.getName());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>>
    getProductsByCategory(
            @PathVariable String category){

        log.info("GET /api/productos/category/{} - Buscando productos por categoría", category);

        List<ProductResponseDTO> products =
                service.getProductsByCategory(category);

        log.info("Productos encontrados en categoría {}: {}", category, products.size());

        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO>
    updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO dto){

        log.info("PUT /api/productos/{} - Actualizando producto", id);

        ProductResponseDTO response =
                service.updateProduct(id, dto);

        log.info("Producto actualizado correctamente con ID: {}", response.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteProduct(@PathVariable Long id){

        log.info("DELETE /api/productos/{} - Eliminando producto lógico", id);

        service.deleteProduct(id);

        log.info("Producto eliminado correctamente con ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}