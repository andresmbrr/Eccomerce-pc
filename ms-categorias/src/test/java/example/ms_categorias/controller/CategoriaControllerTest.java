package example.ms_categorias.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import example.ms_categorias.dto.CategoriaRequestDTO;
import example.ms_categorias.dto.CategoriaResponseDTO;
import example.ms_categorias.service.CategoriaService;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoriaService service;

    @Test
    void crearCategoria_debeRetornar201CuandoDatosSonValidos()
            throws Exception {

        // ARRANGE: preparar datos y mocks.
        CategoriaRequestDTO request =
                new CategoriaRequestDTO(
                        "Computadores",
                        "Categoría de computadores"
                );

        CategoriaResponseDTO response =
                CategoriaResponseDTO.builder()
                        .id(1L)
                        .nombre("Computadores")
                        .descripcion("Categoría de computadores")
                        .active(true)
                        .build();

        when(service.crearCategoria(any(CategoriaRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado.
        mockMvc.perform(
                        post("/api/categorias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Computadores"))
                .andExpect(jsonPath("$.descripcion")
                        .value("Categoría de computadores"))
                .andExpect(jsonPath("$.active").value(true));

        // Caso hipotético de falla para QA:
        // Se esperaba HTTP 201 Created
        // pero se obtuvo HTTP 500 Internal Server Error.
        // QA debe reportar que la creación de categorías falla.
        // Desarrollo debe revisar el flujo crearCategoria().
    }

    @Test
    void listarCategorias_debeRetornar200YListaCategorias()
            throws Exception {

        // ARRANGE: preparar datos y mocks.
        CategoriaResponseDTO categoria1 =
                CategoriaResponseDTO.builder()
                        .id(1L)
                        .nombre("Computadores")
                        .descripcion("Categoría de computadores")
                        .active(true)
                        .build();

        CategoriaResponseDTO categoria2 =
                CategoriaResponseDTO.builder()
                        .id(2L)
                        .nombre("Accesorios")
                        .descripcion("Categoría de accesorios")
                        .active(true)
                        .build();

        when(service.listarCategorias())
                .thenReturn(List.of(categoria1, categoria2));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado.
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre")
                        .value("Computadores"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nombre")
                        .value("Accesorios"));

        // Caso hipotético de falla para QA:
        // Se esperaba una lista con 2 categorías
        // y se obtuvo una lista vacía.
        // QA debe reportar que el endpoint no retorna categorías.
        // Desarrollo debe revisar listarCategorias().
    }

        @Test
    void buscarPorId_debeRetornar200CuandoCategoriaExiste()
            throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        CategoriaResponseDTO response =
                CategoriaResponseDTO.builder()
                        .id(id)
                        .nombre("Computadores")
                        .descripcion("Categoría de computadores")
                        .active(true)
                        .build();

        when(service.buscarPorId(id))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado.
        mockMvc.perform(get("/api/categorias/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre")
                        .value("Computadores"))
                .andExpect(jsonPath("$.descripcion")
                        .value("Categoría de computadores"))
                .andExpect(jsonPath("$.active")
                        .value(true));

        // Caso hipotético de falla para QA:
        // Se esperaba HTTP 200 OK
        // pero se obtuvo HTTP 404 Not Found.
        // QA debe reportar que el endpoint no encuentra
        // una categoría existente.
        // Desarrollo debe revisar buscarPorId()
        // en CategoriaController y CategoriaService.
    }
        @Test
    void actualizarCategoria_debeRetornar200CuandoCategoriaExiste()
            throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        CategoriaRequestDTO request =
                new CategoriaRequestDTO(
                        "Computadores Actualizado",
                        "Categoría actualizada"
                );

        CategoriaResponseDTO response =
                CategoriaResponseDTO.builder()
                        .id(id)
                        .nombre("Computadores Actualizado")
                        .descripcion("Categoría actualizada")
                        .active(true)
                        .build();

        when(service.actualizarCategoria(
                org.mockito.ArgumentMatchers.eq(id),
                org.mockito.ArgumentMatchers.any(CategoriaRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado.
        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/categorias/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre")
                        .value("Computadores Actualizado"))
                .andExpect(jsonPath("$.descripcion")
                        .value("Categoría actualizada"))
                .andExpect(jsonPath("$.active")
                        .value(true));

        // Caso hipotético de falla para QA:
        // Se esperaba HTTP 200 OK
        // pero se obtuvo HTTP 404 Not Found.
        // QA debe reportar que la categoría existente
        // no puede ser actualizada.
        // Desarrollo debe revisar actualizarCategoria()
        // en CategoriaController y CategoriaService.
    }
        @Test
    void eliminarCategoria_debeRetornar204CuandoCategoriaExiste()
            throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        doNothing().when(service)
                .eliminarCategoria(id);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado.
        mockMvc.perform(delete("/api/categorias/{id}", id))
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamada al mock.
        verify(service).eliminarCategoria(id);

        // Caso hipotético de falla para QA:
        // Se esperaba HTTP 204 No Content
        // pero se obtuvo HTTP 404 Not Found.
        // QA debe reportar que no es posible eliminar
        // una categoría existente.
        // Desarrollo debe revisar eliminarCategoria()
        // en CategoriaController y CategoriaService.
    }
}