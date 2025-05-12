package com.um.biblioteca.exception;

public class LibroNoEncontradoException extends BibliotecaException {
    
    public LibroNoEncontradoException(Long id) {
        super("No se encontró el libro con id: " + id, 404);
    }
    
    public LibroNoEncontradoException(String isbn) {
        super("No se encontró el libro con ISBN: " + isbn, 404);
    }
} 