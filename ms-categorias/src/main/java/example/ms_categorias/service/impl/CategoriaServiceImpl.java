package example.ms_categorias.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import example.ms_categorias.dto.CategoriaRequestDTO;
import example.ms_categorias.dto.CategoriaResponseDTO;
import example.ms_categorias.exception.CategoriaAlreadyExistsException;
import example.ms_categorias.exception.CategoriaNotFoundException;
import example.ms_categorias.model.Categoria;
import example.ms_categorias.repository.CategoriaRepository;
import example.ms_categorias.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaServiceImpl
        implements CategoriaService {

    private final CategoriaRepository repository;

    @Override
    public CategoriaResponseDTO crearCategoria(
            CategoriaRequestDTO dto) {

        log.info("Creando categoría: {}",
                dto.getNombre());

        if (repository.existsByNombre(dto.getNombre())) {
            log.warn("Intento de crear categoría duplicada: {}",
                    dto.getNombre());

            throw new CategoriaAlreadyExistsException(
                    "Ya existe una categoría con ese nombre");
        }

        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .active(true)
                .build();

        Categoria saved = repository.save(categoria);

        log.info("Categoría creada correctamente con ID: {}",
                saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<CategoriaResponseDTO> listarCategorias() {

        log.info("Listando categorías activas");

        List<CategoriaResponseDTO> categorias =
                repository.findAll()
                        .stream()
                        .filter(categoria ->
                                Boolean.TRUE.equals(categoria.getActive()))
                        .map(this::mapToDTO)
                        .toList();

        log.info("Categorías activas encontradas: {}",
                categorias.size());

        return categorias;
    }

    @Override
    public CategoriaResponseDTO buscarPorId(Long id) {

        log.info("Buscando categoría con ID: {}",
                id);

        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Categoría no encontrada con ID: {}",
                            id);

                    return new CategoriaNotFoundException(
                            "Categoría no encontrada con ID: " + id);
                });

        return mapToDTO(categoria);
    }

        @Override
    public CategoriaResponseDTO actualizarCategoria(
            Long id,
            CategoriaRequestDTO dto) {

        log.info("Actualizando categoría con ID: {}",
                id);

        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("No se pudo actualizar. Categoría no encontrada con ID: {}",
                            id);

                    return new CategoriaNotFoundException(
                            "Categoría no encontrada con ID: " + id);
                });

        // Validar que no exista otra categoría con el mismo nombre
        if (repository.existsByNombreAndIdNot(
                dto.getNombre(), id)) {

            log.warn("Ya existe otra categoría con nombre: {}",
                    dto.getNombre());

            throw new CategoriaAlreadyExistsException(
                    "Ya existe otra categoría con ese nombre");
        }

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        Categoria updated = repository.save(categoria);

        log.info("Categoría actualizada correctamente con ID: {}",
                updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void eliminarCategoria(Long id) {

        log.info("Eliminando lógicamente categoría con ID: {}",
                id);

        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("No se pudo eliminar. Categoría no encontrada con ID: {}",
                            id);

                    return new CategoriaNotFoundException(
                            "Categoría no encontrada con ID: " + id);
                });

        categoria.setActive(false);

        repository.save(categoria);

        log.info("Categoría desactivada correctamente con ID: {}",
                id);
    }

    private CategoriaResponseDTO mapToDTO(
            Categoria categoria) {

        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .active(categoria.getActive())
                .build();
    }
}