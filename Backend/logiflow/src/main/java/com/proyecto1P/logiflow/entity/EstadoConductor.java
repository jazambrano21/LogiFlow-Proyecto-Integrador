package com.proyecto1P.logiflow.entity;

/**
 * Enum EstadoConductor.
 * Define los posibles estados de un conductor en el sistema.
 *
 * Estados:
 * - ACTIVO: conductor disponible para trabajar.
 * - INACTIVO: conductor no disponible temporalmente.
 * - SUSPENDIDO: conductor suspendido por infracciones o problemas.
 */
public enum EstadoConductor {
    ACTIVO,
    INACTIVO,
    SUSPENDIDO
}