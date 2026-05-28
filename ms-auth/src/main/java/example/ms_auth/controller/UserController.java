package example.ms_auth.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated; // <-- IMPORTANTE
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
@RequestMapping("/api/auth") // <-- RECOMENDACIÓN: Dejar la ruta base más genérica
@RequiredArgsConstructor
@Validated // <-- IMPORTANTE: Permite validar respuestas (LoginResponseDTO) si así lo deseas
@Slf4j
public class UserController {

    private final UserService userService;

    // 1. Registro de usuarios (Se usa /api/auth/register)
    @PostMapping("/register") 
    public ResponseEntity<@Valid UserResponseDTO> createUser(
        @Valid @RequestBody UserRequestDTO dto) {

        log.info("POST /api/auth/register ejecutado");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(dto));
    }

    // 2. Login de usuarios (¡Aquí faltaba el @Valid!)
    @PostMapping("/login")
    public ResponseEntity<@Valid LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) { 
        
        log.info("POST /api/auth/login ejecutado");

        return ResponseEntity.ok(userService.login(dto));
    }

    // 3. Obtener todos los usuarios
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        log.info("GET /api/auth/users ejecutado");

        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 4. Obtener usuario por ID
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {

        log.info("GET /api/auth/users/{} ejecutado", id);

        return ResponseEntity.ok(userService.getUserById(id));
    }

    // 5. Actualizar usuario
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {

        log.info("PUT /api/auth/users/{} ejecutado", id);

        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    // 6. Eliminar usuario
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        log.info("DELETE /api/auth/users/{} ejecutado", id);

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}