package example.ms_auth.service;

import java.util.List;

import example.ms_auth.dto.LoginRequestDTO;
import example.ms_auth.dto.LoginResponseDTO;
import example.ms_auth.dto.UserRequestDTO;
import example.ms_auth.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO dto);

    void deleteUser(Long id);
    
    LoginResponseDTO login(LoginRequestDTO dto);

    
}