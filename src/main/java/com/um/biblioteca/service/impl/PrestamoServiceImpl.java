package com.um.biblioteca.service.impl;

import com.um.biblioteca.exception.LibroNoEncontradoException;
import com.um.biblioteca.exception.PrestamoNoEncontradoException;
import com.um.biblioteca.exception.RecursoNoDisponibleException;
import com.um.biblioteca.exception.UsuarioNoEncontradoException;
import com.um.biblioteca.model.EstadoLibro;
import com.um.biblioteca.model.EstadoUsuario;
import com.um.biblioteca.model.Libro;
import com.um.biblioteca.model.Prestamo;
import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.repository.PrestamoRepository;
import com.um.biblioteca.service.LibroService;
import com.um.biblioteca.service.PrestamoService;
import com.um.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoServiceImpl implements PrestamoService {
    
    private final PrestamoRepository prestamoRepository;
    private final LibroService libroService;
    private final UsuarioService usuarioService;
    
    @Autowired
    public PrestamoServiceImpl(
            PrestamoRepository prestamoRepository,
            LibroService libroService,
            UsuarioService usuarioService) {
        this.prestamoRepository = prestamoRepository;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }
    
    @Override
    public Prestamo buscarPorId(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new PrestamoNoEncontradoException(id));
    }
    
    @Override
    public List<Prestamo> obtenerTodos() {
        return prestamoRepository.findAll();
    }
    
    @Override
    public List<Prestamo> buscarPorUsuario(Usuario usuario) {
        return prestamoRepository.findByUsuario(usuario);
    }
    
    @Override
    public List<Prestamo> buscarPorLibro(Libro libro) {
        return prestamoRepository.findByLibro(libro);
    }
    
    @Override
    public List<Prestamo> buscarPorFechaPrestamo(LocalDate fecha) {
        return prestamoRepository.findByFechaPrestamo(fecha);
    }
    
    @Override
    public List<Prestamo> buscarVencidos() {
        return prestamoRepository.findByFechaDevolucionBefore(LocalDate.now());
    }
    
    @Override
    public Prestamo crearPrestamo(Long usuarioId, Long libroId, LocalDate fechaDevolucion) {
        // Buscar el usuario y el libro
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Libro libro = libroService.buscarPorId(libroId);
        
        // Verificar que el usuario esté activo
        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            throw new RecursoNoDisponibleException("usuario", 
                    "el usuario no está activo");
        }
        
        // Verificar que el libro esté disponible
        if (libro.getEstado() != EstadoLibro.DISPONIBLE) {
            throw new RecursoNoDisponibleException("libro", 
                    "el libro no está disponible");
        }
        
        // Cambiar el estado del libro a prestado
        libro.setEstado(EstadoLibro.PRESTADO);
        libroService.actualizar(libro.getId(), libro);
        
        // Crear el préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        
        // Si no se especifica fecha de devolución, establecer por defecto a 15 días
        if (fechaDevolucion == null) {
            prestamo.setFechaDevolucion(LocalDate.now().plusDays(15));
        } else {
            // Verificar que la fecha de devolución sea posterior a la actual
            if (fechaDevolucion.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "La fecha de devolución debe ser posterior a la fecha actual");
            }
            prestamo.setFechaDevolucion(fechaDevolucion);
        }
        
        return prestamoRepository.save(prestamo);
    }
    
    @Override
    public Prestamo finalizarPrestamo(Long id) {
        Prestamo prestamo = buscarPorId(id);
        
        // Cambiar el estado del libro a disponible
        Libro libro = prestamo.getLibro();
        libro.setEstado(EstadoLibro.DISPONIBLE);
        libroService.actualizar(libro.getId(), libro);
        
        // Finalizar el préstamo (establecer fecha de devolución a hoy)
        prestamo.setFechaDevolucion(LocalDate.now());
        
        return prestamoRepository.save(prestamo);
    }
    
    @Override
    public Prestamo extenderPrestamo(Long id, LocalDate nuevaFechaDevolucion) {
        Prestamo prestamo = buscarPorId(id);
        
        // Verificar que la fecha de devolución sea posterior a la actual
        if (nuevaFechaDevolucion == null || nuevaFechaDevolucion.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "La fecha de devolución debe ser posterior a la fecha actual");
        }
        
        // Verificar que la nueva fecha sea posterior a la fecha actual de devolución
        if (nuevaFechaDevolucion.isBefore(prestamo.getFechaDevolucion())) {
            throw new IllegalArgumentException(
                    "La nueva fecha de devolución debe ser posterior a la fecha actual de devolución");
        }
        
        // Extender el préstamo
        prestamo.setFechaDevolucion(nuevaFechaDevolucion);
        
        return prestamoRepository.save(prestamo);
    }
    
    @Override
    public void eliminar(Long id) {
        // Verificar que el préstamo existe
        if (!prestamoRepository.existsById(id)) {
            throw new PrestamoNoEncontradoException(id);
        }
        
        // Recuperar el préstamo antes de eliminarlo para poder acceder al libro
        Prestamo prestamo = buscarPorId(id);
        
        // Si el libro está prestado, cambiar su estado a disponible
        if (prestamo.getLibro().getEstado() == EstadoLibro.PRESTADO) {
            Libro libro = prestamo.getLibro();
            libro.setEstado(EstadoLibro.DISPONIBLE);
            libroService.actualizar(libro.getId(), libro);
        }
        
        prestamoRepository.deleteById(id);
    }
} 