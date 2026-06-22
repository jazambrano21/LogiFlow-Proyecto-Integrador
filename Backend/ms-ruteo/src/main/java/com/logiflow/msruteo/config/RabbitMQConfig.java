package com.logiflow.msruteo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ para ms-ruteo.
 * Declara el exchange y la cola que este servicio consume.
 * Los bindings están pre-configurados en rabbitmq/definitions.json.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.queue.pedido-creado}")
    private String queuePedidoCreado;

    @Bean
    public TopicExchange logisticaExchange() {
        return new TopicExchange(exchange, true, false);
    }

    /** Cola que este servicio consume — declarada aquí para garantizar existencia */
    @Bean
    public Queue queuePedidoCreado() {
        return new Queue(queuePedidoCreado, true);
    }

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
