package com.logiflow.msruteo.dto;

import com.logiflow.msruteo.entity.EstadoEnvio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvioResponse {
    private Long id;
    private Long pedidoId;
    private Long vehiculoId;
    private Long conductorId;
    private RutaDTO ruta;
    private LocalDateTime horarioEstimado;
    private EstadoEnvio estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RutaDTO {
        private UbicacionDTO origen;
        private UbicacionDTO destino;
        private Double kmsEstimados;
    }
}
