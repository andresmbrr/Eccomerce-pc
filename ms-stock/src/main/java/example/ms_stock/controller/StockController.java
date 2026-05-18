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
            @Valid @RequestBody StockRequestDTO dto){

        log.info("POST /api/stock ejecutado");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createStock(dto));
    }

    @GetMapping
    public ResponseEntity<List<StockResponseDTO>>
    getAllStock(){

        log.info("GET /api/stock ejecutado");

        return ResponseEntity.ok(
                service.getAllStock());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDTO>
    getStockById(@PathVariable Long id){

        log.info("GET /api/stock/{} ejecutado", id);

        return ResponseEntity.ok(
                service.getStockById(id));
    }

  @GetMapping("/product/{productId}")
public ResponseEntity<StockResponseDTO>
getStockByProductId(
        @PathVariable("productId") Long productId){

    log.info("GET stock producto {}",
            productId);

    return ResponseEntity.ok(
            service.getStockByProductId(productId));
}

    @PutMapping("/{id}")
    public ResponseEntity<StockResponseDTO>
    updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockRequestDTO dto){

        log.info("PUT /api/stock/{} ejecutado", id);

        return ResponseEntity.ok(
                service.updateStock(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteStock(@PathVariable Long id){

        log.info("DELETE /api/stock/{} ejecutado", id);

        service.deleteStock(id);

        return ResponseEntity.noContent().build();
    }
}