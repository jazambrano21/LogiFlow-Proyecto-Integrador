package com.ms_taller_rest.dto;

import com.ms_taller_rest.enums.EstadoMantenimiento;
import com.ms_taller_rest.enums.TipoMantenimiento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenMantenimientoResponse {

    private Long id;
    private String matricula;
    private String descripcion;
    private LocalDateTime fechaRegistro;
    private EstadoMantenimiento estado;
    private String observacion;
    private TipoMantenimiento tipoMantenimiento;
    private Double costoEstimado;
}
