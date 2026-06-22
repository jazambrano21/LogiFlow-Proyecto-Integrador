package com.logiflow.msruteo;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

/**
 * Test de integración mínimo para ms-ruteo.
 *
 * Estrategia:
 * - Perfil "test" → H2 en memoria, sin MySQL real
 * - RabbitAutoConfiguration excluida via application-test.properties
 * - RabbitTemplate mockeado: EnvioEventPublisher no publica eventos reales
 * - RestTemplate mockeado: FlotaClient no llama a ms-flota-rest real
 */
@SpringBootTest
@ActiveProfiles("test")
class MsRuteoApplicationTests {

    /**
     * Mock de RabbitTemplate: evita que EnvioEventPublisher
     * intente conectarse al broker AMQP durante tests de CI.
     */
    @MockBean
    private RabbitTemplate rabbitTemplate;

    /**
     * Mock de RestTemplate: evita que FlotaClient
     * intente hacer HTTP a ms-flota-rest (no disponible en CI).
     */
    @MockBean
    private RestTemplate restTemplate;

    @Test
    void contextLoads() {
        // Verifica que el contexto Spring arranca correctamente
        // con H2 en memoria, sin RabbitMQ ni ms-flota-rest reales.
    }
}
