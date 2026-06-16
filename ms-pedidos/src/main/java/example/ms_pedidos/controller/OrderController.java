package example.ms_pedidos.controller;

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

import example.ms_pedidos.dto.OrderRequestDTO;
import example.ms_pedidos.dto.OrderResponseDTO;
import example.ms_pedidos.dto.OrderStatusRequestDTO;
import example.ms_pedidos.service.OrderService;
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
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Pedidos",
        description = "Endpoints para administrar pedidos del ecommerce, incluyendo creación, actualización, consulta y cambio de estado"
)
public class OrderController {

    private final OrderService service;

    @PostMapping
    @Operation(
            summary = "Crear pedido",
            description = "Permite registrar un nuevo pedido asociado a un usuario. El pedido se crea inicialmente con estado PENDING."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido creado correctamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
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
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO dto) {

        log.info("POST /api/pedidos - Creando pedido para usuario {}",
                dto.getUserId());

        OrderResponseDTO response = service.createOrder(dto);

        log.info("Pedido creado con ID: {}", response.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar pedidos",
            description = "Obtiene todos los pedidos activos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {

        log.info("GET /api/pedidos - Listando pedidos");

        List<OrderResponseDTO> response = service.getAllOrders();

        log.info("Pedidos encontrados: {}", response.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar pedido por ID",
            description = "Busca un pedido específico utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado correctamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable Long id) {

        log.info("GET /api/pedidos/{} - Buscando pedido por ID", id);

        return ResponseEntity.ok(service.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Buscar pedidos por usuario",
            description = "Obtiene todos los pedidos activos asociados a un usuario específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos del usuario obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedidos no encontrados para el usuario indicado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(
            @PathVariable Long userId) {

        log.info("GET /api/pedidos/user/{} - Buscando pedidos por usuario", userId);

        return ResponseEntity.ok(service.getOrdersByUserId(userId));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar pedido",
            description = "Actualiza los datos principales de un pedido existente, como usuario y total."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequestDTO dto) {

        log.info("PUT /api/pedidos/{} - Actualizando pedido", id);

        return ResponseEntity.ok(service.updateOrder(id, dto));
    }

    @PutMapping("/{id}/status")
    @Operation(
            summary = "Actualizar estado del pedido",
            description = "Permite cambiar el estado de un pedido. Estados disponibles: PENDING, PAID, CANCELLED, SHIPPED y DELIVERED."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado del pedido actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Estado inválido o error de validación",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusRequestDTO dto) {

        log.info("PUT /api/pedidos/{}/status - Actualizando estado", id);

        return ResponseEntity.ok(service.updateStatus(id, dto.getStatus()));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar pedido",
            description = "Realiza una eliminación lógica del pedido, dejando el registro inactivo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Pedido eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long id) {

        log.info("DELETE /api/pedidos/{} - Eliminando pedido lógico", id);

        service.deleteOrder(id);

        log.info("Pedido eliminado correctamente ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}