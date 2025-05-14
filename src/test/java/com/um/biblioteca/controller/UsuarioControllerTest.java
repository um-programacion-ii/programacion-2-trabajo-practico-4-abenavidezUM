package com.um.biblioteca.controller;

import com.um.biblioteca.model.EstadoUsuario;
import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.service.UsuarioService;
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

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Juan Pérez");
        usuario1.setEmail("juan@example.com");
        usuario1.setEstado(EstadoUsuario.ACTIVO);
        
        usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("María López");
        usuario2.setEmail("maria@example.com");
        usuario2.setEstado(EstadoUsuario.ACTIVO);
    }

    @Test
    void obtenerTodos() {
        when(usuarioService.obtenerTodos()).thenReturn(Arrays.asList(usuario1, usuario2));
        
        ResponseEntity<List<Usuario>> response = usuarioController.obtenerTodos();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(usuarioService, times(1)).obtenerTodos();
    }

    @Test
    void obtenerPorId() {
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario1);
        
        ResponseEntity<Usuario> response = usuarioController.obtenerPorId(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario1, response.getBody());
        verify(usuarioService, times(1)).buscarPorId(1L);
    }

    @Test
    void obtenerPorEmail() {
        when(usuarioService.buscarPorEmail("juan@example.com")).thenReturn(usuario1);
        
        ResponseEntity<Usuario> response = usuarioController.obtenerPorEmail("juan@example.com");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario1, response.getBody());
        verify(usuarioService, times(1)).buscarPorEmail("juan@example.com");
    }

    @Test
    void buscarPorNombre() {
        when(usuarioService.buscarPorNombre("Juan")).thenReturn(Arrays.asList(usuario1));
        
        ResponseEntity<List<Usuario>> response = usuarioController.buscarPorNombre("Juan");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(usuarioService, times(1)).buscarPorNombre("Juan");
    }

    @Test
    void crear() {
        when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuario1);
        
        ResponseEntity<Usuario> response = usuarioController.crear(usuario1);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(usuario1, response.getBody());
        verify(usuarioService, times(1)).guardar(any(Usuario.class));
    }

    @Test
    void actualizar() {
        when(usuarioService.actualizar(eq(1L), any(Usuario.class))).thenReturn(usuario1);
        
        ResponseEntity<Usuario> response = usuarioController.actualizar(1L, usuario1);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario1, response.getBody());
        verify(usuarioService, times(1)).actualizar(eq(1L), any(Usuario.class));
    }

    @Test
    void cambiarEstado() {
        when(usuarioService.cambiarEstado(1L, EstadoUsuario.INACTIVO)).thenReturn(usuario1);
        
        ResponseEntity<Usuario> response = usuarioController.cambiarEstado(1L, EstadoUsuario.INACTIVO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario1, response.getBody());
        verify(usuarioService, times(1)).cambiarEstado(1L, EstadoUsuario.INACTIVO);
    }

    @Test
    void eliminar() {
        doNothing().when(usuarioService).eliminar(1L);
        
        ResponseEntity<Void> response = usuarioController.eliminar(1L);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuarioService, times(1)).eliminar(1L);
    }
} 