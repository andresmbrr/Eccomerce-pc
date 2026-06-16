package example.ms_carrito.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msCarritoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Carrito API")
                        .version("1.0")
                        .description("Documentación OpenAPI del microservicio de carrito de compras con integración Feign a productos y stock."))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8085")
                                .description("Servidor local ms-carrito")
                ));
    }
}