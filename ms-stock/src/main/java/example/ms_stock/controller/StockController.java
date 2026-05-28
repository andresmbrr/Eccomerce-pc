package example.ms_stock.controller;

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

import example.ms_stock.dto.StockRequestDTO;
import example.ms_stock.dto.StockResponseDTO;
import example.ms_stock.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockService service;

    @PostMapping
    public ResponseEntity<StockResponseDTO>
    createStock(
            @Valid @RequestBody StockRequestDTO dto) {

        log.info("POST /api/stock - Creando stock para producto ID: {}",
                dto.getProductId());

        StockResponseDTO response =
                service.createStock(dto);

        log.info("Stock creado correctamente con ID: {}",
                response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<StockResponseDTO>>
    getAllStock() {

        log.info("GET /api/stock - Listando stock");

        List<StockResponseDTO> stockList =
                service.getAllStock();

        log.info("Cantidad de registros encontrados: {}",
                stockList.size());

        return ResponseEntity.ok(stockList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDTO>
    getStockById(
            @PathVariable Long id) {

        log.info("GET /api/stock/{} - Buscando stock por ID",
                id);

        StockResponseDTO response =
                service.getStockById(id);

        log.info("Stock encontrado con ID: {}",
                response.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<StockResponseDTO>
    getStockByProductId(
            @PathVariable Long productId) {

        log.info("GET /api/stock/product/{} - Buscando stock por producto",
                productId);

        StockResponseDTO response =
                service.getStockByProductId(productId);

        log.info("Stock encontrado para producto ID: {}",
                productId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockResponseDTO>
    updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockRequestDTO dto) {

        log.info("PUT /api/stock/{} - Actualizando stock",
                id);

        StockResponseDTO response =
                service.updateStock(id, dto);

        log.info("Stock actualizado correctamente con ID: {}",
                response.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteStock(
            @PathVariable Long id) {

        log.info("DELETE /api/stock/{} - Eliminando stock lógico",
                id);

        service.deleteStock(id);

        log.info("Stock eliminado correctamente con ID: {}",
                id);

        return ResponseEntity.noContent().build();
    }
}