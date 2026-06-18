package example.ms_stock.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import example.ms_stock.dto.StockRequestDTO;
import example.ms_stock.dto.StockResponseDTO;
import example.ms_stock.service.StockService;

@ExtendWith(MockitoExtension.class)
class StockControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private StockService service;

    @BeforeEach
    void setUp() {
        StockController controller =
                new StockController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void createStock_debeRetornar201CuandoStockEsValido() throws Exception {

        // ARRANGE: preparar datos y mocks.
        StockRequestDTO request =
                new StockRequestDTO(
                        1L,
                        20,
                        true
                );

        StockResponseDTO response =
                StockResponseDTO.builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(20)
                        .available(true)
                        .build();

        when(service.createStock(any(StockRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(post("/api/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.available").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).createStock(any(StockRequestDTO.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 201 Created y se obtiene HTTP 200 OK,
        // QA debe reportar que el endpoint POST /api/stock
        // crea el stock, pero no responde con el código HTTP correcto.
        // Desarrollo debe revisar el método createStock()
        // en StockController.
    }

    @Test
    void getAllStock_debeRetornar200YListaDeStock() throws Exception {

        // ARRANGE: preparar datos y mocks.
        StockResponseDTO stock1 =
                StockResponseDTO.builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(20)
                        .available(true)
                        .build();

        StockResponseDTO stock2 =
                StockResponseDTO.builder()
                        .id(2L)
                        .productId(2L)
                        .quantity(10)
                        .available(true)
                        .build();

        when(service.getAllStock())
                .thenReturn(List.of(stock1, stock2));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].quantity").value(20))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].productId").value(2L))
                .andExpect(jsonPath("$[1].quantity").value(10))
                .andExpect(jsonPath("$[1].available").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getAllStock();

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 500 Internal Server Error,
        // QA debe reportar que el endpoint GET /api/stock falla
        // al listar los registros de stock.
        // Desarrollo debe revisar el método getAllStock()
        // en StockController y StockService.
    }

    @Test
    void getStockById_debeRetornar200CuandoStockExiste() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        StockResponseDTO response =
                StockResponseDTO.builder()
                        .id(id)
                        .productId(1L)
                        .quantity(20)
                        .available(true)
                        .build();

        when(service.getStockById(id))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/stock/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.available").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getStockById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 404 Not Found,
        // QA debe reportar que el endpoint GET /api/stock/{id}
        // no está encontrando un stock que debería existir.
        // Desarrollo debe revisar el método getStockById()
        // en StockController y StockService.
    }

    @Test
    void getStockByProductId_debeRetornar200CuandoExisteStockDelProducto() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long productId = 1L;

        StockResponseDTO response =
                StockResponseDTO.builder()
                        .id(1L)
                        .productId(productId)
                        .quantity(20)
                        .available(true)
                        .build();

        when(service.getStockByProductId(productId))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/stock/product/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.available").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getStockByProductId(productId);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 404 Not Found,
        // QA debe reportar que el endpoint GET /api/stock/product/{productId}
        // no está encontrando el stock de un producto que debería existir.
        // Desarrollo debe revisar el método getStockByProductId()
        // en StockController y StockService.
    }

    @Test
    void updateStock_debeRetornar200CuandoStockEsActualizado() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        StockRequestDTO request =
                new StockRequestDTO(
                        1L,
                        50,
                        true
                );

        StockResponseDTO response =
                StockResponseDTO.builder()
                        .id(id)
                        .productId(1L)
                        .quantity(50)
                        .available(true)
                        .build();

        when(service.updateStock(any(Long.class), any(StockRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(put("/api/stock/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.quantity").value(50))
                .andExpect(jsonPath("$.available").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).updateStock(any(Long.class), any(StockRequestDTO.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 404 Not Found,
        // QA debe reportar que el endpoint PUT /api/stock/{id}
        // no está actualizando un stock que debería existir.
        // Desarrollo debe revisar el método updateStock()
        // en StockController y StockService.
    }

    @Test
    void deleteStock_debeRetornar204CuandoStockEsEliminado() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        // Para métodos void, normalmente no necesitamos when().
        // Solo verificamos que el endpoint llame al service.deleteStock(id).

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(delete("/api/stock/{id}", id))
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock.
        verify(service).deleteStock(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 204 No Content y se obtiene HTTP 200 OK,
        // QA debe reportar que el endpoint DELETE /api/stock/{id}
        // elimina el stock, pero no responde con el código HTTP esperado.
        // Desarrollo debe revisar el método deleteStock()
        // en StockController.
    }
}