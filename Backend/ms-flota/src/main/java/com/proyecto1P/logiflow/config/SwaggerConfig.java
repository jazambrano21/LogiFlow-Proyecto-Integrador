package com.proyecto1P.logiflow.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI logiFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-flota API REST")
                        .description("API REST para gestión de flota, vehículos, conductores y disponibilidad para ruteo")
                        .version("1.0")
                        .contact(new Contact().name("Proyecto LogiFlow"))
                        .license(new License()
                                .name("API License")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación del proyecto Fase 1"));
    }
}
