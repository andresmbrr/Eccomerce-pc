package example.ms_carrito.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_carrito.Repository.CartRepository;
import example.ms_carrito.client.ProductClient;
import example.ms_carrito.client.StockClient;
import example.ms_carrito.dto.CartRequestDTO;
import example.ms_carrito.dto.CartResponseDTO;
import example.ms_carrito.dto.ProductResponseDTO;
import example.ms_carrito.dto.StockResponseDTO;
import example.ms_carrito.exception.CartNotFoundException;
import example.ms_carrito.exception.InsufficientStockException;
import example.ms_carrito.model.CartItem;
import example.ms_carrito.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository repository;
    private final ProductClient productClient;
    private final StockClient stockClient;

    @Override
    public CartResponseDTO addToCart(CartRequestDTO dto) {

        log.info("Agregando producto ID {} al carrito del usuario ID {}",
                dto.getProductId(), dto.getUserId());

        ProductResponseDTO product =
                productClient.getProductById(dto.getProductId());

        log.info("Producto recibido desde ms-productos: {}",
                product.getName());

        StockResponseDTO stock =
                stockClient.getStockByProductId(dto.getProductId());

        log.info("Stock recibido desde ms-stock. Producto ID: {}, cantidad disponible: {}",
                stock.getProductId(), stock.getQuantity());

        if (Boolean.FALSE.equals(stock.getAvailable())) {
            log.warn("Producto ID {} no disponible en stock",
                    dto.getProductId());

            throw new InsufficientStockException(
                    "El producto no está disponible");
        }

        if (stock.getQuantity() < dto.getQuantity()) {
            log.warn("Stock insuficiente para producto ID {}. Solicitado: {}, Disponible: {}",
                    dto.getProductId(), dto.getQuantity(), stock.getQuantity());

            throw new InsufficientStockException(
                    "Stock insuficiente para el producto");
        }

        BigDecimal price = product.getPrice();

        BigDecimal subtotal = price.multiply(
                BigDecimal.valueOf(dto.getQuantity()));

        CartItem item = CartItem.builder()
                .userId(dto.getUserId())
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .price(price)
                .subtotal(subtotal)
                .active(true)
                .build();

        CartItem saved = repository.save(item);

        log.info("Producto agregado correctamente al carrito. Item ID: {}",
                saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<CartResponseDTO> getCartByUser(Long userId) {

        log.info("Obteniendo carrito del usuario ID: {}", userId);

        List<CartResponseDTO> items = repository.findByUserId(userId)
                .stream()
                .filter(item -> Boolean.TRUE.equals(item.getActive()))
                .map(this::mapToDTO)
                .toList();

        log.info("Items activos encontrados para usuario {}: {}",
                userId, items.size());

        return items;
    }

    @Override
    public void removeItem(Long id) {

        log.info("Eliminando lógicamente item del carrito ID: {}", id);

        CartItem item = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Item de carrito no encontrado con ID: {}", id);
                    return new CartNotFoundException(
                            "Item de carrito no encontrado con ID: " + id);
                });

        item.setActive(false);
        repository.save(item);

        log.info("Item de carrito desactivado correctamente ID: {}", id);
    }

    @Override
    public void clearCart(Long userId) {

        log.info("Limpiando lógicamente carrito del usuario ID: {}", userId);

        List<CartItem> items = repository.findByUserId(userId);

        items.forEach(item -> item.setActive(false));

        repository.saveAll(items);

        log.info("Carrito del usuario {} limpiado correctamente. Items afectados: {}",
                userId, items.size());
    }

    private CartResponseDTO mapToDTO(CartItem item) {

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