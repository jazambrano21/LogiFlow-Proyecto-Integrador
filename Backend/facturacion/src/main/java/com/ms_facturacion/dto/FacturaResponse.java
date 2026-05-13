package com.ms_facturacion.dto;

import com.ms_facturacion.enums.EstadoFactura;
import com.ms_facturacion.enums.NivelEntrega;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaResponse {

    private Long id;
    private Long pedidoId;
    private Long clienteId;
    private NivelEntrega nivelEntrega;
    private Double pesoKg;
    private Double kilometros;
    private Double tarifaBase;
    private Double subtotal;
    private Double recargo;
    private Double total;
    private EstadoFactura estado;
    private LocalDateTime fechaEmision;
}
