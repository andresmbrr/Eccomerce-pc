package example.ms_auth.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.ms_auth.dto.RoleRequestDTO;
import example.ms_auth.dto.RoleResponseDTO;
import example.ms_auth.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(
            @Valid @RequestBody RoleRequestDTO dto){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createRole(dto));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles(){

        return ResponseEntity.ok(service.getAllRoles());
    }
}