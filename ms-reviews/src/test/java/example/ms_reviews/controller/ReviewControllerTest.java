package example.ms_reviews.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import example.ms_reviews.dto.ReviewRequestDTO;
import example.ms_reviews.dto.ReviewResponseDTO;
import example.ms_reviews.service.ReviewService;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(reviewController)
                .build();

        objectMapper = new ObjectMapper();
    }
        @Test
    void crearReview_DeberiaRetornar201() throws Exception {

        // ARRANGE

        ReviewRequestDTO request =
                new ReviewRequestDTO(
                        1L,
                        10L,
                        5,
                        "Excelente producto"
                );

        ReviewResponseDTO response =
                ReviewResponseDTO.builder()
                        .id(100L)
                        .userId(1L)
                        .productId(10L)
                        .rating(5)
                        .comentario("Excelente producto")
                        .fecha(LocalDateTime.now())
                        .active(true)
                        .build();

        Mockito.when(reviewService.crearReview(any()))
                .thenReturn(response);

        // ACT + ASSERT

        mockMvc.perform(
                post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isCreated());

        // VERIFY

        Mockito.verify(reviewService)
                .crearReview(any());
    }
        @Test
    void listarReviews_DeberiaRetornar200() throws Exception {

        // ARRANGE

        List<ReviewResponseDTO> reviews =
                List.of(
                        ReviewResponseDTO.builder()
                                .id(1L)
                                .userId(1L)
                                .productId(10L)
                                .rating(5)
                                .comentario("Excelente")
                                .active(true)
                                .build()
                );

        Mockito.when(reviewService.listarReviews())
                .thenReturn(reviews);

        // ACT + ASSERT

        mockMvc.perform(
                get("/api/reviews")
        )
                .andExpect(status().isOk());

        // VERIFY

        Mockito.verify(reviewService)
                .listarReviews();
    }
        @Test
    void buscarPorId_DeberiaRetornar200() throws Exception {

        // ARRANGE

        ReviewResponseDTO response =
                ReviewResponseDTO.builder()
                        .id(1L)
                        .userId(10L)
                        .productId(100L)
                        .rating(5)
                        .comentario("Excelente")
                        .active(true)
                        .build();

        Mockito.when(reviewService.buscarPorId(1L))
                .thenReturn(response);

        // ACT + ASSERT

        mockMvc.perform(
                get("/api/reviews/1")
        )
                .andExpect(status().isOk());

        // VERIFY

        Mockito.verify(reviewService)
                .buscarPorId(1L);
    }
        @Test
    void buscarPorProducto_DeberiaRetornar200() throws Exception {

        // ARRANGE

        List<ReviewResponseDTO> reviews =
                List.of(
                        ReviewResponseDTO.builder()
                                .id(1L)
                                .productId(100L)
                                .userId(10L)
                                .rating(5)
                                .comentario("Excelente")
                                .active(true)
                                .build()
                );

        Mockito.when(reviewService.buscarPorProducto(100L))
                .thenReturn(reviews);

        // ACT + ASSERT

        mockMvc.perform(
                get("/api/reviews/producto/100")
        )
                .andExpect(status().isOk());

        // VERIFY

        Mockito.verify(reviewService)
                .buscarPorProducto(100L);
    }
        @Test
    void buscarPorUsuario_DeberiaRetornar200() throws Exception {

        // ARRANGE

        List<ReviewResponseDTO> reviews =
                List.of(
                        ReviewResponseDTO.builder()
                                .id(1L)
                                .userId(50L)
                                .productId(100L)
                                .rating(5)
                                .comentario("Excelente")
                                .active(true)
                                .build()
                );

        Mockito.when(reviewService.buscarPorUsuario(50L))
                .thenReturn(reviews);

        // ACT + ASSERT

        mockMvc.perform(
                get("/api/reviews/usuario/50")
        )
                .andExpect(status().isOk());

        // VERIFY

        Mockito.verify(reviewService)
                .buscarPorUsuario(50L);
    }
        @Test
    void actualizarReview_DeberiaRetornar200() throws Exception {

        // ARRANGE

        ReviewRequestDTO request =
                new ReviewRequestDTO(
                        1L,
                        10L,
                        4,
                        "Comentario actualizado"
                );

        ReviewResponseDTO response =
                ReviewResponseDTO.builder()
                        .id(1L)
                        .userId(1L)
                        .productId(10L)
                        .rating(4)
                        .comentario("Comentario actualizado")
                        .active(true)
                        .build();

        Mockito.when(
                reviewService.actualizarReview(
                        Mockito.eq(1L),
                        any(ReviewRequestDTO.class)
                ))
                .thenReturn(response);

        // ACT + ASSERT

        mockMvc.perform(
                put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isOk());

        // VERIFY

        Mockito.verify(reviewService)
                .actualizarReview(
                        Mockito.eq(1L),
                        any(ReviewRequestDTO.class)
                );
    }
        @Test
    void eliminarReview_DeberiaRetornar204() throws Exception {

        // ARRANGE

        Mockito.doNothing()
                .when(reviewService)
                .eliminarReview(1L);

        // ACT + ASSERT

        mockMvc.perform(
                delete("/api/reviews/1")
        )
                .andExpect(status().isNoContent());

        // VERIFY

        Mockito.verify(reviewService)
                .eliminarReview(1L);
    }
}