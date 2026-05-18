package example.ms_carrito.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import example.ms_carrito.model.CartItem;

public interface CartRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);
}