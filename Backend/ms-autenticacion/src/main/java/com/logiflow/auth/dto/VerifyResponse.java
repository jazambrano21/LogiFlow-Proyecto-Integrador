package com.logiflow.auth.dto;

import com.logiflow.auth.model.Rol;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyResponse {

    private Boolean valid;
    private UUID userId;
    private String email;
    private Rol rol;
    private String message;
}
