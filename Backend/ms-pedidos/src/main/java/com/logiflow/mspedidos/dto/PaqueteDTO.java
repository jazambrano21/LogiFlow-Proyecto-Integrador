package com.logiflow.mspedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaqueteDTO {

    @NotNull(message = "El peso del paquete es obligatorio")
    @Positive(message = "El peso debe ser positivo")
    private Double pesoKg;

    @NotBlank(message = "La descripción del paquete es obligatoria")
    private String descripcion;
}
