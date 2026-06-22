package com.logiflow.mspedidos;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test de integración mínimo para ms-pedidos.
 *
 * Estrategia:
 * - Perfil "test" → H2 en memoria, sin MySQL real
 * - RabbitAutoConfiguration excluida via application-test.properties
 * - RabbitTemplate mockeado con @MockBean para que el contexto Spring
 *   pueda resolver la inyección de dependencias en PedidoService
 *   y PedidoEventPublisher sin necesitar broker real.
 */
@SpringBootTest
@ActiveProfiles("test")
class MsPedidosApplicationTests {

    /**
     * Mock de RabbitTemplate: evita que PedidoEventPublisher
     * intente conectarse a un broker AMQP durante los tests de CI.
     */
    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        // Verifica que el contexto Spring arranca correctamente
        // con H2 en memoria y sin RabbitMQ real.
    }
}
