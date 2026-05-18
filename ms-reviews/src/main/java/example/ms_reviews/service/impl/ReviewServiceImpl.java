package example.ms_reviews.service.impl;

import example.ms_reviews.dto.*;

import example.ms_reviews.exception.ReviewNotFoundException;

import example.ms_reviews.model.Review;

import example.ms_reviews.repository.ReviewRepository;

import example.ms_reviews.service.ReviewService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl
        implements ReviewService {

    private final ReviewRepository repository;

    @Override
    public ReviewResponseDTO crearReview(
            ReviewRequestDTO dto) {

        log.info("Creando review producto {}",
                dto.getProductId());

        Review review = Review.builder()
                .userId(dto.getUserId())
                .productId(dto.getProductId())
                .rating(dto.getRating())
                .comentario(dto.getComentario())
                .fecha(LocalDateTime.now())
                .build();

        Review saved = repository.save(review);

        return mapToDTO(saved);
    }

    @Override
    public List<ReviewResponseDTO> listarReviews() {

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ReviewResponseDTO buscarPorId(Long id) {

        Review review = repository.findById(id)
                .orElseThrow(() ->
                        new ReviewNotFoundException(
                                "Review no encontrada"));

        return mapToDTO(review);
    }

    @Override
    public List<ReviewResponseDTO>
    buscarPorProducto(Long productId) {

        return repository.findByProductId(productId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ReviewResponseDTO>
    buscarPorUsuario(Long userId) {

        return repository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ReviewResponseDTO actualizarReview(
            Long id,
            ReviewRequestDTO dto) {

        Review review = repository.findById(id)
                .orElseThrow(() ->
                        new ReviewNotFoundException(
                                "Review no encontrada"));

        review.setUserId(dto.getUserId());
        review.setProductId(dto.getProductId());
        review.setRating(dto.getRating());
        review.setComentario(dto.getComentario());

        Review updated = repository.save(review);

        return mapToDTO(updated);
    }

    @Override
    public void eliminarReview(Long id) {

        Review review = repository.findById(id)
                .orElseThrow(() ->
                        new ReviewNotFoundException(
                                "Review no encontrada"));

        repository.delete(review);

        log.info("Review eliminada ID {}", id);
    }

    private ReviewResponseDTO mapToDTO(
            Review review){

        return ReviewResponseDTO.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .productId(review.getProductId())
                .rating(review.getRating())
                .comentario(review.getComentario())
                .fecha(review.getFecha())
                .build();
    }
}