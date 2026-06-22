package com.logiflow.msruteo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configura RestTemplate como bean Spring para que pueda ser
 * inyectado en FlotaClient y mockeado con @MockBean en tests.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
