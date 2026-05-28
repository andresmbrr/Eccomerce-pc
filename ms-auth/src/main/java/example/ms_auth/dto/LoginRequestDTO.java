package example.ms_auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "Por favor, introduce un formato de correo válido.")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía.")
    private String password;
}