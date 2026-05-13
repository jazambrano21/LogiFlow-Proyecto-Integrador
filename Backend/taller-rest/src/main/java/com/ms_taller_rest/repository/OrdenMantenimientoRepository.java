package com.ms_taller_rest.repository;

import com.ms_taller_rest.entity.OrdenMantenimiento;
import com.ms_taller_rest.enums.EstadoMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenMantenimientoRepository extends JpaRepository<OrdenMantenimiento, Long> {

    List<OrdenMantenimiento> findByMatricula(String matricula);
    List<OrdenMantenimiento> findByEstado(EstadoMantenimiento estado);
    boolean existsByMatricula(String matricula);
}
