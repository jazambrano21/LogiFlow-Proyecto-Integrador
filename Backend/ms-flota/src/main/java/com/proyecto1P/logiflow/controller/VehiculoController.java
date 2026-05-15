package com.proyecto1P.logiflow.controller;

import com.proyecto1P.logiflow.dtos.VehiculoDTO;
import com.proyecto1P.logiflow.services.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@Tag(name = "Vehículos", description = "API para gestión de vehículos")
public class VehiculoController {

    private final VehiculoService service;

    public VehiculoController(VehiculoService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todos los vehículos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vehículos obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> listarTodos() {
        List<VehiculoDTO> vehiculos = service.obtenerTodos();
        return ResponseEntity.ok(vehiculos);
    }

    @Operation(summary = "Obtener un vehículo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDTO> obtenerPorId(@PathVariable Long id) {
        VehiculoDTO vehiculo = service.obtenerPorId(id);
        return ResponseEntity.ok(vehiculo);
    }

    @Operation(summary = "Obtener un vehículo por matrícula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<VehiculoDTO> obtenerPorMatricula(@PathVariable String matricula) {
        return ResponseEntity.ok(service.obtenerPorMatricula(matricula));
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Obtener vehículos disponibles", description = "Retorna vehículos con estado DISPONIBLE")
    public ResponseEntity<List<VehiculoDTO>> obtenerDisponibles(
            @RequestParam(required = false) Double capacidadMinima) {
        return ResponseEntity.ok(service.obtenerDisponibles(capacidadMinima));
    }

    @GetMapping("/disponibilidad")
    @Operation(summary = "Consultar disponibilidad para ruteo", description = "Retorna vehículos disponibles y opcionalmente filtra por capacidad mínima")
    public ResponseEntity<List<VehiculoDTO>> consultarDisponibilidad(
            @RequestParam(required = false) Double capacidadMinima) {
        return ResponseEntity.ok(service.obtenerDisponibles(capacidadMinima));
    }

    @Operation(summary = "Crear un nuevo vehículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehículo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @PostMapping
    public ResponseEntity<VehiculoDTO> crear(@Valid @RequestBody VehiculoDTO vehiculoDTO) {

        VehiculoDTO creado = service.guardar(vehiculoDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    @Operation(summary = "Actualizar un vehículo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoDTO vehiculoDTO) {

        VehiculoDTO actualizado = service.actualizar(id, vehiculoDTO);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar un vehículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehículo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
