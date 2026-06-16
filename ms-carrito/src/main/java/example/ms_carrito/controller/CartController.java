package example.ms_carrito.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.ms_carrito.dto.CartRequestDTO;
import example.ms_carrito.dto.CartResponseDTO;
import example.ms_carrito.service.CartService;
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
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Carrito",
        description = "Endpoints para administrar el carrito de compras. Este microservicio se comunica mediante Feign con productos y stock."
)
public class CartController {

    private final CartService service;

    @PostMapping
    @Operation(
            summary = "Agregar producto al carrito",
            description = "Permite agregar un producto al carrito de un usuario. El microservicio valida el producto y el stock disponible usando comunicación Feign."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto agregado correctamente al carrito",
                    content = @Content(schema = @Schema(implementation = CartResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados o stock insuficiente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto, stock o recurso no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<CartResponseDTO> addToCart(
            @Valid @RequestBody CartRequestDTO dto) {

        log.info("POST /api/carrito - Agregando producto {} al carrito del usuario {}",
                dto.getProductId(), dto.getUserId());

        CartResponseDTO response = service.addToCart(dto);

        log.info("Item agregado al carrito con ID: {}", response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Consultar carrito por usuario",
            description = "Obtiene todos los productos activos que se encuentran en el carrito de un usuario específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Carrito obtenido correctamente",
                    content = @Content(schema = @Schema(implementation = CartResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario o carrito no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<CartResponseDTO>> getCartByUser(
            @PathVariable Long userId) {

        log.info("GET /api/carrito/user/{} - Consultando carrito de usuario",
                userId);

        List<CartResponseDTO> response =
                service.getCartByUser(userId);

        log.info("Items encontrados en carrito del usuario {}: {}",
                userId, response.size());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar item del carrito",
            description = "Elimina un producto específico del carrito mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Item eliminado correctamente del carrito",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item del carrito no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<Void> removeItem(
            @PathVariable Long id) {

        log.info("DELETE /api/carrito/{} - Eliminando item del carrito",
                id);

        service.removeItem(id);

        log.info("Item eliminado correctamente del carrito ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    @Operation(
            summary = "Limpiar carrito de usuario",
            description = "Elimina todos los productos del carrito asociados a un usuario específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Carrito limpiado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Carrito o usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<Void> clearCart(
            @PathVariable Long userId) {

        log.info("DELETE /api/carrito/user/{} - Limpiando carrito de usuario",
                userId);

        service.clearCart(userId);

        log.info("Carrito limpiado correctamente para usuario ID: {}",
                userId);

        return ResponseEntity.noContent().build();
    }
}