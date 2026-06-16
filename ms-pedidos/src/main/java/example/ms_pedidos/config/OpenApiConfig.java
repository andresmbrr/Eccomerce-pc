package example.ms_pedidos.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msPedidosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Pedidos API")
                        .version("1.0")
                        .description("Documentación OpenAPI del microservicio de pedidos y estados de pedido."))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8086")
                                .description("Servidor local ms-pedidos")
                ));
    }
}