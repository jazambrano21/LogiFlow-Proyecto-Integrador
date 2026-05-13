package com.proyecto1P.logiflow.dtos;

import com.proyecto1P.logiflow.entity.EstadoMantenimiento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class MantenimientoDTO {

    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "El tipo de mantenimiento es obligatorio")
    private String tipoMantenimiento;

    @NotNull(message = "El costo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo no puede ser negativo")
    private Double costo;

    // Ahora usamos ENUM directamente
    private EstadoMantenimiento estado;

    @NotNull(message = "El vehiculoId es obligatorio")
    private Long vehiculoId;

    private String matriculaVehiculo;

    private Long conductorId;

    private String nombreConductor;

    // Constructor vacío
    public MantenimientoDTO() {
    }

    // Constructor completo
    public MantenimientoDTO(
            Long id,
            LocalDate fecha,
            String descripcion,
            String tipoMantenimiento,
            Double costo,
            EstadoMantenimiento estado,
            Long vehiculoId,
            String matriculaVehiculo,
            Long conductorId,
            String nombreConductor
    ) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipoMantenimiento = tipoMantenimiento;
        this.costo = costo;
        this.estado = estado;
        this.vehiculoId = vehiculoId;
        this.matriculaVehiculo = matriculaVehiculo;
        this.conductorId = conductorId;
        this.nombreConductor = nombreConductor;
    }

    // GETTERS

    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public Double getCosto() {
        return costo;
    }

    public EstadoMantenimiento getEstado() {
        return estado;
    }

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public String getMatriculaVehiculo() {
        return matriculaVehiculo;
    }

    public Long getConductorId() {
        return conductorId;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipoMantenimiento(String tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public void setEstado(EstadoMantenimiento estado) {
        this.estado = estado;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public void setMatriculaVehiculo(String matriculaVehiculo) {
        this.matriculaVehiculo = matriculaVehiculo;
    }

    public void setConductorId(Long conductorId) {
        this.conductorId = conductorId;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }
}