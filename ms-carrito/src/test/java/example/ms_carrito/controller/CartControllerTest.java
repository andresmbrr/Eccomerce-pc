package example.ms_carrito.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import example.ms_carrito.dto.CartRequestDTO;
import example.ms_carrito.dto.CartResponseDTO;
import example.ms_carrito.service.CartService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private CartService service;

    @BeforeEach
    void setUp() {
        CartController controller =
                new CartController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void addToCart_debeRetornar201CuandoItemEsValido() throws Exception {

        // ARRANGE: preparar datos y mocks.
        CartRequestDTO request =
                new CartRequestDTO(
                        1L,
                        1L,
                        2
                );

        CartResponseDTO response =
                CartResponseDTO.builder()
                        .id(1L)
                        .userId(1L)
                        .productId(1L)
                        .quantity(2)
                        .price(new BigDecimal("899990"))
                        .subtotal(new BigDecimal("1799980"))
                        .build();

        when(service.addToCart(any(CartRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(post("/api/carrito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.price").value(899990))
                .andExpect(jsonPath("$.subtotal").value(1799980));

        // VERIFY: comprobar llamadas al mock.
        verify(service).addToCart(any(CartRequestDTO.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 201 Created y se obtiene HTTP 200 OK,
        // QA debe reportar que el endpoint POST /api/carrito
        // agrega el producto al carrito, pero no responde con el código HTTP correcto.
        // Desarrollo debe revisar el método addToCart()
        // en CartController.
    }

    @Test
    void getCartByUser_debeRetornar200YListaDelCarrito() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long userId = 1L;

        CartResponseDTO item1 =
                CartResponseDTO.builder()
                        .id(1L)
                        .userId(userId)
                        .productId(1L)
                        .quantity(2)
                        .price(new BigDecimal("899990"))
                        .subtotal(new BigDecimal("1799980"))
                        .build();

        CartResponseDTO item2 =
                CartResponseDTO.builder()
                        .id(2L)
                        .userId(userId)
                        .productId(2L)
                        .quantity(1)
                        .price(new BigDecimal("19990"))
                        .subtotal(new BigDecimal("19990"))
                        .build();

        when(service.getCartByUser(userId))
                .thenReturn(List.of(item1, item2));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/carrito/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].price").value(899990))
                .andExpect(jsonPath("$[0].subtotal").value(1799980))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userId").value(1L))
                .andExpect(jsonPath("$[1].productId").value(2L))
                .andExpect(jsonPath("$[1].quantity").value(1))
                .andExpect(jsonPath("$[1].price").value(19990))
                .andExpect(jsonPath("$[1].subtotal").value(19990));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getCartByUser(userId);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 500 Internal Server Error,
        // QA debe reportar que el endpoint GET /api/carrito/user/{userId}
        // falla al listar el carrito del usuario.
        // Desarrollo debe revisar el método getCartByUser()
        // en CartController y CartService.
    }

    @Test
    void removeItem_debeRetornar204CuandoItemEsEliminado() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        // Para métodos void, normalmente no necesitamos when().
        // Solo verificamos que el endpoint llame al service.removeItem(id).

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(delete("/api/carrito/{id}", id))
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock.
        verify(service).removeItem(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 204 No Content y se obtiene HTTP 200 OK,
        // QA debe reportar que el endpoint DELETE /api/carrito/{id}
        // elimina el item del carrito, pero no responde con el código HTTP esperado.
        // Desarrollo debe revisar el método removeItem() en CartController.
    }

    @Test
    void clearCart_debeRetornar204CuandoCarritoDelUsuarioEsEliminado() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long userId = 1L;

        // Para métodos void, normalmente no necesitamos when().
        // Solo verificamos que el endpoint llame al service.clearCart(userId).

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(delete("/api/carrito/user/{userId}", userId))
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock.
        verify(service).clearCart(userId);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 204 No Content y se obtiene HTTP 200 OK,
        // QA debe reportar que el endpoint DELETE /api/carrito/user/{userId}
        // elimina el carrito del usuario, pero no responde con el código HTTP esperado.
        // Desarrollo debe revisar el método clearCart() en CartController.
    }
}