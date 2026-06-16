package example.ms_notificaciones.controller;

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

import example.ms_notificaciones.dto.NotificacionRequestDTO;
import example.ms_notificaciones.dto.NotificacionResponseDTO;
import example.ms_notificaciones.service.NotificacionService;
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
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Notificaciones",
        description = "Endpoints para administrar las notificaciones enviadas a los usuarios del ecommerce"
)
public class NotificacionController {

    private final NotificacionService service;

    @PostMapping
    @Operation(
            summary = "Crear notificación",
            description = "Permite registrar una nueva notificación asociada a un usuario. Puede representar mensajes de pago, pedido, envío u otros eventos del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Notificación creada correctamente",
                    content = @Content(schema = @Schema(implementation = NotificacionResponseDTO.class))
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
    public ResponseEntity<NotificacionResponseDTO>
    crearNotificacion(
            @Valid @RequestBody NotificacionRequestDTO dto) {

        log.info("POST /api/notificaciones - Creando notificación para usuario {}",
                dto.getUserId());

        NotificacionResponseDTO response =
                service.crearNotificacion(dto);

        log.info("Notificación creada con ID: {}",
                response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar notificaciones",
            description = "Obtiene todas las notificaciones activas registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notificaciones obtenidas correctamente",
                    content = @Content(schema = @Schema(implementation = NotificacionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<NotificacionResponseDTO>>
    listarNotificaciones() {

        log.info("GET /api/notificaciones - Listando notificaciones");

        List<NotificacionResponseDTO> response =
                service.listarNotificaciones();

        log.info("Notificaciones encontradas: {}",
                response.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar notificación por ID",
            description = "Busca una notificación específica utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notificación encontrada correctamente",
                    content = @Content(schema = @Schema(implementation = NotificacionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notificación no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<NotificacionResponseDTO>
    buscarPorId(@PathVariable Long id) {

        log.info("GET /api/notificaciones/{} - Buscando por ID",
                id);

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @GetMapping("/usuario/{userId}")
    @Operation(
            summary = "Buscar notificaciones por usuario",
            description = "Obtiene todas las notificaciones activas asociadas a un usuario específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notificaciones del usuario obtenidas correctamente",
                    content = @Content(schema = @Schema(implementation = NotificacionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario o notificaciones no encontradas",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<NotificacionResponseDTO>>
    buscarPorUsuario(
            @PathVariable Long userId) {

        log.info("GET /api/notificaciones/usuario/{} - Buscando por usuario",
                userId);

        return ResponseEntity.ok(
                service.buscarPorUsuario(userId));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar notificación",
            description = "Actualiza los datos de una notificación existente, incluyendo usuario, título, mensaje y tipo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notificación actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = NotificacionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notificación no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<NotificacionResponseDTO>
    actualizarNotificacion(
            @PathVariable Long id,
            @Valid @RequestBody NotificacionRequestDTO dto) {

        log.info("PUT /api/notificaciones/{} - Actualizando notificación",
                id);

        NotificacionResponseDTO response =
                service.actualizarNotificacion(id, dto);

        log.info("Notificación actualizada con ID: {}",
                response.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar notificación",
            description = "Realiza una eliminación lógica de la notificación, dejando el registro inactivo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Notificación eliminada correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notificación no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<Void>
    eliminarNotificacion(@PathVariable Long id) {

        log.info("DELETE /api/notificaciones/{} - Eliminando notificación lógica",
                id);

        service.eliminarNotificacion(id);

        log.info("Notificación eliminada correctamente ID: {}",
                id);

        return ResponseEntity.noContent().build();
    }
}