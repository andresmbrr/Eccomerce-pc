package example.ms_carrito.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_carrito.Repository.CartRepository;
import example.ms_carrito.client.ProductClient;
import example.ms_carrito.client.StockClient;
import example.ms_carrito.dto.CartRequestDTO;
import example.ms_carrito.dto.CartResponseDTO;
import example.ms_carrito.exception.CartNotFoundException;
import example.ms_carrito.model.CartItem;
import example.ms_carrito.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl
        implements CartService {

    private final CartRepository repository;
    private final ProductClient productoClient;

    private final StockClient stockClient;

    @Override
    public CartResponseDTO addToCart(
            CartRequestDTO dto) {

        log.info("Agregando producto {} al carrito",
                dto.getProductId());

        BigDecimal fakePrice =
                BigDecimal.valueOf(10000);

        BigDecimal subtotal =
                fakePrice.multiply(
                        BigDecimal.valueOf(
                                dto.getQuantity()));

        CartItem item = CartItem.builder()
                .userId(dto.getUserId())
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .price(fakePrice)
                .subtotal(subtotal)
                .build();

        CartItem saved = repository.save(item);

        log.info("Producto agregado carrito ID {}",
                saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<CartResponseDTO> getCartByUser(
            Long userId) {

        log.info("Obteniendo carrito usuario {}",
                userId);

        return repository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public void removeItem(Long id) {

        log.info("Eliminando item carrito ID {}", id);

        CartItem item = repository.findById(id)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Item no encontrado"));

        repository.delete(item);

        log.info("Item eliminado carrito");
    }

    @Override
    public void clearCart(Long userId) {

        log.info("Limpiando carrito usuario {}",
                userId);

        List<CartItem> items =
                repository.findByUserId(userId);

        repository.deleteAll(items);

        log.info("Carrito limpiado");
    }

    private CartResponseDTO mapToDTO(
            CartItem item){

        return CartResponseDTO.builder()
                .id(item.getId())
                .userId(item.getUserId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}