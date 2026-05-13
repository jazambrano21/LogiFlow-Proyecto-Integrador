package com.ms.ruteo_asignacion.controller;

import com.ms.ruteo_asignacion.dto.EnvioRequest;
import com.ms.ruteo_asignacion.dto.EnvioResponse;
import com.ms.ruteo_asignacion.dto.EstadoEnvioRequest;
import com.ms.ruteo_asignacion.service.EnvioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ruteo")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    @PostMapping("/asignar")
    public ResponseEntity<EnvioResponse> asignar(@Valid @RequestBody EnvioRequest request) {
        EnvioResponse response = envioService.asignarEnvio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/envios")
    public ResponseEntity<List<EnvioResponse>> listar() {
        return ResponseEntity.ok(envioService.listarEnvios());
    }

    @GetMapping("/envios/{id}")
    public ResponseEntity<EnvioResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(envioService.obtenerPorId(id));
    }

    @GetMapping("/envios/pedido/{pedidoId}")
    public ResponseEntity<List<EnvioResponse>> obtenerPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(envioService.obtenerPorPedidoId(pedidoId));
    }

    @GetMapping("/envios/vehiculo/{vehiculoId}")
    public ResponseEntity<List<EnvioResponse>> obtenerPorVehiculo(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(envioService.obtenerPorVehiculoId(vehiculoId));
    }

    @GetMapping("/envios/conductor/{conductorId}")
    public ResponseEntity<List<EnvioResponse>> obtenerPorConductor(@PathVariable Long conductorId) {
        return ResponseEntity.ok(envioService.obtenerPorConductorId(conductorId));
    }

    @PutMapping("/envios/{id}/estado")
    public ResponseEntity<EnvioResponse> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoEnvioRequest request) {
        return ResponseEntity.ok(envioService.actualizarEstado(id, request.getEstado()));
    }

    @DeleteMapping("/envios/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        envioService.eliminarEnvio(id);
        return ResponseEntity.noContent().build();
    }
}
