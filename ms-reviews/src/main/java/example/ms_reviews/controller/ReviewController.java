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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Reviews",
        description = "Endpoints para administrar reseñas y calificaciones de productos realizadas por usuarios"
)
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    @Operation(
            summary = "Crear review",
            description = "Permite registrar una nueva reseña asociada a un usuario y a un producto. La calificación debe estar dentro del rango permitido."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Review creada correctamente",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Listar reviews",
            description = "Obtiene todas las reviews activas registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reviews obtenidas correctamente",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Buscar review por ID",
            description = "Busca una review específica utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Review encontrada correctamente",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Review no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<ReviewResponseDTO>
    buscarPorId(@PathVariable Long id) {

        log.info("GET /api/reviews/{} - Buscando por ID",
                id);

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @GetMapping("/producto/{productId}")
    @Operation(
            summary = "Buscar reviews por producto",
            description = "Obtiene todas las reviews activas asociadas a un producto específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reviews del producto obtenidas correctamente",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<ReviewResponseDTO>>
    buscarPorProducto(
            @PathVariable Long productId) {

        log.info("GET /api/reviews/producto/{} - Buscando por producto",
                productId);

        return ResponseEntity.ok(
                service.buscarPorProducto(productId));
    }

    @GetMapping("/usuario/{userId}")
    @Operation(
            summary = "Buscar reviews por usuario",
            description = "Obtiene todas las reviews activas realizadas por un usuario específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reviews del usuario obtenidas correctamente",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<ReviewResponseDTO>>
    buscarPorUsuario(
            @PathVariable Long userId) {

        log.info("GET /api/reviews/usuario/{} - Buscando por usuario",
                userId);

        return ResponseEntity.ok(
                service.buscarPorUsuario(userId));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar review",
            description = "Actualiza los datos de una review existente, incluyendo usuario, producto, calificación y comentario."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Review actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Review no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Eliminar review",
            description = "Realiza una eliminación lógica de la review, dejando el registro inactivo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Review eliminada correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Review no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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