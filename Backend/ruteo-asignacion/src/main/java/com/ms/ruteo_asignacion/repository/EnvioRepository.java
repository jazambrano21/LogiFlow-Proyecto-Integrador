package com.ms.ruteo_asignacion.repository;

import com.ms.ruteo_asignacion.entity.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {

    List<Envio> findByPedidoId(Long pedidoId);

    List<Envio> findByVehiculoId(Long vehiculoId);

    List<Envio> findByConductorId(Long conductorId);
}
