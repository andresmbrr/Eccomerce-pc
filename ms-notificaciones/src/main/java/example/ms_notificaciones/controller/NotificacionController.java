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
            @Valid @RequestBody NotificacionRequestDTO dto){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearNotificacion(dto));
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>>
    listarNotificaciones(){

        return ResponseEntity.ok(
                service.listarNotificaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO>
    buscarPorId(@PathVariable Long id){

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<NotificacionResponseDTO>>
    buscarPorUsuario(
            @PathVariable Long userId){

        return ResponseEntity.ok(
                service.buscarPorUsuario(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO>
    actualizarNotificacion(
            @PathVariable Long id,
            @Valid @RequestBody NotificacionRequestDTO dto){

        return ResponseEntity.ok(
                service.actualizarNotificacion(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    eliminarNotificacion(@PathVariable Long id){

        service.eliminarNotificacion(id);

        return ResponseEntity.noContent().build();
    }
}