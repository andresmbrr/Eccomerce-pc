package example.ms_notificaciones.service.impl;
import example.ms_notificaciones.dto.*;

import example.ms_notificaciones.exception.NotificacionNotFoundException;

import example.ms_notificaciones.model.Notificacion;

import example.ms_notificaciones.repository.NotificacionRepository;

import example.ms_notificaciones.service.NotificacionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionServiceImpl
        implements NotificacionService {

    private final NotificacionRepository repository;

    @Override
    public NotificacionResponseDTO crearNotificacion(
            NotificacionRequestDTO dto) {

        log.info("Creando notificación usuario {}",
                dto.getUserId());

        Notificacion notificacion =
                Notificacion.builder()
                        .userId(dto.getUserId())
                        .titulo(dto.getTitulo())
                        .mensaje(dto.getMensaje())
                        .tipo(dto.getTipo())
                        .enviado(true)
                        .fechaEnvio(LocalDateTime.now())
                        .build();

        Notificacion saved =
                repository.save(notificacion);

        return mapToDTO(saved);
    }

    @Override
    public List<NotificacionResponseDTO>
    listarNotificaciones() {

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public NotificacionResponseDTO buscarPorId(
            Long id) {

        Notificacion notificacion =
                repository.findById(id)
                        .orElseThrow(() ->
                                new NotificacionNotFoundException(
                                        "Notificación no encontrada"));

        return mapToDTO(notificacion);
    }

    @Override
    public List<NotificacionResponseDTO>
    buscarPorUsuario(Long userId) {

        return repository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public NotificacionResponseDTO
    actualizarNotificacion(
            Long id,
            NotificacionRequestDTO dto) {

        Notificacion notificacion =
                repository.findById(id)
                        .orElseThrow(() ->
                                new NotificacionNotFoundException(
                                        "Notificación no encontrada"));

        notificacion.setUserId(dto.getUserId());
        notificacion.setTitulo(dto.getTitulo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setTipo(dto.getTipo());

        Notificacion updated =
                repository.save(notificacion);

        return mapToDTO(updated);
    }

    @Override
    public void eliminarNotificacion(Long id) {

        Notificacion notificacion =
                repository.findById(id)
                        .orElseThrow(() ->
                                new NotificacionNotFoundException(
                                        "Notificación no encontrada"));

        repository.delete(notificacion);

        log.info("Notificación eliminada ID {}",
                id);
    }

    private NotificacionResponseDTO mapToDTO(
            Notificacion notificacion){

        return NotificacionResponseDTO.builder()
                .id(notificacion.getId())
                .userId(notificacion.getUserId())
                .titulo(notificacion.getTitulo())
                .mensaje(notificacion.getMensaje())
                .tipo(notificacion.getTipo())
                .enviado(notificacion.getEnviado())
                .fechaEnvio(notificacion.getFechaEnvio())
                .build();
    }
}