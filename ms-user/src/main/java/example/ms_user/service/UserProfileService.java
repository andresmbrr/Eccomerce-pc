package example.ms_user.service;

import java.util.List;

import example.ms_user.dto.UserProfileRequestDTO;
import example.ms_user.dto.UserProfileResponseDTO;

public interface UserProfileService {

    UserProfileResponseDTO create(
            UserProfileRequestDTO dto);

    List<UserProfileResponseDTO> getAll();

    UserProfileResponseDTO getById(Long id);

    UserProfileResponseDTO update(
            Long id,
            UserProfileRequestDTO dto);

    void delete(Long id);
}