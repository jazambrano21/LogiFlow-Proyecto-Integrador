package com.proyecto1P.logiflow.controller;

import com.proyecto1P.logiflow.dtos.MantenimientoDTO;
import com.proyecto1P.logiflow.services.MantenimientoService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/mantenimientos")
@Tag(name = "Mantenimientos", description = "API para gestión de mantenimientos")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @Operation(summary = "Obtener todos los mantenimientos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de mantenimientos obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<MantenimientoDTO>> obtenerTodos() {
        return ResponseEntity.ok(mantenimientoService.obtenerTodos());
    }

    @Operation(summary = "Obtener mantenimiento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mantenimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MantenimientoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mantenimientoService.obtenerPorId(id));
    }

    @Operation(summary = "Crear un nuevo mantenimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mantenimiento creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @PostMapping
    public ResponseEntity<MantenimientoDTO> crear(
            @Valid @RequestBody MantenimientoDTO mantenimientoDTO) {

        MantenimientoDTO guardado = mantenimientoService.guardar(mantenimientoDTO);

        return ResponseEntity
                .created(URI.create("/api/mantenimientos/" + guardado.getId()))
                .body(guardado);
    }

    @Operation(summary = "Actualizar un mantenimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mantenimiento actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MantenimientoDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MantenimientoDTO mantenimientoDTO) {

        return ResponseEntity.ok(
                mantenimientoService.actualizar(id, mantenimientoDTO)
        );
    }

    @Operation(summary = "Eliminar un mantenimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mantenimiento eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mantenimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}