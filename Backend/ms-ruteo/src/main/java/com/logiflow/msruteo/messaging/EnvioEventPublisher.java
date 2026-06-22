package com.logiflow.msruteo.messaging;

import com.logiflow.msruteo.dto.EnvioAsignadoEvent;
import com.logiflow.msruteo.dto.PedidoEntregadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Publica eventos de envío al exchange logistica.topic.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EnvioEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key.envio-asignado}")
    private String rkEnvioAsignado;

    @Value("${rabbitmq.routing-key.pedido-entregado}")
    private String rkPedidoEntregado;

    public void publicarEnvioAsignado(EnvioAsignadoEvent event) {
        log.info("Publicando envio.asignado para envioId={}, pedidoId={}",
                event.getEnvioId(), event.getPedidoId());
        rabbitTemplate.convertAndSend(exchange, rkEnvioAsignado, event);
    }

    public void publicarPedidoEntregado(PedidoEntregadoEvent event) {
        log.info("Publicando pedido.entregado para pedidoId={}", event.getPedidoId());
        rabbitTemplate.convertAndSend(exchange, rkPedidoEntregado, event);
    }
}
