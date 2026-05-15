package com.proyecto1P.logiflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Entidad Mantenimiento.
 * Estamos ok
 * Relación:
 * - Un Vehiculo puede tener muchos mantenimientos.
 * - Cada Mantenimiento pertenece a un solo Vehiculo.
 * - La FK queda en la tabla mantenimiento: vehiculo_id.
 */
@Entity
@Table(name = "mantenimientos")
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotBlank(message = "El tipo de mantenimiento es obligatorio")
    @Column(nullable = false, length = 100)
    private String tipoMantenimiento;

    @NotNull(message = "El costo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo no puede ser negativo")
    @Column(nullable = false)
    private Double costo;

    /**
     * Estado del mantenimiento.
     * Se usa enum para evitar errores de escritura y mantener valores controlados.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EstadoMantenimiento estado = EstadoMantenimiento.PENDIENTE;

    /**
     * Relación Many-to-One:
     * muchos mantenimientos -> un vehículo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    public Mantenimiento() {
    }

    public Mantenimiento(Long id, LocalDate fecha, String descripcion, String tipoMantenimiento, Double costo, EstadoMantenimiento estado, Vehiculo vehiculo) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipoMantenimiento = tipoMantenimiento;
        this.costo = costo;
        this.estado = estado;
        this.vehiculo = vehiculo;
    }

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

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

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

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
}