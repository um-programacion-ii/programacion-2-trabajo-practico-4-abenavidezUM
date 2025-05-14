package com.um.biblioteca.repository;

import com.um.biblioteca.model.EstadoLibro;
import com.um.biblioteca.model.Libro;
import com.um.biblioteca.repository.impl.LibroRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LibroRepositoryTest {
    
    private LibroRepositoryImpl libroRepository;
    private Libro libro1;
    private Libro libro2;
    
    @BeforeEach
    void setUp() {
        libroRepository = new LibroRepositoryImpl();
        
        libro1 = new Libro();
        libro1.setIsbn("123456789");
        libro1.setTitulo("El principito");
        libro1.setAutor("Antoine de Saint-Exupéry");
        libro1.setEstado(EstadoLibro.DISPONIBLE);
        
        libro2 = new Libro();
        libro2.setIsbn("987654321");
        libro2.setTitulo("Cien años de soledad");
        libro2.setAutor("Gabriel García Márquez");
        libro2.setEstado(EstadoLibro.DISPONIBLE);
        
        libroRepository.save(libro1);
        libroRepository.save(libro2);
    }
    
    @Test
    void save_debePersistirNuevoLibro() {
        // Arrange
        Libro nuevoLibro = new Libro();
        nuevoLibro.setIsbn("111222333");
        nuevoLibro.setTitulo("Nuevo libro");
        nuevoLibro.setAutor("Autor Nuevo");
        nuevoLibro.setEstado(EstadoLibro.DISPONIBLE);
        
        // Act
        Libro result = libroRepository.save(nuevoLibro);
        
        // Assert
        assertNotNull(result.getId());
        assertEquals("Nuevo libro", result.getTitulo());
        assertEquals(3, libroRepository.findAll().size());
    }
    
    @Test
    void save_debeActualizarLibroExistente() {
        // Arrange
        Long id = libro1.getId();
        libro1.setTitulo("Título actualizado");
        
        // Act
        Libro result = libroRepository.save(libro1);
        
        // Assert
        assertEquals(id, result.getId());
        assertEquals("Título actualizado", result.getTitulo());
        assertEquals(2, libroRepository.findAll().size());
    }
    
    @Test
    void findById_cuandoExisteId_debeRetornarLibro() {
        // Arrange
        Long id = libro1.getId();
        
        // Act
        Optional<Libro> result = libroRepository.findById(id);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals("El principito", result.get().getTitulo());
    }
    
    @Test
    void findById_cuandoNoExisteId_debeRetornarVacio() {
        // Act
        Optional<Libro> result = libroRepository.findById(999L);
        
        // Assert
        assertFalse(result.isPresent());
    }
    
    @Test
    void findByIsbn_cuandoExisteIsbn_debeRetornarLibro() {
        // Act
        Optional<Libro> result = libroRepository.findByIsbn("123456789");
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals("El principito", result.get().getTitulo());
    }
    
    @Test
    void findByIsbn_cuandoNoExisteIsbn_debeRetornarVacio() {
        // Act
        Optional<Libro> result = libroRepository.findByIsbn("000000000");
        
        // Assert
        assertFalse(result.isPresent());
    }
    
    @Test
    void findAll_debeRetornarTodosLosLibros() {
        // Act
        List<Libro> result = libroRepository.findAll();
        
        // Assert
        assertEquals(2, result.size());
    }
    
    @Test
    void findByTituloContainingIgnoreCase_debeRetornarLibrosConTitulo() {
        // Act
        List<Libro> result = libroRepository.findByTituloContainingIgnoreCase("prin");
        
        // Assert
        assertEquals(1, result.size());
        assertEquals("El principito", result.get(0).getTitulo());
    }
    
    @Test
    void findByAutorContainingIgnoreCase_debeRetornarLibrosConAutor() {
        // Act
        List<Libro> result = libroRepository.findByAutorContainingIgnoreCase("García");
        
        // Assert
        assertEquals(1, result.size());
        assertEquals("Cien años de soledad", result.get(0).getTitulo());
    }
    
    @Test
    void findByEstado_debeRetornarLibrosConEstado() {
        // Arrange
        libro1.setEstado(EstadoLibro.PRESTADO);
        libroRepository.save(libro1);
        
        // Act
        List<Libro> disponibles = libroRepository.findByEstado(EstadoLibro.DISPONIBLE);
        List<Libro> prestados = libroRepository.findByEstado(EstadoLibro.PRESTADO);
        
        // Assert
        assertEquals(1, disponibles.size());
        assertEquals(1, prestados.size());
        assertEquals("Cien años de soledad", disponibles.get(0).getTitulo());
        assertEquals("El principito", prestados.get(0).getTitulo());
    }
    
    @Test
    void deleteById_debeEliminarLibro() {
        // Arrange
        Long id = libro1.getId();
        
        // Act
        libroRepository.deleteById(id);
        
        // Assert
        assertEquals(1, libroRepository.findAll().size());
        assertFalse(libroRepository.findById(id).isPresent());
    }
    
    @Test
    void existsById_cuandoExisteId_debeRetornarTrue() {
        // Arrange
        Long id = libro1.getId();
        
        // Act
        boolean result = libroRepository.existsById(id);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void existsById_cuandoNoExisteId_debeRetornarFalse() {
        // Act
        boolean result = libroRepository.existsById(999L);
        
        // Assert
        assertFalse(result);
    }
} 