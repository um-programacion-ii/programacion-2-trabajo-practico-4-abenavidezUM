package com.um.biblioteca.service.impl;

import com.um.biblioteca.exception.LibroNoEncontradoException;
import com.um.biblioteca.model.EstadoLibro;
import com.um.biblioteca.model.Libro;
import com.um.biblioteca.repository.LibroRepository;
import com.um.biblioteca.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroServiceImpl implements LibroService {
    
    private final LibroRepository libroRepository;
    
    @Autowired
    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }
    
    @Override
    public Libro buscarPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new LibroNoEncontradoException(id));
    }
    
    @Override
    public Libro buscarPorIsbn(String isbn) {
        return libroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException(isbn));
    }
    
    @Override
    public List<Libro> obtenerTodos() {
        return libroRepository.findAll();
    }
    
    @Override
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContaining(titulo);
    }
    
    @Override
    public List<Libro> buscarPorAutor(String autor) {
        return libroRepository.findByAutorContaining(autor);
    }
    
    @Override
    public List<Libro> buscarPorEstado(EstadoLibro estado) {
        return libroRepository.findAll().stream()
                .filter(libro -> libro.getEstado() == estado)
                .collect(Collectors.toList());
    }
    
    @Override
    public Libro guardar(Libro libro) {
        // Validación básica
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del libro es obligatorio");
        }
        
        if (libro.getEstado() == null) {
            libro.setEstado(EstadoLibro.DISPONIBLE);
        }
        
        return libroRepository.save(libro);
    }
    
    @Override
    public Libro actualizar(Long id, Libro libro) {
        // Verificar que el libro existe
        if (!libroRepository.existsById(id)) {
            throw new LibroNoEncontradoException(id);
        }
        
        // Establecer el ID correcto
        libro.setId(id);
        
        // Validación básica
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del libro es obligatorio");
        }
        
        return libroRepository.save(libro);
    }
    
    @Override
    public void eliminar(Long id) {
        // Verificar que el libro existe
        if (!libroRepository.existsById(id)) {
            throw new LibroNoEncontradoException(id);
        }
        
        libroRepository.deleteById(id);
    }
    
    @Override
    public Libro cambiarEstado(Long id, EstadoLibro estado) {
        Libro libro = buscarPorId(id);
        
        // Establecer el nuevo estado
        libro.setEstado(estado);
        
        return libroRepository.save(libro);
    }
} 