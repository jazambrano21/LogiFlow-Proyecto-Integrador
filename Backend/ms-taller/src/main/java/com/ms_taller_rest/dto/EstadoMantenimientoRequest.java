package com.ms_taller_rest.dto;

import com.ms_taller_rest.enums.EstadoMantenimiento;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoMantenimientoRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoMantenimiento estado;
}
