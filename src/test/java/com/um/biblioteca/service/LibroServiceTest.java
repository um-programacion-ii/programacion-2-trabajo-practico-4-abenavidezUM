package com.um.biblioteca.service;

import com.um.biblioteca.exception.LibroNoEncontradoException;
import com.um.biblioteca.model.EstadoLibro;
import com.um.biblioteca.model.Libro;
import com.um.biblioteca.repository.LibroRepository;
import com.um.biblioteca.service.impl.LibroServiceImpl;
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

class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroServiceImpl libroService;

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
    void obtenerTodos_debeRetornarListaDeLibros() {
        // Arrange
        when(libroRepository.findAll()).thenReturn(Arrays.asList(libro1, libro2));
        
        // Act
        List<Libro> result = libroService.obtenerTodos();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(libro1));
        assertTrue(result.contains(libro2));
        verify(libroRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExisteId_debeRetornarLibro() {
        // Arrange
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro1));
        
        // Act
        Libro result = libroService.buscarPorId(1L);
        
        // Assert
        assertEquals(libro1, result);
        verify(libroRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExisteId_debeLanzarExcepcion() {
        // Arrange
        when(libroRepository.findById(3L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(LibroNoEncontradoException.class, () -> {
            libroService.buscarPorId(3L);
        });
        verify(libroRepository, times(1)).findById(3L);
    }

    @Test
    void buscarPorIsbn_cuandoExisteIsbn_debeRetornarLibro() {
        // Arrange
        when(libroRepository.findByIsbn("123456789")).thenReturn(Optional.of(libro1));
        
        // Act
        Libro result = libroService.buscarPorIsbn("123456789");
        
        // Assert
        assertEquals(libro1, result);
        verify(libroRepository, times(1)).findByIsbn("123456789");
    }

    @Test
    void buscarPorIsbn_cuandoNoExisteIsbn_debeLanzarExcepcion() {
        // Arrange
        when(libroRepository.findByIsbn("000000000")).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(LibroNoEncontradoException.class, () -> {
            libroService.buscarPorIsbn("000000000");
        });
        verify(libroRepository, times(1)).findByIsbn("000000000");
    }

    @Test
    void buscarPorTitulo_debeRetornarLibrosConTitulo() {
        // Arrange
        when(libroRepository.findByTituloContainingIgnoreCase("principito")).thenReturn(Arrays.asList(libro1));
        
        // Act
        List<Libro> result = libroService.buscarPorTitulo("principito");
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(libro1, result.get(0));
        verify(libroRepository, times(1)).findByTituloContainingIgnoreCase("principito");
    }

    @Test
    void buscarPorAutor_debeRetornarLibrosConAutor() {
        // Arrange
        when(libroRepository.findByAutorContainingIgnoreCase("García")).thenReturn(Arrays.asList(libro2));
        
        // Act
        List<Libro> result = libroService.buscarPorAutor("García");
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(libro2, result.get(0));
        verify(libroRepository, times(1)).findByAutorContainingIgnoreCase("García");
    }

    @Test
    void buscarPorEstado_debeRetornarLibrosConEstado() {
        // Arrange
        when(libroRepository.findByEstado(EstadoLibro.DISPONIBLE)).thenReturn(Arrays.asList(libro1, libro2));
        
        // Act
        List<Libro> result = libroService.buscarPorEstado(EstadoLibro.DISPONIBLE);
        
        // Assert
        assertEquals(2, result.size());
        verify(libroRepository, times(1)).findByEstado(EstadoLibro.DISPONIBLE);
    }

    @Test
    void guardar_debeGuardarYRetornarLibro() {
        // Arrange
        Libro nuevoLibro = new Libro();
        nuevoLibro.setIsbn("111222333");
        nuevoLibro.setTitulo("Nuevo libro");
        nuevoLibro.setAutor("Autor Nuevo");
        nuevoLibro.setEstado(EstadoLibro.DISPONIBLE);
        
        when(libroRepository.save(any(Libro.class))).thenReturn(nuevoLibro);
        
        // Act
        Libro result = libroService.guardar(nuevoLibro);
        
        // Assert
        assertEquals(nuevoLibro, result);
        verify(libroRepository, times(1)).save(nuevoLibro);
    }

    @Test
    void actualizar_cuandoExisteId_debeActualizarYRetornarLibro() {
        // Arrange
        Libro libroActualizado = new Libro();
        libroActualizado.setId(1L);
        libroActualizado.setIsbn("123456789");
        libroActualizado.setTitulo("Título actualizado");
        libroActualizado.setAutor("Antoine de Saint-Exupéry");
        libroActualizado.setEstado(EstadoLibro.DISPONIBLE);
        
        when(libroRepository.existsById(1L)).thenReturn(true);
        when(libroRepository.save(any(Libro.class))).thenReturn(libroActualizado);
        
        // Act
        Libro result = libroService.actualizar(1L, libroActualizado);
        
        // Assert
        assertEquals("Título actualizado", result.getTitulo());
        verify(libroRepository, times(1)).existsById(1L);
        verify(libroRepository, times(1)).save(libroActualizado);
    }

    @Test
    void actualizar_cuandoNoExisteId_debeLanzarExcepcion() {
        // Arrange
        when(libroRepository.existsById(3L)).thenReturn(false);
        
        // Act & Assert
        assertThrows(LibroNoEncontradoException.class, () -> {
            libroService.actualizar(3L, new Libro());
        });
        verify(libroRepository, times(1)).existsById(3L);
        verify(libroRepository, never()).save(any(Libro.class));
    }

    @Test
    void cambiarEstado_cuandoExisteId_debeCambiarEstadoYRetornarLibro() {
        // Arrange
        Libro libroConEstadoCambiado = new Libro();
        libroConEstadoCambiado.setId(1L);
        libroConEstadoCambiado.setIsbn("123456789");
        libroConEstadoCambiado.setTitulo("El principito");
        libroConEstadoCambiado.setAutor("Antoine de Saint-Exupéry");
        libroConEstadoCambiado.setEstado(EstadoLibro.PRESTADO);
        
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro1));
        when(libroRepository.save(any(Libro.class))).thenReturn(libroConEstadoCambiado);
        
        // Act
        Libro result = libroService.cambiarEstado(1L, EstadoLibro.PRESTADO);
        
        // Assert
        assertEquals(EstadoLibro.PRESTADO, result.getEstado());
        verify(libroRepository, times(1)).findById(1L);
        verify(libroRepository, times(1)).save(any(Libro.class));
    }

    @Test
    void cambiarEstado_cuandoNoExisteId_debeLanzarExcepcion() {
        // Arrange
        when(libroRepository.findById(3L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(LibroNoEncontradoException.class, () -> {
            libroService.cambiarEstado(3L, EstadoLibro.PRESTADO);
        });
        verify(libroRepository, times(1)).findById(3L);
        verify(libroRepository, never()).save(any(Libro.class));
    }

    @Test
    void eliminar_debeEliminarLibro() {
        // Arrange
        doNothing().when(libroRepository).deleteById(anyLong());
        
        // Act
        libroService.eliminar(1L);
        
        // Assert
        verify(libroRepository, times(1)).deleteById(1L);
    }
} 