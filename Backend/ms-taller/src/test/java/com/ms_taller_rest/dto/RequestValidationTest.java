package com.ms_taller_rest.dto;

import com.ms_taller_rest.enums.TipoMantenimiento;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void ordenMantenimientoRequest_sinCamposObligatorios_debeFallarValidacion() {
        OrdenMantenimientoRequest request = new OrdenMantenimientoRequest();
        request.setObservacion("Opcional");
        request.setCostoEstimado(100.0);

        Set<ConstraintViolation<OrdenMantenimientoRequest>> violations = validator.validate(request);

        assertEquals(3, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("matricula")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tipoMantenimiento")));
    }

    @Test
    void estadoMantenimientoRequest_sinEstado_debeFallarValidacion() {
        EstadoMantenimientoRequest request = new EstadoMantenimientoRequest();

        Set<ConstraintViolation<EstadoMantenimientoRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("estado", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void ordenMantenimientoRequest_conCamposValidos_debePasar() {
        OrdenMantenimientoRequest request = new OrdenMantenimientoRequest(
                "PBA-1234",
                "Cambio de aceite",
                "Observación",
                TipoMantenimiento.PREVENTIVO,
                100.0
        );

        Set<ConstraintViolation<OrdenMantenimientoRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}


