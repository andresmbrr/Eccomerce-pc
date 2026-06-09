package example.ms_categorias.service;

import java.util.List;

import example.ms_categorias.dto.CategoriaRequestDTO;
import example.ms_categorias.dto.CategoriaResponseDTO;

public interface CategoriaService {

    CategoriaResponseDTO crearCategoria(
            CategoriaRequestDTO dto);

    List<CategoriaResponseDTO> listarCategorias();

    CategoriaResponseDTO buscarPorId(Long id);

    CategoriaResponseDTO actualizarCategoria(
            Long id,
            CategoriaRequestDTO dto);

    void eliminarCategoria(Long id);
}