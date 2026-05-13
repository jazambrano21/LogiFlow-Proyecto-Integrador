package com.ms.ruteo_asignacion.service;

import com.ms.ruteo_asignacion.dto.EnvioRequest;
import com.ms.ruteo_asignacion.dto.EnvioResponse;
import com.ms.ruteo_asignacion.entity.Envio;
import com.ms.ruteo_asignacion.enums.EstadoEnvio;
import com.ms.ruteo_asignacion.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;

    @Transactional
    public EnvioResponse asignarEnvio(EnvioRequest request) {
        Envio envio = Envio.builder()
                .pedidoId(request.getPedidoId())
                .vehiculoId(request.getVehiculoId())
                .conductorId(request.getConductorId())
                .rutaEstimada(request.getRutaEstimada())
                .kilometros(request.getKilometros())
                .estado(EstadoEnvio.ASIGNADO)
                .fechaAsignacion(LocalDateTime.now())
                .build();

        Envio guardado = envioRepository.save(envio);
        return convertirAResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<EnvioResponse> listarEnvios() {
        return envioRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EnvioResponse obtenerPorId(Long id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envio no encontrado con id: " + id));
        return convertirAResponse(envio);
    }

    @Transactional(readOnly = true)
    public List<EnvioResponse> obtenerPorPedidoId(Long pedidoId) {
        return envioRepository.findByPedidoId(pedidoId).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnvioResponse> obtenerPorVehiculoId(Long vehiculoId) {
        return envioRepository.findByVehiculoId(vehiculoId).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnvioResponse> obtenerPorConductorId(Long conductorId) {
        return envioRepository.findByConductorId(conductorId).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional
    public EnvioResponse actualizarEstado(Long id, EstadoEnvio nuevoEstado) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envio no encontrado con id: " + id));
        envio.setEstado(nuevoEstado);
        Envio actualizado = envioRepository.save(envio);
        return convertirAResponse(actualizado);
    }

    @Transactional
    public void eliminarEnvio(Long id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envio no encontrado con id: " + id));
        envioRepository.delete(envio);
    }

    private EnvioResponse convertirAResponse(Envio envio) {
        return EnvioResponse.builder()
                .id(envio.getId())
                .pedidoId(envio.getPedidoId())
                .vehiculoId(envio.getVehiculoId())
                .conductorId(envio.getConductorId())
                .rutaEstimada(envio.getRutaEstimada())
                .kilometros(envio.getKilometros())
                .estado(envio.getEstado())
                .fechaAsignacion(envio.getFechaAsignacion())
                .build();
    }
}
