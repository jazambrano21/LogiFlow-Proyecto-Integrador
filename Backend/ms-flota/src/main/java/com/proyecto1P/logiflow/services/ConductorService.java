package com.proyecto1P.logiflow.services;

import com.proyecto1P.logiflow.dtos.ConductorDTO;

import java.util.List;

/**
 * Interfaz ConductorService.
 * Define los métodos CRUD que cualquier implementación debe cumplir.
 *
 * Métodos:
 * - obtenerTodos(): devuelve lista de todos los conductores.
 * - obtenerPorId(id): devuelve un conductor por su id.
 * - guardar(dto): crea un nuevo conductor.
 * - actualizar(id, dto): actualiza un conductor existente.
 * - eliminar(id): elimina un conductor.
 */
public interface ConductorService {

    List<ConductorDTO> obtenerTodos();

    ConductorDTO obtenerPorId(Long id);

    ConductorDTO guardar(ConductorDTO conductorDTO);

    ConductorDTO actualizar(Long id, ConductorDTO conductorDTO);

    void eliminar(Long id);
}