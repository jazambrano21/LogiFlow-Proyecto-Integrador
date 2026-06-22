package com.logiflow.mspedidos.entity;

/**
 * Estados del ciclo de vida de un pedido.
 * Transiciones válidas:
 * CREADO → ASIGNADO → EN_RUTA → ENTREGADO
 * CREADO → CANCELADO
 * ASIGNADO → CANCELADO
 */
public enum EstadoPedido {
    CREADO,
    ASIGNADO,
    EN_RUTA,
    ENTREGADO,
    CANCELADO
}
