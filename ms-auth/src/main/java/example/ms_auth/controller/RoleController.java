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
@RequestMapping("/api/auth/roles")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Roles",
        description = "Endpoints para la administración de roles del sistema"
)
public class RoleController {

    private final RoleService service;

    @PostMapping
    @Operation(
            summary = "Crear rol",
            description = "Permite crear un nuevo rol en el sistema, por ejemplo ADMIN, CLIENTE u OPERADOR."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Rol creado correctamente",
                    content = @Content(schema = @Schema(implementation = RoleResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe un rol con el mismo nombre",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<RoleResponseDTO> createRole(
            @Valid @RequestBody RoleRequestDTO dto) {

        log.info("POST /api/auth/roles ejecutado");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createRole(dto));
    }

    @GetMapping
    @Operation(
            summary = "Listar roles",
            description = "Obtiene el listado completo de roles registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Roles obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = RoleResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {

        log.info("GET /api/auth/roles ejecutado");

        return ResponseEntity.ok(
                service.getAllRoles());
    }
}