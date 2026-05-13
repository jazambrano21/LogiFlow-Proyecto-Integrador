package com.proyecto1P.logiflow.services;

import com.proyecto1P.logiflow.dtos.MantenimientoDTO;
import java.util.List;

public interface MantenimientoService {
    List<MantenimientoDTO> obtenerTodos();
    MantenimientoDTO obtenerPorId(Long id);
    MantenimientoDTO guardar(MantenimientoDTO mantenimientoDTO);
    MantenimientoDTO actualizar(Long id, MantenimientoDTO mantenimientoDTO);
    void eliminar(Long id);
}