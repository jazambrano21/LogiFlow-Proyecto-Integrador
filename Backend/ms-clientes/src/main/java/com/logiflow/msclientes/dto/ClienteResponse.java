package com.logiflow.msclientes.dto;

import com.logiflow.msclientes.entity.TipoCliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private TipoCliente tipo;
    private LocalDateTime fechaRegistro;
}
