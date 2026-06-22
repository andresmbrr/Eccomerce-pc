package example.ms_pagos.controller;

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
import example.ms_pagos.dto.PaymentRequestDTO;
import example.ms_pagos.dto.PaymentResponseDTO;
import example.ms_pagos.service.PaymentService;
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
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Pagos",
        description = "Endpoints para administrar pagos del ecommerce. Este microservicio se comunica mediante Feign con ms-pedidos."
)
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    @Operation(
            summary = "Crear pago",
            description = "Permite registrar un pago asociado a un pedido. Antes de crear el pago, el microservicio valida el pedido mediante comunicación Feign con ms-pedidos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pago creado correctamente",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido o recurso no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<PaymentResponseDTO>
    createPayment(
            @Valid @RequestBody PaymentRequestDTO dto) {

        log.info("POST /api/pagos - Procesando pago para pedido ID: {}",
                dto.getOrderId());

        PaymentResponseDTO response =
                service.createPayment(dto);

        log.info("Pago creado correctamente con ID: {}",
                response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar pagos",
            description = "Obtiene todos los pagos activos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pagos obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<PaymentResponseDTO>>
    getAllPayments() {

        log.info("GET /api/pagos - Listando pagos");

        List<PaymentResponseDTO> payments =
                service.getAllPayments();

        log.info("Pagos encontrados: {}",
                payments.size());

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar pago por ID",
            description = "Busca un pago específico utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago encontrado correctamente",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<PaymentResponseDTO>
    getPaymentById(@PathVariable Long id) {

        log.info("GET /api/pagos/{} - Buscando pago por ID",
                id);

        return ResponseEntity.ok(
                service.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    @Operation(
            summary = "Buscar pagos por pedido",
            description = "Obtiene todos los pagos asociados a un pedido específico mediante el ID del pedido."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pagos del pedido obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido o pagos no encontrados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<PaymentResponseDTO>>
    getPaymentsByOrderId(
            @PathVariable Long orderId) {

        log.info("GET /api/pagos/order/{} - Buscando pagos por pedido",
                orderId);

        return ResponseEntity.ok(
                service.getPaymentsByOrderId(orderId));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar pago",
            description = "Actualiza la información de un pago existente, incluyendo pedido, monto y método de pago."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago o pedido no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<PaymentResponseDTO>
    updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequestDTO dto) {

        log.info("PUT /api/pagos/{} - Actualizando pago",
                id);

        PaymentResponseDTO response =
                service.updatePayment(id, dto);

        log.info("Pago actualizado correctamente con ID: {}",
                response.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar pago",
            description = "Realiza una eliminación lógica del pago, dejando el registro inactivo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Pago eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<Void>
    deletePayment(@PathVariable Long id) {

        log.info("DELETE /api/pagos/{} - Eliminando pago lógico",
                id);

        service.deletePayment(id);

        log.info("Pago eliminado correctamente con ID: {}",
                id);

        return ResponseEntity.noContent().build();
    }
        
        }

