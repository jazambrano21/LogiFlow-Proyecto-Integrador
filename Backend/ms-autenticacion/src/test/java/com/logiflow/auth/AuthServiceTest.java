package com.logiflow.auth;

import com.logiflow.auth.dto.LoginRequest;
import com.logiflow.auth.dto.LoginResponse;
import com.logiflow.auth.dto.RegisterRequest;
import com.logiflow.auth.model.Rol;
import com.logiflow.auth.model.Usuario;
import com.logiflow.auth.repository.UsuarioRepository;
import com.logiflow.auth.service.AuthService;
import com.logiflow.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .nombre("Test")
                .apellido("User")
                .email("test@logiflow.com")
                .passwordHash("encodedPass")
                .rol(Rol.CLIENTE)
                .build();
    }

    @Test
    void testLoginExitoso() {
        LoginRequest request = LoginRequest.builder()
                .email("test@logiflow.com")
                .password("password")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(usuarioRepository.findByEmail("test@logiflow.com")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any(), any(), any())).thenReturn("jwt-token");
        when(jwtService.getExpirationMs()).thenReturn(86400000L);

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getType());
    }

    @Test
    void testLoginCredencialesIncorrectas() {
        LoginRequest request = LoginRequest.builder()
                .email("test@logiflow.com")
                .password("wrongpassword")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void testRegisterEmailDuplicado() {
        RegisterRequest request = RegisterRequest.builder()
                .nombre("Test")
                .apellido("User")
                .email("test@logiflow.com")
                .password("password")
                .rol(Rol.CLIENTE)
                .build();

        when(usuarioRepository.existsByEmail("test@logiflow.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(request));
        assertEquals("El email ya está registrado", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testRegisterExitoso() {
        RegisterRequest request = RegisterRequest.builder()
                .nombre("Test")
                .apellido("User")
                .email("new@logiflow.com")
                .password("password")
                .rol(Rol.CLIENTE)
                .build();

        when(usuarioRepository.existsByEmail("new@logiflow.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPass");

        assertDoesNotThrow(() -> authService.register(request));
        verify(usuarioRepository).save(any(Usuario.class));
    }
}
