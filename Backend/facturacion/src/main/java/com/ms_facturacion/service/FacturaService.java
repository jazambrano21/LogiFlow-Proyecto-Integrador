package com.ms_facturacion.service;

import com.ms_facturacion.dto.FacturaRequest;
import com.ms_facturacion.dto.FacturaResponse;
import com.ms_facturacion.entity.Factura;
import com.ms_facturacion.enums.EstadoFactura;
import com.ms_facturacion.enums.NivelEntrega;
import com.ms_facturacion.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepository;

    public FacturaResponse generarFactura(FacturaRequest request) {
        Double tarifaBase = calcularTarifaBase(request.getNivelEntrega());
        Double subtotal = tarifaBase + (request.getPesoKg() * 0.50) + (request.getKilometros() * 0.10);
        Double recargo = subtotal * 0.12;
        Double total = subtotal + recargo;

        Factura factura = Factura.builder()
                .pedidoId(request.getPedidoId())
                .clienteId(request.getClienteId())
                .nivelEntrega(request.getNivelEntrega())
                .pesoKg(request.getPesoKg())
                .kilometros(request.getKilometros())
                .tarifaBase(round(tarifaBase))
                .subtotal(round(subtotal))
                .recargo(round(recargo))
                .total(round(total))
                .estado(EstadoFactura.GENERADA)
                .fechaEmision(LocalDateTime.now())
                .build();

        factura = facturaRepository.save(factura);
        return convertirAResponse(factura);
    }

    public List<FacturaResponse> listarFacturas() {
        return facturaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public FacturaResponse obtenerPorId(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));
        return convertirAResponse(factura);
    }

    public List<FacturaResponse> obtenerPorClienteId(Long clienteId) {
        return facturaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public List<FacturaResponse> obtenerPorPedidoId(Long pedidoId) {
        return facturaRepository.findByPedidoId(pedidoId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public List<FacturaResponse> obtenerPorEstado(EstadoFactura estado) {
        return facturaRepository.findByEstado(estado)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public FacturaResponse actualizarEstado(Long id, EstadoFactura nuevoEstado) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));
        factura.setEstado(nuevoEstado);
        factura = facturaRepository.save(factura);
        return convertirAResponse(factura);
    }

    public FacturaResponse anularFactura(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));
        factura.setEstado(EstadoFactura.ANULADA);
        factura = facturaRepository.save(factura);
        return convertirAResponse(factura);
    }

    public void eliminarFactura(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));
        facturaRepository.delete(factura);
    }

    public Double calcularTarifaBase(NivelEntrega nivelEntrega) {
        return switch (nivelEntrega) {
            case LOCAL -> 2.00;
            case PROVINCIAL -> 5.00;
            case NACIONAL -> 10.00;
        };
    }

    public FacturaResponse convertirAResponse(Factura factura) {
        return FacturaResponse.builder()
                .id(factura.getId())
                .pedidoId(factura.getPedidoId())
                .clienteId(factura.getClienteId())
                .nivelEntrega(factura.getNivelEntrega())
                .pesoKg(factura.getPesoKg())
                .kilometros(factura.getKilometros())
                .tarifaBase(factura.getTarifaBase())
                .subtotal(factura.getSubtotal())
                .recargo(factura.getRecargo())
                .total(factura.getTotal())
                .estado(factura.getEstado())
                .fechaEmision(factura.getFechaEmision())
                .build();
    }

    private Double round(Double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
