package com.logiflow.msclientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaCorporativaResponse {
    private Long id;
    private Long clienteId;
    private String nombreEmpresa;
    private String ruc;
    private BigDecimal saldo;
    private String contrato;
}
