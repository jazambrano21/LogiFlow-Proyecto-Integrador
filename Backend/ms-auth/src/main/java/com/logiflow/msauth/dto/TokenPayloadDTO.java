package com.logiflow.msauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload decodificado del JWT.
 * Devuelto por GET /auth/verify.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPayloadDTO {
    private Long userId;
    private String email;
    private String rol;
}
