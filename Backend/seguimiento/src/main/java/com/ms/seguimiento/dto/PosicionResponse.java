package com.ms.seguimiento.dto;

import com.ms.seguimiento.enums.EstadoPosicion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PosicionResponse {

    private Long id;
    private Long envioId;
    private Double latitud;
    private Double longitud;
    private Double velocidad;
    private EstadoPosicion estado;
    private LocalDateTime fechaHora;
}
