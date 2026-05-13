package com.proyecto1P.logiflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

/**
 * Entidad Conductor.
 * Representa un conductor de vehículos en el sistema de logística.
 * Mapea a la tabla 'conductores' en la base de datos.
 *
 * Campos:
 * - id: clave primaria (auto-incremento).
 * - nombre: nombre del conductor (obligatorio).
 * - apellido: apellido del conductor (obligatorio).
 * - cedula: número de cédula/DNI (único, obligatorio).
 * - licencia: número de licencia de conducción (único, obligatorio).
 * - telefono: teléfono de contacto (obligatorio).
 * - email: correo electrónico (opcional, validado si se envía).
 * - estado: estado del conductor (enum: ACTIVO, INACTIVO, SUSPENDIDO).
 */
@Entity
@Table(name = "conductores")
public class Conductor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellido;

    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "\\d{10}", message = "La cédula debe tener exactamente 10 dígitos")
    @Column(nullable = false, unique = true)
    private String cedula;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener exactamente 10 dígitos")
    @Column(nullable = false, length = 50)
    private String telefono;

    @NotBlank(message = "La licencia es obligatoria")
    @Column(nullable = false, unique = true)
    private String licencia;

    @Email(message = "El email debe ser válido")
    @Column(nullable = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoConductor estado = EstadoConductor.ACTIVO;

    // Constructor sin parámetros (requerido por JPA)
    public Conductor() {
    }

    // Constructor con todos los parámetros
    public Conductor(Long id, String nombre, String apellido, String cedula, String licencia, String telefono, String email, EstadoConductor estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.licencia = licencia;
        this.telefono = telefono;
        this.email = email;
        this.estado = estado;
    }

    // ============ GETTERS Y SETTERS ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EstadoConductor getEstado() {
        return estado;
    }

    public void setEstado(EstadoConductor estado) {
        this.estado = estado;
    }
}