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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService service;

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(
            @Valid @RequestBody RoleRequestDTO dto){

        log.info("POST /api/auth/roles ejecutado");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createRole(dto));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles(){

        log.info("GET /api/auth/roles ejecutado");

        return ResponseEntity.ok(
                service.getAllRoles());
    }
}