package example.ms_user.dto;
import java.time.LocalDate;

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
public class UserProfileResponseDTO {

    private Long id;

    private Long authUserId;

    private String firstName;

    private String lastName;

    private String phone;

    private String address;

    private LocalDate birthDate;

    private Boolean active;
}