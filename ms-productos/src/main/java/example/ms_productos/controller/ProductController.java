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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Productos",
        description = "Endpoints para administrar productos del ecommerce"
)
public class ProductController {

    private final ProductService service;

    @PostMapping
    @Operation(
            summary = "Crear producto",
            description = "Permite registrar un nuevo producto en el sistema con nombre, descripción, precio, categoría y estado activo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado correctamente",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<ProductResponseDTO>
    createProduct(
            @Valid @RequestBody ProductRequestDTO dto) {

        log.info("POST /api/productos - Creando producto: {}",
                dto.getName());

        ProductResponseDTO response =
                service.createProduct(dto);

        log.info("Producto creado correctamente con ID: {}",
                response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar productos",
            description = "Obtiene todos los productos activos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<ProductResponseDTO>>
    getAllProducts() {

        log.info("GET /api/productos - Listando productos");

        List<ProductResponseDTO> products =
                service.getAllProducts();

        log.info("Cantidad de productos encontrados: {}",
                products.size());

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar producto por ID",
            description = "Busca un producto específico utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado correctamente",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<ProductResponseDTO>
    getProductById(@PathVariable Long id) {

        log.info("GET /api/productos/{} - Buscando producto por ID",
                id);

        ProductResponseDTO response =
                service.getProductById(id);

        log.info("Producto encontrado: {}",
                response.getName());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    @Operation(
            summary = "Buscar productos por categoría",
            description = "Obtiene todos los productos activos asociados a una categoría específica."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos de la categoría obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<ProductResponseDTO>>
    getProductsByCategory(
            @PathVariable String category) {

        log.info("GET /api/productos/category/{} - Buscando productos por categoría",
                category);

        List<ProductResponseDTO> products =
                service.getProductsByCategory(category);

        log.info("Productos encontrados en categoría {}: {}",
                category,
                products.size());

        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza los datos de un producto existente, incluyendo nombre, descripción, precio, categoría y estado activo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<ProductResponseDTO>
    updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO dto) {

        log.info("PUT /api/productos/{} - Actualizando producto",
                id);

        ProductResponseDTO response =
                service.updateProduct(id, dto);

        log.info("Producto actualizado correctamente con ID: {}",
                response.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar producto",
            description = "Realiza una eliminación lógica del producto, dejando el registro inactivo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<Void>
    deleteProduct(@PathVariable Long id) {

        log.info("DELETE /api/productos/{} - Eliminando producto lógico",
                id);

        service.deleteProduct(id);

        log.info("Producto eliminado correctamente con ID: {}",
                id);

        return ResponseEntity.noContent().build();
    }
}