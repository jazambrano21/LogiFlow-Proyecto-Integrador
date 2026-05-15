package com.ms_taller_rest.controller;

import com.ms_taller_rest.dto.*;
import com.ms_taller_rest.service.OrdenMantenimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taller")
@RequiredArgsConstructor
@Tag(name = "Taller - Órdenes de Mantenimiento", description = "API para gestión de órdenes de mantenimiento de vehículos")
public class OrdenMantenimientoController {

    private final OrdenMantenimientoService ordenMantenimientoService;

    @PostMapping("/ordenes-mantenimiento")
    @Operation(summary = "Registrar orden de mantenimiento", description = "Crea una nueva orden de mantenimiento para un vehículo")
    public ResponseEntity<OrdenMantenimientoResponse> registrarOrden(@Valid @RequestBody OrdenMantenimientoRequest request) {
        OrdenMantenimientoResponse response = ordenMantenimientoService.registrarOrden(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/registrar-orden-mantenimiento")
    @Operation(summary = "Registrar orden de mantenimiento para taller", description = "Alias REST del caso de uso registrarOrdenMantenimiento(matricula, descripcion)")
    public ResponseEntity<OrdenMantenimientoResponse> registrarOrdenMantenimiento(
            @Valid @RequestBody OrdenMantenimientoRequest request) {
        return registrarOrden(request);
    }

    @GetMapping("/ordenes-mantenimiento")
    @Operation(summary = "Listar todas las órdenes", description = "Retorna todas las órdenes de mantenimiento registradas")
    public ResponseEntity<List<OrdenMantenimientoResponse>> listarOrdenes() {
        List<OrdenMantenimientoResponse> response = ordenMantenimientoService.listarOrdenes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ordenes-mantenimiento/{id}")
    @Operation(summary = "Obtener orden por ID", description = "Busca una orden de mantenimiento por su identificador")
    public ResponseEntity<OrdenMantenimientoResponse> obtenerPorId(
            @Parameter(description = "ID de la orden de mantenimiento") @PathVariable Long id) {
        OrdenMantenimientoResponse response = ordenMantenimientoService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ordenes-mantenimiento/matricula/{matricula}")
    @Operation(summary = "Obtener órdenes por matrícula", description = "Busca todas las órdenes asociadas a una matrícula de vehículo")
    public ResponseEntity<List<OrdenMantenimientoResponse>> obtenerPorMatricula(
            @Parameter(description = "Matrícula del vehículo") @PathVariable String matricula) {
        List<OrdenMantenimientoResponse> response = ordenMantenimientoService.obtenerPorMatricula(matricula);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ordenes-mantenimiento/estado/{estado}")
    @Operation(summary = "Obtener órdenes por estado", description = "Busca todas las órdenes con un estado específico")
    public ResponseEntity<List<OrdenMantenimientoResponse>> obtenerPorEstado(
            @Parameter(description = "Estado de la orden (PENDIENTE, EN_PROCESO, FINALIZADO, CANCELADO)") @PathVariable String estado) {
        List<OrdenMantenimientoResponse> response = ordenMantenimientoService.obtenerPorEstado(estado);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/ordenes-mantenimiento/{id}")
    @Operation(summary = "Actualizar orden de mantenimiento", description = "Actualiza la información completa de una orden de mantenimiento")
    public ResponseEntity<OrdenMantenimientoResponse> actualizarOrden(
            @Parameter(description = "ID de la orden de mantenimiento") @PathVariable Long id,
            @Valid @RequestBody OrdenMantenimientoRequest request) {
        OrdenMantenimientoResponse response = ordenMantenimientoService.actualizarOrden(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/ordenes-mantenimiento/{id}/estado")
    @Operation(summary = "Actualizar estado de la orden", description = "Actualiza solo el estado de una orden de mantenimiento")
    public ResponseEntity<OrdenMantenimientoResponse> actualizarEstado(
            @Parameter(description = "ID de la orden de mantenimiento") @PathVariable Long id,
            @Valid @RequestBody EstadoMantenimientoRequest estadoRequest) {
        OrdenMantenimientoResponse response = ordenMantenimientoService.actualizarEstado(id, estadoRequest.getEstado().name());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/ordenes-mantenimiento/{id}")
    @Operation(summary = "Eliminar orden de mantenimiento", description = "Elimina una orden de mantenimiento por su ID")
    public ResponseEntity<Void> eliminarOrden(
            @Parameter(description = "ID de la orden de mantenimiento") @PathVariable Long id) {
        ordenMantenimientoService.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehiculos/{matricula}")
    @Operation(summary = "Consultar vehículo por matrícula", description = "Consulta o simula la consulta de información de un vehículo por su matrícula")
    public ResponseEntity<VehiculoConsultaResponse> consultarVehiculoPorMatricula(
            @Parameter(description = "Matrícula del vehículo") @PathVariable String matricula) {
        VehiculoConsultaResponse response = ordenMantenimientoService.consultarVehiculoPorMatricula(matricula);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consultar-vehiculo/{matricula}")
    @Operation(summary = "Consultar vehículo para taller", description = "Alias REST del caso de uso consultarVehiculo(matricula)")
    public ResponseEntity<VehiculoConsultaResponse> consultarVehiculo(
            @Parameter(description = "Matrícula del vehículo") @PathVariable String matricula) {
        return consultarVehiculoPorMatricula(matricula);
    }
}
