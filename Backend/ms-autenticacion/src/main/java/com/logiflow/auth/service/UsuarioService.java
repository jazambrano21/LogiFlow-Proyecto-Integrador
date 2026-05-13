package com.logiflow.auth.service;

import com.logiflow.auth.dto.UsuarioDto;
import com.logiflow.auth.model.Usuario;
import com.logiflow.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Page<UsuarioDto> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(this::toDto);
    }

    public UsuarioDto cambiarRol(UUID id, String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setRol(com.logiflow.auth.model.Rol.valueOf(nuevoRol));
        return toDto(usuarioRepository.save(usuario));
    }

    private UsuarioDto toDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .createdAt(usuario.getCreatedAt())
                .updatedAt(usuario.getUpdatedAt())
                .build();
    }
}
