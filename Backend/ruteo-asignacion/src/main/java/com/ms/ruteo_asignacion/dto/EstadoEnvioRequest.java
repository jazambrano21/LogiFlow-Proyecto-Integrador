package com.ms.ruteo_asignacion.dto;

import com.ms.ruteo_asignacion.enums.EstadoEnvio;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoEnvioRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoEnvio estado;
}
