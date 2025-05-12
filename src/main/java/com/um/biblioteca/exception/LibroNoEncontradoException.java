package com.um.biblioteca.exception;

public class LibroNoEncontradoException extends RuntimeException {
    
    public LibroNoEncontradoException(Long id) {
        super("No se encontró el libro con id: " + id);
    }
    
    public LibroNoEncontradoException(String isbn) {
        super("No se encontró el libro con ISBN: " + isbn);
    }
} 