package example.ms_auth.controller;

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

import example.ms_auth.dto.LoginRequestDTO;
import example.ms_auth.dto.LoginResponseDTO;
import example.ms_auth.dto.UserRequestDTO;
import example.ms_auth.dto.UserResponseDTO;
import example.ms_auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO dto) {

        log.info("POST /api/auth/register - Registrando usuario: {}",
                dto.getEmail());

        UserResponseDTO response =
                userService.createUser(dto);

        log.info("Usuario registrado correctamente con ID: {}",
                response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        log.info("POST /api/auth/login - Intento de login: {}",
                dto.getEmail());

        LoginResponseDTO response =
                userService.login(dto);

        log.info("Login exitoso para usuario: {}",
                dto.getEmail());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        log.info("GET /api/auth/users - Listando usuarios");

        List<UserResponseDTO> users =
                userService.getAllUsers();

        log.info("Usuarios encontrados: {}",
                users.size());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {

        log.info("GET /api/auth/users/{} - Buscando usuario por ID",
                id);

        return ResponseEntity.ok(
                userService.getUserById(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {

        log.info("PUT /api/auth/users/{} - Actualizando usuario",
                id);

        UserResponseDTO response =
                userService.updateUser(id, dto);

        log.info("Usuario actualizado correctamente con ID: {}",
                response.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        log.info("DELETE /api/auth/users/{} - Eliminando usuario",
                id);

        userService.deleteUser(id);

        log.info("Usuario eliminado correctamente con ID: {}",
                id);

        return ResponseEntity.noContent().build();
    }
}