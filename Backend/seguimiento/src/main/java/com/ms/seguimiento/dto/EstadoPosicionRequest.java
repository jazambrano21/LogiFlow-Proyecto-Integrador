package com.ms.seguimiento.dto;

import com.ms.seguimiento.enums.EstadoPosicion;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoPosicionRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoPosicion estado;
}
