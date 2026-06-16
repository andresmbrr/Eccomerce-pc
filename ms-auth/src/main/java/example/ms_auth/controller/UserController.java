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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Autenticación y Usuarios Auth",
        description = "Endpoints para registro, login y administración de usuarios de autenticación"
)
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(
            summary = "Registrar usuario",
            description = "Registra un nuevo usuario en el microservicio de autenticación. La contraseña se almacena cifrada con BCrypt."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado correctamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Rol no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe un usuario con el mismo email",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Iniciar sesión",
            description = "Valida las credenciales de un usuario registrado y retorna la información básica del usuario autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación o contraseña incorrecta",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Listar usuarios auth",
            description = "Obtiene todos los usuarios registrados en el microservicio de autenticación."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuarios obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        log.info("GET /api/auth/users - Listando usuarios");

        List<UserResponseDTO> users =
                userService.getAllUsers();

        log.info("Usuarios encontrados: {}",
                users.size());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    @Operation(
            summary = "Buscar usuario auth por ID",
            description = "Busca un usuario de autenticación específico utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado correctamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {

        log.info("GET /api/auth/users/{} - Buscando usuario por ID",
                id);

        return ResponseEntity.ok(
                userService.getUserById(id));
    }

    @PutMapping("/users/{id}")
    @Operation(
            summary = "Actualizar usuario auth",
            description = "Actualiza los datos de un usuario de autenticación, incluyendo username, email, password y rol."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario o rol no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto por email duplicado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Eliminar usuario auth",
            description = "Elimina un usuario del microservicio de autenticación según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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