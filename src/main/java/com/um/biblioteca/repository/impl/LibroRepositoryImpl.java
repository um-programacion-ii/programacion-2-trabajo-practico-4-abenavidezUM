package com.um.biblioteca.repository.impl;

import com.um.biblioteca.model.Libro;
import com.um.biblioteca.repository.LibroRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LibroRepositoryImpl implements LibroRepository {
    
    private final Map<Long, Libro> libros = new HashMap<>();
    private Long nextId = 1L;
    
    @Override
    public Libro save(Libro libro) {
        if (libro.getId() == null) {
            libro.setId(nextId++);
        }
        libros.put(libro.getId(), libro);
        return libro;
    }
    
    @Override
    public Optional<Libro> findById(Long id) {
        return Optional.ofNullable(libros.get(id));
    }
    
    @Override
    public List<Libro> findAll() {
        return new ArrayList<>(libros.values());
    }
    
    @Override
    public void deleteById(Long id) {
        libros.remove(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return libros.containsKey(id);
    }
    
    @Override
    public Optional<Libro> findByIsbn(String isbn) {
        return libros.values().stream()
                .filter(libro -> libro.getIsbn().equals(isbn))
                .findFirst();
    }
    
    @Override
    public List<Libro> findByTituloContaining(String titulo) {
        if (titulo == null || titulo.isEmpty()) {
            return Collections.emptyList();
        }
        
        return libros.values().stream()
                .filter(libro -> libro.getTitulo() != null && 
                        libro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Libro> findByAutorContaining(String autor) {
        if (autor == null || autor.isEmpty()) {
            return Collections.emptyList();
        }
        
        return libros.values().stream()
                .filter(libro -> libro.getAutor() != null && 
                        libro.getAutor().toLowerCase().contains(autor.toLowerCase()))
                .collect(Collectors.toList());
    }
} 