package com.logiflow.auth.controller;

import com.logiflow.auth.dto.*;
import com.logiflow.auth.service.AuthService;
import com.logiflow.auth.service.JwtService;
import com.logiflow.auth.service.UsuarioService;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints de autenticación y gestión de usuarios")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/verify")
    @Operation(summary = "Verificar validez del token JWT")
    public ResponseEntity<VerifyResponse> verify(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(VerifyResponse.builder()
                    .valid(false)
                    .message("Header Authorization no proporcionado")
                    .build());
        }

        String token = authHeader.substring(7);
        try {
            String userId = jwtService.extractUserId(token);
            String email = jwtService.extractEmail(token);
            String rol = jwtService.extractRol(token);

            if (jwtService.isTokenExpired(token)) {
                return ResponseEntity.ok(VerifyResponse.builder()
                        .valid(false)
                        .message("Token expirado")
                        .build());
            }

            return ResponseEntity.ok(VerifyResponse.builder()
                    .valid(true)
                    .userId(UUID.fromString(userId))
                    .email(email)
                    .rol(com.logiflow.auth.model.Rol.valueOf(rol))
                    .message("Token válido")
                    .build());
        } catch (JwtException e) {
            return ResponseEntity.ok(VerifyResponse.builder()
                    .valid(false)
                    .message("Token inválido: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/usuarios")
    @Operation(summary = "Listar usuarios paginados (ADMIN)")
    public ResponseEntity<Page<UsuarioDto>> listarUsuarios(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarUsuarios(pageable));
    }

    @PutMapping("/usuarios/{id}/rol")
    @Operation(summary = "Cambiar rol de un usuario (ADMIN)")
    public ResponseEntity<UsuarioDto> cambiarRol(@PathVariable UUID id, @RequestParam String rol) {
        return ResponseEntity.ok(usuarioService.cambiarRol(id, rol));
    }
}
