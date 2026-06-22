package com.logiflow.mspedidos.controller;

import com.logiflow.mspedidos.dto.*;
import com.logiflow.mspedidos.entity.EstadoPedido;
import com.logiflow.mspedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gestión de pedidos con publicación de eventos a RabbitMQ")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Crear pedido",
               description = "Crea un pedido en estado CREADO y publica evento 'pedido.creado' a RabbitMQ")
    public ResponseEntity<ApiResponse<PedidoResponse>> crear(@Valid @RequestBody PedidoRequest request) {
        PedidoResponse created = pedidoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Pedido creado correctamente", created));
    }

    @GetMapping
    @Operation(summary = "Listar pedidos",
               description = "Lista todos los pedidos. Filtros opcionales: estado, clienteId")
    public ResponseEntity<ApiResponse<List<PedidoResponse>>> listar(
            @Parameter(description = "Filtrar por estado") @RequestParam(required = false) EstadoPedido estado,
            @Parameter(description = "Filtrar por clienteId") @RequestParam(required = false) Long clienteId) {
        return ResponseEntity.ok(ApiResponse.ok("Pedidos obtenidos", pedidoService.listar(estado, clienteId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID")
    public ResponseEntity<ApiResponse<PedidoResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Pedido encontrado", pedidoService.obtenerPorId(id)));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pedido",
               description = "Transiciones válidas: CREADO→ASIGNADO, CREADO→CANCELADO, ASIGNADO→EN_RUTA, ASIGNADO→CANCELADO, EN_RUTA→ENTREGADO")
    public ResponseEntity<ApiResponse<PedidoResponse>> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequest estadoRequest) {
        PedidoResponse updated = pedidoService.actualizarEstado(id, estadoRequest.getEstado());
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido",
               description = "Cambia el estado a CANCELADO y publica evento 'pedido.cancelado' a RabbitMQ")
    public ResponseEntity<ApiResponse<PedidoResponse>> cancelar(@PathVariable Long id) {
        PedidoResponse cancelled = pedidoService.cancelar(id);
        return ResponseEntity.ok(ApiResponse.ok("Pedido cancelado", cancelled));
    }
}
