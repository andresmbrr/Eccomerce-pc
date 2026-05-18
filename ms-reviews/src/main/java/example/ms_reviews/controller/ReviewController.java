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
            @Valid @RequestBody ReviewRequestDTO dto){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearReview(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>>
    listarReviews(){

        return ResponseEntity.ok(
                service.listarReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO>
    buscarPorId(@PathVariable Long id){

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @GetMapping("/producto/{productId}")
    public ResponseEntity<List<ReviewResponseDTO>>
    buscarPorProducto(
            @PathVariable Long productId){

        return ResponseEntity.ok(
                service.buscarPorProducto(productId));
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>>
    buscarPorUsuario(
            @PathVariable Long userId){

        return ResponseEntity.ok(
                service.buscarPorUsuario(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO>
    actualizarReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequestDTO dto){

        return ResponseEntity.ok(
                service.actualizarReview(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    eliminarReview(@PathVariable Long id){

        service.eliminarReview(id);

        return ResponseEntity.noContent().build();
    }
}