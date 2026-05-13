package com.logiflow.auth.dto;

import com.logiflow.auth.model.Rol;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;
    private String type;
    private UUID userId;
    private String email;
    private Rol rol;
    private Long expiresIn;
}
