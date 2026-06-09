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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Slf4j
public class CategoriaController {

    private final CategoriaService service;

    @PostMapping
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
    public ResponseEntity<CategoriaResponseDTO>
    buscarPorId(
            @PathVariable Long id) {

        log.info("GET /api/categorias/{} - Buscando categoría",
                id);

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @PutMapping("/{id}")
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