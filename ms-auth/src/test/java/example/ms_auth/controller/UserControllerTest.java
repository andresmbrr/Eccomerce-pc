package example.ms_auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import example.ms_auth.dto.LoginRequestDTO;
import example.ms_auth.dto.LoginResponseDTO;
import example.ms_auth.dto.UserRequestDTO;
import example.ms_auth.dto.UserResponseDTO;
import example.ms_auth.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();

        objectMapper = new ObjectMapper();
    }
        @Test
    void getAllUsers_DeberiaRetornar200() throws Exception {

        // ARRANGE: preparar datos y mocks.

        List<UserResponseDTO> users = List.of(
                UserResponseDTO.builder()
                        .id(1L)
                        .username("admin")
                        .email("admin@test.com")
                        .role("ADMIN")
                        .build(),

                UserResponseDTO.builder()
                        .id(2L)
                        .username("cliente")
                        .email("cliente@test.com")
                        .role("CLIENTE")
                        .build()
        );

        Mockito.when(userService.getAllUsers())
                .thenReturn(users);

        // ACT + ASSERT: ejecutar endpoint y verificar respuesta.

        mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userService)
                .getAllUsers();

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * HTTP 200 OK
        *
        * Se obtuvo:
        * HTTP 500 Internal Server Error
        *
        * Revisar:
        * - implementación de UserService
        * - configuración del controller
        * - manejo de excepciones
        */
    }
        @Test
    void createUser_DeberiaRetornar201() throws Exception {

        // ARRANGE: preparar datos y mocks.

        UserRequestDTO request =
                new UserRequestDTO(
                        "admin",
                        "admin@test.com",
                        "123456",
                        1L
                );

        UserResponseDTO response =
                UserResponseDTO.builder()
                        .id(1L)
                        .username("admin")
                        .email("admin@test.com")
                        .role("ADMIN")
                        .build();

        Mockito.when(
                userService.createUser(
                        any(UserRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar respuesta.

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userService)
                .createUser(any(UserRequestDTO.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * HTTP 201 Created
        *
        * Se obtuvo:
        * HTTP 200 OK
        *
        * Revisar:
        * - ResponseEntity.status(HttpStatus.CREATED)
        * - implementación createUser()
        * - configuración del endpoint
        */
    }
        @Test
    void login_DeberiaRetornar200() throws Exception {

        // ARRANGE: preparar datos y mocks.

        LoginRequestDTO request =
                new LoginRequestDTO(
                        "admin@test.com",
                        "123456"
                );

        LoginResponseDTO response =
                LoginResponseDTO.builder()
                        .id(1L)
                        .username("admin")
                        .email("admin@test.com")
                        .role("ADMIN")
                        .message("Login exitoso")
                        .build();

        Mockito.when(
                userService.login(
                        any(LoginRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar respuesta.

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userService)
                .login(any(LoginRequestDTO.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * HTTP 200 OK
        *
        * Se obtuvo:
        * HTTP 401 Unauthorized
        *
        * Revisar:
        * - validación de credenciales
        * - implementación de login()
        * - configuración del endpoint
        */
    }
        @Test
    void getUserById_DeberiaRetornar200() throws Exception {

        // ARRANGE: preparar datos y mocks.

        UserResponseDTO response =
                UserResponseDTO.builder()
                        .id(1L)
                        .username("admin")
                        .email("admin@test.com")
                        .role("ADMIN")
                        .build();

        Mockito.when(userService.getUserById(1L))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar respuesta.

        mockMvc.perform(
                        get("/api/auth/users/1")
                )
                .andExpect(status().isOk());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userService)
                .getUserById(1L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * HTTP 200 OK
        *
        * Se obtuvo:
        * HTTP 404 Not Found
        *
        * Revisar:
        * - búsqueda por ID
        * - implementación getUserById()
        * - mapeo de rutas
        */
    }
        @Test
    void updateUser_DeberiaRetornar200() throws Exception {

        // ARRANGE: preparar datos y mocks.

        UserRequestDTO request =
                new UserRequestDTO(
                        "admin_actualizado",
                        "admin@test.com",
                        "123456",
                        1L
                );

        UserResponseDTO response =
                UserResponseDTO.builder()
                        .id(1L)
                        .username("admin_actualizado")
                        .email("admin@test.com")
                        .role("ADMIN")
                        .build();

        Mockito.when(
                userService.updateUser(
                        eq(1L),
                        any(UserRequestDTO.class)))
                .thenReturn(response);

        // ACT: ejecutar endpoint.

        mockMvc.perform(
                        put("/api/auth/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )

                // ASSERT: verificar resultado esperado.

                .andExpect(status().isOk());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userService)
                .updateUser(
                        eq(1L),
                        any(UserRequestDTO.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * HTTP 200 OK
        *
        * Se obtuvo:
        * HTTP 404 Not Found
        *
        * Revisar:
        * - existencia del usuario
        * - implementación updateUser()
        * - mapeo de rutas PUT
        */
    }
        @Test
    void deleteUser_DeberiaRetornar204() throws Exception {

        // ARRANGE: preparar datos y mocks.

        Mockito.doNothing()
                .when(userService)
                .deleteUser(1L);

        // ACT + ASSERT: ejecutar endpoint y verificar respuesta.

        mockMvc.perform(
                        delete("/api/auth/users/1")
                )
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userService)
                .deleteUser(1L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * HTTP 204 No Content
        *
        * Se obtuvo:
        * HTTP 404 Not Found
        *
        * Revisar:
        * - existencia del usuario
        * - implementación deleteUser()
        * - mapeo del endpoint DELETE
        */
    }
}