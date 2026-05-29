package example.ms_auth.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_auth.dto.RoleRequestDTO;
import example.ms_auth.dto.RoleResponseDTO;
import example.ms_auth.model.Role;
import example.ms_auth.repository.RoleRepository;
import example.ms_auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO dto) {

        log.info("Creando rol: {}", dto.getName());

        if (repository.existsByName(dto.getName())) {
            log.warn("Intento de crear rol duplicado: {}", dto.getName());
            throw new IllegalArgumentException(
                    "Ya existe un rol con el nombre: " + dto.getName());
        }

        Role role = Role.builder()
                .name(dto.getName())
                .build();

        Role saved = repository.save(role);

        log.info("Rol creado correctamente con ID: {}", saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {

        log.info("Listando roles");

        List<RoleResponseDTO> roles = repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();

        log.info("Roles encontrados: {}", roles.size());

        return roles;
    }

    private RoleResponseDTO mapToDTO(Role role) {

        return RoleResponseDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}