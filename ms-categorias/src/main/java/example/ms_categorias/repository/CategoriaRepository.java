package example.ms_categorias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import example.ms_categorias.model.Categoria;

public interface CategoriaRepository
        extends JpaRepository<Categoria, Long> {

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(
            String nombre,
            Long id);
}