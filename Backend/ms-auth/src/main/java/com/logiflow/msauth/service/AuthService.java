package com.logiflow.msauth.service;

import com.logiflow.msauth.dto.LoginRequest;
import com.logiflow.msauth.dto.RegisterRequest;
import com.logiflow.msauth.dto.TokenPayloadDTO;
import com.logiflow.msauth.entity.Usuario;
import com.logiflow.msauth.exception.EmailAlreadyExistsException;
import com.logiflow.msauth.exception.InvalidCredentialsException;
import com.logiflow.msauth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lógica de negocio para autenticación y verificación.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Registra un nuevo usuario con contraseña hasheada en bcrypt.
     */
    @Transactional
    public Usuario register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("El email '" + request.getEmail() + "' ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .activo(true)
                .build();

        return usuarioRepository.save(usuario);
    }

    /**
     * Autentica al usuario y retorna el JWT firmado.
     */
    public String login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        if (!usuario.isActivo()) {
            throw new InvalidCredentialsException("El usuario está inactivo");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        return jwtService.generateToken(usuario);
    }

    /**
     * Valida el token JWT y retorna el payload decodificado.
     */
    public TokenPayloadDTO verify(String token) {
        return jwtService.validateAndExtract(token);
    }
}
