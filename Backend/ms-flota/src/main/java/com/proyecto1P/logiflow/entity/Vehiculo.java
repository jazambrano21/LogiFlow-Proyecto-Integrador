package com.proyecto1P.logiflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La matrícula es obligatoria")
    @Column(nullable = false, unique = true)
    private String matricula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVehiculo tipo;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser positiva")
    @Column(nullable = false)
    private Double capacidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;

    /**
     * Relación MANY-TO-ONE:
     * - muchos vehículos -> un conductor
     * - nullable=true permite que el vehículo esté sin asignación
     * Probando
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conductor_id", nullable = true)
    private Conductor conductor;

    public Vehiculo() {
    }

    public Vehiculo(Long id, String matricula, TipoVehiculo tipo, Double capacidad, EstadoVehiculo estado, Conductor conductor) {
        this.id = id;
        this.matricula = matricula;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.estado = estado;
        this.conductor = conductor;
    }

    public Long getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public Double getCapacidad() {
        return capacidad;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public void setCapacidad(Double capacidad) {
        this.capacidad = capacidad;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }
}