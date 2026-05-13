package com.ms.seguimiento.service;

import com.ms.seguimiento.dto.PosicionRequest;
import com.ms.seguimiento.dto.PosicionResponse;
import com.ms.seguimiento.entity.Posicion;
import com.ms.seguimiento.enums.EstadoPosicion;
import com.ms.seguimiento.repository.PosicionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosicionService {

    private final PosicionRepository posicionRepository;

    public PosicionResponse registrarPosicion(PosicionRequest request) {
        Posicion posicion = Posicion.builder()
                .envioId(request.getEnvioId())
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .velocidad(request.getVelocidad())
                .estado(request.getEstado() != null ? request.getEstado() : EstadoPosicion.REGISTRADA)
                .fechaHora(LocalDateTime.now())
                .build();

        posicion = posicionRepository.save(posicion);
        return convertirAResponse(posicion);
    }

    public List<PosicionResponse> listarPosiciones() {
        return posicionRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public PosicionResponse obtenerPorId(Long id) {
        Posicion posicion = posicionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Posición no encontrada con ID: " + id));
        return convertirAResponse(posicion);
    }

    public List<PosicionResponse> obtenerPorEnvioId(Long envioId) {
        return posicionRepository.findByEnvioId(envioId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public PosicionResponse obtenerUltimaPosicion(Long envioId) {
        Posicion posicion = posicionRepository.findTopByEnvioIdOrderByFechaHoraDesc(envioId)
                .orElseThrow(() -> new RuntimeException("No se encontraron posiciones para el envío con ID: " + envioId));
        return convertirAResponse(posicion);
    }

    public PosicionResponse actualizarEstado(Long id, EstadoPosicion nuevoEstado) {
        Posicion posicion = posicionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Posición no encontrada con ID: " + id));
        
        posicion.setEstado(nuevoEstado);
        posicion = posicionRepository.save(posicion);
        return convertirAResponse(posicion);
    }

    public void eliminarPosicion(Long id) {
        Posicion posicion = posicionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Posición no encontrada con ID: " + id));
        posicionRepository.delete(posicion);
    }

    private PosicionResponse convertirAResponse(Posicion posicion) {
        return PosicionResponse.builder()
                .id(posicion.getId())
                .envioId(posicion.getEnvioId())
                .latitud(posicion.getLatitud())
                .longitud(posicion.getLongitud())
                .velocidad(posicion.getVelocidad())
                .estado(posicion.getEstado())
                .fechaHora(posicion.getFechaHora())
                .build();
    }
}
