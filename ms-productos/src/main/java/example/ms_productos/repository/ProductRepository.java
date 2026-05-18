package example.ms_productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import example.ms_productos.model.Product;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);
}