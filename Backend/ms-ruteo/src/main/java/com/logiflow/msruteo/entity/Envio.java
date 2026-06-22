package com.logiflow.msruteo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad Envio — tabla 'envios'.
 * Representa la asignación de un pedido a un vehículo y conductor,
 * junto con la ruta calculada.
 */
@Entity
@Table(name = "envios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pedidoId;

    @Column(nullable = false)
    private Long vehiculoId;

    /** Puede ser nulo si el vehículo no tiene conductor asignado aún */
    @Column
    private Long conductorId;

    // ── Ruta ──
    @Column(name = "ruta_origen_ciudad", nullable = false)
    private String rutaOrigenCiudad;

    @Column(name = "ruta_origen_direccion", nullable = false)
    private String rutaOrigenDireccion;

    @Column(name = "ruta_destino_ciudad", nullable = false)
    private String rutaDestinoCiudad;

    @Column(name = "ruta_destino_direccion", nullable = false)
    private String rutaDestinoDireccion;

    @Column(name = "kms_estimados", nullable = false)
    private Double kmsEstimados;

    @Column(name = "horario_estimado")
    private LocalDateTime horarioEstimado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoEnvio estado = EstadoEnvio.ASIGNADO;

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
