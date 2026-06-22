package com.logiflow.mspedidos.dto;

import com.logiflow.mspedidos.entity.NivelGeografico;
import com.logiflow.mspedidos.entity.Prioridad;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoRequest {

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @Valid
    @NotNull(message = "El origen es obligatorio")
    private UbicacionDTO origen;

    @Valid
    @NotNull(message = "El destino es obligatorio")
    private UbicacionDTO destino;

    @Valid
    @NotNull(message = "El paquete es obligatorio")
    private PaqueteDTO paquete;

    @NotNull(message = "La prioridad es obligatoria")
    private Prioridad prioridad;

    @NotNull(message = "El nivel geográfico es obligatorio")
    private NivelGeografico nivelGeografico;
}
