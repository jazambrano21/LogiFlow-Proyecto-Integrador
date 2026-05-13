package com.ms.seguimiento.repository;

import com.ms.seguimiento.entity.Posicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PosicionRepository extends JpaRepository<Posicion, Long> {

    List<Posicion> findByEnvioId(Long envioId);

    Optional<Posicion> findTopByEnvioIdOrderByFechaHoraDesc(Long envioId);
}
