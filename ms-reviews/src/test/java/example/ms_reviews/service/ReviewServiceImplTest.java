package example.ms_reviews.service;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import example.ms_reviews.dto.ReviewRequestDTO;
import example.ms_reviews.dto.ReviewResponseDTO;
import example.ms_reviews.exception.ReviewNotFoundException;
import example.ms_reviews.model.Review;
import example.ms_reviews.repository.ReviewRepository;
import example.ms_reviews.service.impl.ReviewServiceImpl;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository repository;

    @InjectMocks
    private ReviewServiceImpl service;

        @Test
    void crearReview_DeberiaCrearReview() {

        // ARRANGE: preparar datos y mocks.

        ReviewRequestDTO request =
                new ReviewRequestDTO(
                        1L,
                        10L,
                        5,
                        "Excelente producto"
                );

        Review savedReview = Review.builder()
                .id(100L)
                .userId(1L)
                .productId(10L)
                .rating(5)
                .comentario("Excelente producto")
                .fecha(LocalDateTime.now())
                .active(true)
                .build();

        Mockito.when(repository.save(any(Review.class)))
                .thenReturn(savedReview);

        // ACT: ejecutar método.

        ReviewResponseDTO response =
                service.crearReview(request);

        // ASSERT: verificar resultado.

        assertNotNull(response);

        assertEquals(100L,
                response.getId());

        assertEquals(1L,
                response.getUserId());

        assertEquals(10L,
                response.getProductId());

        assertEquals(5,
                response.getRating());

        assertEquals(
                "Excelente producto",
                response.getComentario());

        assertTrue(
                response.getActive());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .save(any(Review.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Review creada correctamente.
        *
        * Se obtuvo:
        * Error al guardar la review.
        *
        * Revisar:
        * - repository.save()
        * - construcción de entidad Review
        * - mapeo a DTO
        */
    }
        @Test
    void listarReviews_DeberiaRetornarSoloActivas() {

        // ARRANGE: preparar datos y mocks.

        Review activa1 = Review.builder()
                .id(1L)
                .userId(1L)
                .productId(10L)
                .rating(5)
                .comentario("Excelente producto")
                .fecha(LocalDateTime.now())
                .active(true)
                .build();

        Review activa2 = Review.builder()
                .id(2L)
                .userId(2L)
                .productId(20L)
                .rating(4)
                .comentario("Muy buen producto")
                .fecha(LocalDateTime.now())
                .active(true)
                .build();

        Review inactiva = Review.builder()
                .id(3L)
                .userId(3L)
                .productId(30L)
                .rating(1)
                .comentario("Producto malo")
                .fecha(LocalDateTime.now())
                .active(false)
                .build();

        Mockito.when(repository.findAll())
                .thenReturn(
                        List.of(
                                activa1,
                                activa2,
                                inactiva
                        ));

        // ACT: ejecutar método.

        List<ReviewResponseDTO> response =
                service.listarReviews();

        // ASSERT: verificar resultado.

        assertNotNull(response);

        assertEquals(
                2,
                response.size());

        assertTrue(
                response.stream()
                        .allMatch(
                                ReviewResponseDTO::getActive));

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .findAll();

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Retornar únicamente reviews activas.
        *
        * Se obtuvo:
        * También se retornan reviews inactivas.
        *
        * Revisar:
        * - filtro active=true
        * - stream().filter()
        * - mapeo a DTO
        */
    }
        @Test
    void buscarPorId_DeberiaRetornarReview() {

        // ARRANGE: preparar datos y mocks.

        Review review = Review.builder()
                .id(1L)
                .userId(100L)
                .productId(200L)
                .rating(5)
                .comentario("Excelente producto")
                .fecha(LocalDateTime.now())
                .active(true)
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(review));

        // ACT: ejecutar método.

        ReviewResponseDTO response =
                service.buscarPorId(1L);

        // ASSERT: verificar resultado.

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId());

        assertEquals(
                100L,
                response.getUserId());

        assertEquals(
                200L,
                response.getProductId());

        assertEquals(
                5,
                response.getRating());

        assertEquals(
                "Excelente producto",
                response.getComentario());

        assertTrue(
                response.getActive());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .findById(1L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Obtener la review solicitada.
        *
        * Se obtuvo:
        * Error al buscar la review.
        *
        * Revisar:
        * - repository.findById()
        * - mapeo a DTO
        * - manejo de Optional
        */
    }
        @Test
    void buscarPorId_ReviewNoExiste_DeberiaLanzarReviewNotFoundException() {

        // ARRANGE: preparar datos y mocks.

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: verificar excepción.

        ReviewNotFoundException exception =
                assertThrows(
                        ReviewNotFoundException.class,
                        () -> service.buscarPorId(99L)
                );

        assertEquals(
                "Review no encontrada con ID: 99",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .findById(99L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * ReviewNotFoundException.
        *
        * Se obtuvo:
        * Retorno exitoso de una review inexistente.
        *
        * Revisar:
        * - repository.findById()
        * - Optional.empty()
        * - orElseThrow()
        */
    }
        @Test
    void buscarPorProducto_DeberiaRetornarReviewsActivas() {

        // ARRANGE: preparar datos y mocks.

        Review activa = Review.builder()
                .id(1L)
                .userId(10L)
                .productId(100L)
                .rating(5)
                .comentario("Excelente producto")
                .fecha(LocalDateTime.now())
                .active(true)
                .build();

        Review inactiva = Review.builder()
                .id(2L)
                .userId(20L)
                .productId(100L)
                .rating(1)
                .comentario("Mal producto")
                .fecha(LocalDateTime.now())
                .active(false)
                .build();

        Mockito.when(repository.findByProductId(100L))
                .thenReturn(
                        List.of(
                                activa,
                                inactiva
                        ));

        // ACT: ejecutar método.

        List<ReviewResponseDTO> response =
                service.buscarPorProducto(100L);

        // ASSERT: verificar resultado.

        assertNotNull(response);

        assertEquals(
                1,
                response.size());

        assertEquals(
                100L,
                response.get(0).getProductId());

        assertTrue(
                response.get(0).getActive());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .findByProductId(100L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Retornar únicamente reviews activas
        * del producto.
        *
        * Se obtuvo:
        * También se retornan reviews inactivas.
        *
        * Revisar:
        * - repository.findByProductId()
        * - filtro active=true
        * - stream().filter()
        */
    }
        @Test
    void buscarPorUsuario_DeberiaRetornarReviewsActivas() {

        // ARRANGE: preparar datos y mocks.

        Review activa = Review.builder()
                .id(1L)
                .userId(50L)
                .productId(100L)
                .rating(5)
                .comentario("Excelente producto")
                .fecha(LocalDateTime.now())
                .active(true)
                .build();

        Review inactiva = Review.builder()
                .id(2L)
                .userId(50L)
                .productId(200L)
                .rating(2)
                .comentario("No me gustó")
                .fecha(LocalDateTime.now())
                .active(false)
                .build();

        Mockito.when(repository.findByUserId(50L))
                .thenReturn(
                        List.of(
                                activa,
                                inactiva
                        ));

        // ACT: ejecutar método.

        List<ReviewResponseDTO> response =
                service.buscarPorUsuario(50L);

        // ASSERT: verificar resultado.

        assertNotNull(response);

        assertEquals(
                1,
                response.size());

        assertEquals(
                50L,
                response.get(0).getUserId());

        assertTrue(
                response.get(0).getActive());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .findByUserId(50L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Retornar únicamente reviews activas
        * del usuario.
        *
        * Se obtuvo:
        * También se retornan reviews inactivas.
        *
        * Revisar:
        * - repository.findByUserId()
        * - filtro active=true
        * - stream().filter()
        */
    }
        @Test
    void actualizarReview_DeberiaActualizarReview() {

        // ARRANGE: preparar datos y mocks.

        ReviewRequestDTO request =
                new ReviewRequestDTO(
                        20L,
                        200L,
                        4,
                        "Producto actualizado"
                );

        Review review = Review.builder()
                .id(1L)
                .userId(10L)
                .productId(100L)
                .rating(2)
                .comentario("Comentario antiguo")
                .fecha(LocalDateTime.now())
                .active(true)
                .build();

        Review reviewActualizada = Review.builder()
                .id(1L)
                .userId(20L)
                .productId(200L)
                .rating(4)
                .comentario("Producto actualizado")
                .fecha(review.getFecha())
                .active(true)
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(review));

        Mockito.when(repository.save(any(Review.class)))
                .thenReturn(reviewActualizada);

        // ACT: ejecutar método.

        ReviewResponseDTO response =
                service.actualizarReview(1L, request);

        // ASSERT: verificar resultado.

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId());

        assertEquals(
                20L,
                response.getUserId());

        assertEquals(
                200L,
                response.getProductId());

        assertEquals(
                4,
                response.getRating());

        assertEquals(
                "Producto actualizado",
                response.getComentario());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .findById(1L);

        Mockito.verify(repository)
                .save(any(Review.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Actualizar correctamente la review.
        *
        * Se obtuvo:
        * Los datos permanecen sin cambios.
        *
        * Revisar:
        * - findById()
        * - setters de Review
        * - repository.save()
        */
    }
        @Test
    void actualizarReview_ReviewNoExiste_DeberiaLanzarReviewNotFoundException() {

        // ARRANGE: preparar datos y mocks.

        ReviewRequestDTO request =
                new ReviewRequestDTO(
                        1L,
                        10L,
                        5,
                        "Excelente producto"
                );

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: verificar excepción.

        ReviewNotFoundException exception =
                assertThrows(
                        ReviewNotFoundException.class,
                        () -> service.actualizarReview(99L, request)
                );

        assertEquals(
                "Review no encontrada con ID: 99",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .findById(99L);

        Mockito.verify(repository, Mockito.never())
                .save(any());

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * ReviewNotFoundException.
        *
        * Se obtuvo:
        * Se intentó actualizar una review inexistente.
        *
        * Revisar:
        * - repository.findById()
        * - Optional.empty()
        * - orElseThrow()
        */
    }
        @Test
    void eliminarReview_DeberiaDesactivarReview() {

        // ARRANGE: preparar datos y mocks.

        Review review = Review.builder()
                .id(1L)
                .userId(10L)
                .productId(100L)
                .rating(5)
                .comentario("Excelente producto")
                .fecha(LocalDateTime.now())
                .active(true)
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(review));

        Mockito.when(repository.save(any(Review.class)))
                .thenReturn(review);

        // ACT: ejecutar método.

        service.eliminarReview(1L);

        // ASSERT: verificar cambio de estado.

        assertFalse(review.getActive());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .findById(1L);

        Mockito.verify(repository)
                .save(review);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Desactivar la review.
        *
        * Se obtuvo:
        * La review continúa activa.
        *
        * Revisar:
        * - setActive(false)
        * - repository.save()
        */
    }
        @Test
    void eliminarReview_ReviewNoExiste_DeberiaLanzarReviewNotFoundException() {

        // ARRANGE: preparar datos y mocks.

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: verificar excepción.

        ReviewNotFoundException exception =
                assertThrows(
                        ReviewNotFoundException.class,
                        () -> service.eliminarReview(99L)
                );

        assertEquals(
                "Review no encontrada con ID: 99",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .findById(99L);

        Mockito.verify(repository, Mockito.never())
                .save(any());

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * ReviewNotFoundException.
        *
        * Se obtuvo:
        * Se intentó eliminar una review inexistente.
        *
        * Revisar:
        * - repository.findById()
        * - Optional.empty()
        * - orElseThrow()
        */
    }

}