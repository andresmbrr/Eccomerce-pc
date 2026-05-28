package example.ms_reviews.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_reviews.dto.ReviewRequestDTO;
import example.ms_reviews.dto.ReviewResponseDTO;
import example.ms_reviews.exception.ReviewNotFoundException;
import example.ms_reviews.model.Review;
import example.ms_reviews.repository.ReviewRepository;
import example.ms_reviews.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl
        implements ReviewService {

    private final ReviewRepository repository;

    @Override
    public ReviewResponseDTO crearReview(
            ReviewRequestDTO dto) {

        log.info("Creando review para producto ID: {} y usuario ID: {}",
                dto.getProductId(),
                dto.getUserId());

        Review review = Review.builder()
                .userId(dto.getUserId())
                .productId(dto.getProductId())
                .rating(dto.getRating())
                .comentario(dto.getComentario())
                .fecha(LocalDateTime.now())
                .active(true)
                .build();

        Review saved = repository.save(review);

        log.info("Review creada correctamente con ID: {}",
                saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<ReviewResponseDTO> listarReviews() {

        log.info("Listando reviews activas");

        List<ReviewResponseDTO> reviews =
                repository.findAll()
                        .stream()
                        .filter(review ->
                                Boolean.TRUE.equals(review.getActive()))
                        .map(this::mapToDTO)
                        .toList();

        log.info("Reviews activas encontradas: {}",
                reviews.size());

        return reviews;
    }

    @Override
    public ReviewResponseDTO buscarPorId(Long id) {

        log.info("Buscando review con ID: {}",
                id);

        Review review = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Review no encontrada con ID: {}",
                            id);

                    return new ReviewNotFoundException(
                            "Review no encontrada con ID: " + id);
                });

        return mapToDTO(review);
    }

    @Override
    public List<ReviewResponseDTO>
    buscarPorProducto(Long productId) {

        log.info("Buscando reviews del producto ID: {}",
                productId);

        List<ReviewResponseDTO> reviews =
                repository.findByProductId(productId)
                        .stream()
                        .filter(review ->
                                Boolean.TRUE.equals(review.getActive()))
                        .map(this::mapToDTO)
                        .toList();

        log.info("Reviews encontradas para producto {}: {}",
                productId,
                reviews.size());

        return reviews;
    }

    @Override
    public List<ReviewResponseDTO>
    buscarPorUsuario(Long userId) {

        log.info("Buscando reviews del usuario ID: {}",
                userId);

        List<ReviewResponseDTO> reviews =
                repository.findByUserId(userId)
                        .stream()
                        .filter(review ->
                                Boolean.TRUE.equals(review.getActive()))
                        .map(this::mapToDTO)
                        .toList();

        log.info("Reviews encontradas para usuario {}: {}",
                userId,
                reviews.size());

        return reviews;
    }

    @Override
    public ReviewResponseDTO actualizarReview(
            Long id,
            ReviewRequestDTO dto) {

        log.info("Actualizando review con ID: {}",
                id);

        Review review = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("No se pudo actualizar. Review no encontrada con ID: {}",
                            id);

                    return new ReviewNotFoundException(
                            "Review no encontrada con ID: " + id);
                });

        review.setUserId(dto.getUserId());
        review.setProductId(dto.getProductId());
        review.setRating(dto.getRating());
        review.setComentario(dto.getComentario());

        Review updated = repository.save(review);

        log.info("Review actualizada correctamente con ID: {}",
                updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void eliminarReview(Long id) {

        log.info("Eliminando lógicamente review con ID: {}",
                id);

        Review review = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("No se pudo eliminar. Review no encontrada con ID: {}",
                            id);

                    return new ReviewNotFoundException(
                            "Review no encontrada con ID: " + id);
                });

        review.setActive(false);

        repository.save(review);

        log.info("Review desactivada correctamente con ID: {}",
                id);
    }

    private ReviewResponseDTO mapToDTO(
            Review review) {

        return ReviewResponseDTO.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .productId(review.getProductId())
                .rating(review.getRating())
                .comentario(review.getComentario())
                .fecha(review.getFecha())
                .active(review.getActive())
                .build();
    }
}