package example.ms_pagos.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msPagosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Pagos API")
                        .version("1.0")
                        .description("Documentación OpenAPI del microservicio de pagos con integración Feign a pedidos."))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8087")
                                .description("Servidor local ms-pagos")
                ));
    }
}