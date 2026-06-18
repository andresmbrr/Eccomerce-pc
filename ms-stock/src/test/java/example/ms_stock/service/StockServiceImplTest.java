package example.ms_stock.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import example.ms_stock.dto.StockRequestDTO;
import example.ms_stock.dto.StockResponseDTO;
import example.ms_stock.model.Stock;
import example.ms_stock.repository.StockRepository;
import example.ms_stock.service.impl.StockServiceImpl;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private StockRepository repository;

    @InjectMocks
    private StockServiceImpl service;

    @Test
    void createStock_debeCrearStockCuandoProductoNoTieneStock() {

        // ARRANGE: preparar datos y mocks.
        StockRequestDTO request =
                new StockRequestDTO(
                        1L,
                        20,
                        true
                );

        Stock stockGuardado =
                Stock.builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(20)
                        .available(true)
                        .build();

        when(repository.existsByProductId(1L))
                .thenReturn(false);

        when(repository.save(any(Stock.class)))
                .thenReturn(stockGuardado);

        // ACT: ejecutar método del service.
        StockResponseDTO response =
                service.createStock(request);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getProductId());
        assertEquals(20, response.getQuantity());
        assertTrue(response.getAvailable());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).existsByProductId(1L);
        verify(repository).save(any(Stock.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba available = true y se obtiene available = false,
        // QA debe reportar que el stock se crea, pero queda no disponible.
        // Desarrollo debe revisar createStock() en StockServiceImpl.
    }

    @Test
    void getAllStock_debeRetornarListaDeStock() {

        // ARRANGE: preparar datos y mocks.
        Stock stock1 =
                Stock.builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(20)
                        .available(true)
                        .build();

        Stock stock2 =
                Stock.builder()
                        .id(2L)
                        .productId(2L)
                        .quantity(10)
                        .available(true)
                        .build();

        when(repository.findAll())
                .thenReturn(List.of(stock1, stock2));

        // ACT: ejecutar método del service.
        List<StockResponseDTO> response =
                service.getAllStock();

        // ASSERT: verificar resultado esperado.
        assertEquals(2, response.size());

        assertEquals(1L, response.get(0).getId());
        assertEquals(1L, response.get(0).getProductId());
        assertEquals(20, response.get(0).getQuantity());
        assertTrue(response.get(0).getAvailable());

        assertEquals(2L, response.get(1).getId());
        assertEquals(2L, response.get(1).getProductId());
        assertEquals(10, response.get(1).getQuantity());
        assertTrue(response.get(1).getAvailable());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findAll();

        // Caso hipotético de falla para QA:
        // Si se esperaban 2 registros de stock y se obtiene una lista vacía,
        // QA debe reportar que getAllStock() no retorna los stocks existentes.
        // Desarrollo debe revisar getAllStock() en StockServiceImpl.
    }

    @Test
    void getStockById_debeRetornarStockCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Stock stock =
                Stock.builder()
                        .id(id)
                        .productId(1L)
                        .quantity(20)
                        .available(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(stock));

        // ACT: ejecutar método del service.
        StockResponseDTO response =
                service.getStockById(id);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getProductId());
        assertEquals(20, response.getQuantity());
        assertTrue(response.getAvailable());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba encontrar el stock con ID 1 y se obtiene una excepción,
        // QA debe reportar que getStockById() no retorna un stock existente.
        // Desarrollo debe revisar getStockById() en StockServiceImpl.
    }

    @Test
    void getStockByProductId_debeRetornarStockCuandoExisteProducto() {

        // ARRANGE: preparar datos y mocks.
        Long productId = 1L;

        Stock stock =
                Stock.builder()
                        .id(1L)
                        .productId(productId)
                        .quantity(20)
                        .available(true)
                        .build();

        when(repository.findByProductId(productId))
                .thenReturn(Optional.of(stock));

        // ACT: ejecutar método del service.
        StockResponseDTO response =
                service.getStockByProductId(productId);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getProductId());
        assertEquals(20, response.getQuantity());
        assertTrue(response.getAvailable());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findByProductId(productId);

        // Caso hipotético de falla para QA:
        // Si se esperaba encontrar stock para productId 1 y se obtiene una excepción,
        // QA debe reportar que getStockByProductId() no retorna el stock
        // asociado a un producto existente.
        // Desarrollo debe revisar getStockByProductId() en StockServiceImpl.
    }

    @Test
    void updateStock_debeActualizarStockCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Stock stockExistente =
                Stock.builder()
                        .id(id)
                        .productId(1L)
                        .quantity(20)
                        .available(true)
                        .build();

        StockRequestDTO request =
                new StockRequestDTO(
                        1L,
                        50,
                        true
                );

        Stock stockActualizado =
                Stock.builder()
                        .id(id)
                        .productId(1L)
                        .quantity(50)
                        .available(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(stockExistente));

        when(repository.save(any(Stock.class)))
                .thenReturn(stockActualizado);

        // ACT: ejecutar método del service.
        StockResponseDTO response =
                service.updateStock(id, request);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getProductId());
        assertEquals(50, response.getQuantity());
        assertTrue(response.getAvailable());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);
        verify(repository).save(any(Stock.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba quantity = 50 y se obtiene quantity = 20,
        // QA debe reportar que updateStock() no actualiza
        // correctamente la cantidad del stock.
        // Desarrollo debe revisar updateStock() en StockServiceImpl.
    }

    @Test
    void deleteStock_debeDesactivarStockCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Stock stockExistente =
                Stock.builder()
                        .id(id)
                        .productId(1L)
                        .quantity(20)
                        .available(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(stockExistente));

        when(repository.save(any(Stock.class)))
                .thenReturn(stockExistente);

        // ACT: ejecutar método del service.
        service.deleteStock(id);

        // ASSERT: verificar que el stock quedó no disponible.
        assertFalse(stockExistente.getAvailable());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);
        verify(repository).save(stockExistente);

        // Caso hipotético de falla para QA:
        // Si se esperaba available = false y se obtiene available = true,
        // QA debe reportar que deleteStock() no desactiva
        // correctamente el stock.
        // Desarrollo debe revisar deleteStock() en StockServiceImpl.
    }
}