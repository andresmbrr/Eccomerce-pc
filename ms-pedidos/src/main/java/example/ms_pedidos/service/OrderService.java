package example.ms_pedidos.service;



import java.util.List;

import example.ms_pedidos.dto.OrderRequestDTO;
import example.ms_pedidos.dto.OrderResponseDTO;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO dto);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO getOrderById(Long id);

    List<OrderResponseDTO> getOrdersByUserId(Long userId);

    OrderResponseDTO updateOrder(
            Long id,
            OrderRequestDTO dto);

    void deleteOrder(Long id);
}