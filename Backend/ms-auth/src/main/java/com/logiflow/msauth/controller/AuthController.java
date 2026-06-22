package com.logiflow.msauth.controller;

import com.logiflow.msauth.dto.ApiResponse;
import com.logiflow.msauth.dto.LoginRequest;
import com.logiflow.msauth.dto.RegisterRequest;
import com.logiflow.msauth.dto.TokenPayloadDTO;
import com.logiflow.msauth.entity.Usuario;
import com.logiflow.msauth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Registro, login y verificación de tokens JWT")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario",
               description = "Crea un usuario con contraseña hasheada en bcrypt. Roles: CLIENTE, CONDUCTOR, OPERADOR, ADMIN")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email ya registrado")
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@Valid @RequestBody RegisterRequest request) {
        Usuario usuario = authService.register(request);
        Map<String, Object> data = Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "email", usuario.getEmail(),
                "rol", usuario.getRol(),
                "fechaCreacion", usuario.getFechaCreacion()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Usuario registrado correctamente", data));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión",
               description = "Autentica al usuario y retorna un JWT firmado con payload: { userId, email, rol }")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login exitoso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login exitoso", Map.of("token", token)));
    }

    @GetMapping("/verify")
    @Operation(summary = "Verificar token JWT",
               description = "Valida el token enviado en el header Authorization: Bearer <token> y retorna el payload decodificado")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token válido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Token inválido o expirado")
    })
    public ResponseEntity<ApiResponse<TokenPayloadDTO>> verify(
            @Parameter(description = "Bearer token", required = true)
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Header Authorization inválido. Formato: Bearer <token>"));
        }

        String token = authHeader.substring(7);
        TokenPayloadDTO payload = authService.verify(token);
        return ResponseEntity.ok(ApiResponse.ok("Token válido", payload));
    }
}
