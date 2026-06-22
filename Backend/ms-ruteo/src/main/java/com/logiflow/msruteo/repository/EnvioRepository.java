package com.logiflow.msruteo.repository;

import com.logiflow.msruteo.entity.Envio;
import com.logiflow.msruteo.entity.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByEstado(EstadoEnvio estado);
    List<Envio> findByPedidoId(Long pedidoId);
    List<Envio> findByVehiculoId(Long vehiculoId);
    List<Envio> findByEstadoAndPedidoId(EstadoEnvio estado, Long pedidoId);
    List<Envio> findByEstadoAndVehiculoId(EstadoEnvio estado, Long vehiculoId);
    Optional<Envio> findByPedidoIdAndEstadoNot(Long pedidoId, EstadoEnvio estado);
}
