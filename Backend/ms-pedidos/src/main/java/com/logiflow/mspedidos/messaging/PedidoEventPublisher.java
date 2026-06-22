package com.logiflow.mspedidos.messaging;

import com.logiflow.mspedidos.dto.PedidoEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Publica eventos de pedido al exchange 'logistica.topic' en RabbitMQ.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key.pedido-creado}")
    private String routingKeyCreado;

    @Value("${rabbitmq.routing-key.pedido-cancelado}")
    private String routingKeyCancelado;

    public void publicarPedidoCreado(PedidoEventDTO event) {
        log.info("Publicando evento pedido.creado para pedidoId={}", event.getPedidoId());
        rabbitTemplate.convertAndSend(exchange, routingKeyCreado, event);
    }

    public void publicarPedidoCancelado(PedidoEventDTO event) {
        log.info("Publicando evento pedido.cancelado para pedidoId={}", event.getPedidoId());
        rabbitTemplate.convertAndSend(exchange, routingKeyCancelado, event);
    }
}
