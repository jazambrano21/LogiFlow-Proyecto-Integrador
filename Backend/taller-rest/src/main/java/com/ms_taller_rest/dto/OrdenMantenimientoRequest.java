package com.ms_taller_rest.dto;

import com.ms_taller_rest.enums.TipoMantenimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenMantenimientoRequest {

    @NotBlank(message = "La matrícula es obligatoria")
    private String matricula;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private String observacion;

    @NotNull(message = "El tipo de mantenimiento es obligatorio")
    private TipoMantenimiento tipoMantenimiento;

    private Double costoEstimado;
}
