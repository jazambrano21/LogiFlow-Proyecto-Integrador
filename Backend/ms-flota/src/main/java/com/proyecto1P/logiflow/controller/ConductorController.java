package com.proyecto1P.logiflow.controller;

import com.proyecto1P.logiflow.dtos.ConductorDTO;
import com.proyecto1P.logiflow.services.ConductorService;
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
@RequestMapping("/api/conductores")
@Tag(name = "Conductores", description = "API para gestión de conductores")
public class ConductorController {

    private final ConductorService service;

    public ConductorController(ConductorService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todos los conductores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de conductores obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ConductorDTO>> listarTodos() {
        List<ConductorDTO> conductores = service.obtenerTodos();
        return ResponseEntity.ok(conductores);
    }

    @Operation(summary = "Obtener un conductor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conductor encontrado"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConductorDTO> obtenerPorId(@PathVariable Long id) {
        ConductorDTO conductor = service.obtenerPorId(id);
        return ResponseEntity.ok(conductor);
    }

    @Operation(summary = "Crear un nuevo conductor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conductor creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @PostMapping
    public ResponseEntity<ConductorDTO> crear(@Valid @RequestBody ConductorDTO conductorDTO) {

        ConductorDTO creado = service.guardar(conductorDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    @Operation(summary = "Actualizar un conductor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conductor actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConductorDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConductorDTO conductorDTO) {

        ConductorDTO actualizado = service.actualizar(id, conductorDTO);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar un conductor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conductor eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}