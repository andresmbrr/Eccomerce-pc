package example.ms_pedidos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import example.ms_pedidos.exception.GlobalExceptionHandler;
import example.ms_pedidos.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import example.ms_pedidos.dto.OrderRequestDTO;
import example.ms_pedidos.dto.OrderResponseDTO;
import example.ms_pedidos.dto.OrderStatusRequestDTO;
import example.ms_pedidos.model.OrderStatus;
import example.ms_pedidos.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private OrderService service;

        @BeforeEach
    void setUp() {

        OrderController controller =
                new OrderController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createOrder_debeRetornar201CuandoPedidoEsValido() throws Exception {

        // ARRANGE: preparar datos y mocks.
        OrderRequestDTO request =
                new OrderRequestDTO(
                        1L,
                        new BigDecimal("1799980")
                );

        OrderResponseDTO response =
                OrderResponseDTO.builder()
                        .id(1L)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        when(service.createOrder(any(OrderRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.total").value(1799980))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).createOrder(any(OrderRequestDTO.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 201 Created y se obtiene HTTP 200 OK,
        // QA debe reportar que el endpoint POST /api/pedidos
        // crea el pedido, pero no responde con el código HTTP correcto.
        // Desarrollo debe revisar el método createOrder()
        // en OrderController.
    }

    @Test
    void getAllOrders_debeRetornar200YListaDePedidos() throws Exception {

        // ARRANGE: preparar datos y mocks.
        OrderResponseDTO pedido1 =
                OrderResponseDTO.builder()
                        .id(1L)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        OrderResponseDTO pedido2 =
                OrderResponseDTO.builder()
                        .id(2L)
                        .userId(2L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 20, 0))
                        .total(new BigDecimal("899990"))
                        .status(OrderStatus.PAID)
                        .active(true)
                        .build();

        when(service.getAllOrders())
                .thenReturn(List.of(pedido1, pedido2));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].total").value(1799980))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[1].total").value(899990))
                .andExpect(jsonPath("$[1].status").value("PAID"))
                .andExpect(jsonPath("$[1].active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getAllOrders();

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 500 Internal Server Error,
        // QA debe reportar que el endpoint GET /api/pedidos falla
        // al listar los pedidos.
        // Desarrollo debe revisar el método getAllOrders()
        // en OrderController y OrderService.
    }

    @Test
    void getOrderById_debeRetornar200CuandoPedidoExiste() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        OrderResponseDTO response =
                OrderResponseDTO.builder()
                        .id(id)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        when(service.getOrderById(id))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/pedidos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.total").value(1799980))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getOrderById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 404 Not Found,
        // QA debe reportar que el endpoint GET /api/pedidos/{id}
        // no está encontrando un pedido que debería existir.
        // Desarrollo debe revisar el método getOrderById()
        // en OrderController y OrderService.
    }

    @Test
    void getOrdersByUserId_debeRetornar200YListaDePedidosDelUsuario() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long userId = 1L;

        OrderResponseDTO pedido1 =
                OrderResponseDTO.builder()
                        .id(1L)
                        .userId(userId)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        OrderResponseDTO pedido2 =
                OrderResponseDTO.builder()
                        .id(2L)
                        .userId(userId)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 20, 0))
                        .total(new BigDecimal("899990"))
                        .status(OrderStatus.PAID)
                        .active(true)
                        .build();

        when(service.getOrdersByUserId(userId))
                .thenReturn(List.of(pedido1, pedido2));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/pedidos/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].total").value(1799980))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userId").value(1L))
                .andExpect(jsonPath("$[1].total").value(899990))
                .andExpect(jsonPath("$[1].status").value("PAID"))
                .andExpect(jsonPath("$[1].active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getOrdersByUserId(userId);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 404 Not Found,
        // QA debe reportar que el endpoint GET /api/pedidos/user/{userId}
        // no retorna pedidos para un usuario que sí debería tener pedidos.
        // Desarrollo debe revisar getOrdersByUserId() en OrderController y OrderService.
    }

    @Test
    void updateOrder_debeRetornar200CuandoPedidoEsActualizado() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        OrderRequestDTO request =
                new OrderRequestDTO(
                        1L,
                        new BigDecimal("1999990")
                );

        OrderResponseDTO response =
                OrderResponseDTO.builder()
                        .id(id)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1999990"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        when(service.updateOrder(any(Long.class), any(OrderRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(put("/api/pedidos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.total").value(1999990))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).updateOrder(any(Long.class), any(OrderRequestDTO.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 404 Not Found,
        // QA debe reportar que el endpoint PUT /api/pedidos/{id}
        // no está actualizando un pedido que debería existir.
        // Desarrollo debe revisar updateOrder() en OrderController y OrderService.
    }

    @Test
    void updateStatus_debeRetornar200CuandoEstadoEsActualizado() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        OrderStatusRequestDTO request =
                new OrderStatusRequestDTO(
                        OrderStatus.PAID
                );

        OrderResponseDTO response =
                OrderResponseDTO.builder()
                        .id(id)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PAID)
                        .active(true)
                        .build();

        when(service.updateStatus(any(Long.class), any(OrderStatus.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(put("/api/pedidos/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.total").value(1799980))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).updateStatus(any(Long.class), any(OrderStatus.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba status = "PAID" y se obtiene status = "PENDING",
        // QA debe reportar que el endpoint PUT /api/pedidos/{id}/status
        // no actualiza correctamente el estado del pedido.
        // Desarrollo debe revisar updateStatus() en OrderController y OrderService.
    }

    @Test
    void deleteOrder_debeRetornar204CuandoPedidoEsEliminado() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        // Para métodos void, normalmente no necesitamos when().
        // Solo verificamos que el endpoint llame al service.deleteOrder(id).

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(delete("/api/pedidos/{id}", id))
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock.
        verify(service).deleteOrder(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 204 No Content y se obtiene HTTP 200 OK,
        // QA debe reportar que el endpoint DELETE /api/pedidos/{id}
        // elimina el pedido, pero no responde con el código HTTP esperado.
        // Desarrollo debe revisar el método deleteOrder()
        // en OrderController.
    }
        @Test
    void getOrderById_debeRetornar404CuandoPedidoNoExiste() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        when(service.getOrderById(id))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Pedido no encontrado con ID: " + id));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/pedidos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Pedido no encontrado con ID: 999"));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getOrderById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 404 Not Found
        // y se obtiene HTTP 500 Internal Server Error,
        // el manejo de excepciones del controller
        // no está funcionando correctamente.
    }
        @Test
    void updateOrder_debeRetornar404CuandoPedidoNoExiste() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        OrderRequestDTO request =
                new OrderRequestDTO(
                        1L,
                        new BigDecimal("1999990")
                );

        when(service.updateOrder(any(Long.class), any(OrderRequestDTO.class)))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Pedido no encontrado con ID: " + id));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(put("/api/pedidos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Pedido no encontrado con ID: 999"));

        // VERIFY: comprobar llamadas al mock.
        verify(service).updateOrder(any(Long.class), any(OrderRequestDTO.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 404 Not Found
        // y se obtiene HTTP 200 OK,
        // el endpoint permite actualizar pedidos inexistentes.
    }
        @Test
    void updateStatus_debeRetornar404CuandoPedidoNoExiste() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        OrderStatusRequestDTO request =
                new OrderStatusRequestDTO(
                        OrderStatus.PAID
                );

        when(service.updateStatus(any(Long.class), any(OrderStatus.class)))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Pedido no encontrado con ID: " + id));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(put("/api/pedidos/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Pedido no encontrado con ID: 999"));

        // VERIFY: comprobar llamadas al mock.
        verify(service).updateStatus(any(Long.class), any(OrderStatus.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 404 Not Found
        // y se obtiene HTTP 200 OK,
        // el endpoint permite actualizar el estado
        // de un pedido inexistente.
    }
        @Test
    void deleteOrder_debeRetornar404CuandoPedidoNoExiste() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        doThrow(
                new ResourceNotFoundException(
                        "Pedido no encontrado con ID: " + id))
                .when(service)
                .deleteOrder(id);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(delete("/api/pedidos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Pedido no encontrado con ID: 999"));

        // VERIFY: comprobar llamadas al mock.
        verify(service).deleteOrder(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 404 Not Found
        // y se obtiene HTTP 204 No Content,
        // el endpoint informa una eliminación exitosa
        // sobre un recurso inexistente.
}
}