package com.logiflow.auth;

import com.logiflow.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "logiflow-secret-key-2026-32bytes-long!!");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 86400000L);
    }

    @Test
    void testGenerarToken() {
        String token = jwtService.generateToken("550e8400-e29b-41d4-a716-446655440000", "test@logiflow.com", "ADMIN");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtraerEmail() {
        String token = jwtService.generateToken("550e8400-e29b-41d4-a716-446655440000", "test@logiflow.com", "ADMIN");
        assertEquals("test@logiflow.com", jwtService.extractEmail(token));
    }

    @Test
    void testExtraerUserId() {
        String token = jwtService.generateToken("550e8400-e29b-41d4-a716-446655440000", "test@logiflow.com", "ADMIN");
        assertEquals("550e8400-e29b-41d4-a716-446655440000", jwtService.extractUserId(token));
    }

    @Test
    void testExtraerRol() {
        String token = jwtService.generateToken("550e8400-e29b-41d4-a716-446655440000", "test@logiflow.com", "ADMIN");
        assertEquals("ADMIN", jwtService.extractRol(token));
    }

    @Test
    void testTokenNoExpirado() {
        String token = jwtService.generateToken("550e8400-e29b-41d4-a716-446655440000", "test@logiflow.com", "ADMIN");
        assertFalse(jwtService.isTokenExpired(token));
    }
}
