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
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Perfiles de Usuario",
        description = "Endpoints para administrar los perfiles de usuario asociados a usuarios registrados en ms-auth"
)
public class UserProfileController {

    private final UserProfileService service;

    @PostMapping
    @Operation(
            summary = "Crear perfil de usuario",
            description = "Permite crear un perfil de usuario asociado a un usuario existente del microservicio ms-auth mediante authUserId."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Perfil creado correctamente",
                    content = @Content(schema = @Schema(implementation = UserProfileResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe un perfil asociado al mismo authUserId",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Listar perfiles de usuario",
            description = "Obtiene todos los perfiles de usuario activos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfiles obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = UserProfileResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Buscar perfil por ID",
            description = "Busca un perfil de usuario específico utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil encontrado correctamente",
                    content = @Content(schema = @Schema(implementation = UserProfileResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Perfil no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<UserProfileResponseDTO>
    getById(@PathVariable Long id) {

        log.info("GET /api/users/{} - Buscando perfil por ID",
                id);

        return ResponseEntity.ok(
                service.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar perfil de usuario",
            description = "Actualiza los datos de un perfil de usuario existente, incluyendo nombre, apellido, teléfono, dirección y fecha de nacimiento."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = UserProfileResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Perfil no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto por authUserId duplicado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Eliminar perfil de usuario",
            description = "Realiza una eliminación lógica del perfil de usuario, dejando el registro inactivo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Perfil eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Perfil no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
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