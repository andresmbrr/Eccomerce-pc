package example.ms_notificaciones.service;
import java.util.List;

import example.ms_notificaciones.dto.NotificacionRequestDTO;
import example.ms_notificaciones.dto.NotificacionResponseDTO;

public interface NotificacionService {

    NotificacionResponseDTO crearNotificacion(
            NotificacionRequestDTO dto);

    List<NotificacionResponseDTO> listarNotificaciones();

    NotificacionResponseDTO buscarPorId(Long id);

    List<NotificacionResponseDTO> buscarPorUsuario(
            Long userId);

    NotificacionResponseDTO actualizarNotificacion(
            Long id,
            NotificacionRequestDTO dto);

    void eliminarNotificacion(Long id);
}