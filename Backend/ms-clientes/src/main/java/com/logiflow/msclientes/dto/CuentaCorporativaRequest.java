package com.logiflow.msclientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaCorporativaRequest {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombreEmpresa;

    @NotBlank(message = "El RUC es obligatorio")
    private String ruc;

    @NotNull(message = "El saldo es obligatorio")
    @PositiveOrZero(message = "El saldo no puede ser negativo")
    private BigDecimal saldo;

    private String contrato;
}
