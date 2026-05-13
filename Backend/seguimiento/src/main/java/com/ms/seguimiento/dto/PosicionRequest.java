package com.ms.seguimiento.dto;

import com.ms.seguimiento.enums.EstadoPosicion;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PosicionRequest {

    @NotNull(message = "El envioId es obligatorio")
    private Long envioId;

    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    @NotNull(message = "La velocidad es obligatoria")
    @DecimalMin(value = "0.0", message = "La velocidad debe ser positiva o cero")
    private Double velocidad;

    private EstadoPosicion estado;
}
