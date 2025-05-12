package com.um.biblioteca.exception;

public class PrestamoNoEncontradoException extends BibliotecaException {
    
    public PrestamoNoEncontradoException(Long id) {
        super("No se encontró el préstamo con id: " + id, 404);
    }
} 