package com.um.biblioteca.repository;

import com.um.biblioteca.model.Libro;
import java.util.Optional;

/**
 * Repositorio para la entidad Libro
 */
public interface LibroRepository extends BaseRepository<Libro, Long> {
    
    /**
     * Busca un libro por su ISBN
     * @param isbn ISBN del libro
     * @return Optional con el libro encontrado o vacío si no existe
     */
    Optional<Libro> findByIsbn(String isbn);
    
    /**
     * Busca libros por título
     * @param titulo Título del libro (búsqueda parcial)
     * @return Lista de libros que coinciden con el título
     */
    java.util.List<Libro> findByTituloContaining(String titulo);
    
    /**
     * Busca libros por autor
     * @param autor Nombre del autor (búsqueda parcial)
     * @return Lista de libros que coinciden con el autor
     */
    java.util.List<Libro> findByAutorContaining(String autor);
} 