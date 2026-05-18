package example.ms_notificaciones.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import example.ms_notificaciones.model.Notificacion;

public interface NotificacionRepository
        extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUserId(Long userId);
}