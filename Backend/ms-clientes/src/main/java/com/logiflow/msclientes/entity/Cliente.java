package com.logiflow.msclientes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad Cliente — tabla 'clientes'.
 */
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Column(nullable = false, length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de cliente es obligatorio")
    @Column(nullable = false)
    private TipoCliente tipo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}
