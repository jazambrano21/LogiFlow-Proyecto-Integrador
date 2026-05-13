package com.ms_facturacion.repository;

import com.ms_facturacion.entity.Factura;
import com.ms_facturacion.enums.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findByClienteId(Long clienteId);

    List<Factura> findByPedidoId(Long pedidoId);

    List<Factura> findByEstado(EstadoFactura estado);
}
