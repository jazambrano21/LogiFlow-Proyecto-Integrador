package com.logiflow.auth.dto;

import com.logiflow.auth.model.Rol;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDto {

    private UUID id;
    private String nombre;
    private String apellido;
    private String email;
    private Rol rol;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
