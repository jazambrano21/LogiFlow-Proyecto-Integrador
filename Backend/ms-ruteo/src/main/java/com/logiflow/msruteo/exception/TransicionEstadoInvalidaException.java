package com.logiflow.msruteo.exception;

public class TransicionEstadoInvalidaException extends RuntimeException {
    public TransicionEstadoInvalidaException(String actual, String destino) {
        super("Transición de estado inválida: " + actual + " → " + destino);
    }
}
