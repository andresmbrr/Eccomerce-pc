package example.ms_notificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser mayor a 0")
    private Long userId;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 120, message = "El título debe tener entre 3 y 120 caracteres")
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 5, max = 500, message = "El mensaje debe tener entre 5 y 500 caracteres")
    private String mensaje;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(min = 3, max = 50, message = "El tipo debe tener entre 3 y 50 caracteres")
    private String tipo;
}