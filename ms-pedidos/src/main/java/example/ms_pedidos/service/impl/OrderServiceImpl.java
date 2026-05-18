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
    public OrderResponseDTO createOrder(
            OrderRequestDTO dto) {

        log.info("Creando pedido para usuario {}",
                dto.getUserId());

        Order order = Order.builder()
                .userId(dto.getUserId())
                .orderDate(LocalDateTime.now())
                .total(dto.getTotal())
                .status(OrderStatus.PENDING)
                .build();

        Order saved = repository.save(order);

        log.info("Pedido creado ID {}", saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {

        log.info("Listando pedidos");

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {

        log.info("Buscando pedido ID {}", id);

        Order order = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pedido no encontrado"));

        return mapToDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(
            Long userId) {

        log.info("Buscando pedidos del usuario {}",
                userId);

        return repository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public OrderResponseDTO updateOrder(
            Long id,
            OrderRequestDTO dto) {

        log.info("Actualizando pedido ID {}", id);

        Order order = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pedido no encontrado"));

        order.setUserId(dto.getUserId());
        order.setTotal(dto.getTotal());

        Order updated = repository.save(order);

        log.info("Pedido actualizado ID {}",
                updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void deleteOrder(Long id) {

        log.info("Eliminando pedido ID {}", id);

        Order order = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pedido no encontrado"));

        repository.delete(order);

        log.info("Pedido eliminado ID {}", id);
    }

    private OrderResponseDTO mapToDTO(Order order){

        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .total(order.getTotal())
                .status(order.getStatus())
                .build();
    }
}