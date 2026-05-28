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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Slf4j
public class NotificacionController {

    private final NotificacionService service;

    @PostMapping
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
    public ResponseEntity<NotificacionResponseDTO>
    buscarPorId(@PathVariable Long id) {

        log.info("GET /api/notificaciones/{} - Buscando por ID",
                id);

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<NotificacionResponseDTO>>
    buscarPorUsuario(
            @PathVariable Long userId) {

        log.info("GET /api/notificaciones/usuario/{} - Buscando por usuario",
                userId);

        return ResponseEntity.ok(
                service.buscarPorUsuario(userId));
    }

    @PutMapping("/{id}")
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