package com.logiflow.msruteo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaqueteDTO {
    private Double pesoKg;
    private String descripcion;
}
