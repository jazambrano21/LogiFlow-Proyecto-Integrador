package com.proyecto1P.logiflow.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de excepciones para la API.
 * Provee respuestas JSON consistentes para:
 *  - 404 VehiculoNotFoundException
 *  - 400 MethodArgumentNotValidException (validación de campos)
 *  - 400 HttpMessageNotReadableException (JSON mal formado / enum inválido)
 *  - 409 DataIntegrityViolationException (violación de constraints, p.ej. matrícula duplicada)
 *  - 500 cualquier otra excepción inesperada
 */
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(VehiculoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> handleNotFound(VehiculoNotFoundException ex) {
        return Map.of(
                "error", "Not Found",
                "message", ex.getMessage()
        );
    }

    /**
     * Manejo 404 para conductores no encontrados.
     */
    @ExceptionHandler(ConductorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> handleConductorNotFound(ConductorNotFoundException ex) {
        return Map.of(
                "error", "Not Found",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return Map.of(
                "error", "Validation Failed",
                "errors", errors
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Map<String, Object> handleDataIntegrity(DataIntegrityViolationException ex) {
        String message = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();

        // Mensajes más amigables para constraints de unicidad
        if (message != null) {
            String lower = message.toLowerCase();
            if (lower.contains("matricula")) {
                message = "La matrícula ya existe.";
            } else if (lower.contains("cedula")) {
                message = "La cédula ya existe.";
            } else if (lower.contains("licencia")) {
                message = "La licencia ya existe.";
            }
        }

        return Map.of(
                "error", "Data Integrity Violation",
                "message", message
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleUnreadable(HttpMessageNotReadableException ex) {
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        if (msg != null && msg.toLowerCase().contains("cannot deserialize")) {
            msg = "JSON inválido o valor de enum no válido.";
        }
        return Map.of(
                "error", "Malformed JSON",
                "message", msg
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, Object> handleAll(Exception ex) {
        return Map.of(
                "error", "Internal Server Error",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(MantenimientoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> handleMantenimientoNotFound(MantenimientoNotFoundException ex) {
        return Map.of(
                "error", "Not Found",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(MantenimientoVehiculoRequiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleMantenimientoVehiculoRequired(MantenimientoVehiculoRequiredException ex) {
        return Map.of(
                "error", "Bad Request",
                "message", ex.getMessage()
        );
    }

}