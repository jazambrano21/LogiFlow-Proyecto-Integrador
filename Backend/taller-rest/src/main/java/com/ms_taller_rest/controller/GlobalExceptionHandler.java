package com.ms_taller_rest.controller;

import com.ms_taller_rest.exception.MatriculaExisteException;
import com.ms_taller_rest.exception.OrdenNoExisteException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // asegura que este advice tenga prioridad
public class GlobalExceptionHandler {

    @ExceptionHandler(MatriculaExisteException.class)
    public ResponseEntity<Map<String, String>> handleMatriculaExiste(MatriculaExisteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(OrdenNoExisteException.class)
    public ResponseEntity<Map<String, String>> handleOrdenNoExiste(OrdenNoExisteException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", "Dato inválido: " + ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", ex.getMessage()));
    }

    // Maneja validaciones de @Valid sobre @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        // Puedes devolver el primer mensaje:
        String mensaje = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Error de validación");

        // Si prefieres devolver todos los errores, descomenta esta línea y ajusta el body:
        // Map<String, String> erroresPorCampo = fieldErrors.stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a,b)->a));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", mensaje));
    }

    // Maneja validaciones en parámetros de formulario / query params
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleBindException(BindException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).findFirst().orElse("Error de validación");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", mensaje));
    }

    // Maneja JSON inválido / valores enum no reconocidos
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String detalle = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        String mensaje = "Solicitud inválida: " + detalle;
        // Si es un enum desconocido, puedes proporcionar un mensaje más amigable aquí.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", mensaje));
    }
}