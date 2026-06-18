package example.ms_categorias.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import org.mockito.junit.jupiter.MockitoExtension;

import example.ms_categorias.dto.CategoriaRequestDTO;
import example.ms_categorias.dto.CategoriaResponseDTO;
import example.ms_categorias.exception.CategoriaAlreadyExistsException;
import example.ms_categorias.exception.CategoriaNotFoundException;
import example.ms_categorias.model.Categoria;
import example.ms_categorias.repository.CategoriaRepository;
import example.ms_categorias.service.impl.CategoriaServiceImpl;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaServiceImpl service;

    @Test
    void crearCategoria_debeCrearCategoriaCuandoNombreNoExiste() {

        // ARRANGE: preparar datos y mocks.
        CategoriaRequestDTO request =
                new CategoriaRequestDTO(
                        "Computadores",
                        "Categoría de computadores"
                );

        Categoria categoriaGuardada =
                Categoria.builder()
                        .id(1L)
                        .nombre("Computadores")
                        .descripcion("Categoría de computadores")
                        .active(true)
                        .build();

        when(repository.existsByNombre("Computadores"))
                .thenReturn(false);

        when(repository.save(any(Categoria.class)))
                .thenReturn(categoriaGuardada);

        // ACT: ejecutar método del service.
        CategoriaResponseDTO response =
                service.crearCategoria(request);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals("Computadores", response.getNombre());
        assertEquals(
                "Categoría de computadores",
                response.getDescripcion());
        assertTrue(response.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository)
                .existsByNombre("Computadores");

        verify(repository)
                .save(any(Categoria.class));

        // Caso hipotético de falla para QA:
        // Se esperaba que la categoría fuera creada.
        // Pero el sistema devolvió error indicando
        // que la categoría ya existía.
        // Desarrollo debe revisar existsByNombre()
        // y crearCategoria() en CategoriaServiceImpl.
    }
        @Test
    void crearCategoria_debeLanzarExcepcionCuandoNombreYaExiste() {

        // ARRANGE: preparar datos y mocks.
        CategoriaRequestDTO request =
                new CategoriaRequestDTO(
                        "Computadores",
                        "Categoría de computadores"
                );

        when(repository.existsByNombre("Computadores"))
                .thenReturn(true);

        // ACT + ASSERT: verificar excepción.
        CategoriaAlreadyExistsException exception =
                assertThrows(
                        CategoriaAlreadyExistsException.class,
                        () -> service.crearCategoria(request)
                );

        assertEquals(
                "Ya existe una categoría con ese nombre",
                exception.getMessage()
        );

        // VERIFY: comprobar llamadas al mock.
        verify(repository).existsByNombre("Computadores");

        // Caso hipotético de falla para QA:
        // Se esperaba CategoriaAlreadyExistsException
        // pero el sistema permitió crear una categoría duplicada.
        // QA debe reportar que la validación de nombres
        // duplicados no está funcionando.
        // Desarrollo debe revisar existsByNombre()
        // y crearCategoria() en CategoriaServiceImpl.
    }
        @Test
    void listarCategorias_debeRetornarSoloCategoriasActivas() {

        // ARRANGE: preparar datos y mocks.
        Categoria categoriaActiva =
                Categoria.builder()
                        .id(1L)
                        .nombre("Computadores")
                        .descripcion("Categoría de computadores")
                        .active(true)
                        .build();

        Categoria categoriaInactiva =
                Categoria.builder()
                        .id(2L)
                        .nombre("Accesorios")
                        .descripcion("Categoría de accesorios")
                        .active(false)
                        .build();

        when(repository.findAll())
                .thenReturn(List.of(
                        categoriaActiva,
                        categoriaInactiva
                ));

        // ACT: ejecutar método del service.
        List<CategoriaResponseDTO> response =
                service.listarCategorias();

        // ASSERT: verificar resultado esperado.
        assertEquals(1, response.size());

        assertEquals(1L, response.get(0).getId());
        assertEquals("Computadores",
                response.get(0).getNombre());
        assertEquals("Categoría de computadores",
                response.get(0).getDescripcion());
        assertTrue(response.get(0).getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findAll();

        // Caso hipotético de falla para QA:
        // Se esperaba una sola categoría activa
        // pero el sistema devolvió también
        // categorías inactivas.
        // QA debe reportar que listarCategorias()
        // no está filtrando correctamente.
        // Desarrollo debe revisar el filtro por active
        // en CategoriaServiceImpl.
    }
        @Test
    void buscarPorId_debeRetornarCategoriaCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Categoria categoria =
                Categoria.builder()
                        .id(id)
                        .nombre("Computadores")
                        .descripcion("Categoría de computadores")
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(categoria));

        // ACT: ejecutar método del service.
        CategoriaResponseDTO response =
                service.buscarPorId(id);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals("Computadores", response.getNombre());
        assertEquals("Categoría de computadores",
                response.getDescripcion());
        assertTrue(response.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        // Caso hipotético de falla para QA:
        // Se esperaba encontrar la categoría con ID 1
        // pero el sistema devolvió una excepción.
        // QA debe reportar que buscarPorId()
        // no retorna una categoría existente.
        // Desarrollo debe revisar buscarPorId()
        // en CategoriaServiceImpl.
    }
        @Test
    void buscarPorId_debeLanzarExcepcionCuandoNoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: verificar excepción.
        CategoriaNotFoundException exception =
                assertThrows(
                        CategoriaNotFoundException.class,
                        () -> service.buscarPorId(id)
                );

        assertEquals(
                "Categoría no encontrada con ID: 999",
                exception.getMessage()
        );

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        // Caso hipotético de falla para QA:
        // Se esperaba CategoriaNotFoundException
        // pero el sistema devolvió null
        // o una excepción diferente.
        // QA debe reportar que buscarPorId()
        // no maneja correctamente categorías inexistentes.
        // Desarrollo debe revisar buscarPorId()
        // en CategoriaServiceImpl.
    }
        @Test
    void actualizarCategoria_debeActualizarCategoriaCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Categoria categoriaExistente =
                Categoria.builder()
                        .id(id)
                        .nombre("Computadores")
                        .descripcion("Categoría antigua")
                        .active(true)
                        .build();

        CategoriaRequestDTO request =
                new CategoriaRequestDTO(
                        "Computadores Actualizados",
                        "Nueva descripción"
                );

        Categoria categoriaActualizada =
                Categoria.builder()
                        .id(id)
                        .nombre("Computadores Actualizados")
                        .descripcion("Nueva descripción")
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(categoriaExistente));

        when(repository.existsByNombreAndIdNot(
                "Computadores Actualizados",
                id))
                .thenReturn(false);

        when(repository.save(categoriaExistente))
                .thenReturn(categoriaActualizada);

        // ACT: ejecutar método del service.
        CategoriaResponseDTO response =
                service.actualizarCategoria(id, request);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals(
                "Computadores Actualizados",
                response.getNombre()
        );
        assertEquals(
                "Nueva descripción",
                response.getDescripcion()
        );
        assertTrue(response.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        verify(repository).existsByNombreAndIdNot(
                "Computadores Actualizados",
                id
        );

        verify(repository).save(categoriaExistente);

        // Caso hipotético de falla para QA:
        // Se esperaba que la categoría fuera actualizada
        // pero el sistema mantuvo los datos antiguos.
        // QA debe reportar que actualizarCategoria()
        // no modifica correctamente la información.
        // Desarrollo debe revisar la asignación de campos
        // y el guardado en CategoriaServiceImpl.
    }
        @Test
    void actualizarCategoria_debeLanzarExcepcionCuandoNoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        CategoriaRequestDTO request =
                new CategoriaRequestDTO(
                        "Computadores",
                        "Categoría actualizada"
                );

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: verificar excepción.
        CategoriaNotFoundException exception =
                assertThrows(
                        CategoriaNotFoundException.class,
                        () -> service.actualizarCategoria(id, request)
                );

        assertEquals(
                "Categoría no encontrada con ID: 999",
                exception.getMessage()
        );

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        // Caso hipotético de falla para QA:
        // Se esperaba CategoriaNotFoundException
        // pero el sistema permitió actualizar
        // una categoría inexistente.
        // QA debe reportar que actualizarCategoria()
        // no valida correctamente la existencia de la categoría.
        // Desarrollo debe revisar findById()
        // en CategoriaServiceImpl.
    }
        @Test
    void actualizarCategoria_debeLanzarExcepcionCuandoNombreYaExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Categoria categoriaExistente =
                Categoria.builder()
                        .id(id)
                        .nombre("Computadores")
                        .descripcion("Categoría original")
                        .active(true)
                        .build();

        CategoriaRequestDTO request =
                new CategoriaRequestDTO(
                        "Accesorios",
                        "Nueva descripción"
                );

        when(repository.findById(id))
                .thenReturn(Optional.of(categoriaExistente));

        when(repository.existsByNombreAndIdNot(
                "Accesorios",
                id))
                .thenReturn(true);

        // ACT + ASSERT: verificar excepción.
        CategoriaAlreadyExistsException exception =
                assertThrows(
                        CategoriaAlreadyExistsException.class,
                        () -> service.actualizarCategoria(id, request)
                );

        assertEquals(
                "Ya existe otra categoría con ese nombre",
                exception.getMessage()
        );

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        verify(repository).existsByNombreAndIdNot(
                "Accesorios",
                id
        );

        // Caso hipotético de falla para QA:
        // Se esperaba CategoriaAlreadyExistsException
        // pero el sistema permitió actualizar
        // la categoría con un nombre duplicado.
        // QA debe reportar que la validación
        // de nombres duplicados no funciona.
        // Desarrollo debe revisar
        // existsByNombreAndIdNot()
        // en CategoriaServiceImpl.
    }
        @Test
    void eliminarCategoria_debeDesactivarCategoriaCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Categoria categoriaExistente =
                Categoria.builder()
                        .id(id)
                        .nombre("Computadores")
                        .descripcion("Categoría de computadores")
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(categoriaExistente));

        when(repository.save(categoriaExistente))
                .thenReturn(categoriaExistente);

        // ACT: ejecutar método del service.
        service.eliminarCategoria(id);

        // ASSERT: verificar que la categoría quedó inactiva.
        assertFalse(categoriaExistente.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);
        verify(repository).save(categoriaExistente);

        // Caso hipotético de falla para QA:
        // Se esperaba active = false
        // pero se obtuvo active = true.
        // QA debe reportar que eliminarCategoria()
        // no realiza correctamente la eliminación lógica.
        // Desarrollo debe revisar el setActive(false)
        // en CategoriaServiceImpl.
    }
        @Test
    void eliminarCategoria_debeLanzarExcepcionCuandoNoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: verificar excepción.
        CategoriaNotFoundException exception =
                assertThrows(
                        CategoriaNotFoundException.class,
                        () -> service.eliminarCategoria(id)
                );

        assertEquals(
                "Categoría no encontrada con ID: 999",
                exception.getMessage()
        );

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        // Caso hipotético de falla para QA:
        // Se esperaba CategoriaNotFoundException
        // pero el sistema informó eliminación exitosa.
        // QA debe reportar que eliminarCategoria()
        // permite eliminar categorías inexistentes.
        // Desarrollo debe revisar la validación
        // de existencia previa en CategoriaServiceImpl.
    }
      
}