package example.ms_auth.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import example.ms_auth.dto.RoleRequestDTO;
import example.ms_auth.dto.RoleResponseDTO;
import example.ms_auth.service.RoleService;
@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(roleController)
                .build();
                objectMapper = new ObjectMapper();

    }

        @Test
        void getAllRoles_DeberiaRetornar200() throws Exception {

        // ARRANGE: preparar datos y mocks.

        List<RoleResponseDTO> roles = List.of(
                RoleResponseDTO.builder()
                        .id(1L)
                        .name("ADMIN")
                        .build(),
                RoleResponseDTO.builder()
                        .id(2L)
                        .name("CLIENTE")
                        .build()
        );

        Mockito.when(roleService.getAllRoles())
                .thenReturn(roles);

        // ACT + ASSERT: ejecutar endpoint y verificar respuesta.

        mockMvc.perform(get("/api/auth/roles"))
                .andExpect(status().isOk());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(roleService)
                .getAllRoles();

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
        * - implementación de RoleService
        * - manejo de excepciones
        * - configuración del controller
        */
        }
                @Test
        void createRole_DeberiaRetornar201() throws Exception {

        // ARRANGE: preparar datos y mocks.

        RoleRequestDTO request =
                new RoleRequestDTO("ADMIN");

        RoleResponseDTO response =
                RoleResponseDTO.builder()
                        .id(1L)
                        .name("ADMIN")
                        .build();

        Mockito.when(roleService.createRole(any(RoleRequestDTO.class)))
                .thenReturn(response);

        // ACT + ASSERT: ejecutar endpoint y verificar respuesta.

        mockMvc.perform(post("/api/auth/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(roleService)
                .createRole(any(RoleRequestDTO.class));

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
        * - configuración del endpoint
        * - invocación al service
        */
        }
}