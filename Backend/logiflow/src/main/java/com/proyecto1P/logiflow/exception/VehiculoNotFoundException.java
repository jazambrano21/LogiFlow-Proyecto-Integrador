package com.proyecto1P.logiflow.exception;

public class VehiculoNotFoundException extends RuntimeException {

    public VehiculoNotFoundException(String mensaje) {
        super(mensaje);
    }
}