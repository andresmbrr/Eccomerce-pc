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
    public ResponseEntity<CartResponseDTO>
    addToCart(
            @Valid @RequestBody CartRequestDTO dto){

        log.info("POST /api/cart ejecutado");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addToCart(dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponseDTO>>
    getCartByUser(@PathVariable Long userId){

        log.info("GET carrito usuario {}",
                userId);

        return ResponseEntity.ok(
                service.getCartByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    removeItem(@PathVariable Long id){

        log.info("DELETE item carrito {}", id);

        service.removeItem(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void>
    clearCart(@PathVariable Long userId){

        log.info("DELETE limpiar carrito {}", userId);

        service.clearCart(userId);

        return ResponseEntity.noContent().build();
    }
}