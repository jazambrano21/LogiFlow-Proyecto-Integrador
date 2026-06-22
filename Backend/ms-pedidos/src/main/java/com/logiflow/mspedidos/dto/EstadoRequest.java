package com.logiflow.mspedidos.dto;

import com.logiflow.mspedidos.entity.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoPedido estado;
}
