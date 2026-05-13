package com.ms_facturacion.entity;

import com.ms_facturacion.enums.EstadoFactura;
import com.ms_facturacion.enums.NivelEntrega;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pedidoId;

    @Column(nullable = false)
    private Long clienteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelEntrega nivelEntrega;

    @Column(nullable = false)
    private Double pesoKg;

    @Column(nullable = false)
    private Double kilometros;

    @Column(nullable = false)
    private Double tarifaBase;

    @Column(nullable = false)
    private Double subtotal;

    @Column(nullable = false)
    private Double recargo;

    @Column(nullable = false)
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estado;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;
}
