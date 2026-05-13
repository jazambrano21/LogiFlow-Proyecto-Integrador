package com.ms.ruteo_asignacion.dto;

import com.ms.ruteo_asignacion.enums.EstadoEnvio;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioResponse {

    private Long id;
    private Long pedidoId;
    private Long vehiculoId;
    private Long conductorId;
    private String rutaEstimada;
    private Double kilometros;
    private EstadoEnvio estado;
    private LocalDateTime fechaAsignacion;
}
