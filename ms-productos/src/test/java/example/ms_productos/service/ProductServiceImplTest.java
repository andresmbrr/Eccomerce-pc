package example.ms_productos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import example.ms_productos.client.CategoriaClient;
import example.ms_productos.dto.CategoriaResponseDTO;
import example.ms_productos.dto.ProductRequestDTO;
import example.ms_productos.dto.ProductResponseDTO;
import example.ms_productos.model.Product;
import example.ms_productos.repository.ProductRepository;
import example.ms_productos.service.impl.ProductServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoriaClient categoriaClient;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void createProduct_debeCrearProductoCuandoDatosSonValidos() {

        // ARRANGE: preparar datos y mocks.
        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Notebook Gamer",
                        "Notebook para desarrollo y juegos",
                        new BigDecimal("899990"),
                        1L,
                        true
                );

        CategoriaResponseDTO categoria =
                new CategoriaResponseDTO();
        categoria.setId(1L);
        categoria.setNombre("Computadores");
        categoria.setDescripcion("Categoría de computadores");
        categoria.setActive(true);

        Product productoGuardado =
                Product.builder()
                        .id(1L)
                        .name("Notebook Gamer")
                        .description("Notebook para desarrollo y juegos")
                        .price(new BigDecimal("899990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        when(categoriaClient.obtenerCategoriaPorId(1L))
                .thenReturn(categoria);

        when(repository.save(any(Product.class)))
                .thenReturn(productoGuardado);

        // ACT: ejecutar método del service.
        ProductResponseDTO response =
                service.createProduct(request);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals("Notebook Gamer", response.getName());
        assertEquals("Notebook para desarrollo y juegos", response.getDescription());
        assertEquals(new BigDecimal("899990"), response.getPrice());
        assertEquals("Computadores", response.getCategory());
        assertTrue(response.getActive());

        // VERIFY: comprobar llamadas a los mocks.
        verify(categoriaClient).obtenerCategoriaPorId(1L);
        verify(repository).save(any(Product.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba category = "Computadores" y se obtiene null,
        // QA debe reportar que el producto se crea, pero no asigna
        // correctamente la categoría obtenida desde ms-categorias.
        // Desarrollo debe revisar createProduct() en ProductServiceImpl
        // y la llamada al CategoriaClient.
    }

    @Test
    void getAllProducts_debeRetornarSoloProductosActivos() {

        // ARRANGE: preparar datos y mocks.
        Product productoActivo =
                Product.builder()
                        .id(1L)
                        .name("Notebook Gamer")
                        .description("Notebook para desarrollo y juegos")
                        .price(new BigDecimal("899990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        Product productoInactivo =
                Product.builder()
                        .id(2L)
                        .name("Mouse Antiguo")
                        .description("Producto desactivado")
                        .price(new BigDecimal("9990"))
                        .category("Accesorios")
                        .active(false)
                        .build();

        when(repository.findAll())
                .thenReturn(List.of(productoActivo, productoInactivo));

        // ACT: ejecutar método del service.
        List<ProductResponseDTO> response =
                service.getAllProducts();

        // ASSERT: verificar resultado esperado.
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getId());
        assertEquals("Notebook Gamer", response.get(0).getName());
        assertEquals("Notebook para desarrollo y juegos", response.get(0).getDescription());
        assertEquals(new BigDecimal("899990"), response.get(0).getPrice());
        assertEquals("Computadores", response.get(0).getCategory());
        assertTrue(response.get(0).getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findAll();

        // Caso hipotético de falla para QA:
        // Si se esperaba 1 producto activo y se obtienen 2 productos,
        // QA debe reportar que getAllProducts() está mostrando productos inactivos.
        // Desarrollo debe revisar el filtro por active en ProductServiceImpl.
    }

    @Test
    void getProductById_debeRetornarProductoCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Product producto =
                Product.builder()
                        .id(id)
                        .name("Notebook Gamer")
                        .description("Notebook para desarrollo y juegos")
                        .price(new BigDecimal("899990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(producto));

        // ACT: ejecutar método del service.
        ProductResponseDTO response =
                service.getProductById(id);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals("Notebook Gamer", response.getName());
        assertEquals("Notebook para desarrollo y juegos", response.getDescription());
        assertEquals(new BigDecimal("899990"), response.getPrice());
        assertEquals("Computadores", response.getCategory());
        assertTrue(response.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba encontrar el producto con ID 1 y se obtiene una excepción,
        // QA debe reportar que getProductById() no está retornando
        // un producto existente.
        // Desarrollo debe revisar el método getProductById() de ProductServiceImpl.
    }

    @Test
    void getProductsByCategory_debeRetornarProductosPorCategoria() {

        // ARRANGE: preparar datos y mocks.
        String category = "Computadores";

        Product producto1 =
                Product.builder()
                        .id(1L)
                        .name("Notebook Gamer")
                        .description("Notebook para desarrollo y juegos")
                        .price(new BigDecimal("899990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        Product producto2 =
                Product.builder()
                        .id(2L)
                        .name("PC Escritorio")
                        .description("Computador de escritorio")
                        .price(new BigDecimal("699990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        when(repository.findByCategory(category))
                .thenReturn(List.of(producto1, producto2));

        // ACT: ejecutar método del service.
        List<ProductResponseDTO> response =
                service.getProductsByCategory(category);

        // ASSERT: verificar resultado esperado.
        assertEquals(2, response.size());

        assertEquals(1L, response.get(0).getId());
        assertEquals("Notebook Gamer", response.get(0).getName());
        assertEquals("Computadores", response.get(0).getCategory());
        assertTrue(response.get(0).getActive());

        assertEquals(2L, response.get(1).getId());
        assertEquals("PC Escritorio", response.get(1).getName());
        assertEquals("Computadores", response.get(1).getCategory());
        assertTrue(response.get(1).getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findByCategory(category);

        // Caso hipotético de falla para QA:
        // Si se esperaba una lista con productos de la categoría "Computadores"
        // y se obtiene una lista vacía,
        // QA debe reportar que getProductsByCategory() no retorna
        // productos existentes para la categoría indicada.
        // Desarrollo debe revisar ProductServiceImpl y ProductRepository.
    }

    @Test
    void updateProduct_debeActualizarProductoCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Product productoExistente =
                Product.builder()
                        .id(id)
                        .name("Notebook Gamer")
                        .description("Notebook para desarrollo y juegos")
                        .price(new BigDecimal("899990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Notebook Gamer Actualizado",
                        "Notebook actualizado para desarrollo",
                        new BigDecimal("999990"),
                        1L,
                        true
                );

        CategoriaResponseDTO categoria =
                new CategoriaResponseDTO();
        categoria.setId(1L);
        categoria.setNombre("Computadores");
        categoria.setDescripcion("Categoría de computadores");
        categoria.setActive(true);

        Product productoActualizado =
                Product.builder()
                        .id(id)
                        .name("Notebook Gamer Actualizado")
                        .description("Notebook actualizado para desarrollo")
                        .price(new BigDecimal("999990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(productoExistente));

        when(categoriaClient.obtenerCategoriaPorId(1L))
                .thenReturn(categoria);

        when(repository.save(any(Product.class)))
                .thenReturn(productoActualizado);

        // ACT: ejecutar método del service.
        ProductResponseDTO response =
                service.updateProduct(id, request);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals("Notebook Gamer Actualizado", response.getName());
        assertEquals("Notebook actualizado para desarrollo", response.getDescription());
        assertEquals(new BigDecimal("999990"), response.getPrice());
        assertEquals("Computadores", response.getCategory());
        assertTrue(response.getActive());

        // VERIFY: comprobar llamadas a los mocks.
        verify(repository).findById(id);
        verify(categoriaClient).obtenerCategoriaPorId(1L);
        verify(repository).save(any(Product.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba name = "Notebook Gamer Actualizado"
        // y se obtiene name = "Notebook Gamer",
        // QA debe reportar que updateProduct() no actualiza
        // correctamente los datos del producto.
        // Desarrollo debe revisar updateProduct() en ProductServiceImpl.
    }

    @Test
    void deleteProduct_debeDesactivarProductoCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        Product productoExistente =
                Product.builder()
                        .id(id)
                        .name("Notebook Gamer")
                        .description("Notebook para desarrollo y juegos")
                        .price(new BigDecimal("899990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(productoExistente));

        when(repository.save(any(Product.class)))
                .thenReturn(productoExistente);

        // ACT: ejecutar método del service.
        service.deleteProduct(id);

        // ASSERT: verificar que el producto quedó inactivo.
        assertFalse(productoExistente.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);
        verify(repository).save(productoExistente);

        // Caso hipotético de falla para QA:
        // Si se esperaba active = false y se obtiene active = true,
        // QA debe reportar que deleteProduct() no desactiva
        // correctamente el producto.
        // Desarrollo debe revisar deleteProduct() en ProductServiceImpl.
    }
}