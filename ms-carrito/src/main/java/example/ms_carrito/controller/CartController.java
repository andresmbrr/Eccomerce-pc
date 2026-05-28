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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService service;

    @PostMapping
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
    public ResponseEntity<Void> removeItem(
            @PathVariable Long id) {

        log.info("DELETE /api/carrito/{} - Eliminando item del carrito",
                id);

        service.removeItem(id);

        log.info("Item eliminado correctamente del carrito ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
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