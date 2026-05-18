package example.ms_stock.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_stock.dto.StockRequestDTO;
import example.ms_stock.dto.StockResponseDTO;
import example.ms_stock.exception.ResourceNotFoundException;
import example.ms_stock.model.Stock;
import example.ms_stock.repository.StockRepository;
import example.ms_stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl
        implements StockService {

    private final StockRepository repository;

    @Override
    public StockResponseDTO createStock(
            StockRequestDTO dto) {

        log.info("Creando stock producto {}",
                dto.getProductId());

        Stock stock = Stock.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .available(dto.getQuantity() > 0)
                .build();

        Stock saved = repository.save(stock);

        log.info("Stock creado ID {}",
                saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<StockResponseDTO> getAllStock() {

        log.info("Listando stock");

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public StockResponseDTO getStockById(Long id) {

        log.info("Buscando stock ID {}", id);

        Stock stock = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Stock no encontrado"));

        return mapToDTO(stock);
    }

    @Override
    public StockResponseDTO getStockByProductId(
            Long productId) {

        log.info("Buscando stock producto {}",
                productId);

        Stock stock = repository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Stock no encontrado"));

        return mapToDTO(stock);
    }

    @Override
    public StockResponseDTO updateStock(
            Long id,
            StockRequestDTO dto) {

        log.info("Actualizando stock ID {}", id);

        Stock stock = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Stock no encontrado"));

        stock.setProductId(dto.getProductId());
        stock.setQuantity(dto.getQuantity());
        stock.setAvailable(dto.getQuantity() > 0);

        Stock updated = repository.save(stock);

        log.info("Stock actualizado ID {}",
                updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void deleteStock(Long id) {

        log.info("Eliminando stock ID {}", id);

        Stock stock = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Stock no encontrado"));

        repository.delete(stock);

        log.info("Stock eliminado ID {}", id);
    }

    private StockResponseDTO mapToDTO(
            Stock stock){

        return StockResponseDTO.builder()
                .id(stock.getId())
                .productId(stock.getProductId())
                .quantity(stock.getQuantity())
                .available(stock.getAvailable())
                .build();
    }
}