package example.ms_stock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_stock.dto.StockRequestDTO;
import example.ms_stock.dto.StockResponseDTO;
import example.ms_stock.exception.StockAlreadyExistsException;
import example.ms_stock.exception.StockNotFoundException;
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

        log.info("Creando stock para producto ID: {}",
                dto.getProductId());

        if (repository.existsByProductId(
                dto.getProductId())) {

            log.warn("Ya existe stock para producto ID: {}",
                    dto.getProductId());

            throw new StockAlreadyExistsException(
                    "Ya existe stock para este producto");
        }

        Stock stock = Stock.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .available(true)
                .build();

        Stock saved = repository.save(stock);

        log.info("Stock creado correctamente con ID: {}",
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
    public StockResponseDTO getStockById(
            Long id) {

        log.info("Buscando stock con ID: {}",
                id);

        Stock stock = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Stock no encontrado con ID: {}",
                            id);

                    return new StockNotFoundException(
                            "Stock no encontrado con ID: " + id);
                });

        return mapToDTO(stock);
    }

    @Override
    public StockResponseDTO getStockByProductId(
            Long productId) {

        log.info("Buscando stock para producto ID: {}",
                productId);

        Stock stock = repository.findByProductId(productId)
                .orElseThrow(() -> {

                    log.warn("Stock no encontrado para producto ID: {}",
                            productId);

                    return new StockNotFoundException(
                            "Stock no encontrado para producto ID: " + productId);
                });

        return mapToDTO(stock);
    }

    @Override
    public StockResponseDTO updateStock(
            Long id,
            StockRequestDTO dto) {

        log.info("Actualizando stock con ID: {}",
                id);

        Stock stock = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("No se pudo actualizar. Stock no encontrado con ID: {}",
                            id);

                    return new StockNotFoundException(
                            "Stock no encontrado con ID: " + id);
                });

        stock.setProductId(dto.getProductId());
        stock.setQuantity(dto.getQuantity());

        if (dto.getAvailable() != null) {
            stock.setAvailable(dto.getAvailable());
        }

        Stock updated = repository.save(stock);

        log.info("Stock actualizado correctamente con ID: {}",
                updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void deleteStock(Long id) {

        log.info("Eliminando stock lógico con ID: {}",
                id);

        Stock stock = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("No se pudo eliminar. Stock no encontrado con ID: {}",
                            id);

                    return new StockNotFoundException(
                            "Stock no encontrado con ID: " + id);
                });

        stock.setAvailable(false);

        repository.save(stock);

        log.info("Stock desactivado correctamente con ID: {}",
                id);
    }

    private StockResponseDTO mapToDTO(
            Stock stock) {

        return StockResponseDTO.builder()
                .id(stock.getId())
                .productId(stock.getProductId())
                .quantity(stock.getQuantity())
                .available(stock.getAvailable())
                .build();
    }
}