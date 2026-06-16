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
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Stock",
        description = "Endpoints para administrar el inventario y disponibilidad de productos"
)
public class StockController {

    private final StockService service;

    @PostMapping
    @Operation(
            summary = "Crear stock",
            description = "Permite registrar stock para un producto específico, indicando cantidad disponible y estado de disponibilidad."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Stock creado correctamente",
                    content = @Content(schema = @Schema(implementation = StockResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe stock registrado para ese producto",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Listar stock",
            description = "Obtiene todos los registros de stock activos del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock obtenido correctamente",
                    content = @Content(schema = @Schema(implementation = StockResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Buscar stock por ID",
            description = "Busca un registro de stock específico utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock encontrado correctamente",
                    content = @Content(schema = @Schema(implementation = StockResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Buscar stock por producto",
            description = "Obtiene el stock asociado a un producto específico utilizando el ID del producto."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock del producto obtenido correctamente",
                    content = @Content(schema = @Schema(implementation = StockResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock no encontrado para el producto indicado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Actualizar stock",
            description = "Actualiza la cantidad disponible y el estado de disponibilidad de un registro de stock existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = StockResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Eliminar stock",
            description = "Realiza una eliminación lógica del stock, dejando el registro no disponible o inactivo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Stock eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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