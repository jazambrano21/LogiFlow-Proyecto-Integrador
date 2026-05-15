package com.ms_taller_rest.service;

import com.ms_taller_rest.dto.*;

import java.util.List;

public interface OrdenMantenimientoService {

    OrdenMantenimientoResponse registrarOrden(OrdenMantenimientoRequest request);
    List<OrdenMantenimientoResponse> listarOrdenes();
    OrdenMantenimientoResponse obtenerPorId(Long id);
    List<OrdenMantenimientoResponse> obtenerPorMatricula(String matricula);
    List<OrdenMantenimientoResponse> obtenerPorEstado(String estado);
    OrdenMantenimientoResponse actualizarOrden(Long id, OrdenMantenimientoRequest request);
    OrdenMantenimientoResponse actualizarEstado(Long id, String nuevoEstado);
    void eliminarOrden(Long id);
    VehiculoConsultaResponse consultarVehiculoPorMatricula(String matricula);
}
