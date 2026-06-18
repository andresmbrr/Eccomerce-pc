package example.ms_user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import example.ms_user.exception.ResourceNotFoundException;

import static org.mockito.Mockito.doThrow;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import example.ms_user.dto.UserProfileRequestDTO;
import example.ms_user.dto.UserProfileResponseDTO;
import example.ms_user.exception.GlobalExceptionHandler;
import example.ms_user.service.UserProfileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private UserProfileService service;

    @BeforeEach
    void setUp() {
        UserProfileController controller =
                new UserProfileController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void create_debeRetornar201CuandoPerfilEsValido() throws Exception {

        // ARRANGE: preparar datos y mocks.
        UserProfileRequestDTO request =
                new UserProfileRequestDTO(
                        1L,
                        "Andres",
                        "Bustamante",
                        "912345678",
                        "Av Siempre Viva 123",
                        LocalDate.of(2000, 5, 10),
                        true
                );

        UserProfileResponseDTO response =
                UserProfileResponseDTO.builder()
                        .id(1L)
                        .authUserId(1L)
                        .firstName("Andres")
                        .lastName("Bustamante")
                        .phone("912345678")
                        .address("Av Siempre Viva 123")
                        .birthDate(LocalDate.of(2000, 5, 10))
                        .active(true)
                        .build();

        when(service.create(any(UserProfileRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.authUserId").value(1L))
                .andExpect(jsonPath("$.firstName").value("Andres"))
                .andExpect(jsonPath("$.lastName").value("Bustamante"));

        // VERIFY: comprobar llamadas al mock.
        verify(service).create(any(UserProfileRequestDTO.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 201 Created y se obtiene HTTP 200 OK,
        // QA debe reportar que el endpoint POST /api/users no cumple
        // con el código de respuesta esperado para creación.
        // Desarrollo debe revisar el return del método create()
        // en UserProfileController.
    }

    @Test
    void getAll_debeRetornar200YListaDePerfiles() throws Exception {

        // ARRANGE: preparar datos y mocks.
        UserProfileResponseDTO perfil1 =
                UserProfileResponseDTO.builder()
                        .id(1L)
                        .authUserId(1L)
                        .firstName("Andres")
                        .lastName("Bustamante")
                        .phone("912345678")
                        .address("Av Siempre Viva 123")
                        .birthDate(LocalDate.of(2000, 5, 10))
                        .active(true)
                        .build();

        UserProfileResponseDTO perfil2 =
                UserProfileResponseDTO.builder()
                        .id(2L)
                        .authUserId(2L)
                        .firstName("Matias")
                        .lastName("Latrach")
                        .phone("987654321")
                        .address("Calle Principal 456")
                        .birthDate(LocalDate.of(2001, 3, 15))
                        .active(true)
                        .build();

        when(service.getAll())
                .thenReturn(List.of(perfil1, perfil2));

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].authUserId").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Andres"))
                .andExpect(jsonPath("$[0].lastName").value("Bustamante"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].authUserId").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Matias"))
                .andExpect(jsonPath("$[1].lastName").value("Latrach"));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getAll();

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 500 Internal Server Error,
        // QA debe reportar que el endpoint GET /api/users falla al listar perfiles.
        // Desarrollo debe revisar el método getAll() del controller y del service.
    }

    @Test
    void getById_debeRetornar200CuandoPerfilExiste() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        UserProfileResponseDTO response =
                UserProfileResponseDTO.builder()
                        .id(id)
                        .authUserId(1L)
                        .firstName("Andres")
                        .lastName("Bustamante")
                        .phone("912345678")
                        .address("Av Siempre Viva 123")
                        .birthDate(LocalDate.of(2000, 5, 10))
                        .active(true)
                        .build();

        when(service.getById(id))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.authUserId").value(1L))
                .andExpect(jsonPath("$.firstName").value("Andres"))
                .andExpect(jsonPath("$.lastName").value("Bustamante"))
                .andExpect(jsonPath("$.phone").value("912345678"))
                .andExpect(jsonPath("$.address").value("Av Siempre Viva 123"))
                .andExpect(jsonPath("$.active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).getById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 404 Not Found,
        // QA debe reportar que el endpoint GET /api/users/{id}
        // no está encontrando un perfil que debería existir.
        // Desarrollo debe revisar el método getById() del controller
        // y del service.
    }

    @Test
    void update_debeRetornar200CuandoPerfilEsActualizado() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        UserProfileRequestDTO request =
                new UserProfileRequestDTO(
                        1L,
                        "Andres",
                        "Bustamante Actualizado",
                        "987654321",
                        "Nueva Direccion 456",
                        LocalDate.of(2000, 5, 10),
                        true
                );

        UserProfileResponseDTO response =
                UserProfileResponseDTO.builder()
                        .id(id)
                        .authUserId(1L)
                        .firstName("Andres")
                        .lastName("Bustamante Actualizado")
                        .phone("987654321")
                        .address("Nueva Direccion 456")
                        .birthDate(LocalDate.of(2000, 5, 10))
                        .active(true)
                        .build();

        when(service.update(any(Long.class), any(UserProfileRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.authUserId").value(1L))
                .andExpect(jsonPath("$.firstName").value("Andres"))
                .andExpect(jsonPath("$.lastName").value("Bustamante Actualizado"))
                .andExpect(jsonPath("$.phone").value("987654321"))
                .andExpect(jsonPath("$.address").value("Nueva Direccion 456"))
                .andExpect(jsonPath("$.active").value(true));

        // VERIFY: comprobar llamadas al mock.
        verify(service).update(any(Long.class), any(UserProfileRequestDTO.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 200 OK y se obtiene HTTP 404 Not Found,
        // QA debe reportar que el endpoint PUT /api/users/{id}
        // no está actualizando un perfil que debería existir.
        // Desarrollo debe revisar el método update() del controller
        // y del service.
    }

    @Test
    void delete_debeRetornar204CuandoPerfilEsEliminado() throws Exception {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        // Para métodos void, normalmente no necesitamos when().
        // Solo verificamos que el endpoint llame al service.delete(id).

        // ACT + ASSERT: ejecutar endpoint y verificar resultado esperado.
        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock.
        verify(service).delete(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba HTTP 204 No Content y se obtiene HTTP 200 OK,
        // QA debe reportar que el endpoint DELETE /api/users/{id}
        // elimina el perfil, pero no responde con el código HTTP esperado.
        // Desarrollo debe revisar el método delete() del controller.
    }
        @Test
        void getById_debeRetornar404CuandoPerfilNoExiste() throws Exception {

        // ARRANGE
        Long id = 999L;

        when(service.getById(id))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Perfil no encontrado con ID: " + id
                        )
                );

        // ACT + ASSERT
        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Perfil no encontrado con ID: 999"));

        // VERIFY
        verify(service).getById(id);
        }
                @Test
        void update_debeRetornar404CuandoPerfilNoExiste() throws Exception {

        // ARRANGE
        Long id = 999L;

        UserProfileRequestDTO request =
                new UserProfileRequestDTO(
                        1L,
                        "Andres",
                        "Bustamante",
                        "912345678",
                        "Direccion",
                        LocalDate.of(2000, 5, 10),
                        true
                );

        when(service.update(any(Long.class), any(UserProfileRequestDTO.class)))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Perfil no encontrado con ID: " + id
                        )
                );

        // ACT + ASSERT
        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Perfil no encontrado con ID: 999"));

        // VERIFY
        verify(service).update(any(Long.class), any(UserProfileRequestDTO.class));
        }
}