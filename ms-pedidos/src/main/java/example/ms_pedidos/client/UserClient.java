package example.ms_pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import example.ms_pedidos.dto.UserResponseDTO;

@FeignClient(name = "ms-user")
public interface UserClient {
     @GetMapping("/api/users/{id}")
    UserResponseDTO obtenerUsuarioPorId(
            @PathVariable("id") Long id);


}
