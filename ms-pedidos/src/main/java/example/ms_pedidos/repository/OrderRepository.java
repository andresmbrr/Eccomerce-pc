package example.ms_pedidos.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import example.ms_pedidos.model.Order;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
}