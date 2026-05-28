package example.ms_auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserResponseDTO {


    @NotNull(message = "El ID de usuario no puede ser nulo.")
    private Long id;
    
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    private String username;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El correo electrónico debe tener un formato válido.")
    private String email;

    @NotBlank(message = "El rol asignado al usuario es obligatorio.")
    private String role;
}