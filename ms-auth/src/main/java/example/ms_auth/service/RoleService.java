package example.ms_auth.service;

import java.util.List;

import example.ms_auth.dto.RoleRequestDTO;
import example.ms_auth.dto.RoleResponseDTO;

public interface RoleService {

    RoleResponseDTO createRole(RoleRequestDTO dto);

    List<RoleResponseDTO> getAllRoles();
}