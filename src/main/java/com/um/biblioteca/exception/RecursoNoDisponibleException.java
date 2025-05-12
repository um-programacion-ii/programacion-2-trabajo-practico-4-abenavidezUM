package com.um.biblioteca.exception;

public class RecursoNoDisponibleException extends RuntimeException {
    
    public RecursoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
    
    public RecursoNoDisponibleException(String recurso, String motivo) {
        super("El recurso " + recurso + " no está disponible: " + motivo);
    }
} 