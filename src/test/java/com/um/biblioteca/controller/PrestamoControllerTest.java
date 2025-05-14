package com.um.biblioteca.controller;

import com.um.biblioteca.model.Libro;
import com.um.biblioteca.model.Prestamo;
import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.service.LibroService;
import com.um.biblioteca.service.PrestamoService;
import com.um.biblioteca.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrestamoControllerTest {

    @Mock
    private PrestamoService prestamoService;
    
    @Mock
    private UsuarioService usuarioService;
    
    @Mock
    private LibroService libroService;

    @InjectMocks
    private PrestamoController prestamoController;

    private Prestamo prestamo1;
    private Prestamo prestamo2;
    private Usuario usuario;
    private Libro libro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        usuario = new Usuario();
        usuario.setId(1L);
        
        libro = new Libro();
        libro.setId(1L);
        
        prestamo1 = new Prestamo();
        prestamo1.setId(1L);
        prestamo1.setUsuario(usuario);
        prestamo1.setLibro(libro);
        prestamo1.setFechaPrestamo(LocalDate.now());
        prestamo1.setFechaDevolucion(LocalDate.now().plusDays(7));
        prestamo1.setFechaDevolucionReal(null);
        
        prestamo2 = new Prestamo();
        prestamo2.setId(2L);
        prestamo2.setUsuario(usuario);
        prestamo2.setLibro(libro);
        prestamo2.setFechaPrestamo(LocalDate.now().minusDays(3));
        prestamo2.setFechaDevolucion(LocalDate.now().plusDays(4));
        prestamo2.setFechaDevolucionReal(null);
    }

    @Test
    void obtenerTodos() {
        when(prestamoService.obtenerTodos()).thenReturn(Arrays.asList(prestamo1, prestamo2));
        
        ResponseEntity<List<Prestamo>> response = prestamoController.obtenerTodos();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(prestamoService, times(1)).obtenerTodos();
    }

    @Test
    void obtenerPorId() {
        when(prestamoService.buscarPorId(1L)).thenReturn(prestamo1);
        
        ResponseEntity<Prestamo> response = prestamoController.obtenerPorId(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(prestamo1, response.getBody());
        verify(prestamoService, times(1)).buscarPorId(1L);
    }

    @Test
    void buscarPorUsuario() {
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);
        when(prestamoService.buscarPorUsuario(usuario)).thenReturn(Arrays.asList(prestamo1, prestamo2));
        
        ResponseEntity<List<Prestamo>> response = prestamoController.buscarPorUsuario(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(usuarioService, times(1)).buscarPorId(1L);
        verify(prestamoService, times(1)).buscarPorUsuario(usuario);
    }

    @Test
    void buscarPorLibro() {
        when(libroService.buscarPorId(1L)).thenReturn(libro);
        when(prestamoService.buscarPorLibro(libro)).thenReturn(Arrays.asList(prestamo1, prestamo2));
        
        ResponseEntity<List<Prestamo>> response = prestamoController.buscarPorLibro(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(libroService, times(1)).buscarPorId(1L);
        verify(prestamoService, times(1)).buscarPorLibro(libro);
    }

    @Test
    void buscarPorFechaPrestamo() {
        LocalDate fecha = LocalDate.now();
        when(prestamoService.buscarPorFechaPrestamo(fecha)).thenReturn(Arrays.asList(prestamo1));
        
        ResponseEntity<List<Prestamo>> response = prestamoController.buscarPorFechaPrestamo(fecha);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(prestamoService, times(1)).buscarPorFechaPrestamo(fecha);
    }

    @Test
    void buscarVencidos() {
        when(prestamoService.buscarVencidos()).thenReturn(Arrays.asList(prestamo2));
        
        ResponseEntity<List<Prestamo>> response = prestamoController.buscarVencidos();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(prestamoService, times(1)).buscarVencidos();
    }

    @Test
    void crearPrestamo() {
        when(prestamoService.crearPrestamo(1L, 1L, LocalDate.now().plusDays(7))).thenReturn(prestamo1);
        
        ResponseEntity<Prestamo> response = prestamoController.crearPrestamo(
                1L, 1L, LocalDate.now().plusDays(7));
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(prestamo1, response.getBody());
        verify(prestamoService, times(1)).crearPrestamo(1L, 1L, LocalDate.now().plusDays(7));
    }

    @Test
    void finalizarPrestamo() {
        when(prestamoService.finalizarPrestamo(1L)).thenReturn(prestamo1);
        
        ResponseEntity<Prestamo> response = prestamoController.finalizarPrestamo(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(prestamo1, response.getBody());
        verify(prestamoService, times(1)).finalizarPrestamo(1L);
    }

    @Test
    void extenderPrestamo() {
        LocalDate nuevaFecha = LocalDate.now().plusDays(14);
        when(prestamoService.extenderPrestamo(1L, nuevaFecha)).thenReturn(prestamo1);
        
        ResponseEntity<Prestamo> response = prestamoController.extenderPrestamo(1L, nuevaFecha);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(prestamo1, response.getBody());
        verify(prestamoService, times(1)).extenderPrestamo(1L, nuevaFecha);
    }

    @Test
    void eliminar() {
        doNothing().when(prestamoService).eliminar(1L);
        
        ResponseEntity<Void> response = prestamoController.eliminar(1L);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(prestamoService, times(1)).eliminar(1L);
    }
} 