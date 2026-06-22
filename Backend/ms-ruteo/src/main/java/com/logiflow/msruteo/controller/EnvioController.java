package com.logiflow.msruteo.controller;

import com.logiflow.msruteo.dto.*;
import com.logiflow.msruteo.entity.EstadoEnvio;
import com.logiflow.msruteo.service.EnvioService;
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
@RequestMapping("/envios")
@RequiredArgsConstructor
@Tag(name = "Envíos", description = "Gestión de envíos y rutas — ms-ruteo")
@SecurityRequirement(name = "bearerAuth")
public class EnvioController {

    private final EnvioService envioService;

    @GetMapping
    @Operation(
        summary = "Listar envíos",
        description = "Filtra opcionalmente por estado, pedidoId y/o vehiculoId"
    )
    public ResponseEntity<ApiResponse<List<EnvioResponse>>> listar(
            @Parameter(description = "Filtrar por estado")
            @RequestParam(required = false) EstadoEnvio estado,
            @Parameter(description = "Filtrar por pedidoId")
            @RequestParam(required = false) Long pedidoId,
            @Parameter(description = "Filtrar por vehiculoId")
            @RequestParam(required = false) Long vehiculoId) {

        return ResponseEntity.ok(
                ApiResponse.ok("Envíos obtenidos",
                        envioService.listar(estado, pedidoId, vehiculoId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de un envío con ruta completa")
    public ResponseEntity<ApiResponse<EnvioResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok("Envío encontrado", envioService.obtenerPorId(id)));
    }

    @PutMapping("/{id}/estado")
    @Operation(
        summary = "Actualizar estado del envío",
        description = "Transiciones válidas: ASIGNADO → EN_RUTA → ENTREGADO. " +
                      "Al llegar a ENTREGADO publica evento 'pedido.entregado' a RabbitMQ."
    )
    public ResponseEntity<ApiResponse<EnvioResponse>> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoEnvioRequest request) {

        EnvioResponse updated = envioService.actualizarEstado(id, request.getEstado());
        return ResponseEntity.ok(ApiResponse.ok("Estado del envío actualizado", updated));
    }

    @PostMapping("/asignar-manual")
    @Operation(
        summary = "Asignación manual de envío",
        description = "Crea un envío sin ejecutar el algoritmo automático. " +
                      "Útil cuando el operador elige directamente el vehículo y conductor."
    )
    public ResponseEntity<ApiResponse<EnvioResponse>> asignarManual(
            @Valid @RequestBody AsignarManualRequest request) {

        EnvioResponse created = envioService.asignarManual(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Envío asignado manualmente", created));
    }
}
