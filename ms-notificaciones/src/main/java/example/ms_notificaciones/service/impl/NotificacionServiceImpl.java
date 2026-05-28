package example.ms_notificaciones.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_notificaciones.dto.NotificacionRequestDTO;
import example.ms_notificaciones.dto.NotificacionResponseDTO;
import example.ms_notificaciones.exception.NotificacionNotFoundException;
import example.ms_notificaciones.model.Notificacion;
import example.ms_notificaciones.repository.NotificacionRepository;
import example.ms_notificaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionServiceImpl
        implements NotificacionService {

    private final NotificacionRepository repository;

    @Override
    public NotificacionResponseDTO crearNotificacion(
            NotificacionRequestDTO dto) {

        log.info("Creando notificación para usuario ID: {}",
                dto.getUserId());

        Notificacion notificacion =
                Notificacion.builder()
                        .userId(dto.getUserId())
                        .titulo(dto.getTitulo())
                        .mensaje(dto.getMensaje())
                        .tipo(dto.getTipo())
                        .enviado(true)
                        .fechaEnvio(LocalDateTime.now())
                        .active(true)
                        .build();

        Notificacion saved =
                repository.save(notificacion);

        log.info("Notificación creada correctamente con ID: {}",
                saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<NotificacionResponseDTO>
    listarNotificaciones() {

        log.info("Listando notificaciones activas");

        List<NotificacionResponseDTO> notificaciones =
                repository.findAll()
                        .stream()
                        .filter(notificacion ->
                                Boolean.TRUE.equals(notificacion.getActive()))
                        .map(this::mapToDTO)
                        .toList();

        log.info("Notificaciones activas encontradas: {}",
                notificaciones.size());

        return notificaciones;
    }

    @Override
    public NotificacionResponseDTO buscarPorId(
            Long id) {

        log.info("Buscando notificación con ID: {}",
                id);

        Notificacion notificacion =
                repository.findById(id)
                        .orElseThrow(() -> {

                            log.warn("Notificación no encontrada con ID: {}",
                                    id);

                            return new NotificacionNotFoundException(
                                    "Notificación no encontrada con ID: " + id);
                        });

        return mapToDTO(notificacion);
    }

    @Override
    public List<NotificacionResponseDTO>
    buscarPorUsuario(Long userId) {

        log.info("Buscando notificaciones del usuario ID: {}",
                userId);

        List<NotificacionResponseDTO> notificaciones =
                repository.findByUserId(userId)
                        .stream()
                        .filter(notificacion ->
                                Boolean.TRUE.equals(notificacion.getActive()))
                        .map(this::mapToDTO)
                        .toList();

        log.info("Notificaciones encontradas para usuario {}: {}",
                userId,
                notificaciones.size());

        return notificaciones;
    }

    @Override
    public NotificacionResponseDTO
    actualizarNotificacion(
            Long id,
            NotificacionRequestDTO dto) {

        log.info("Actualizando notificación con ID: {}",
                id);

        Notificacion notificacion =
                repository.findById(id)
                        .orElseThrow(() -> {

                            log.warn("No se pudo actualizar. Notificación no encontrada con ID: {}",
                                    id);

                            return new NotificacionNotFoundException(
                                    "Notificación no encontrada con ID: " + id);
                        });

        notificacion.setUserId(dto.getUserId());
        notificacion.setTitulo(dto.getTitulo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setTipo(dto.getTipo());

        Notificacion updated =
                repository.save(notificacion);

        log.info("Notificación actualizada correctamente con ID: {}",
                updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void eliminarNotificacion(Long id) {

        log.info("Eliminando lógicamente notificación con ID: {}",
                id);

        Notificacion notificacion =
                repository.findById(id)
                        .orElseThrow(() -> {

                            log.warn("No se pudo eliminar. Notificación no encontrada con ID: {}",
                                    id);

                            return new NotificacionNotFoundException(
                                    "Notificación no encontrada con ID: " + id);
                        });

        notificacion.setActive(false);

        repository.save(notificacion);

        log.info("Notificación desactivada correctamente con ID: {}",
                id);
    }

    private NotificacionResponseDTO mapToDTO(
            Notificacion notificacion) {

        return NotificacionResponseDTO.builder()
                .id(notificacion.getId())
                .userId(notificacion.getUserId())
                .titulo(notificacion.getTitulo())
                .mensaje(notificacion.getMensaje())
                .tipo(notificacion.getTipo())
                .enviado(notificacion.getEnviado())
                .fechaEnvio(notificacion.getFechaEnvio())
                .active(notificacion.getActive())
                .build();
    }
}