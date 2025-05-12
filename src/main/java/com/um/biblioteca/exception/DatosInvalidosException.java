package com.um.biblioteca.exception;

public class DatosInvalidosException extends BibliotecaException {
    
    public DatosInvalidosException(String mensaje) {
        super(mensaje, 400);
    }
    
    public DatosInvalidosException(String campo, String motivo) {
        super("El campo " + campo + " es inválido: " + motivo, 400);
    }
} 