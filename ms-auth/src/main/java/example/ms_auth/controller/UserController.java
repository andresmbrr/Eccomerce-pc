package example.ms_auth.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.ms_auth.dto.LoginRequestDTO;
import example.ms_auth.dto.LoginResponseDTO;
import example.ms_auth.dto.UserRequestDTO;
import example.ms_auth.dto.UserResponseDTO;
import example.ms_auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth/register")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO dto){

        log.info("POST /users ejecutado");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
         @RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(userService.login(dto));
}

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){

        log.info("GET /users ejecutado");

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id){

        log.info("GET /users/{} ejecutado", id);

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto){

        log.info("PUT /users/{} ejecutado", id);

        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){

        log.info("DELETE /users/{} ejecutado", id);

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
