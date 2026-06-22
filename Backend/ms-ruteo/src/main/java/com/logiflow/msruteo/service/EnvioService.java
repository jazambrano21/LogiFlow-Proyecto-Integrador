package com.logiflow.msruteo.service;

import com.logiflow.msruteo.client.FlotaClient;
import com.logiflow.msruteo.dto.*;
import com.logiflow.msruteo.entity.Envio;
import com.logiflow.msruteo.entity.EstadoEnvio;
import com.logiflow.msruteo.exception.EnvioNotFoundException;
import com.logiflow.msruteo.exception.TransicionEstadoInvalidaException;
import com.logiflow.msruteo.exception.VehiculoNoDisponibleException;
import com.logiflow.msruteo.messaging.EnvioEventPublisher;
import com.logiflow.msruteo.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final FlotaClient flotaClient;
    private final AsignacionService asignacionService;
    private final EnvioEventPublisher eventPublisher;

    // ──────────────── ASIGNACIÓN AUTOMÁTICA (desde RabbitMQ) ────────────────

    /**
     * Procesa un evento pedido.creado:
     * 1. Consulta vehículos disponibles en ms-flota
     * 2. Aplica algoritmo de selección
     * 3. Persiste el envío
     * 4. Publica evento envio.asignado
     */
    @Transactional
    public Envio procesarPedidoCreado(PedidoCreadoEvent event) {
        log.info("Procesando pedido.creado para pedidoId={}, nivel={}",
                event.getPedidoId(), event.getNivelGeografico());

        Double pesoKg = event.getPaquete() != null ? event.getPaquete().getPesoKg() : 0.0;

        List<VehiculoDisponibleDTO> disponibles =
                flotaClient.obtenerVehiculosDisponibles(pesoKg);

        VehiculoDisponibleDTO vehiculo = asignacionService
                .seleccionar(disponibles, event.getNivelGeografico(), pesoKg)
                .orElseThrow(() -> new VehiculoNoDisponibleException(
                        "No hay vehículos disponibles para nivel " +
                        event.getNivelGeografico() + " y peso " + pesoKg + " kg"));

        double kms = asignacionService.estimarKms(event.getNivelGeografico());

        Envio envio = Envio.builder()
                .pedidoId(event.getPedidoId())
                .vehiculoId(vehiculo.getId())
                .conductorId(vehiculo.getConductorId())
                .rutaOrigenCiudad(event.getOrigen() != null ? event.getOrigen().getCiudad() : "")
                .rutaOrigenDireccion(event.getOrigen() != null ? event.getOrigen().getDireccion() : "")
                .rutaDestinoCiudad(event.getDestino() != null ? event.getDestino().getCiudad() : "")
                .rutaDestinoDireccion(event.getDestino() != null ? event.getDestino().getDireccion() : "")
                .kmsEstimados(kms)
                .horarioEstimado(LocalDateTime.now().plusHours(estimarHoras(kms)))
                .estado(EstadoEnvio.ASIGNADO)
                .build();

        Envio saved = envioRepository.save(envio);

        eventPublisher.publicarEnvioAsignado(EnvioAsignadoEvent.builder()
                .envioId(saved.getId())
                .pedidoId(saved.getPedidoId())
                .vehiculoId(saved.getVehiculoId())
                .conductorId(saved.getConductorId())
                .horarioEstimado(saved.getHorarioEstimado())
                .timestamp(LocalDateTime.now())
                .build());

        return saved;
    }

    // ──────────────── ASIGNACIÓN MANUAL ────────────────

    @Transactional
    public EnvioResponse asignarManual(AsignarManualRequest request) {
        double kms = 50.0; // valor base para asignación manual

        // Si se especifican origen/destino, usar datos del request
        String origenCiudad    = request.getOrigen() != null ? request.getOrigen().getCiudad() : "";
        String origenDireccion = request.getOrigen() != null ? request.getOrigen().getDireccion() : "";
        String destCiudad      = request.getDestino() != null ? request.getDestino().getCiudad() : "";
        String destDireccion   = request.getDestino() != null ? request.getDestino().getDireccion() : "";

        Envio envio = Envio.builder()
                .pedidoId(request.getPedidoId())
                .vehiculoId(request.getVehiculoId())
                .conductorId(request.getConductorId())
                .rutaOrigenCiudad(origenCiudad)
                .rutaOrigenDireccion(origenDireccion)
                .rutaDestinoCiudad(destCiudad)
                .rutaDestinoDireccion(destDireccion)
                .kmsEstimados(kms)
                .horarioEstimado(LocalDateTime.now().plusHours(2))
                .estado(EstadoEnvio.ASIGNADO)
                .build();

        Envio saved = envioRepository.save(envio);

        eventPublisher.publicarEnvioAsignado(EnvioAsignadoEvent.builder()
                .envioId(saved.getId())
                .pedidoId(saved.getPedidoId())
                .vehiculoId(saved.getVehiculoId())
                .conductorId(saved.getConductorId())
                .horarioEstimado(saved.getHorarioEstimado())
                .timestamp(LocalDateTime.now())
                .build());

        return toResponse(saved);
    }

    // ──────────────── CONSULTAS Y CAMBIO DE ESTADO ────────────────

    public List<EnvioResponse> listar(EstadoEnvio estado, Long pedidoId, Long vehiculoId) {
        List<Envio> envios;
        if (estado != null && pedidoId != null) {
            envios = envioRepository.findByEstadoAndPedidoId(estado, pedidoId);
        } else if (estado != null && vehiculoId != null) {
            envios = envioRepository.findByEstadoAndVehiculoId(estado, vehiculoId);
        } else if (estado != null) {
            envios = envioRepository.findByEstado(estado);
        } else if (pedidoId != null) {
            envios = envioRepository.findByPedidoId(pedidoId);
        } else if (vehiculoId != null) {
            envios = envioRepository.findByVehiculoId(vehiculoId);
        } else {
            envios = envioRepository.findAll();
        }
        return envios.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public EnvioResponse obtenerPorId(Long id) {
        return toResponse(findEnvio(id));
    }

    @Transactional
    public EnvioResponse actualizarEstado(Long id, EstadoEnvio nuevoEstado) {
        Envio envio = findEnvio(id);
        validarTransicion(envio.getEstado(), nuevoEstado);
        envio.setEstado(nuevoEstado);
        Envio saved = envioRepository.save(envio);

        if (nuevoEstado == EstadoEnvio.ENTREGADO) {
            eventPublisher.publicarPedidoEntregado(PedidoEntregadoEvent.builder()
                    .envioId(saved.getId())
                    .pedidoId(saved.getPedidoId())
                    .vehiculoId(saved.getVehiculoId())
                    .conductorId(saved.getConductorId())
                    .fechaEntrega(LocalDateTime.now())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        return toResponse(saved);
    }

    // ──────────────── ESTADO MACHINE ────────────────

    /**
     * Transiciones válidas: ASIGNADO → EN_RUTA → ENTREGADO
     */
    private void validarTransicion(EstadoEnvio actual, EstadoEnvio destino) {
        boolean valida = switch (actual) {
            case ASIGNADO  -> destino == EstadoEnvio.EN_RUTA;
            case EN_RUTA   -> destino == EstadoEnvio.ENTREGADO;
            case ENTREGADO -> false;
        };
        if (!valida) {
            throw new TransicionEstadoInvalidaException(actual.name(), destino.name());
        }
    }

    // ──────────────── HELPERS ────────────────

    private Envio findEnvio(Long id) {
        return envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNotFoundException(id));
    }

    private long estimarHoras(double kms) {
        // 60 km/h promedio
        return Math.max(1, Math.round(kms / 60));
    }

    private EnvioResponse toResponse(Envio e) {
        return EnvioResponse.builder()
                .id(e.getId())
                .pedidoId(e.getPedidoId())
                .vehiculoId(e.getVehiculoId())
                .conductorId(e.getConductorId())
                .ruta(EnvioResponse.RutaDTO.builder()
                        .origen(new UbicacionDTO(e.getRutaOrigenCiudad(), e.getRutaOrigenDireccion()))
                        .destino(new UbicacionDTO(e.getRutaDestinoCiudad(), e.getRutaDestinoDireccion()))
                        .kmsEstimados(e.getKmsEstimados())
                        .build())
                .horarioEstimado(e.getHorarioEstimado())
                .estado(e.getEstado())
                .fechaCreacion(e.getFechaCreacion())
                .fechaActualizacion(e.getFechaActualizacion())
                .build();
    }
}
