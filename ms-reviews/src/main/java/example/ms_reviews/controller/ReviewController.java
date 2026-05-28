package example.ms_reviews.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.ms_reviews.dto.ReviewRequestDTO;
import example.ms_reviews.dto.ReviewResponseDTO;
import example.ms_reviews.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    public ResponseEntity<ReviewResponseDTO>
    crearReview(
            @Valid @RequestBody ReviewRequestDTO dto) {

        log.info("POST /api/reviews - Creando review para producto {}",
                dto.getProductId());

        ReviewResponseDTO response =
                service.crearReview(dto);

        log.info("Review creada con ID: {}",
                response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>>
    listarReviews() {

        log.info("GET /api/reviews - Listando reviews");

        List<ReviewResponseDTO> response =
                service.listarReviews();

        log.info("Reviews encontradas: {}",
                response.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO>
    buscarPorId(@PathVariable Long id) {

        log.info("GET /api/reviews/{} - Buscando por ID",
                id);

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @GetMapping("/producto/{productId}")
    public ResponseEntity<List<ReviewResponseDTO>>
    buscarPorProducto(
            @PathVariable Long productId) {

        log.info("GET /api/reviews/producto/{} - Buscando por producto",
                productId);

        return ResponseEntity.ok(
                service.buscarPorProducto(productId));
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>>
    buscarPorUsuario(
            @PathVariable Long userId) {

        log.info("GET /api/reviews/usuario/{} - Buscando por usuario",
                userId);

        return ResponseEntity.ok(
                service.buscarPorUsuario(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO>
    actualizarReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequestDTO dto) {

        log.info("PUT /api/reviews/{} - Actualizando review",
                id);

        ReviewResponseDTO response =
                service.actualizarReview(id, dto);

        log.info("Review actualizada con ID: {}",
                response.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    eliminarReview(@PathVariable Long id) {

        log.info("DELETE /api/reviews/{} - Eliminando review lógica",
                id);

        service.eliminarReview(id);

        log.info("Review eliminada correctamente ID: {}",
                id);

        return ResponseEntity.noContent().build();
    }
}