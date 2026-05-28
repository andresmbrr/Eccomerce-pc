package example.ms_pedidos.service.impl;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_pedidos.dto.OrderRequestDTO;
import example.ms_pedidos.dto.OrderResponseDTO;
import example.ms_pedidos.exception.ResourceNotFoundException;
import example.ms_pedidos.model.Order;
import example.ms_pedidos.model.OrderStatus;
import example.ms_pedidos.repository.OrderRepository;
import example.ms_pedidos.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {

        log.info("Creando pedido para usuario ID: {}", dto.getUserId());

        Order order = Order.builder()
                .userId(dto.getUserId())
                .orderDate(LocalDateTime.now())
                .total(dto.getTotal())
                .status(OrderStatus.PENDING)
                .active(true)
                .build();

        Order saved = repository.save(order);

        log.info("Pedido creado correctamente con ID: {}", saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {

        log.info("Listando todos los pedidos");

        List<OrderResponseDTO> orders = repository.findAll()
                .stream()
                .filter(order -> Boolean.TRUE.equals(order.getActive()))
                .map(this::mapToDTO)
                .toList();

        log.info("Pedidos activos encontrados: {}", orders.size());

        return orders;
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {

        log.info("Buscando pedido con ID: {}", id);

        Order order = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pedido no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Pedido no encontrado con ID: " + id);
                });

        return mapToDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {

        log.info("Buscando pedidos del usuario ID: {}", userId);

        List<OrderResponseDTO> orders = repository.findByUserId(userId)
                .stream()
                .filter(order -> Boolean.TRUE.equals(order.getActive()))
                .map(this::mapToDTO)
                .toList();

        log.info("Pedidos encontrados para usuario {}: {}", userId, orders.size());

        return orders;
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto) {

        log.info("Actualizando pedido con ID: {}", id);

        Order order = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Pedido no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Pedido no encontrado con ID: " + id);
                });

        order.setUserId(dto.getUserId());
        order.setTotal(dto.getTotal());

        Order updated = repository.save(order);

        log.info("Pedido actualizado correctamente con ID: {}", updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public OrderResponseDTO updateStatus(Long id, OrderStatus status) {

        log.info("Actualizando estado del pedido ID {} a {}", id, status);

        Order order = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar estado. Pedido no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Pedido no encontrado con ID: " + id);
                });

        order.setStatus(status);

        Order updated = repository.save(order);

        log.info("Estado actualizado correctamente. Pedido ID: {}, estado: {}",
                updated.getId(), updated.getStatus());

        return mapToDTO(updated);
    }

    @Override
    public void deleteOrder(Long id) {

        log.info("Eliminando lógicamente pedido con ID: {}", id);

        Order order = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo eliminar. Pedido no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Pedido no encontrado con ID: " + id);
                });

        order.setActive(false);
        repository.save(order);

        log.info("Pedido desactivado correctamente con ID: {}", id);
    }

    private OrderResponseDTO mapToDTO(Order order) {

        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .total(order.getTotal())
                .status(order.getStatus())
                .active(order.getActive())
                .build();
    }
}