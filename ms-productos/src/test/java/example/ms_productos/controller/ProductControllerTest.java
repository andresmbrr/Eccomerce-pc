package example.ms_productos.controller;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import example.ms_productos.dto.ProductRequestDTO;
import example.ms_productos.dto.ProductResponseDTO;
import example.ms_productos.exception.GlobalExceptionHandler;
import example.ms_productos.exception.ProductNotFoundException;
import example.ms_productos.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private ProductService service;

    @BeforeEach
    void setUp() {
        ProductController controller =
                new ProductController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void createProduct_debeRetornar201CuandoProductoEsValido() throws Exception {

        // ARRANGE: preparar datos y mocks.
        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Notebook Gamer",
                        "Notebook para desarrollo y juegos",
                        new BigDecimal("899990"),
                        1L,
                        true
                );

        ProductResponseDTO response =
                ProductResponseDTO.builder()
                        .id(1L)
                        .name("Notebook Gamer")
                        .description("Notebook para desarrollo y juegos")
                        .price(new BigDecimal("899990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        when(service.createProduct(any(ProductRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Notebook Gamer"))
                .andExpect(jsonPath("$.description").value("Notebook para desarrollo y juegos"))
                .andExpect(jsonPath("$.price").value(899990))
                .andExpect(jsonPath("$.category").value("Computadores"))
                .andExpect(jsonPath("$.active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).createProduct(any(ProductRequestDTO.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 201 Created y se obtiene HTTP 200 OK,
        // QA debe reportar que el endpoint POST /api/productos
        // crea el producto, pero no responde con el código HTTP correcto.
        // Desarrollo debe revisar el método createProduct()
        // en ProductController.
    }

    @Test
    void getAllProducts_debeRetornar200YListaDeProductos() throws Exception {

        // ARRANGE: preparar datos y mocks.
        ProductResponseDTO producto1 =
                ProductResponseDTO.builder()
                        .id(1L)
                        .name("Notebook Gamer")
                        .description("Notebook para desarrollo y juegos")
                        .price(new BigDecimal("899990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        ProductResponseDTO producto2 =
                ProductResponseDTO.builder()
                        .id(2L)
                        .name("Mouse Gamer")
                        .description("Mouse RGB")
                        .price(new BigDecimal("19990"))
                        .category("Accesorios")
                        .active(true)
                        .build();

        when(service.getAllProducts())
                .thenReturn(List.of(producto1, producto2));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Notebook Gamer"))
                .andExpect(jsonPath("$[0].description").value("Notebook para desarrollo y juegos"))
                .andExpect(jsonPath("$[0].price").value(899990))
                .andExpect(jsonPath("$[0].category").value("Computadores"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Mouse Gamer"))
                .andExpect(jsonPath("$[1].description").value("Mouse RGB"))
                .andExpect(jsonPath("$[1].price").value(19990))
                .andExpect(jsonPath("$[1].category").value("Accesorios"))
                .andExpect(jsonPath("$[1].active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getAllProducts();

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 500 Internal Server Error,
        // QA debe reportar que el endpoint GET /api/productos falla
        // al listar productos.
        // Desarrollo debe revisar el método getAllProducts()
        // en ProductController y ProductService.
    }

    @Test
    void getProductById_debeRetornar200CuandoProductoExiste() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        ProductResponseDTO response =
                ProductResponseDTO.builder()
                        .id(id)
                        .name("Notebook Gamer")
                        .description("Notebook para desarrollo y juegos")
                        .price(new BigDecimal("899990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        when(service.getProductById(id))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/productos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Notebook Gamer"))
                .andExpect(jsonPath("$.description").value("Notebook para desarrollo y juegos"))
                .andExpect(jsonPath("$.price").value(899990))
                .andExpect(jsonPath("$.category").value("Computadores"))
                .andExpect(jsonPath("$.active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getProductById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 404 Not Found,
        // QA debe reportar que el endpoint GET /api/productos/{id}
        // no está encontrando un producto que debería existir.
        // Desarrollo debe revisar el método getProductById()
        // en ProductController y ProductService.
    }

    @Test
    void getProductsByCategory_debeRetornar200YListaFiltradaPorCategoria() throws Exception {

        // ARRANGE: preparar datos y mocks.
        String category = "Computadores";

        ProductResponseDTO producto1 =
                ProductResponseDTO.builder()
                        .id(1L)
                        .name("Notebook Gamer")
                        .description("Notebook para desarrollo y juegos")
                        .price(new BigDecimal("899990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        ProductResponseDTO producto2 =
                ProductResponseDTO.builder()
                        .id(2L)
                        .name("PC Escritorio")
                        .description("Computador de escritorio")
                        .price(new BigDecimal("699990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        when(service.getProductsByCategory(category))
                .thenReturn(List.of(producto1, producto2));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/productos/category/{category}", category))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Notebook Gamer"))
                .andExpect(jsonPath("$[0].category").value("Computadores"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("PC Escritorio"))
                .andExpect(jsonPath("$[1].category").value("Computadores"))
                .andExpect(jsonPath("$[1].active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getProductsByCategory(category);

        // Caso hipotético de falla para QA:
        // Si se esperaba una lista de productos de la categoría "Computadores"
        // y se obtiene una lista vacía,
        // QA debe reportar que el endpoint GET /api/productos/category/{category}
        // no está filtrando correctamente por categoría.
        // Desarrollo debe revisar el método getProductsByCategory()
        // en ProductController y ProductService.
    }

    @Test
    void updateProduct_debeRetornar200CuandoProductoEsActualizado() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Notebook Gamer Actualizado",
                        "Notebook actualizado para desarrollo",
                        new BigDecimal("999990"),
                        1L,
                        true
                );

        ProductResponseDTO response =
                ProductResponseDTO.builder()
                        .id(id)
                        .name("Notebook Gamer Actualizado")
                        .description("Notebook actualizado para desarrollo")
                        .price(new BigDecimal("999990"))
                        .category("Computadores")
                        .active(true)
                        .build();

        when(service.updateProduct(any(Long.class), any(ProductRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(put("/api/productos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Notebook Gamer Actualizado"))
                .andExpect(jsonPath("$.description").value("Notebook actualizado para desarrollo"))
                .andExpect(jsonPath("$.price").value(999990))
                .andExpect(jsonPath("$.category").value("Computadores"))
                .andExpect(jsonPath("$.active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).updateProduct(any(Long.class), any(ProductRequestDTO.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 404 Not Found,
        // QA debe reportar que el endpoint PUT /api/productos/{id}
        // no está actualizando un producto que debería existir.
        // Desarrollo debe revisar el método updateProduct()
        // en ProductController y ProductService.
    }

    @Test
    void deleteProduct_debeRetornar204CuandoProductoEsEliminado() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        // Para métodos void, normalmente no necesitamos when().
        // Solo verificamos que el endpoint llame al service.deleteProduct(id).

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(delete("/api/productos/{id}", id))
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock.
        verify(service).deleteProduct(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 204 No Content y se obtiene HTTP 200 OK,
        // QA debe reportar que el endpoint DELETE /api/productos/{id}
        // elimina el producto, pero no responde con el código HTTP esperado.
        // Desarrollo debe revisar el método deleteProduct()
        // en ProductController.
    }
        @Test
        void getProductById_debeRetornar404CuandoProductoNoExiste() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        when(service.getProductById(id))
                .thenThrow(
                        new ProductNotFoundException(
                                "Producto no encontrado con ID: " + id));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/productos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Producto no encontrado con ID: 999"));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getProductById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 404 Not Found
        // y se obtiene HTTP 500 Internal Server Error,
        // el manejo de excepciones del controller
        // no está funcionando correctamente.
        }
                @Test
        void updateProduct_debeRetornar404CuandoProductoNoExiste() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Notebook Actualizado",
                        "Descripción actualizada",
                        new BigDecimal("999990"),
                        1L,
                        true
                );

        when(service.updateProduct(any(Long.class), any(ProductRequestDTO.class)))
                .thenThrow(
                        new ProductNotFoundException(
                                "Producto no encontrado con ID: " + id));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(put("/api/productos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Producto no encontrado con ID: 999"));

        // VERIFY: comprobar llamadas al mock.
        verify(service).updateProduct(any(Long.class), any(ProductRequestDTO.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 404 Not Found
        // y se obtiene HTTP 200 OK,
        // el endpoint está permitiendo actualizar
        // un producto inexistente.
        }
                @Test
        void deleteProduct_debeRetornar404CuandoProductoNoExiste() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        doThrow(
                new ProductNotFoundException(
                        "Producto no encontrado con ID: " + id))
                .when(service)
                .deleteProduct(id);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(delete("/api/productos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Producto no encontrado con ID: 999"));

        // VERIFY: comprobar llamadas al mock.
        verify(service).deleteProduct(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 404 Not Found
        // y se obtiene HTTP 204 No Content,
        // el endpoint informa eliminación exitosa
        // para un producto inexistente.
        }
                @Test
        void createProduct_debeRetornar400CuandoNombreEsVacio() throws Exception {

        // ARRANGE: request inválido.
        ProductRequestDTO request =
                new ProductRequestDTO(
                        "",
                        "Descripción válida",
                        new BigDecimal("1000"),
                        1L,
                        true
                );

        // ACT + ASSERT
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.name")
                        .value("El nombre es obligatorio"));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 400 y se obtiene HTTP 201,
        // el endpoint está permitiendo crear productos
        // con nombre vacío.
        }
        
}