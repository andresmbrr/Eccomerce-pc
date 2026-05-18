package example.ms_reviews.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import example.ms_reviews.model.Review;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

    List<Review> findByUserId(Long userId);
}