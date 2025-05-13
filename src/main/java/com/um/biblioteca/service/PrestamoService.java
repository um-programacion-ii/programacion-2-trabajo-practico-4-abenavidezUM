package com.um.biblioteca.service;

import com.um.biblioteca.model.Prestamo;
import com.um.biblioteca.model.Libro;
import com.um.biblioteca.model.Usuario;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestionar préstamos
 */
public interface PrestamoService {
    
    /**
     * Busca un préstamo por su ID
     * @param id ID del préstamo
     * @return Préstamo encontrado
     * @throws com.um.biblioteca.exception.PrestamoNoEncontradoException si el préstamo no existe
     */
    Prestamo buscarPorId(Long id);
    
    /**
     * Obtiene todos los préstamos
     * @return Lista de todos los préstamos
     */
    List<Prestamo> obtenerTodos();
    
    /**
     * Busca préstamos por usuario
     * @param usuario Usuario de los préstamos
     * @return Lista de préstamos del usuario
     */
    List<Prestamo> buscarPorUsuario(Usuario usuario);
    
    /**
     * Busca préstamos por libro
     * @param libro Libro de los préstamos
     * @return Lista de préstamos del libro
     */
    List<Prestamo> buscarPorLibro(Libro libro);
    
    /**
     * Busca préstamos por fecha de préstamo
     * @param fecha Fecha de préstamo
     * @return Lista de préstamos realizados en la fecha especificada
     */
    List<Prestamo> buscarPorFechaPrestamo(LocalDate fecha);
    
    /**
     * Busca préstamos vencidos
     * @return Lista de préstamos vencidos
     */
    List<Prestamo> buscarVencidos();
    
    /**
     * Crea un nuevo préstamo
     * @param usuarioId ID del usuario
     * @param libroId ID del libro
     * @param fechaDevolucion Fecha de devolución prevista
     * @return Préstamo creado
     * @throws com.um.biblioteca.exception.UsuarioNoEncontradoException si el usuario no existe
     * @throws com.um.biblioteca.exception.LibroNoEncontradoException si el libro no existe
     * @throws com.um.biblioteca.exception.RecursoNoDisponibleException si el libro no está disponible
     */
    Prestamo crearPrestamo(Long usuarioId, Long libroId, LocalDate fechaDevolucion);
    
    /**
     * Finaliza un préstamo (devuelve el libro)
     * @param id ID del préstamo
     * @return Préstamo finalizado
     * @throws com.um.biblioteca.exception.PrestamoNoEncontradoException si el préstamo no existe
     */
    Prestamo finalizarPrestamo(Long id);
    
    /**
     * Extiende la fecha de devolución de un préstamo
     * @param id ID del préstamo
     * @param nuevaFechaDevolucion Nueva fecha de devolución
     * @return Préstamo actualizado
     * @throws com.um.biblioteca.exception.PrestamoNoEncontradoException si el préstamo no existe
     */
    Prestamo extenderPrestamo(Long id, LocalDate nuevaFechaDevolucion);
    
    /**
     * Elimina un préstamo
     * @param id ID del préstamo a eliminar
     * @throws com.um.biblioteca.exception.PrestamoNoEncontradoException si el préstamo no existe
     */
    void eliminar(Long id);
} 