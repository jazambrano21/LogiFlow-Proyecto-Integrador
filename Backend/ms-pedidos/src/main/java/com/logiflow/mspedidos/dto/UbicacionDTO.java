package com.logiflow.mspedidos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO {

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
}
