package example.ms_reviews.service;
import java.util.List;

import example.ms_reviews.dto.ReviewRequestDTO;
import example.ms_reviews.dto.ReviewResponseDTO;

public interface ReviewService {

    ReviewResponseDTO crearReview(
            ReviewRequestDTO dto);

    List<ReviewResponseDTO> listarReviews();

    ReviewResponseDTO buscarPorId(Long id);

    List<ReviewResponseDTO> buscarPorProducto(
            Long productId);

    List<ReviewResponseDTO> buscarPorUsuario(
            Long userId);

    ReviewResponseDTO actualizarReview(
            Long id,
            ReviewRequestDTO dto);

    void eliminarReview(Long id);
}