package com.logiflow.msruteo.dto;

import com.logiflow.msruteo.entity.EstadoEnvio;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoEnvioRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoEnvio estado;
}
