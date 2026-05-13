package com.ms.ruteo_asignacion.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioRequest {

    @NotNull(message = "El pedidoId es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El vehiculoId es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "El conductorId es obligatorio")
    private Long conductorId;

    @NotBlank(message = "La ruta estimada es obligatoria")
    @Size(max = 150, message = "La ruta estimada no puede superar los 150 caracteres")
    private String rutaEstimada;

    @NotNull(message = "Los kilometros son obligatorios")
    @Positive(message = "Los kilometros deben ser un valor positivo")
    private Double kilometros;
}
