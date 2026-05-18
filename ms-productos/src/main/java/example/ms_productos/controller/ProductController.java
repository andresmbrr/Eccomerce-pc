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

        log.info("POST /api/products");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createProduct(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>>
    getAllProducts(){

        return ResponseEntity.ok(
                service.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO>
    getProductById(@PathVariable Long id){

        return ResponseEntity.ok(
                service.getProductById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>>
    getProductsByCategory(
            @PathVariable String category){

        return ResponseEntity.ok(
                service.getProductsByCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO>
    updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO dto){

        return ResponseEntity.ok(
                service.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteProduct(@PathVariable Long id){

        service.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}