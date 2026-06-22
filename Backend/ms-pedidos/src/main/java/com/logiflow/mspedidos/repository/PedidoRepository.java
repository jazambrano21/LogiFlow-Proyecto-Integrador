package com.logiflow.mspedidos.repository;

import com.logiflow.mspedidos.entity.EstadoPedido;
import com.logiflow.mspedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByClienteIdAndEstado(Long clienteId, EstadoPedido estado);
}
