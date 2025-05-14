package com.um.biblioteca.service;

import com.um.biblioteca.exception.UsuarioNoEncontradoException;
import com.um.biblioteca.model.EstadoUsuario;
import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.repository.UsuarioRepository;
import com.um.biblioteca.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

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
    void obtenerTodos_debeRetornarListaDeUsuarios() {
        // Arrange
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));
        
        // Act
        List<Usuario> result = usuarioService.obtenerTodos();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(usuario1));
        assertTrue(result.contains(usuario2));
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExisteId_debeRetornarUsuario() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario1));
        
        // Act
        Usuario result = usuarioService.buscarPorId(1L);
        
        // Assert
        assertEquals(usuario1, result);
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExisteId_debeLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findById(3L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.buscarPorId(3L);
        });
        verify(usuarioRepository, times(1)).findById(3L);
    }

    @Test
    void buscarPorEmail_cuandoExisteEmail_debeRetornarUsuario() {
        // Arrange
        when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(usuario1));
        
        // Act
        Usuario result = usuarioService.buscarPorEmail("juan@example.com");
        
        // Assert
        assertEquals(usuario1, result);
        verify(usuarioRepository, times(1)).findByEmail("juan@example.com");
    }

    @Test
    void buscarPorEmail_cuandoNoExisteEmail_debeLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findByEmail("no-existe@example.com")).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.buscarPorEmail("no-existe@example.com");
        });
        verify(usuarioRepository, times(1)).findByEmail("no-existe@example.com");
    }

    @Test
    void buscarPorNombre_debeRetornarUsuariosConNombre() {
        // Arrange
        when(usuarioRepository.findByNombreContainingIgnoreCase("Juan")).thenReturn(Arrays.asList(usuario1));
        
        // Act
        List<Usuario> result = usuarioService.buscarPorNombre("Juan");
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(usuario1, result.get(0));
        verify(usuarioRepository, times(1)).findByNombreContainingIgnoreCase("Juan");
    }

    @Test
    void buscarPorEstado_debeRetornarUsuariosConEstado() {
        // Arrange
        when(usuarioRepository.findByEstado(EstadoUsuario.ACTIVO)).thenReturn(Arrays.asList(usuario1, usuario2));
        
        // Act
        List<Usuario> result = usuarioService.buscarPorEstado(EstadoUsuario.ACTIVO);
        
        // Assert
        assertEquals(2, result.size());
        verify(usuarioRepository, times(1)).findByEstado(EstadoUsuario.ACTIVO);
    }

    @Test
    void guardar_debeGuardarYRetornarUsuario() {
        // Arrange
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Nuevo Usuario");
        nuevoUsuario.setEmail("nuevo@example.com");
        nuevoUsuario.setEstado(EstadoUsuario.ACTIVO);
        
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(nuevoUsuario);
        
        // Act
        Usuario result = usuarioService.guardar(nuevoUsuario);
        
        // Assert
        assertEquals(nuevoUsuario, result);
        verify(usuarioRepository, times(1)).save(nuevoUsuario);
    }

    @Test
    void actualizar_cuandoExisteId_debeActualizarYRetornarUsuario() {
        // Arrange
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setNombre("Juan Pérez Actualizado");
        usuarioActualizado.setEmail("juan@example.com");
        usuarioActualizado.setEstado(EstadoUsuario.ACTIVO);
        
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);
        
        // Act
        Usuario result = usuarioService.actualizar(1L, usuarioActualizado);
        
        // Assert
        assertEquals("Juan Pérez Actualizado", result.getNombre());
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).save(usuarioActualizado);
    }

    @Test
    void actualizar_cuandoNoExisteId_debeLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.existsById(3L)).thenReturn(false);
        
        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.actualizar(3L, new Usuario());
        });
        verify(usuarioRepository, times(1)).existsById(3L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void cambiarEstado_cuandoExisteId_debeCambiarEstadoYRetornarUsuario() {
        // Arrange
        Usuario usuarioConEstadoCambiado = new Usuario();
        usuarioConEstadoCambiado.setId(1L);
        usuarioConEstadoCambiado.setNombre("Juan Pérez");
        usuarioConEstadoCambiado.setEmail("juan@example.com");
        usuarioConEstadoCambiado.setEstado(EstadoUsuario.INACTIVO);
        
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioConEstadoCambiado);
        
        // Act
        Usuario result = usuarioService.cambiarEstado(1L, EstadoUsuario.INACTIVO);
        
        // Assert
        assertEquals(EstadoUsuario.INACTIVO, result.getEstado());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void cambiarEstado_cuandoNoExisteId_debeLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findById(3L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.cambiarEstado(3L, EstadoUsuario.INACTIVO);
        });
        verify(usuarioRepository, times(1)).findById(3L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void eliminar_debeEliminarUsuario() {
        // Arrange
        doNothing().when(usuarioRepository).deleteById(anyLong());
        
        // Act
        usuarioService.eliminar(1L);
        
        // Assert
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
} 