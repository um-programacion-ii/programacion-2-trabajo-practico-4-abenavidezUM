package com.um.biblioteca.exception;

public class BibliotecaException extends RuntimeException {
    
    private int status;
    
    public BibliotecaException(String mensaje) {
        super(mensaje);
        this.status = 500;
    }
    
    public BibliotecaException(String mensaje, int status) {
        super(mensaje);
        this.status = status;
    }
    
    public BibliotecaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.status = 500;
    }
    
    public BibliotecaException(String mensaje, Throwable causa, int status) {
        super(mensaje, causa);
        this.status = status;
    }
    
    public int getStatus() {
        return status;
    }
} 