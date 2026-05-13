package com.proyecto1P.logiflow.dtos;

import com.proyecto1P.logiflow.entity.EstadoConductor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO para Conductor.
 * Se usa para recibir datos en las peticiones HTTP (POST, PUT).
 * Contiene validaciones con anotaciones Jakarta Validation.
 *
 * Validaciones:
 * - nombre, apellido, cedula, licencia, telefono: obligatorios (@NotBlank).
 * - email: opcional pero validado si se envía (@Email).
 * - estado: enum con valores permitidos.
 */
public class ConductorDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "\\d{10}", message = "La cédula debe tener exactamente 10 dígitos")
    private String cedula;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener exactamente 10 dígitos")
    private String telefono;

    @NotBlank(message = "La licencia es obligatoria")
    private String licencia;

    @Email(message = "El email debe ser válido")
    private String email;

    @NotNull(message = "El estado es obligatorio")
    private EstadoConductor estado;

    // Constructor sin parámetros
    public ConductorDTO() {
    }

    // Constructor con todos los parámetros
    public ConductorDTO(Long id, String nombre, String apellido, String cedula, String licencia, String telefono, String email, EstadoConductor estado) {
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