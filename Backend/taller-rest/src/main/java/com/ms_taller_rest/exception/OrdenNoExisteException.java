package com.ms_taller_rest.exception;

public class OrdenNoExisteException extends RuntimeException {
    public OrdenNoExisteException(String message) {
        super(message);
    }
}