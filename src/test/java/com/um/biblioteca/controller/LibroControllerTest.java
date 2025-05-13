package com.um.biblioteca.controller;

import com.um.biblioteca.model.EstadoLibro;
import com.um.biblioteca.model.Libro;
import com.um.biblioteca.service.LibroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibroControllerTest {

    @Mock
    private LibroService libroService;

    @InjectMocks
    private LibroController libroController;

    private Libro libro1;
    private Libro libro2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        libro1 = new Libro();
        libro1.setId(1L);
        libro1.setIsbn("123456789");
        libro1.setTitulo("El principito");
        libro1.setAutor("Antoine de Saint-Exupéry");
        libro1.setEstado(EstadoLibro.DISPONIBLE);
        
        libro2 = new Libro();
        libro2.setId(2L);
        libro2.setIsbn("987654321");
        libro2.setTitulo("Cien años de soledad");
        libro2.setAutor("Gabriel García Márquez");
        libro2.setEstado(EstadoLibro.DISPONIBLE);
    }

    @Test
    void obtenerTodos() {
        when(libroService.obtenerTodos()).thenReturn(Arrays.asList(libro1, libro2));
        
        ResponseEntity<List<Libro>> response = libroController.obtenerTodos();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(libroService, times(1)).obtenerTodos();
    }

    @Test
    void obtenerPorId() {
        when(libroService.buscarPorId(1L)).thenReturn(libro1);
        
        ResponseEntity<Libro> response = libroController.obtenerPorId(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(libro1, response.getBody());
        verify(libroService, times(1)).buscarPorId(1L);
    }

    @Test
    void obtenerPorIsbn() {
        when(libroService.buscarPorIsbn("123456789")).thenReturn(libro1);
        
        ResponseEntity<Libro> response = libroController.obtenerPorIsbn("123456789");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(libro1, response.getBody());
        verify(libroService, times(1)).buscarPorIsbn("123456789");
    }

    @Test
    void crear() {
        when(libroService.guardar(any(Libro.class))).thenReturn(libro1);
        
        ResponseEntity<Libro> response = libroController.crear(libro1);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(libro1, response.getBody());
        verify(libroService, times(1)).guardar(any(Libro.class));
    }

    @Test
    void actualizar() {
        when(libroService.actualizar(eq(1L), any(Libro.class))).thenReturn(libro1);
        
        ResponseEntity<Libro> response = libroController.actualizar(1L, libro1);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(libro1, response.getBody());
        verify(libroService, times(1)).actualizar(eq(1L), any(Libro.class));
    }

    @Test
    void cambiarEstado() {
        when(libroService.cambiarEstado(1L, EstadoLibro.PRESTADO)).thenReturn(libro1);
        
        ResponseEntity<Libro> response = libroController.cambiarEstado(1L, EstadoLibro.PRESTADO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(libro1, response.getBody());
        verify(libroService, times(1)).cambiarEstado(1L, EstadoLibro.PRESTADO);
    }

    @Test
    void eliminar() {
        doNothing().when(libroService).eliminar(1L);
        
        ResponseEntity<Void> response = libroController.eliminar(1L);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(libroService, times(1)).eliminar(1L);
    }
} 