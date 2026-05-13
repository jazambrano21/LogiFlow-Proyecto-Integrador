package com.ms_facturacion.dto;

import com.ms_facturacion.enums.NivelEntrega;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaRequest {

    @NotNull(message = "El pedidoId es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @NotNull(message = "El nivel de entrega es obligatorio")
    private NivelEntrega nivelEntrega;

    @NotNull(message = "El peso en kg es obligatorio")
    @Positive(message = "El peso en kg debe ser positivo")
    private Double pesoKg;

    @NotNull(message = "Los kilómetros son obligatorios")
    @PositiveOrZero(message = "Los kilómetros deben ser positivos o cero")
    private Double kilometros;
}
