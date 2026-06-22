package com.logiflow.mspedidos.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ.
 * Exchange: logistica.topic (tipo topic)
 * Routing keys: pedido.creado, pedido.cancelado
 */
@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    /**
     * Exchange tipo topic para eventos de logística.
     * durable=true para sobrevivir reinicios del broker.
     */
    @Bean
    public TopicExchange logisticaExchange() {
        return new TopicExchange(exchange, true, false);
    }

    /**
     * Convertidor JSON para serializar automáticamente los DTOs.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
