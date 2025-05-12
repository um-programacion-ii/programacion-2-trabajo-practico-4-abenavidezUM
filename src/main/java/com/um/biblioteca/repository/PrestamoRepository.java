package com.um.biblioteca.repository;

import com.um.biblioteca.model.Libro;
import com.um.biblioteca.model.Prestamo;
import com.um.biblioteca.model.Usuario;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para la entidad Prestamo
 */
public interface PrestamoRepository extends BaseRepository<Prestamo, Long> {
    
    /**
     * Busca préstamos por usuario
     * @param usuario Usuario de los préstamos
     * @return Lista de préstamos del usuario
     */
    List<Prestamo> findByUsuario(Usuario usuario);
    
    /**
     * Busca préstamos por libro
     * @param libro Libro de los préstamos
     * @return Lista de préstamos del libro
     */
    List<Prestamo> findByLibro(Libro libro);
    
    /**
     * Busca préstamos por fecha de préstamo
     * @param fecha Fecha de préstamo
     * @return Lista de préstamos realizados en la fecha especificada
     */
    List<Prestamo> findByFechaPrestamo(LocalDate fecha);
    
    /**
     * Busca préstamos vencidos (fecha de devolución anterior a la fecha actual)
     * @return Lista de préstamos vencidos
     */
    List<Prestamo> findByFechaDevolucionBefore(LocalDate fecha);
} 