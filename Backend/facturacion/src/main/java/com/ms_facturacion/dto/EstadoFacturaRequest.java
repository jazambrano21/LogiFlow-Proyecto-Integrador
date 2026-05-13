package com.ms_facturacion.dto;

import com.ms_facturacion.enums.EstadoFactura;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoFacturaRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoFactura estado;
}
