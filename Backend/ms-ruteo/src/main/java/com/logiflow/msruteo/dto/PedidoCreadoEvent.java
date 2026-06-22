package com.logiflow.msruteo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Payload del evento "pedido.creado" recibido de RabbitMQ.
 * Publicado por ms-pedidos al exchange logistica.topic.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCreadoEvent {
    private Long pedidoId;
    private Long clienteId;
    private UbicacionDTO origen;
    private UbicacionDTO destino;
    private PaqueteDTO paquete;
    private String nivelGeografico;   // LOCAL, PROVINCIAL, NACIONAL
    private String prioridad;          // NORMAL, URGENTE
    private LocalDateTime timestamp;
}
