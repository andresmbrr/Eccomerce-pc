package example.ms_auth.dto;

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
public class RoleResponseDTO {

    @NotNull(message = "El ID del rol no puede ser nulo.")
    private Long id;

    @NotBlank(message = "El nombre del rol es obligatorio y no puede estar vacío.")
    private String name;
}