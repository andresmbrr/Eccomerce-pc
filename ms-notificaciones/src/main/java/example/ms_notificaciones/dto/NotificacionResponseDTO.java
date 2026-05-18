package example.ms_notificaciones.dto;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionResponseDTO {

    private Long id;

    private Long userId;

    private String titulo;

    private String mensaje;

    private String tipo;

    private Boolean enviado;

    private LocalDateTime fechaEnvio;
}