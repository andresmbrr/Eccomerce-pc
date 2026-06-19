package example.ms_auth.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import example.ms_auth.dto.LoginRequestDTO;
import example.ms_auth.dto.LoginResponseDTO;
import example.ms_auth.dto.UserRequestDTO;
import example.ms_auth.dto.UserResponseDTO;
import example.ms_auth.exception.ResourceNotFoundException;
import example.ms_auth.model.Role;
import example.ms_auth.model.User;
import example.ms_auth.repository.RoleRepository;
import example.ms_auth.repository.UserRepository;
import example.ms_auth.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

        @Test
    void createUser_DeberiaCrearUsuarioCorrectamente() {

        // ARRANGE: preparar datos y mocks.

        UserRequestDTO request =
                new UserRequestDTO(
                        "admin",
                        "admin@test.com",
                        "123456",
                        1L
                );

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@test.com")
                .password("password_encriptada")
                .role(role)
                .build();

        Mockito.when(
                userRepository.existsByEmail(
                        "admin@test.com"))
                .thenReturn(false);

        Mockito.when(
                roleRepository.findById(1L))
                .thenReturn(Optional.of(role));

        Mockito.when(
                passwordEncoder.encode("123456"))
                .thenReturn("password_encriptada");

        Mockito.when(
                userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // ACT: ejecutar método.

        UserResponseDTO response =
                service.createUser(request);

        // ASSERT: verificar resultado esperado.

        assertNotNull(response);

        assertEquals(1L, response.getId());

        assertEquals(
                "admin",
                response.getUsername());

        assertEquals(
                "admin@test.com",
                response.getEmail());

        assertEquals(
                "ADMIN",
                response.getRole());

        // VERIFY: comprobar llamadas a mocks.

        Mockito.verify(userRepository)
                .existsByEmail("admin@test.com");

        Mockito.verify(roleRepository)
                .findById(1L);

        Mockito.verify(passwordEncoder)
                .encode("123456");

        Mockito.verify(userRepository)
                .save(any(User.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Usuario creado correctamente.
        *
        * Se obtuvo:
        * Excepción durante el registro.
        *
        * Revisar:
        * - búsqueda de rol
        * - validación de email duplicado
        * - cifrado de contraseña
        * - persistencia del usuario
        */
    }
        @Test
    void getAllUsers_DeberiaRetornarListaDeUsuarios() {

        // ARRANGE: preparar datos y mocks.

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        User user1 = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@test.com")
                .password("123456")
                .role(role)
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("cliente")
                .email("cliente@test.com")
                .password("654321")
                .role(role)
                .build();

        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        // ACT: ejecutar método.

        List<UserResponseDTO> response =
                service.getAllUsers();

        // ASSERT: verificar resultado esperado.

        assertNotNull(response);

        assertEquals(2, response.size());

        assertEquals(
                "admin",
                response.get(0).getUsername());

        assertEquals(
                "cliente",
                response.get(1).getUsername());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userRepository)
                .findAll();

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Lista con 2 usuarios.
        *
        * Se obtuvo:
        * Lista vacía.
        *
        * Revisar:
        * - consulta findAll()
        * - mapeo User -> UserResponseDTO
        * - datos retornados por repositorio
        */
    }
        @Test
    void getUserById_DeberiaRetornarUsuario() {

        // ARRANGE: preparar datos y mocks.

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@test.com")
                .password("123456")
                .role(role)
                .build();

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // ACT: ejecutar método.

        UserResponseDTO response =
                service.getUserById(1L);

        // ASSERT: verificar resultado esperado.

        assertNotNull(response);

        assertEquals(1L, response.getId());

        assertEquals(
                "admin",
                response.getUsername());

        assertEquals(
                "admin@test.com",
                response.getEmail());

        assertEquals(
                "ADMIN",
                response.getRole());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userRepository)
                .findById(1L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Usuario encontrado correctamente.
        *
        * Se obtuvo:
        * ResourceNotFoundException.
        *
        * Revisar:
        * - búsqueda por ID
        * - datos existentes en repositorio
        * - mapeo User -> UserResponseDTO
        */
    }
        @Test
    void updateUser_DeberiaActualizarUsuarioCorrectamente() {

        // ARRANGE: preparar datos y mocks.

        UserRequestDTO request =
                new UserRequestDTO(
                        "admin_actualizado",
                        "nuevo@test.com",
                        "654321",
                        1L
                );

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        User existingUser = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@test.com")
                .password("password_antigua")
                .role(role)
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .username("admin_actualizado")
                .email("nuevo@test.com")
                .password("password_encriptada")
                .role(role)
                .build();

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        Mockito.when(roleRepository.findById(1L))
                .thenReturn(Optional.of(role));

        Mockito.when(passwordEncoder.encode("654321"))
                .thenReturn("password_encriptada");

        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(updatedUser);

        // ACT: ejecutar método.

        UserResponseDTO response =
                service.updateUser(1L, request);

        // ASSERT: verificar resultado esperado.

        assertNotNull(response);

        assertEquals(1L, response.getId());

        assertEquals(
                "admin_actualizado",
                response.getUsername());

        assertEquals(
                "nuevo@test.com",
                response.getEmail());

        assertEquals(
                "ADMIN",
                response.getRole());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userRepository)
                .findById(1L);

        Mockito.verify(roleRepository)
                .findById(1L);

        Mockito.verify(passwordEncoder)
                .encode("654321");

        Mockito.verify(userRepository)
                .save(any(User.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Usuario actualizado correctamente.
        *
        * Se obtuvo:
        * ResourceNotFoundException.
        *
        * Revisar:
        * - existencia del usuario
        * - existencia del rol
        * - actualización de datos
        * - guardado en repositorio
        */
    }
        @Test
    void deleteUser_DeberiaEliminarUsuarioCorrectamente() {

        // ARRANGE: preparar datos y mocks.

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@test.com")
                .password("123456")
                .role(role)
                .build();

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        Mockito.doNothing()
                .when(userRepository)
                .delete(user);

        // ACT: ejecutar método.

        service.deleteUser(1L);

        // ASSERT:
        // No se requiere assert porque el método es void.
        // Si no lanza excepción, el flujo es correcto.

        // VERIFY: comprobar llamadas a mocks.

        Mockito.verify(userRepository)
                .findById(1L);

        Mockito.verify(userRepository)
                .delete(user);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Usuario eliminado correctamente.
        *
        * Se obtuvo:
        * ResourceNotFoundException.
        *
        * Revisar:
        * - existencia del usuario
        * - ejecución de delete()
        * - búsqueda previa findById()
        */
    }
        @Test
    void login_DeberiaRetornarLoginResponseDTO() {

        // ARRANGE: preparar datos y mocks.

        LoginRequestDTO request =
                new LoginRequestDTO(
                        "admin@test.com",
                        "123456"
                );

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@test.com")
                .password("password_encriptada")
                .role(role)
                .build();

        Mockito.when(
                userRepository.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(user));

        Mockito.when(
                passwordEncoder.matches(
                        "123456",
                        "password_encriptada"))
                .thenReturn(true);

        // ACT: ejecutar método.

        LoginResponseDTO response =
                service.login(request);

        // ASSERT: verificar resultado esperado.

        assertNotNull(response);

        assertEquals(1L, response.getId());

        assertEquals(
                "admin",
                response.getUsername());

        assertEquals(
                "admin@test.com",
                response.getEmail());

        assertEquals(
                "ADMIN",
                response.getRole());

        assertEquals(
                "Login exitoso",
                response.getMessage());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userRepository)
                .findByEmail("admin@test.com");

        Mockito.verify(passwordEncoder)
                .matches(
                        "123456",
                        "password_encriptada");

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Login exitoso.
        *
        * Se obtuvo:
        * IllegalArgumentException:
        * Contraseña incorrecta.
        *
        * Revisar:
        * - búsqueda por email
        * - validación BCrypt
        * - construcción de LoginResponseDTO
        */
    }
        @Test
        void createUser_EmailDuplicado_DeberiaLanzarIllegalArgumentException() {

        // ARRANGE: preparar datos y mocks.

        UserRequestDTO request =
                new UserRequestDTO(
                        "admin",
                        "admin@test.com",
                        "123456",
                        1L
                );

        Mockito.when(
                userRepository.existsByEmail(
                        "admin@test.com"))
                .thenReturn(true);

        // ACT + ASSERT: verificar excepción.

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.createUser(request)
                );

        assertEquals(
                "Ya existe un usuario con ese email",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userRepository)
                .existsByEmail("admin@test.com");

        Mockito.verify(roleRepository, Mockito.never())
                .findById(anyLong());

        Mockito.verify(userRepository, Mockito.never())
                .save(any(User.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Error por email duplicado.
        *
        * Se obtuvo:
        * Usuario registrado correctamente.
        *
        * Revisar:
        * - validación existsByEmail()
        * - restricción de unicidad
        * - lógica createUser()
        */
        }
                @Test
        void getUserById_NoExiste_DeberiaLanzarResourceNotFoundException() {

        // ARRANGE: preparar datos y mocks.

        Mockito.when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: verificar excepción.

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.getUserById(99L)
                );

        assertEquals(
                "Usuario no encontrado con ID: 99",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userRepository)
                .findById(99L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * ResourceNotFoundException.
        *
        * Se obtuvo:
        * HTTP 200 con usuario vacío.
        *
        * Revisar:
        * - búsqueda por ID
        * - manejo de Optional.empty()
        * - lanzamiento de excepción
        */
        }
                @Test
        void updateUser_UsuarioNoExiste_DeberiaLanzarResourceNotFoundException() {

        // ARRANGE: preparar datos y mocks.

        UserRequestDTO request =
                new UserRequestDTO(
                        "admin",
                        "admin@test.com",
                        "123456",
                        1L
                );

        Mockito.when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: verificar excepción.

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.updateUser(99L, request)
                );

        assertEquals(
                "Usuario no encontrado con ID: 99",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userRepository)
                .findById(99L);

        Mockito.verify(roleRepository, Mockito.never())
                .findById(anyLong());

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(anyString());

        Mockito.verify(userRepository, Mockito.never())
                .save(any(User.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * ResourceNotFoundException.
        *
        * Se obtuvo:
        * Usuario actualizado correctamente.
        *
        * Revisar:
        * - búsqueda findById()
        * - manejo de Optional.empty()
        * - validación previa a actualización
        */
        }
                @Test
        void createUser_RolNoExiste_DeberiaLanzarResourceNotFoundException() {

        // ARRANGE: preparar datos y mocks.

        UserRequestDTO request =
                new UserRequestDTO(
                        "admin",
                        "admin@test.com",
                        "123456",
                        99L
                );

        Mockito.when(userRepository.existsByEmail("admin@test.com"))
                .thenReturn(false);

        Mockito.when(roleRepository.findById(99L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: verificar excepción.

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.createUser(request)
                );

        assertEquals(
                "Rol no encontrado con ID: 99",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userRepository)
                .existsByEmail("admin@test.com");

        Mockito.verify(roleRepository)
                .findById(99L);

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(anyString());

        Mockito.verify(userRepository, Mockito.never())
                .save(any(User.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * ResourceNotFoundException por rol inexistente.
        *
        * Se obtuvo:
        * Usuario creado correctamente.
        *
        * Revisar:
        * - validación del rol
        * - búsqueda en RoleRepository
        * - flujo createUser()
        */
        }
                @Test
        void updateUser_RolNoExiste_DeberiaLanzarResourceNotFoundException() {

        // ARRANGE: preparar datos y mocks.

        UserRequestDTO request =
                new UserRequestDTO(
                        "admin",
                        "admin@test.com",
                        "123456",
                        99L
                );

        Role currentRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@test.com")
                .password("password")
                .role(currentRole)
                .build();

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        Mockito.when(roleRepository.findById(99L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: verificar excepción.

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.updateUser(1L, request)
                );

        assertEquals(
                "Rol no encontrado con ID: 99",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        Mockito.verify(userRepository)
                .findById(1L);

        Mockito.verify(roleRepository)
                .findById(99L);

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(anyString());

        Mockito.verify(userRepository, Mockito.never())
                .save(any(User.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Error por rol inexistente.
        *
        * Se obtuvo:
        * Usuario actualizado correctamente.
        *
        * Revisar:
        * - validación del rol
        * - búsqueda en RoleRepository
        * - flujo updateUser()
        */
        }
}