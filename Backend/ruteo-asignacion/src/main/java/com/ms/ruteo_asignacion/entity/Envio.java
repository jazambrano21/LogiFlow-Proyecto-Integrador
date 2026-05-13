package com.ms.ruteo_asignacion.entity;

import com.ms.ruteo_asignacion.enums.EstadoEnvio;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "envios")
@Getter
@Setter
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

    @Column(nullable = false)
    private Long conductorId;

    @Column(nullable = false, length = 150)
    private String rutaEstimada;

    @Column(nullable = false)
    private Double kilometros;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoEnvio estado;

    @Column(nullable = false)
    private LocalDateTime fechaAsignacion;
}
