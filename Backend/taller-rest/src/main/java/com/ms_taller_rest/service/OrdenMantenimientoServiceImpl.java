package com.ms_taller_rest.service;

import com.ms_taller_rest.dto.*;
import com.ms_taller_rest.entity.OrdenMantenimiento;
import com.ms_taller_rest.enums.EstadoMantenimiento;
import com.ms_taller_rest.exception.MatriculaExisteException;
import com.ms_taller_rest.exception.OrdenNoExisteException;
import com.ms_taller_rest.repository.OrdenMantenimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenMantenimientoServiceImpl implements OrdenMantenimientoService {

    private final OrdenMantenimientoRepository ordenMantenimientoRepository;

    @Override
    @Transactional
    public OrdenMantenimientoResponse registrarOrden(OrdenMantenimientoRequest request) {
        if (ordenMantenimientoRepository.existsByMatricula(request.getMatricula())) {
            throw new MatriculaExisteException("La matrícula ya existe");
        }

        OrdenMantenimiento orden = new OrdenMantenimiento();
        orden.setMatricula(request.getMatricula());
        orden.setDescripcion(request.getDescripcion());
        orden.setObservacion(request.getObservacion());
        orden.setTipoMantenimiento(request.getTipoMantenimiento());
        orden.setCostoEstimado(request.getCostoEstimado());
        orden.setEstado(EstadoMantenimiento.PENDIENTE);
        orden.setFechaRegistro(LocalDateTime.now());

        OrdenMantenimiento savedOrden = ordenMantenimientoRepository.save(orden);
        return convertirAResponse(savedOrden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenMantenimientoResponse> listarOrdenes() {
        return ordenMantenimientoRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenMantenimientoResponse obtenerPorId(Long id) {
        OrdenMantenimiento orden = ordenMantenimientoRepository.findById(id)
                .orElseThrow(() -> new OrdenNoExisteException("Orden de mantenimiento no encontrada con ID: " + id));
        return convertirAResponse(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenMantenimientoResponse> obtenerPorMatricula(String matricula) {
        return ordenMantenimientoRepository.findByMatricula(matricula).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenMantenimientoResponse> obtenerPorEstado(String estado) {
        EstadoMantenimiento estadoEnum = EstadoMantenimiento.valueOf(estado.toUpperCase());
        return ordenMantenimientoRepository.findByEstado(estadoEnum).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrdenMantenimientoResponse actualizarOrden(Long id, OrdenMantenimientoRequest request) {
        OrdenMantenimiento orden = ordenMantenimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de mantenimiento no encontrada con ID: " + id));

        orden.setMatricula(request.getMatricula());
        orden.setDescripcion(request.getDescripcion());
        orden.setObservacion(request.getObservacion());
        orden.setTipoMantenimiento(request.getTipoMantenimiento());
        orden.setCostoEstimado(request.getCostoEstimado());

        OrdenMantenimiento updatedOrden = ordenMantenimientoRepository.save(orden);
        return convertirAResponse(updatedOrden);
    }

    @Override
    @Transactional
    public OrdenMantenimientoResponse actualizarEstado(Long id, String nuevoEstado) {
        OrdenMantenimiento orden = ordenMantenimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de mantenimiento no encontrada con ID: " + id));

        EstadoMantenimiento estadoEnum = EstadoMantenimiento.valueOf(nuevoEstado.toUpperCase());
        orden.setEstado(estadoEnum);

        OrdenMantenimiento updatedOrden = ordenMantenimientoRepository.save(orden);
        return convertirAResponse(updatedOrden);
    }

    @Override
    @Transactional
    public void eliminarOrden(Long id) {
        OrdenMantenimiento orden = ordenMantenimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de mantenimiento no encontrada con ID: " + id));
        ordenMantenimientoRepository.delete(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculoConsultaResponse consultarVehiculoPorMatricula(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new RuntimeException("La matrícula es obligatoria para consultar el vehículo");
        }

        VehiculoConsultaResponse response = new VehiculoConsultaResponse();
        response.setMatricula(matricula);
        response.setMarca("Hino");
        response.setModelo("XZU");
        response.setTipo("CAMION");
        response.setEstado("DISPONIBLE");
        response.setMensaje("Vehículo consultado correctamente desde ms-taller-rest");

        return response;
    }

    private OrdenMantenimientoResponse convertirAResponse(OrdenMantenimiento orden) {
        OrdenMantenimientoResponse response = new OrdenMantenimientoResponse();
        response.setId(orden.getId());
        response.setMatricula(orden.getMatricula());
        response.setDescripcion(orden.getDescripcion());
        response.setFechaRegistro(orden.getFechaRegistro());
        response.setEstado(orden.getEstado());
        response.setObservacion(orden.getObservacion());
        response.setTipoMantenimiento(orden.getTipoMantenimiento());
        response.setCostoEstimado(orden.getCostoEstimado());
        return response;
    }
}
