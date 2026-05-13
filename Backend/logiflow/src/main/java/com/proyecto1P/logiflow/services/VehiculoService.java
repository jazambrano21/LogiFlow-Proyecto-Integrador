package com.proyecto1P.logiflow.services;

import com.proyecto1P.logiflow.dtos.VehiculoDTO;

import java.util.List;

public interface VehiculoService {

    List<VehiculoDTO> obtenerTodos();

    VehiculoDTO obtenerPorId(Long id);

    VehiculoDTO guardar(VehiculoDTO vehiculoDTO);

    VehiculoDTO actualizar(Long id, VehiculoDTO vehiculoDTO);

    List<VehiculoDTO> obtenerDisponibles();

    void eliminar(Long id);
}