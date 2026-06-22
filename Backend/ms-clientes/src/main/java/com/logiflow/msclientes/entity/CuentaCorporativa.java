package com.logiflow.msclientes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad CuentaCorporativa — tabla 'cuentas_corporativas'.
 * Un cliente de tipo CORPORATIVO puede tener una cuenta corporativa.
 */
@Entity
@Table(name = "cuentas_corporativas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaCorporativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Column(nullable = false)
    private String nombreEmpresa;

    @NotBlank(message = "El RUC es obligatorio")
    @Column(nullable = false, unique = true, length = 13)
    private String ruc;

    @NotNull(message = "El saldo es obligatorio")
    @PositiveOrZero(message = "El saldo no puede ser negativo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal saldo;

    @Column(columnDefinition = "TEXT")
    private String contrato;
}
