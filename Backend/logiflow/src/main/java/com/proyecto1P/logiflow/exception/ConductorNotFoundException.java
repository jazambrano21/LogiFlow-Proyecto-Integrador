package com.proyecto1P.logiflow.exception;

public class ConductorNotFoundException extends RuntimeException {

    public ConductorNotFoundException(String mensaje) {
        super(mensaje);
    }
}