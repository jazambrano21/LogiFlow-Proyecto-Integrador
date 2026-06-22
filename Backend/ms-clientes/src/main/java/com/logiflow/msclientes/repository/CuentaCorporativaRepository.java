package com.logiflow.msclientes.repository;

import com.logiflow.msclientes.entity.CuentaCorporativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaCorporativaRepository extends JpaRepository<CuentaCorporativa, Long> {
    Optional<CuentaCorporativa> findByClienteId(Long clienteId);
    boolean existsByClienteId(Long clienteId);
    boolean existsByRuc(String ruc);
}
