package com.logiflow.msruteo.exception;

public class EnvioNotFoundException extends RuntimeException {
    public EnvioNotFoundException(Long id) {
        super("Envío no encontrado con id: " + id);
    }
}
