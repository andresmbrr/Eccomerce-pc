package example.ms_user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import example.ms_user.exception.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import example.ms_user.dto.UserProfileRequestDTO;
import example.ms_user.dto.UserProfileResponseDTO;
import example.ms_user.model.UserProfile;
import example.ms_user.repository.UserProfileRepository;
import example.ms_user.service.impl.UserProfileServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceImplTest {

    @Mock
    private UserProfileRepository repository;

    @InjectMocks
    private UserProfileServiceImpl service;

    @Test
    void create_debeCrearPerfilCuandoDatosSonValidos() {

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

        UserProfile savedProfile =
                UserProfile.builder()
                        .id(1L)
                        .authUserId(1L)
                        .firstName("Andres")
                        .lastName("Bustamante")
                        .phone("912345678")
                        .address("Av Siempre Viva 123")
                        .birthDate(LocalDate.of(2000, 5, 10))
                        .active(true)
                        .build();

        when(repository.save(any(UserProfile.class)))
                .thenReturn(savedProfile);

        // ACT: ejecutar método del service.
        UserProfileResponseDTO response =
                service.create(request);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getAuthUserId());
        assertEquals("Andres", response.getFirstName());
        assertEquals("Bustamante", response.getLastName());
        assertEquals("912345678", response.getPhone());
        assertEquals("Av Siempre Viva 123", response.getAddress());
        assertEquals(LocalDate.of(2000, 5, 10), response.getBirthDate());
        assertTrue(response.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).save(any(UserProfile.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba active = true y se obtiene active = false,
        // QA debe reportar que el perfil se crea, pero queda inactivo.
        // Desarrollo debe revisar el método create() de UserProfileServiceImpl.
    }

    @Test
    void getAll_debeRetornarListaDePerfiles() {

        // ARRANGE: preparar datos y mocks.
        UserProfile perfil1 =
                UserProfile.builder()
                        .id(1L)
                        .authUserId(1L)
                        .firstName("Andres")
                        .lastName("Bustamante")
                        .phone("912345678")
                        .address("Av Siempre Viva 123")
                        .birthDate(LocalDate.of(2000, 5, 10))
                        .active(true)
                        .build();

        UserProfile perfil2 =
                UserProfile.builder()
                        .id(2L)
                        .authUserId(2L)
                        .firstName("Matias")
                        .lastName("Latrach")
                        .phone("987654321")
                        .address("Calle Principal 456")
                        .birthDate(LocalDate.of(2001, 3, 15))
                        .active(true)
                        .build();

        when(repository.findAll())
                .thenReturn(List.of(perfil1, perfil2));

        // ACT: ejecutar método del service.
        List<UserProfileResponseDTO> response =
                service.getAll();

        // ASSERT: verificar resultado esperado.
        assertEquals(2, response.size());

        assertEquals(1L, response.get(0).getId());
        assertEquals(1L, response.get(0).getAuthUserId());
        assertEquals("Andres", response.get(0).getFirstName());
        assertEquals("Bustamante", response.get(0).getLastName());

        assertEquals(2L, response.get(1).getId());
        assertEquals(2L, response.get(1).getAuthUserId());
        assertEquals("Matias", response.get(1).getFirstName());
        assertEquals("Latrach", response.get(1).getLastName());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findAll();

        // Caso hipotético de falla para QA:
        // Si se esperaban 2 perfiles y se obtiene una lista vacía,
        // QA debe reportar que el método getAll() no está retornando
        // los perfiles existentes.
        // Desarrollo debe revisar el método getAll() de UserProfileServiceImpl.
    }

    @Test
    void getById_debeRetornarPerfilCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        UserProfile perfil =
                UserProfile.builder()
                        .id(id)
                        .authUserId(1L)
                        .firstName("Andres")
                        .lastName("Bustamante")
                        .phone("912345678")
                        .address("Av Siempre Viva 123")
                        .birthDate(LocalDate.of(2000, 5, 10))
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(perfil));

        // ACT: ejecutar método del service.
        UserProfileResponseDTO response =
                service.getById(id);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getAuthUserId());
        assertEquals("Andres", response.getFirstName());
        assertEquals("Bustamante", response.getLastName());
        assertEquals("912345678", response.getPhone());
        assertEquals("Av Siempre Viva 123", response.getAddress());
        assertEquals(LocalDate.of(2000, 5, 10), response.getBirthDate());
        assertTrue(response.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba encontrar el perfil con ID 1 y se obtiene una excepción,
        // QA debe reportar que el método getById() no está encontrando
        // un perfil existente.
        // Desarrollo debe revisar el método getById() de UserProfileServiceImpl.
    }

    @Test
    void update_debeActualizarPerfilCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        UserProfile perfilExistente =
                UserProfile.builder()
                        .id(id)
                        .authUserId(1L)
                        .firstName("Andres")
                        .lastName("Bustamante")
                        .phone("912345678")
                        .address("Av Siempre Viva 123")
                        .birthDate(LocalDate.of(2000, 5, 10))
                        .active(true)
                        .build();

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

        UserProfile perfilActualizado =
                UserProfile.builder()
                        .id(id)
                        .authUserId(1L)
                        .firstName("Andres")
                        .lastName("Bustamante Actualizado")
                        .phone("987654321")
                        .address("Nueva Direccion 456")
                        .birthDate(LocalDate.of(2000, 5, 10))
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(perfilExistente));

        when(repository.save(any(UserProfile.class)))
                .thenReturn(perfilActualizado);

        // ACT: ejecutar método del service.
        UserProfileResponseDTO response =
                service.update(id, request);

        // ASSERT: verificar resultado esperado.
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getAuthUserId());
        assertEquals("Andres", response.getFirstName());
        assertEquals("Bustamante Actualizado", response.getLastName());
        assertEquals("987654321", response.getPhone());
        assertEquals("Nueva Direccion 456", response.getAddress());
        assertEquals(LocalDate.of(2000, 5, 10), response.getBirthDate());
        assertTrue(response.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);
        verify(repository).save(any(UserProfile.class));

        // Caso hipotético de falla para QA:
        // Si se esperaba el apellido "Bustamante Actualizado"
        // y se obtiene "Bustamante",
        // QA debe reportar que el método update() no actualiza
        // correctamente los datos del perfil.
        // Desarrollo debe revisar el método update() de UserProfileServiceImpl.
    }

    @Test
    void delete_debeDesactivarPerfilCuandoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 1L;

        UserProfile perfilExistente =
                UserProfile.builder()
                        .id(id)
                        .authUserId(1L)
                        .firstName("Andres")
                        .lastName("Bustamante")
                        .phone("912345678")
                        .address("Av Siempre Viva 123")
                        .birthDate(LocalDate.of(2000, 5, 10))
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(perfilExistente));

        when(repository.save(any(UserProfile.class)))
                .thenReturn(perfilExistente);

        // ACT: ejecutar método del service.
        service.delete(id);

        // ASSERT: verificar que el perfil quedó inactivo.
        assertFalse(perfilExistente.getActive());

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);
        verify(repository).save(perfilExistente);

        // Caso hipotético de falla para QA:
        // Si se esperaba active = false y se obtiene active = true,
        // QA debe reportar que el método delete() no desactiva
        // correctamente el perfil.
        // Desarrollo debe revisar el método delete() de UserProfileServiceImpl.
    }
        @Test
        void getById_debeLanzarExcepcionCuandoPerfilNoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y verificar excepción esperada.
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.getById(id)
                );

        assertEquals(
                "Perfil no encontrado con ID: 999",
                exception.getMessage()
        );

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba ResourceNotFoundException
        // y el método retorna un UserProfileResponseDTO,
        // QA debe reportar que el servicio no está validando
        // correctamente la existencia del perfil antes de retornarlo.
        // Desarrollo debe revisar el método getById()
        // de UserProfileServiceImpl.
        }
                @Test
        void update_debeLanzarExcepcionCuandoPerfilNoExiste() {

        // ARRANGE: preparar datos y mocks.
        Long id = 999L;

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

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y verificar excepción esperada.
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.update(id, request)
                );

        assertEquals(
                "Perfil no encontrado con ID: 999",
                exception.getMessage()
        );

        // VERIFY: comprobar llamadas al mock.
        verify(repository).findById(id);

        // Caso hipotético de falla para QA:
        // Si se esperaba ResourceNotFoundException
        // y el método actualiza el registro igualmente,
        // QA debe reportar que el servicio permite modificar
        // perfiles inexistentes.
        // Desarrollo debe revisar la validación del método update()
        // en UserProfileServiceImpl.
        }
                @Test
        void delete_debeLanzarExcepcionCuandoPerfilNoExiste() {

        // ARRANGE
        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.delete(id)
                );

        // ASSERT
        assertEquals(
                "Perfil no encontrado con ID: 999",
                exception.getMessage()
        );

        // VERIFY
        verify(repository).findById(id);
        }
    
}