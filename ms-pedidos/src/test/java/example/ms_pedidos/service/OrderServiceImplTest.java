package example.ms_pedidos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import example.ms_pedidos.dto.OrderRequestDTO;
import example.ms_pedidos.dto.OrderResponseDTO;
import example.ms_pedidos.exception.ResourceNotFoundException;
import example.ms_pedidos.model.Order;
import example.ms_pedidos.model.OrderStatus;
import example.ms_pedidos.repository.OrderRepository;
import example.ms_pedidos.service.impl.OrderServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderServiceImpl service;

    @Test
    void createOrder_debeCrearPedidoCuandoDatosSonValidos() {

        // ARRANGE: preparar datos y mocks.
        OrderRequestDTO request =
                new OrderRequestDTO(
                        1L,
                        new BigDecimal("1799980")
                );

        Order pedidoGuardado =
                Order.builder()
                        .id(1L)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        when(repository.save(any(Order.class)))
                .thenReturn(pedidoGuardado);

        // ACT: ejecutar método del service.
        OrderResponseDTO response =
                service.createOrder(request);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(new BigDecimal("1799980"), response.getTotal());
        assertEquals(OrderStatus.PENDING, response.getStatus());
        assertTrue(response.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).save(any(Order.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba status = PENDING y se obtiene status = PAID,
        // QA debe reportar que createOrder() no asigna
        // correctamente el estado inicial del pedido.
        // Desarrollo debe revisar createOrder() en OrderServiceImpl.
    }

        @Test
    void getAllOrders_debeRetornarListaDePedidos() {

        // ARRANGE: preparar datos y mocks.
        Order pedido1 =
                Order.builder()
                        .id(1L)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        Order pedido2 =
                Order.builder()
                        .id(2L)
                        .userId(2L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 20, 0))
                        .total(new BigDecimal("899990"))
                        .status(OrderStatus.PAID)
                        .active(true)
                        .build();

        when(repository.findAll())
                .thenReturn(List.of(pedido1, pedido2));

        // ACT: ejecutar método del service.
        List<OrderResponseDTO> response =
                service.getAllOrders();

        // ASSERT: verificar resultado esperado.
        assertEquals(2, response.size());

        assertEquals(1L, response.get(0).getId());
        assertEquals(1L, response.get(0).getUserId());
        assertEquals(OrderStatus.PENDING, response.get(0).getStatus());

        assertEquals(2L, response.get(1).getId());
        assertEquals(2L, response.get(1).getUserId());
        assertEquals(OrderStatus.PAID, response.get(1).getStatus());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findAll();

        // Caso hipotético de falla para QA:
        // Si se esperaban 2 pedidos y se obtiene una lista vacía,
        // QA debe reportar que getAllOrders() no retorna
        // correctamente los pedidos almacenados.
        // Desarrollo debe revisar getAllOrders() en OrderServiceImpl.
    }
        @Test
    void getOrderById_debeRetornarPedidoCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Order pedido =
                Order.builder()
                        .id(id)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(pedido));

        // ACT: ejecutar método del service.
        OrderResponseDTO response =
                service.getOrderById(id);

        // ASSERT: verificar resultado esperado.
        assertEquals(id, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(new BigDecimal("1799980"), response.getTotal());
        assertEquals(OrderStatus.PENDING, response.getStatus());
        assertTrue(response.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba encontrar el pedido y se obtiene una excepción,
        // QA debe reportar que getOrderById() no recupera correctamente
        // un pedido existente.
        // Desarrollo debe revisar getOrderById() en OrderServiceImpl.
    }

        @Test
    void getOrdersByUserId_debeRetornarPedidosDelUsuario() {

        // ARRANGE: preparar datos y mocks.
        Long userId = 1L;

        Order pedido1 =
                Order.builder()
                        .id(1L)
                        .userId(userId)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        Order pedido2 =
                Order.builder()
                        .id(2L)
                        .userId(userId)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 20, 0))
                        .total(new BigDecimal("899990"))
                        .status(OrderStatus.PAID)
                        .active(true)
                        .build();

        when(repository.findByUserId(userId))
                .thenReturn(List.of(pedido1, pedido2));

        // ACT: ejecutar método del service.
        List<OrderResponseDTO> response =
                service.getOrdersByUserId(userId);

        // ASSERT: verificar resultado esperado.
        assertEquals(2, response.size());

        assertEquals(1L, response.get(0).getId());
        assertEquals(userId, response.get(0).getUserId());

        assertEquals(2L, response.get(1).getId());
        assertEquals(userId, response.get(1).getUserId());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findByUserId(userId);

        // Caso hipotético de falla para QA:
        // Si se esperaban pedidos asociados al usuario y se obtiene
        // una lista vacía, QA debe reportar que el método no recupera
        // correctamente los pedidos del usuario.
    }
        @Test
    void updateOrder_debeActualizarPedidoCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        OrderRequestDTO request =
                new OrderRequestDTO(
                        1L,
                        new BigDecimal("1999990")
                );

        Order pedidoExistente =
                Order.builder()
                        .id(id)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        Order pedidoActualizado =
                Order.builder()
                        .id(id)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1999990"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(pedidoExistente));

        when(repository.save(any(Order.class)))
                .thenReturn(pedidoActualizado);

        // ACT: ejecutar método del service.
        OrderResponseDTO response =
                service.updateOrder(id, request);

        // ASSERT: verificar resultado esperado.
        assertEquals(id, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(new BigDecimal("1999990"), response.getTotal());
        assertEquals(OrderStatus.PENDING, response.getStatus());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);
        verify(repository).save(any(Order.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba total = 1999990 y se obtiene 1799980,
        // QA debe reportar que updateOrder() no actualiza
        // correctamente los datos del pedido.
    }
        @Test
    void updateStatus_debeActualizarEstadoCuandoPedidoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Order pedidoExistente =
                Order.builder()
                        .id(id)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        Order pedidoActualizado =
                Order.builder()
                        .id(id)
                        .userId(1L)
                        .orderDate(LocalDateTime.of(2026, 6, 17, 19, 0))
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PAID)
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(pedidoExistente));

        when(repository.save(any(Order.class)))
                .thenReturn(pedidoActualizado);

        // ACT
        OrderResponseDTO response =
                service.updateStatus(id, OrderStatus.PAID);

        // ASSERT
        assertEquals(id, response.getId());
        assertEquals(OrderStatus.PAID, response.getStatus());

        // VERIFY
        verify(repository).findById(id);
        verify(repository).save(any(Order.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba PAID y se obtiene PENDING,
        // QA debe reportar que el método updateStatus()
        // no actualiza correctamente el estado.
    }
        @Test
    void deleteOrder_debeDesactivarPedidoCuandoExiste() {

        // ARRANGE
        Long id = 1L;

        Order pedido =
                Order.builder()
                        .id(id)
                        .userId(1L)
                        .total(new BigDecimal("1799980"))
                        .status(OrderStatus.PENDING)
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(pedido));

        when(repository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        service.deleteOrder(id);

        // ASSERT
        assertFalse(pedido.getActive());

        // VERIFY
        verify(repository).findById(id);
        verify(repository).save(pedido);
    }
        @Test
    void getOrderById_debeLanzarExcepcionCuandoPedidoNoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y verificar excepción.
        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getOrderById(id)
        );

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        // Caso hipotético de falla para QA:
        // Si el método devuelve null en vez de lanzar
        // ResourceNotFoundException, el manejo de errores
        // es incorrecto y debe corregirse.
    }
        @Test
    void updateOrder_debeLanzarExcepcionCuandoPedidoNoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        OrderRequestDTO request =
                new OrderRequestDTO(
                        1L,
                        new BigDecimal("1999990")
                );

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y verificar excepción.
        assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateOrder(id, request)
        );

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);
        verify(repository, never()).save(any(Order.class));

        // Caso hipotético de falla para QA:
        // Si el método actualiza un pedido inexistente
        // o intenta guardar datos, existe un problema
        // en la validación de existencia del pedido.
    }
        @Test
    void updateStatus_debeLanzarExcepcionCuandoPedidoNoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y verificar excepción.
        assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateStatus(id, OrderStatus.PAID)
        );

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);
        verify(repository, never()).save(any(Order.class));

        // Caso hipotético de falla para QA:
        // Si el método actualiza el estado de un pedido inexistente,
        // existe un problema en la validación previa de existencia.
    }
        @Test
    void deleteOrder_debeLanzarExcepcionCuandoPedidoNoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y verificar excepción.
        assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteOrder(id)
        );

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);
        verify(repository, never()).save(any(Order.class));

        // Caso hipotético de falla para QA:
        // Si el método intenta guardar cambios sobre un pedido
        // inexistente, existe un problema en la validación previa.
    }
}