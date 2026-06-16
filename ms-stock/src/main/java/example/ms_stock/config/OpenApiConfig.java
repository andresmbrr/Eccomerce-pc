package example.ms_stock.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msStockOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Stock API")
                        .version("1.0")
                        .description("Documentación OpenAPI del microservicio de inventario y stock de productos."))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8084")
                                .description("Servidor local ms-stock")
                ));
    }
}