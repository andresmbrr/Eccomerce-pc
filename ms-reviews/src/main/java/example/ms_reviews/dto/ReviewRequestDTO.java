package example.ms_reviews.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {

    @NotNull(message = "El userId es obligatorio")
    private Long userId;

    @NotNull(message = "El productId es obligatorio")
    private Long productId;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1,
            message = "La calificación mínima es 1")
    @Max(value = 5,
            message = "La calificación máxima es 5")
    private Integer rating;

    @NotBlank(message = "El comentario es obligatorio")
    @Size(max = 500,
            message = "Máximo 500 caracteres")
    private String comentario;
}