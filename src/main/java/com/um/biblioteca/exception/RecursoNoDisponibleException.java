package com.um.biblioteca.exception;

public class RecursoNoDisponibleException extends BibliotecaException {
    
    public RecursoNoDisponibleException(String mensaje) {
        super(mensaje, 409);
    }
    
    public RecursoNoDisponibleException(String recurso, String motivo) {
        super("El recurso " + recurso + " no está disponible: " + motivo, 409);
    }
} 