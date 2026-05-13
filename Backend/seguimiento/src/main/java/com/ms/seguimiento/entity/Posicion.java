package com.ms.seguimiento.entity;

import com.ms.seguimiento.enums.EstadoPosicion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "posiciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long envioId;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @Column(nullable = false)
    private Double velocidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPosicion estado;

    @Column(nullable = false)
    private LocalDateTime fechaHora;
}
