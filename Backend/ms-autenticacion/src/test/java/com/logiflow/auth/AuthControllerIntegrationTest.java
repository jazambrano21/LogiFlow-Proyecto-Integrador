package com.logiflow.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logiflow.auth.dto.LoginRequest;
import com.logiflow.auth.dto.RegisterRequest;
import com.logiflow.auth.model.Rol;
import com.logiflow.auth.model.Usuario;
import com.logiflow.auth.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        usuarioRepository.save(Usuario.builder()
                .nombre("Admin")
                .apellido("LogiFlow")
                .email("admin@logiflow.com")
                .passwordHash(passwordEncoder.encode("admin123"))
                .rol(Rol.ADMIN)
                .build());
    }

    @Test
    void testRegister() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .nombre("Juan")
                .apellido("Perez")
                .email("juan@logiflow.com")
                .password("password123")
                .rol(Rol.CLIENTE)
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("admin@logiflow.com")
                .password("admin123")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testVerifySinToken() throws Exception {
        mockMvc.perform(get("/api/v1/auth/verify")
                        .header("Authorization", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }

    @Test
    void testListarUsuariosSinAuth() throws Exception {
        mockMvc.perform(get("/api/v1/auth/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    void testCambiarRolSinAuth() throws Exception {
        mockMvc.perform(put("/api/v1/auth/usuarios/{id}/rol", "550e8400-e29b-41d4-a716-446655440000")
                        .param("rol", "OPERADOR"))
                .andExpect(status().isOk());
    }
}
