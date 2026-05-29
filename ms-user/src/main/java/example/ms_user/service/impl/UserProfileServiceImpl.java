package example.ms_user.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_user.dto.UserProfileRequestDTO;
import example.ms_user.dto.UserProfileResponseDTO;
import example.ms_user.exception.ResourceNotFoundException;
import example.ms_user.model.UserProfile;
import example.ms_user.repository.UserProfileRepository;
import example.ms_user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl
        implements UserProfileService {

    private final UserProfileRepository repository;

    @Override
    public UserProfileResponseDTO create(
            UserProfileRequestDTO dto) {

        log.info("Creando perfil para authUserId: {}",
                dto.getAuthUserId());

        UserProfile profile = UserProfile.builder()
                .authUserId(dto.getAuthUserId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .birthDate(dto.getBirthDate())
                .active(true)
                .build();

        UserProfile saved = repository.save(profile);

        log.info("Perfil creado correctamente con ID: {}",
                saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<UserProfileResponseDTO> getAll() {

        log.info("Listando perfiles activos");

        List<UserProfileResponseDTO> profiles =
                repository.findAll()
                        .stream()
                        .filter(profile ->
                                Boolean.TRUE.equals(profile.getActive()))
                        .map(this::mapToDTO)
                        .toList();

        log.info("Perfiles activos encontrados: {}",
                profiles.size());

        return profiles;
    }

    @Override
    public UserProfileResponseDTO getById(Long id) {

        log.info("Buscando perfil con ID: {}",
                id);

        UserProfile profile = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Perfil no encontrado con ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Perfil no encontrado con ID: " + id);
                });

        return mapToDTO(profile);
    }

    @Override
    public UserProfileResponseDTO update(
            Long id,
            UserProfileRequestDTO dto) {

        log.info("Actualizando perfil con ID: {}",
                id);

        UserProfile profile = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("No se pudo actualizar. Perfil no encontrado con ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Perfil no encontrado con ID: " + id);
                });

        profile.setAuthUserId(dto.getAuthUserId());
        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setPhone(dto.getPhone());
        profile.setAddress(dto.getAddress());
        profile.setBirthDate(dto.getBirthDate());

        if (dto.getActive() != null) {
            profile.setActive(dto.getActive());
        }

        UserProfile updated = repository.save(profile);

        log.info("Perfil actualizado correctamente con ID: {}",
                updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void delete(Long id) {

        log.info("Eliminando lógicamente perfil con ID: {}",
                id);

        UserProfile profile = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("No se pudo eliminar. Perfil no encontrado con ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Perfil no encontrado con ID: " + id);
                });

        profile.setActive(false);
        repository.save(profile);

        log.info("Perfil desactivado correctamente con ID: {}",
                id);
    }

    private UserProfileResponseDTO mapToDTO(
            UserProfile profile) {

        return UserProfileResponseDTO.builder()
                .id(profile.getId())
                .authUserId(profile.getAuthUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phone(profile.getPhone())
                .address(profile.getAddress())
                .birthDate(profile.getBirthDate())
                .active(profile.getActive())
                .build();
    }
}