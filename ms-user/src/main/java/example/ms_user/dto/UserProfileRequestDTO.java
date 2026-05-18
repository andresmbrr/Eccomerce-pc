package example.ms_user.dto;


import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequestDTO {

    @NotNull(message = "ID usuario obligatorio")
    private Long authUserId;

    @NotBlank(message = "Nombre obligatorio")
    private String firstName;

    @NotBlank(message = "Apellido obligatorio")
    private String lastName;

    @NotBlank(message = "Teléfono obligatorio")
    private String phone;

    @NotBlank(message = "Dirección obligatoria")
    private String address;

    @NotNull(message = "Fecha nacimiento obligatoria")
    private LocalDate birthDate;

    @NotNull(message = "Estado obligatorio")
    private Boolean active;
}