package example.ms_auth.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import example.ms_auth.dto.RoleRequestDTO;
import example.ms_auth.dto.RoleResponseDTO;
import example.ms_auth.model.Role;
import example.ms_auth.repository.RoleRepository;
import example.ms_auth.service.impl.RoleServiceImpl;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private RoleServiceImpl service;

        @Test
    void createRole_DeberiaCrearRolCorrectamente() {

        // ARRANGE: preparar datos y mocks.

        RoleRequestDTO request =
                new RoleRequestDTO("ADMIN");

        Role savedRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        Mockito.when(
                repository.existsByName("ADMIN"))
                .thenReturn(false);

        Mockito.when(
                repository.save(any(Role.class)))
                .thenReturn(savedRole);

        // ACT: ejecutar método.

        RoleResponseDTO response =
                service.createRole(request);

        // ASSERT: verificar resultado esperado.

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId());

        assertEquals(
                "ADMIN",
                response.getName());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .existsByName("ADMIN");

        Mockito.verify(repository)
                .save(any(Role.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Rol creado correctamente.
        *
        * Se obtuvo:
        * IllegalArgumentException.
        *
        * Revisar:
        * - validación de duplicados
        * - guardado del rol
        * - datos enviados
        */
    }
        @Test
    void getAllRoles_DeberiaRetornarListaDeRoles() {

        // ARRANGE: preparar datos y mocks.

        Role role1 = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        Role role2 = Role.builder()
                .id(2L)
                .name("CLIENTE")
                .build();

        Mockito.when(repository.findAll())
                .thenReturn(List.of(role1, role2));

        // ACT: ejecutar método.

        List<RoleResponseDTO> response =
                service.getAllRoles();

        // ASSERT: verificar resultado esperado.

        assertNotNull(response);

        assertEquals(2, response.size());

        assertEquals(
                "ADMIN",
                response.get(0).getName());

        assertEquals(
                "CLIENTE",
                response.get(1).getName());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .findAll();

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Lista con 2 roles.
        *
        * Se obtuvo:
        * Lista vacía.
        *
        * Revisar:
        * - consulta findAll()
        * - mapeo Role -> RoleResponseDTO
        * - datos retornados por repositorio
        */
    }
        @Test
    void createRole_RolDuplicado_DeberiaLanzarIllegalArgumentException() {

        // ARRANGE: preparar datos y mocks.

        RoleRequestDTO request =
                new RoleRequestDTO("ADMIN");

        Mockito.when(
                repository.existsByName("ADMIN"))
                .thenReturn(true);

        // ACT + ASSERT: verificar excepción.

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.createRole(request)
                );

        assertEquals(
                "Ya existe un rol con el nombre: ADMIN",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(repository)
                .existsByName("ADMIN");

        Mockito.verify(repository, Mockito.never())
                .save(any(Role.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Mensaje indicando rol duplicado.
        *
        * Se obtuvo:
        * El rol fue creado nuevamente.
        *
        * Revisar:
        * - validación existsByName()
        * - restricción de unicidad
        * - lógica createRole()
        */
    }


}