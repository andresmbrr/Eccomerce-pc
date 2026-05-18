package example.ms_auth.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_auth.dto.RoleRequestDTO;
import example.ms_auth.dto.RoleResponseDTO;
import example.ms_auth.model.Role;
import example.ms_auth.repository.RoleRepository;
import example.ms_auth.service.RoleService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO dto) {

        Role role = Role.builder()
                .name(dto.getName())
                .build();

        Role saved = repository.save(role);

        return RoleResponseDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {

        return repository.findAll()
                .stream()
                .map(role -> RoleResponseDTO.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .build())
                .toList();
    }
    
}