package example.ms_auth.service.impl;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import example.ms_auth.dto.LoginRequestDTO;
import example.ms_auth.dto.LoginResponseDTO;
import example.ms_auth.dto.UserRequestDTO;
import example.ms_auth.dto.UserResponseDTO;
import example.ms_auth.exception.ResourceNotFoundException;
import example.ms_auth.model.Role;
import example.ms_auth.model.User;
import example.ms_auth.repository.RoleRepository;
import example.ms_auth.repository.UserRepository;
import example.ms_auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {

        log.info("Creando usuario con email: {}", dto.getEmail());

        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("Intento de registrar email duplicado: {}", dto.getEmail());
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> {
                    log.warn("Rol no encontrado con ID: {}", dto.getRoleId());
                    return new ResourceNotFoundException(
                            "Rol no encontrado con ID: " + dto.getRoleId());
                });

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(role)
                .build();

        User saved = userRepository.save(user);

        log.info("Usuario creado correctamente con ID: {}", saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        log.info("Listando usuarios auth");

        List<UserResponseDTO> users = userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();

        log.info("Usuarios auth encontrados: {}", users.size());

        return users;
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        log.info("Buscando usuario auth ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con ID: {}", id);
                    return new ResourceNotFoundException(
                            "Usuario no encontrado con ID: " + id);
                });

        return mapToDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        log.info("Actualizando usuario auth ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con ID: {}", id);
                    return new ResourceNotFoundException(
                            "Usuario no encontrado con ID: " + id);
                });

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> {
                    log.warn("Rol no encontrado con ID: {}", dto.getRoleId());
                    return new ResourceNotFoundException(
                            "Rol no encontrado con ID: " + dto.getRoleId());
                });

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);

        User updated = userRepository.save(user);

        log.info("Usuario actualizado correctamente con ID: {}", updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void deleteUser(Long id) {

        log.info("Eliminando usuario auth ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con ID: {}", id);
                    return new ResourceNotFoundException(
                            "Usuario no encontrado con ID: " + id);
                });

        userRepository.delete(user);

        log.info("Usuario eliminado correctamente con ID: {}", id);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {

        log.info("Intento de login con email: {}", dto.getEmail());

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado para login: {}", dto.getEmail());
                    return new ResourceNotFoundException("Usuario no encontrado");
                });

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            log.warn("Contraseña incorrecta para email: {}", dto.getEmail());
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        log.info("Login exitoso para email: {}", dto.getEmail());

        return LoginResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .message("Login exitoso")
                .build();
    }

    private UserResponseDTO mapToDTO(User user) {

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }
}