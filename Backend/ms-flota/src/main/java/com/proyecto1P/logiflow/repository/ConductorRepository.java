package com.proyecto1P.logiflow.repository;

import com.proyecto1P.logiflow.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository para Conductor.
 * Extiende JpaRepository para acceso a BD.
 *
 * Spring Data JPA proporciona automáticamente:
 * - findAll() → obtiene todos los conductores.
 * - findById(id) → obtiene un conductor por id.
 * - save(conductor) → guarda o actualiza.
 * - deleteById(id) → elimina por id.
 * - existsById(id) → verifica si existe.
 *
 * Si necesitas consultas personalizadas, agrégalas aquí con @Query.
 */
public interface ConductorRepository extends JpaRepository<Conductor, Long> {
    // Por ahora solo usamos los métodos heredados de JpaRepository
}