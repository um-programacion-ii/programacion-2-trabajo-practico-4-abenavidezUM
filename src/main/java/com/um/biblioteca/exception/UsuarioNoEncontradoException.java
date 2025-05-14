package com.um.biblioteca.exception;

public class UsuarioNoEncontradoException extends BibliotecaException {
    
    public UsuarioNoEncontradoException(Long id) {
        super("No se encontró el usuario con id: " + id, 404);
    }
    
    public UsuarioNoEncontradoException(String email) {
        super("No se encontró el usuario con email: " + email, 404);
    }
} 