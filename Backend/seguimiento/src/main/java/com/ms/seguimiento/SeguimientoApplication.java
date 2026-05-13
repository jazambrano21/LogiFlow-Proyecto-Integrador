package com.ms.seguimiento;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SeguimientoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeguimientoApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("MS Seguimiento API")
						.version("1.0")
						.description("Microservicio de seguimiento para LogiFlow")
						.license(new License().name("Apache 2.0")));
	}

}
