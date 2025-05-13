package com.um.biblioteca.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = new ServletWebRequest(new MockHttpServletRequest());
    }

    @Test
    void handleRecursoNoEncontrado() {
        // Given
        LibroNoEncontradoException exception = new LibroNoEncontradoException("Libro no encontrado con id: 1");
        
        // When
        ResponseEntity<ApiError> response = exceptionHandler.handleRecursoNoEncontrado(exception, webRequest);
        
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Libro no encontrado con id: 1", response.getBody().getMessage());
        assertEquals("NOT_FOUND", response.getBody().getStatus());
    }

    @Test
    void handleRecursoNoDisponible() {
        // Given
        RecursoNoDisponibleException exception = new RecursoNoDisponibleException("El libro no está disponible para préstamo");
        
        // When
        ResponseEntity<ApiError> response = exceptionHandler.handleRecursoNoDisponible(exception, webRequest);
        
        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("El libro no está disponible para préstamo", response.getBody().getMessage());
        assertEquals("CONFLICT", response.getBody().getStatus());
    }

    @Test
    void handleDatosInvalidos() {
        // Given
        DatosInvalidosException exception = new DatosInvalidosException("La fecha de devolución no puede ser anterior a la fecha actual");
        
        // When
        ResponseEntity<ApiError> response = exceptionHandler.handleDatosInvalidos(exception, webRequest);
        
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La fecha de devolución no puede ser anterior a la fecha actual", response.getBody().getMessage());
        assertEquals("BAD_REQUEST", response.getBody().getStatus());
    }

    @Test
    void handleGlobalException() {
        // Given
        Exception exception = new RuntimeException("Error inesperado");
        
        // When
        ResponseEntity<ApiError> response = exceptionHandler.handleGlobalException(exception, webRequest);
        
        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Error inesperado"));
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getStatus());
    }
} 