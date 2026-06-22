package com.logiflow.mspedidos.service;

import com.logiflow.mspedidos.dto.*;
import com.logiflow.mspedidos.entity.EstadoPedido;
import com.logiflow.mspedidos.entity.Pedido;
import com.logiflow.mspedidos.exception.PedidoNotFoundException;
import com.logiflow.mspedidos.exception.TransicionEstadoInvalidaException;
import com.logiflow.mspedidos.messaging.PedidoEventPublisher;
import com.logiflow.mspedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoEventPublisher eventPublisher;

    @Transactional
    public PedidoResponse crear(PedidoRequest request) {
        Pedido pedido = Pedido.builder()
                .clienteId(request.getClienteId())
                .origenCiudad(request.getOrigen().getCiudad())
                .origenDireccion(request.getOrigen().getDireccion())
                .destinoCiudad(request.getDestino().getCiudad())
                .destinoDireccion(request.getDestino().getDireccion())
                .paquetePesoKg(request.getPaquete().getPesoKg())
                .paqueteDescripcion(request.getPaquete().getDescripcion())
                .prioridad(request.getPrioridad())
                .nivelGeografico(request.getNivelGeografico())
                .estado(EstadoPedido.CREADO)
                .build();

        Pedido saved = pedidoRepository.save(pedido);

        // Publicar evento pedido.creado
        PedidoEventDTO event = buildEvent(saved);
        eventPublisher.publicarPedidoCreado(event);

        return toResponse(saved);
    }

    public List<PedidoResponse> listar(EstadoPedido estado, Long clienteId) {
        List<Pedido> pedidos;
        if (estado != null && clienteId != null) {
            pedidos = pedidoRepository.findByClienteIdAndEstado(clienteId, estado);
        } else if (estado != null) {
            pedidos = pedidoRepository.findByEstado(estado);
        } else if (clienteId != null) {
            pedidos = pedidoRepository.findByClienteId(clienteId);
        } else {
            pedidos = pedidoRepository.findAll();
        }
        return pedidos.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public PedidoResponse obtenerPorId(Long id) {
        return toResponse(findPedido(id));
    }

    @Transactional
    public PedidoResponse actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = findPedido(id);
        validarTransicion(pedido.getEstado(), nuevoEstado);

        pedido.setEstado(nuevoEstado);
        Pedido saved = pedidoRepository.save(pedido);

        // Si se cancela, publicar evento pedido.cancelado
        if (nuevoEstado == EstadoPedido.CANCELADO) {
            eventPublisher.publicarPedidoCancelado(buildEvent(saved));
        }

        return toResponse(saved);
    }

    @Transactional
    public PedidoResponse cancelar(Long id) {
        return actualizarEstado(id, EstadoPedido.CANCELADO);
    }

    // ──────────────── ESTADO MACHINE ────────────────

    /**
     * Valida las transiciones permitidas:
     * CREADO → ASIGNADO, CANCELADO
     * ASIGNADO → EN_RUTA, CANCELADO
     * EN_RUTA → ENTREGADO
     * ENTREGADO / CANCELADO → (terminal, sin transición)
     */
    private void validarTransicion(EstadoPedido actual, EstadoPedido destino) {
        boolean valida = switch (actual) {
            case CREADO   -> destino == EstadoPedido.ASIGNADO  || destino == EstadoPedido.CANCELADO;
            case ASIGNADO -> destino == EstadoPedido.EN_RUTA   || destino == EstadoPedido.CANCELADO;
            case EN_RUTA  -> destino == EstadoPedido.ENTREGADO;
            default       -> false; // ENTREGADO y CANCELADO son terminales
        };

        if (!valida) {
            throw new TransicionEstadoInvalidaException(actual.name(), destino.name());
        }
    }

    // ──────────────── MAPPERS ────────────────

    private Pedido findPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException(id));
    }

    private PedidoResponse toResponse(Pedido p) {
        return PedidoResponse.builder()
                .id(p.getId())
                .clienteId(p.getClienteId())
                .origen(new UbicacionDTO(p.getOrigenCiudad(), p.getOrigenDireccion()))
                .destino(new UbicacionDTO(p.getDestinoCiudad(), p.getDestinoDireccion()))
                .paquete(new PaqueteDTO(p.getPaquetePesoKg(), p.getPaqueteDescripcion()))
                .prioridad(p.getPrioridad())
                .nivelGeografico(p.getNivelGeografico())
                .estado(p.getEstado())
                .fechaCreacion(p.getFechaCreacion())
                .fechaActualizacion(p.getFechaActualizacion())
                .build();
    }

    private PedidoEventDTO buildEvent(Pedido p) {
        return PedidoEventDTO.builder()
                .pedidoId(p.getId())
                .clienteId(p.getClienteId())
                .origen(new UbicacionDTO(p.getOrigenCiudad(), p.getOrigenDireccion()))
                .destino(new UbicacionDTO(p.getDestinoCiudad(), p.getDestinoDireccion()))
                .paquete(new PaqueteDTO(p.getPaquetePesoKg(), p.getPaqueteDescripcion()))
                .nivelGeografico(p.getNivelGeografico())
                .prioridad(p.getPrioridad())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
