package com.logiflow.auth.service;

import com.logiflow.auth.dto.LoginRequest;
import com.logiflow.auth.dto.LoginResponse;
import com.logiflow.auth.dto.RegisterRequest;
import com.logiflow.auth.model.Usuario;
import com.logiflow.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .build();

        usuarioRepository.save(usuario);
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
        
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(
                usuario.getId().toString(),
                usuario.getEmail(),
                usuario.getRol().name()
        );

        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(usuario.getId())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .expiresIn(jwtService.getExpirationMs() / 1000)
                .build();
    }
}
