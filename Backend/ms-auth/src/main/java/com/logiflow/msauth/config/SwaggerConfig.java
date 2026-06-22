package com.logiflow.msauth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI msAuthOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-auth API")
                        .description("Microservicio de autenticación y autorización JWT — LogiFlow Fase 2")
                        .version("1.0")
                        .contact(new Contact().name("LogiFlow Team")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresa el JWT obtenido en /auth/login")));
    }
}
