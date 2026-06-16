package example.ms_categorias.controller;

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

import example.ms_categorias.dto.CategoriaRequestDTO;
import example.ms_categorias.dto.CategoriaResponseDTO;
import example.ms_categorias.service.CategoriaService;
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
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Categorías",
        description = "Endpoints para administrar las categorías de productos del ecommerce"
)
public class CategoriaController {

    private final CategoriaService service;

    @PostMapping
    @Operation(
            summary = "Crear categoría",
            description = "Permite registrar una nueva categoría de productos en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Categoría creada correctamente",
                    content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe una categoría con el mismo nombre",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<CategoriaResponseDTO>
    crearCategoria(
            @Valid @RequestBody CategoriaRequestDTO dto) {

        log.info("POST /api/categorias - Creando categoría: {}",
                dto.getNombre());

        CategoriaResponseDTO response =
                service.crearCategoria(dto);

        log.info("Categoría creada con ID: {}",
                response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar categorías",
            description = "Obtiene todas las categorías activas registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categorías obtenidas correctamente",
                    content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<List<CategoriaResponseDTO>>
    listarCategorias() {

        log.info("GET /api/categorias - Listando categorías");

        List<CategoriaResponseDTO> response =
                service.listarCategorias();

        log.info("Categorías encontradas: {}",
                response.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar categoría por ID",
            description = "Busca una categoría específica utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría encontrada correctamente",
                    content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<CategoriaResponseDTO>
    buscarPorId(
            @PathVariable Long id) {

        log.info("GET /api/categorias/{} - Buscando categoría",
                id);

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar categoría",
            description = "Actualiza el nombre y la descripción de una categoría existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe una categoría con el mismo nombre",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<CategoriaResponseDTO>
    actualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto) {

        log.info("PUT /api/categorias/{} - Actualizando categoría",
                id);

        CategoriaResponseDTO response =
                service.actualizarCategoria(id, dto);

        log.info("Categoría actualizada con ID: {}",
                response.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar categoría",
            description = "Realiza una eliminación lógica de la categoría, dejando el campo active en false."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Categoría eliminada correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<Void>
    eliminarCategoria(
            @PathVariable Long id) {

        log.info("DELETE /api/categorias/{} - Eliminando categoría lógica",
                id);

        service.eliminarCategoria(id);

        log.info("Categoría eliminada correctamente ID: {}",
                id);

        return ResponseEntity.noContent().build();
    }
}