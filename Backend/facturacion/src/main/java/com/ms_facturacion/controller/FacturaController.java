package com.ms_facturacion.controller;

import com.ms_facturacion.dto.EstadoFacturaRequest;
import com.ms_facturacion.dto.FacturaRequest;
import com.ms_facturacion.dto.FacturaResponse;
import com.ms_facturacion.enums.EstadoFactura;
import com.ms_facturacion.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
@Tag(name = "Facturación", description = "API para gestión de facturas")
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping("/generar")
    @Operation(summary = "Generar una nueva factura")
    public ResponseEntity<FacturaResponse> generarFactura(@Valid @RequestBody FacturaRequest request) {
        FacturaResponse response = facturaService.generarFactura(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas las facturas")
    public ResponseEntity<List<FacturaResponse>> listarFacturas() {
        List<FacturaResponse> response = facturaService.listarFacturas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener factura por ID")
    public ResponseEntity<FacturaResponse> obtenerPorId(@PathVariable Long id) {
        FacturaResponse response = facturaService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener facturas por cliente ID")
    public ResponseEntity<List<FacturaResponse>> obtenerPorClienteId(@PathVariable Long clienteId) {
        List<FacturaResponse> response = facturaService.obtenerPorClienteId(clienteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Obtener facturas por pedido ID")
    public ResponseEntity<List<FacturaResponse>> obtenerPorPedidoId(@PathVariable Long pedidoId) {
        List<FacturaResponse> response = facturaService.obtenerPorPedidoId(pedidoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener facturas por estado")
    public ResponseEntity<List<FacturaResponse>> obtenerPorEstado(@PathVariable EstadoFactura estado) {
        List<FacturaResponse> response = facturaService.obtenerPorEstado(estado);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de factura")
    public ResponseEntity<FacturaResponse> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoFacturaRequest request) {
        FacturaResponse response = facturaService.actualizarEstado(id, request.getEstado());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/anular")
    @Operation(summary = "Anular factura")
    public ResponseEntity<FacturaResponse> anularFactura(@PathVariable Long id) {
        FacturaResponse response = facturaService.anularFactura(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar factura")
    public ResponseEntity<Void> eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }
}
