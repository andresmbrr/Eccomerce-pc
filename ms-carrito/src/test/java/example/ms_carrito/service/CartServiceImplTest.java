package example.ms_carrito.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import example.ms_carrito.Repository.CartRepository;
import example.ms_carrito.client.ProductClient;
import example.ms_carrito.client.StockClient;
import example.ms_carrito.dto.CartRequestDTO;
import example.ms_carrito.dto.CartResponseDTO;
import example.ms_carrito.dto.ProductResponseDTO;
import example.ms_carrito.dto.StockResponseDTO;
import example.ms_carrito.model.CartItem;
import example.ms_carrito.service.impl.CartServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository repository;

    @Mock
    private ProductClient productClient;

    @Mock
    private StockClient stockClient;

    @InjectMocks
    private CartServiceImpl service;

    @Test
    void addToCart_debeAgregarItemCuandoProductoTieneStockDisponible() {

        // ARRANGE: preparar datos y mocks.
        CartRequestDTO request =
                new CartRequestDTO(
                        1L,
                        1L,
                        2
                );

        ProductResponseDTO product =
                new ProductResponseDTO(
                        1L,
                        "Notebook Gamer",
                        "Notebook para desarrollo y juegos",
                        new BigDecimal("899990"),
                        "Computadores",
                        true
                );

        StockResponseDTO stock =
                new StockResponseDTO(
                        1L,
                        1L,
                        20,
                        true
                );

        CartItem itemGuardado =
                CartItem.builder()
                        .id(1L)
                        .userId(1L)
                        .productId(1L)
                        .quantity(2)
                        .price(new BigDecimal("899990"))
                        .subtotal(new BigDecimal("1799980"))
                        .active(true)
                        .build();

        when(productClient.getProductById(1L))
                .thenReturn(product);

        when(stockClient.getStockByProductId(1L))
                .thenReturn(stock);

        when(repository.save(any(CartItem.class)))
                .thenReturn(itemGuardado);

        // ACT: ejecutar método del service.
        CartResponseDTO response =
                service.addToCart(request);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(1L, response.getProductId());
        assertEquals(2, response.getQuantity());
        assertEquals(new BigDecimal("899990"), response.getPrice());
        assertEquals(new BigDecimal("1799980"), response.getSubtotal());

        // VERIFY: comprobar llamadas a los mocks.
        verify(productClient).getProductById(1L);
        verify(stockClient).getStockByProductId(1L);
        verify(repository).save(any(CartItem.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba subtotal = 1799980 y se obtiene otro valor,
        // QA debe reportar que addToCart() no calcula correctamente
        // el subtotal del item.
        // Desarrollo debe revisar addToCart() en CartServiceImpl.
    }

    @Test
    void getCartByUser_debeRetornarItemsDelUsuario() {

        // ARRANGE: preparar datos y mocks.
        Long userId = 1L;

        CartItem item1 =
                CartItem.builder()
                        .id(1L)
                        .userId(userId)
                        .productId(1L)
                        .quantity(2)
                        .price(new BigDecimal("899990"))
                        .subtotal(new BigDecimal("1799980"))
                        .active(true)
                        .build();

        CartItem item2 =
                CartItem.builder()
                        .id(2L)
                        .userId(userId)
                        .productId(2L)
                        .quantity(1)
                        .price(new BigDecimal("19990"))
                        .subtotal(new BigDecimal("19990"))
                        .active(true)
                        .build();

        when(repository.findByUserId(userId))
                .thenReturn(List.of(item1, item2));

        // ACT: ejecutar método del service.
        List<CartResponseDTO> response =
                service.getCartByUser(userId);

        // ASSERT: verificar resultado esperado.
        assertEquals(2, response.size());

        assertEquals(1L, response.get(0).getId());
        assertEquals(1L, response.get(0).getUserId());
        assertEquals(1L, response.get(0).getProductId());
        assertEquals(2, response.get(0).getQuantity());
        assertEquals(new BigDecimal("899990"), response.get(0).getPrice());
        assertEquals(new BigDecimal("1799980"), response.get(0).getSubtotal());

        assertEquals(2L, response.get(1).getId());
        assertEquals(1L, response.get(1).getUserId());
        assertEquals(2L, response.get(1).getProductId());
        assertEquals(1, response.get(1).getQuantity());
        assertEquals(new BigDecimal("19990"), response.get(1).getPrice());
        assertEquals(new BigDecimal("19990"), response.get(1).getSubtotal());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findByUserId(userId);

        // Caso hipotético de falla para QA:
        // Si se esperaban 2 items y se obtiene una lista vacía,
        // QA debe reportar que getCartByUser() no retorna
        // correctamente el carrito del usuario.
        // Desarrollo debe revisar getCartByUser() en CartServiceImpl.
    }

    @Test
    void removeItem_debeDesactivarItemCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        CartItem itemExistente =
                CartItem.builder()
                        .id(id)
                        .userId(1L)
                        .productId(1L)
                        .quantity(2)
                        .price(new BigDecimal("899990"))
                        .subtotal(new BigDecimal("1799980"))
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(itemExistente));

        when(repository.save(any(CartItem.class)))
                .thenReturn(itemExistente);

        // ACT: ejecutar método del service.
        service.removeItem(id);

        // ASSERT: verificar que el item quedó inactivo.
        assertFalse(itemExistente.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);
        verify(repository).save(itemExistente);

        // Caso hipotético de falla para QA:
        // Si se esperaba active = false y se obtiene active = true,
        // QA debe reportar que removeItem() no desactiva
        // correctamente el item del carrito.
        // Desarrollo debe revisar removeItem() en CartServiceImpl.
    }

    @Test
    void clearCart_debeDesactivarTodosLosItemsDelUsuario() {

        // ARRANGE: preparar datos y mocks.
        Long userId = 1L;

        CartItem item1 =
                CartItem.builder()
                        .id(1L)
                        .userId(userId)
                        .productId(1L)
                        .quantity(2)
                        .price(new BigDecimal("899990"))
                        .subtotal(new BigDecimal("1799980"))
                        .active(true)
                        .build();

        CartItem item2 =
                CartItem.builder()
                        .id(2L)
                        .userId(userId)
                        .productId(2L)
                        .quantity(1)
                        .price(new BigDecimal("19990"))
                        .subtotal(new BigDecimal("19990"))
                        .active(true)
                        .build();

        List<CartItem> items = List.of(item1, item2);

        when(repository.findByUserId(userId))
                .thenReturn(items);

        // ACT: ejecutar método del service.
        service.clearCart(userId);

        // ASSERT: verificar que todos los items quedaron inactivos.
        assertFalse(item1.getActive());
        assertFalse(item2.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findByUserId(userId);
        verify(repository).saveAll(items);

        // Caso hipotético de falla para QA:
        // Si se esperaba que todos los items quedaran con active = false
        // y uno queda con active = true,
        // QA debe reportar que clearCart() no vacía correctamente
        // el carrito completo del usuario.
        // Desarrollo debe revisar clearCart() en CartServiceImpl.
    }
}