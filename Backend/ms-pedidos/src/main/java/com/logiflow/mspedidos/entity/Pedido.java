package com.logiflow.mspedidos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad Pedido — tabla 'pedidos'.
 * Origen, destino y paquete se almacenan como columnas embebidas
 * para evitar tablas adicionales innecesarias en esta fase.
 */
@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El clienteId es obligatorio")
    @Column(nullable = false)
    private Long clienteId;

    // ── Origen ──
    @Column(name = "origen_ciudad", nullable = false)
    private String origenCiudad;

    @Column(name = "origen_direccion", nullable = false)
    private String origenDireccion;

    // ── Destino ──
    @Column(name = "destino_ciudad", nullable = false)
    private String destinoCiudad;

    @Column(name = "destino_direccion", nullable = false)
    private String destinoDireccion;

    // ── Paquete ──
    @Column(name = "paquete_peso_kg", nullable = false)
    private Double paquetePesoKg;

    @Column(name = "paquete_descripcion", nullable = false)
    private String paqueteDescripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelGeografico nivelGeografico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoPedido estado = EstadoPedido.CREADO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
