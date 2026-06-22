package com.logiflow.msauth.service;

import com.logiflow.msauth.dto.TokenPayloadDTO;
import com.logiflow.msauth.entity.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Servicio para generación y validación de tokens JWT.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un JWT firmado con el payload { userId, email, rol }.
     */
    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("userId", usuario.getId())
                .claim("email", usuario.getEmail())
                .claim("rol", usuario.getRol().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida el token y retorna el payload decodificado.
     * Lanza JwtException si el token es inválido o está expirado.
     */
    public TokenPayloadDTO validateAndExtract(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return TokenPayloadDTO.builder()
                .userId(claims.get("userId", Long.class))
                .email(claims.get("email", String.class))
                .rol(claims.get("rol", String.class))
                .build();
    }
}
