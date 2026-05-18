package example.ms_carrito.service;

import java.util.List;

import example.ms_carrito.dto.CartRequestDTO;
import example.ms_carrito.dto.CartResponseDTO;

public interface CartService {

    CartResponseDTO addToCart(
            CartRequestDTO dto);

    List<CartResponseDTO> getCartByUser(
            Long userId);

    void removeItem(Long id);

    void clearCart(Long userId);
}