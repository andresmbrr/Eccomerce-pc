package example.ms_stock.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import example.ms_stock.model.Stock;

public interface StockRepository
        extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductId(Long productId);
}