package example.ms_productos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean active;
}