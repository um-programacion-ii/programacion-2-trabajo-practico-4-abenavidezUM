package com.um.biblioteca.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    
    public UsuarioNoEncontradoException(Long id) {
        super("No se encontró el usuario con id: " + id);
    }
    
    public UsuarioNoEncontradoException(String email) {
        super("No se encontró el usuario con email: " + email);
    }
} 