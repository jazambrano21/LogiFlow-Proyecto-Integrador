package com.ms_taller_rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoConsultaResponse {

    private String matricula;
    private String marca;
    private String modelo;
    private String tipo;
    private String estado;
    private String mensaje;
}
