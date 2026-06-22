package com.logiflow.msruteo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta del endpoint GET /api/vehiculos/disponibles de ms-flota.
 * Solo se mapean los campos necesarios para el algoritmo de asignación.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDisponibleDTO {
    private Long id;
    private String matricula;
    private String tipo;         // MOTO, AUTO, FURGONETA, CAMION
    private Double capacidad;
    private String estado;
    private Long conductorId;    // null si no tiene conductor asignado
}
