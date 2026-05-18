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
            UserProfileRequestDTO dto){

        log.info("POST perfil usuario");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<UserProfileResponseDTO>>
    getAll(){

        log.info("GET perfiles");

        return ResponseEntity.ok(
                service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponseDTO>
    getById(@PathVariable Long id){

        log.info("GET perfil ID {}", id);

        return ResponseEntity.ok(
                service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponseDTO>
    update(
            @PathVariable Long id,
            @Valid @RequestBody
            UserProfileRequestDTO dto){

        log.info("PUT perfil ID {}", id);

        return ResponseEntity.ok(
                service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    delete(@PathVariable Long id){

        log.info("DELETE perfil ID {}", id);

        service.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}