package com.logiflow.msruteo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Payload del evento "envio.asignado" publicado al exchange logistica.topic.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvioAsignadoEvent {
    private Long envioId;
    private Long pedidoId;
    private Long vehiculoId;
    private Long conductorId;
    private LocalDateTime horarioEstimado;
    private LocalDateTime timestamp;
}
