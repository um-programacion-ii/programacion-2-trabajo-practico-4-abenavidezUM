package com.um.biblioteca.repository.impl;

import com.um.biblioteca.model.Libro;
import com.um.biblioteca.model.Prestamo;
import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.repository.PrestamoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PrestamoRepositoryImpl implements PrestamoRepository {
    
    private final Map<Long, Prestamo> prestamos = new HashMap<>();
    private Long nextId = 1L;
    
    @Override
    public Prestamo save(Prestamo prestamo) {
        if (prestamo.getId() == null) {
            prestamo.setId(nextId++);
        }
        prestamos.put(prestamo.getId(), prestamo);
        return prestamo;
    }
    
    @Override
    public Optional<Prestamo> findById(Long id) {
        return Optional.ofNullable(prestamos.get(id));
    }
    
    @Override
    public List<Prestamo> findAll() {
        return new ArrayList<>(prestamos.values());
    }
    
    @Override
    public void deleteById(Long id) {
        prestamos.remove(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return prestamos.containsKey(id);
    }
    
    @Override
    public List<Prestamo> findByUsuario(Usuario usuario) {
        if (usuario == null) {
            return Collections.emptyList();
        }
        
        return prestamos.values().stream()
                .filter(prestamo -> prestamo.getUsuario() != null && 
                        Objects.equals(prestamo.getUsuario().getId(), usuario.getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Prestamo> findByLibro(Libro libro) {
        if (libro == null) {
            return Collections.emptyList();
        }
        
        return prestamos.values().stream()
                .filter(prestamo -> prestamo.getLibro() != null && 
                        Objects.equals(prestamo.getLibro().getId(), libro.getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Prestamo> findByFechaPrestamo(LocalDate fecha) {
        if (fecha == null) {
            return Collections.emptyList();
        }
        
        return prestamos.values().stream()
                .filter(prestamo -> prestamo.getFechaPrestamo() != null &&
                        prestamo.getFechaPrestamo().equals(fecha))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Prestamo> findByFechaDevolucionBefore(LocalDate fecha) {
        if (fecha == null) {
            return Collections.emptyList();
        }
        
        return prestamos.values().stream()
                .filter(prestamo -> prestamo.getFechaDevolucion() != null &&
                        prestamo.getFechaDevolucion().isBefore(fecha))
                .collect(Collectors.toList());
    }
} 