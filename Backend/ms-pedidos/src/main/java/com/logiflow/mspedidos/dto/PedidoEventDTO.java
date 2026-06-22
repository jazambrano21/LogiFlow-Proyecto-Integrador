package com.logiflow.mspedidos.dto;

import com.logiflow.mspedidos.entity.NivelGeografico;
import com.logiflow.mspedidos.entity.Prioridad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Payload publicado a RabbitMQ para los eventos pedido.creado y pedido.cancelado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEventDTO {
    private Long pedidoId;
    private Long clienteId;
    private UbicacionDTO origen;
    private UbicacionDTO destino;
    private PaqueteDTO paquete;
    private NivelGeografico nivelGeografico;
    private Prioridad prioridad;
    private LocalDateTime timestamp;
}
