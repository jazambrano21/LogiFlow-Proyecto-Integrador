package com.logiflow.msruteo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignarManualRequest {

    @NotNull(message = "El pedidoId es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El vehiculoId es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "El conductorId es obligatorio")
    private Long conductorId;

    private UbicacionDTO origen;
    private UbicacionDTO destino;
}
