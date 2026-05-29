package example.ms_user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.ms_user.dto.UserProfileRequestDTO;
import example.ms_user.dto.UserProfileResponseDTO;
import example.ms_user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {

    private final UserProfileService service;

    @PostMapping
    public ResponseEntity<UserProfileResponseDTO>
    create(
            @Valid @RequestBody
            UserProfileRequestDTO dto) {

        log.info("POST /api/users - Creando perfil para authUserId: {}",
                dto.getAuthUserId());

        UserProfileResponseDTO response =
                service.create(dto);

        log.info("Perfil creado con ID: {}",
                response.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserProfileResponseDTO>>
    getAll() {

        log.info("GET /api/users - Listando perfiles");

        List<UserProfileResponseDTO> response =
                service.getAll();

        log.info("Perfiles encontrados: {}",
                response.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponseDTO>
    getById(@PathVariable Long id) {

        log.info("GET /api/users/{} - Buscando perfil por ID",
                id);

        return ResponseEntity.ok(
                service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponseDTO>
    update(
            @PathVariable Long id,
            @Valid @RequestBody
            UserProfileRequestDTO dto) {

        log.info("PUT /api/users/{} - Actualizando perfil",
                id);

        UserProfileResponseDTO response =
                service.update(id, dto);

        log.info("Perfil actualizado con ID: {}",
                response.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    delete(@PathVariable Long id) {

        log.info("DELETE /api/users/{} - Eliminando perfil lógico",
                id);

        service.delete(id);

        log.info("Perfil eliminado correctamente ID: {}",
                id);

        return ResponseEntity
                .noContent()
                .build();
    }
}