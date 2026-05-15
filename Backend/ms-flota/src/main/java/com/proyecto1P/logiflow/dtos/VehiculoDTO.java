package com.proyecto1P.logiflow.dtos;

import com.proyecto1P.logiflow.entity.EstadoVehiculo;
import com.proyecto1P.logiflow.entity.TipoVehiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO de Vehiculo.
 *
 * Mantiene respuesta limpia:
 * - No devolvemos toda la entidad Conductor.
 * - Exponemos solo conductorId y nombreConductor para UI/consultas.
 */
public class VehiculoDTO {

    private Long id;

    @NotBlank(message = "La matrícula es obligatoria")
    private String matricula;

    @NotNull(message = "El tipo de vehículo es obligatorio")
    private TipoVehiculo tipo;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser positiva")
    private Double capacidad;

    // Puedes dejarlo opcional para que el backend ponga DISPONIBLE por defecto al crear
    private EstadoVehiculo estado;

    // Relación con conductor (simple y controlada)
    private Long conductorId;
    private String nombreConductor;

    public VehiculoDTO() {
    }

    public VehiculoDTO(Long id, String matricula, TipoVehiculo tipo, Double capacidad, EstadoVehiculo estado,
                       Long conductorId, String nombreConductor) {
        this.id = id;
        this.matricula = matricula;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.estado = estado;
        this.conductorId = conductorId;
        this.nombreConductor = nombreConductor;
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

    public Long getConductorId() {
        return conductorId;
    }

    public String getNombreConductor() {
        return nombreConductor;
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

    public void setConductorId(Long conductorId) {
        this.conductorId = conductorId;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }
}