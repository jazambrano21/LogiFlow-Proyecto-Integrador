package com.ms_taller_rest.exception;

public class MatriculaExisteException extends RuntimeException {
    public MatriculaExisteException(String message) {
        super(message);
    }
}