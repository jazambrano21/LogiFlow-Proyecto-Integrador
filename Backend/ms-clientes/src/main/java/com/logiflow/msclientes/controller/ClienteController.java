package com.logiflow.msclientes.controller;

import com.logiflow.msclientes.dto.*;
import com.logiflow.msclientes.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "CRUD de clientes y cuentas corporativas")
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {

    private final ClienteService clienteService;

    // ──────────────── CLIENTES ────────────────

    @GetMapping
    @Operation(summary = "Listar todos los clientes")
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok("Clientes obtenidos", clienteService.listarTodos()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID")
    public ResponseEntity<ApiResponse<ClienteResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Cliente encontrado", clienteService.obtenerPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo cliente")
    public ResponseEntity<ApiResponse<ClienteResponse>> crear(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse created = clienteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cliente creado correctamente", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente")
    public ResponseEntity<ApiResponse<ClienteResponse>> actualizar(
            @PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Cliente actualizado", clienteService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Cliente eliminado", null));
    }

    // ──────────────── CUENTA CORPORATIVA ────────────────

    @PostMapping("/{id}/cuenta-corporativa")
    @Operation(summary = "Crear cuenta corporativa para un cliente")
    public ResponseEntity<ApiResponse<CuentaCorporativaResponse>> crearCuenta(
            @PathVariable Long id, @Valid @RequestBody CuentaCorporativaRequest request) {
        CuentaCorporativaResponse created = clienteService.crearCuenta(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cuenta corporativa creada", created));
    }

    @GetMapping("/{id}/cuenta-corporativa")
    @Operation(summary = "Obtener cuenta corporativa de un cliente")
    public ResponseEntity<ApiResponse<CuentaCorporativaResponse>> obtenerCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Cuenta corporativa encontrada", clienteService.obtenerCuenta(id)));
    }

    @PutMapping("/{id}/cuenta-corporativa")
    @Operation(summary = "Actualizar cuenta corporativa de un cliente")
    public ResponseEntity<ApiResponse<CuentaCorporativaResponse>> actualizarCuenta(
            @PathVariable Long id, @Valid @RequestBody CuentaCorporativaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Cuenta corporativa actualizada",
                clienteService.actualizarCuenta(id, request)));
    }
}
