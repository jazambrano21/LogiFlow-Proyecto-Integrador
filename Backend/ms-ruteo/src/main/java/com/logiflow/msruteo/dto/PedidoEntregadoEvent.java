package com.logiflow.msruteo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Payload del evento "pedido.entregado" publicado cuando el envío llega a ENTREGADO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEntregadoEvent {
    private Long envioId;
    private Long pedidoId;
    private Long vehiculoId;
    private Long conductorId;
    private LocalDateTime fechaEntrega;
    private LocalDateTime timestamp;
}
