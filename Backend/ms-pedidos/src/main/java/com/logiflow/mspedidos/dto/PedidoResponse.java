package com.logiflow.mspedidos.dto;

import com.logiflow.mspedidos.entity.EstadoPedido;
import com.logiflow.mspedidos.entity.NivelGeografico;
import com.logiflow.mspedidos.entity.Prioridad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private Long id;
    private Long clienteId;
    private UbicacionDTO origen;
    private UbicacionDTO destino;
    private PaqueteDTO paquete;
    private Prioridad prioridad;
    private NivelGeografico nivelGeografico;
    private EstadoPedido estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
