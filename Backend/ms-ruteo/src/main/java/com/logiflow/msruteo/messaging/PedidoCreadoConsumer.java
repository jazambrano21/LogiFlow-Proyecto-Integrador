package com.logiflow.msruteo.messaging;

import com.logiflow.msruteo.dto.PedidoCreadoEvent;
import com.logiflow.msruteo.exception.VehiculoNoDisponibleException;
import com.logiflow.msruteo.service.EnvioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor de la cola q.ruteo.pedido-creado.
 * Escucha eventos pedido.creado publicados por ms-pedidos
 * y dispara el flujo de asignación automática.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoCreadoConsumer {

    private final EnvioService envioService;

    @RabbitListener(queues = "${rabbitmq.queue.pedido-creado}")
    public void onPedidoCreado(PedidoCreadoEvent event) {
        log.info("Recibido pedido.creado: pedidoId={}, nivel={}, peso={}kg",
                event.getPedidoId(),
                event.getNivelGeografico(),
                event.getPaquete() != null ? event.getPaquete().getPesoKg() : "N/A");

        try {
            envioService.procesarPedidoCreado(event);
            log.info("Asignación completada para pedidoId={}", event.getPedidoId());
        } catch (VehiculoNoDisponibleException e) {
            // No hay vehículos disponibles en este momento.
            // El mensaje no se reencola (se descarta). En producción se podría
            // mover a una dead-letter queue para reintentar más tarde.
            log.warn("Sin vehículo disponible para pedidoId={}: {}", event.getPedidoId(), e.getMessage());
        } catch (Exception e) {
            log.error("Error procesando pedido.creado para pedidoId={}: {}",
                    event.getPedidoId(), e.getMessage(), e);
            // Re-lanzar para que RabbitMQ maneje el reintento / DLQ si está configurado
            throw e;
        }
    }
}
