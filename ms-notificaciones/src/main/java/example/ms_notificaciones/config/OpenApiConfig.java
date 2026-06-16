package example.ms_notificaciones.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msNotificacionesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Notificaciones API")
                        .version("1.0")
                        .description("Documentación OpenAPI del microservicio de notificaciones de usuario."))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8088")
                                .description("Servidor local ms-notificaciones")
                ));
    }
}