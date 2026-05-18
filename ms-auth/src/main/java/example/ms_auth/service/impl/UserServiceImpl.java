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

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {

        log.info("Creando usuario con email {}", dto.getEmail());

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado"));

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(role)
                .build();

        User saved = userRepository.save(user);

        log.info("Usuario creado correctamente ID {}", saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        log.info("Listando usuarios");

        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        log.info("Buscando usuario ID {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));

        return mapToDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        log.info("Actualizando usuario ID {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado"));

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);

        User updated = userRepository.save(user);

        log.info("Usuario actualizado ID {}", updated.getId());

        return mapToDTO(updated);
    }

    @Override
    public void deleteUser(Long id) {

        log.info("Eliminando usuario ID {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));

        userRepository.delete(user);

        log.info("Usuario eliminado ID {}", id);
    }

    private UserResponseDTO mapToDTO(User user){

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }
       @Override
      public LoginResponseDTO login(LoginRequestDTO dto) {

         User user = userRepository.findByEmail(dto.getEmail())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Usuario no encontrado"));

       if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
           throw new RuntimeException("Contraseña incorrecta");
        }

        return LoginResponseDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .role(user.getRole().getName())
            .message("Login exitoso")
            .build();
        }
}