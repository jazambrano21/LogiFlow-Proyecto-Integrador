package com.proyecto1P.logiflow.repository;

import com.proyecto1P.logiflow.entity.EstadoVehiculo;
import com.proyecto1P.logiflow.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByMatricula(String matricula);

    List<Vehiculo> findByEstado(EstadoVehiculo estado);
}
