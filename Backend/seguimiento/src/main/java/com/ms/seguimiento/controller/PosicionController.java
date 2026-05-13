package com.ms.seguimiento.controller;

import com.ms.seguimiento.dto.EstadoPosicionRequest;
import com.ms.seguimiento.dto.PosicionRequest;
import com.ms.seguimiento.dto.PosicionResponse;
import com.ms.seguimiento.service.PosicionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguimiento")
@RequiredArgsConstructor
public class PosicionController {

    private final PosicionService posicionService;

    @PostMapping("/posiciones")
    public ResponseEntity<PosicionResponse> registrarPosicion(@Valid @RequestBody PosicionRequest request) {
        PosicionResponse response = posicionService.registrarPosicion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posiciones")
    public ResponseEntity<List<PosicionResponse>> listarPosiciones() {
        List<PosicionResponse> response = posicionService.listarPosiciones();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posiciones/{id}")
    public ResponseEntity<PosicionResponse> obtenerPorId(@PathVariable Long id) {
        PosicionResponse response = posicionService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/envio/{envioId}")
    public ResponseEntity<List<PosicionResponse>> obtenerPorEnvioId(@PathVariable Long envioId) {
        List<PosicionResponse> response = posicionService.obtenerPorEnvioId(envioId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ultima-posicion/{envioId}")
    public ResponseEntity<PosicionResponse> obtenerUltimaPosicion(@PathVariable Long envioId) {
        PosicionResponse response = posicionService.obtenerUltimaPosicion(envioId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/posiciones/{id}/estado")
    public ResponseEntity<PosicionResponse> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoPosicionRequest request) {
        PosicionResponse response = posicionService.actualizarEstado(id, request.getEstado());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/posiciones/{id}")
    public ResponseEntity<Void> eliminarPosicion(@PathVariable Long id) {
        posicionService.eliminarPosicion(id);
        return ResponseEntity.noContent().build();
    }
}
