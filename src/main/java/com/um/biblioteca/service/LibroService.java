package com.um.biblioteca.service;

import com.um.biblioteca.model.Libro;
import com.um.biblioteca.model.EstadoLibro;

import java.util.List;

/**
 * Servicio para gestionar libros
 */
public interface LibroService {
    
    /**
     * Busca un libro por su ID
     * @param id ID del libro
     * @return Libro encontrado
     * @throws com.um.biblioteca.exception.LibroNoEncontradoException si el libro no existe
     */
    Libro buscarPorId(Long id);
    
    /**
     * Busca un libro por su ISBN
     * @param isbn ISBN del libro
     * @return Libro encontrado
     * @throws com.um.biblioteca.exception.LibroNoEncontradoException si el libro no existe
     */
    Libro buscarPorIsbn(String isbn);
    
    /**
     * Obtiene todos los libros
     * @return Lista de todos los libros
     */
    List<Libro> obtenerTodos();
    
    /**
     * Busca libros por título
     * @param titulo Título del libro (búsqueda parcial)
     * @return Lista de libros que coinciden con el título
     */
    List<Libro> buscarPorTitulo(String titulo);
    
    /**
     * Busca libros por autor
     * @param autor Nombre del autor (búsqueda parcial)
     * @return Lista de libros que coinciden con el autor
     */
    List<Libro> buscarPorAutor(String autor);
    
    /**
     * Busca libros por estado
     * @param estado Estado del libro
     * @return Lista de libros en el estado especificado
     */
    List<Libro> buscarPorEstado(EstadoLibro estado);
    
    /**
     * Guarda un libro
     * @param libro Libro a guardar
     * @return Libro guardado
     */
    Libro guardar(Libro libro);
    
    /**
     * Actualiza un libro existente
     * @param id ID del libro a actualizar
     * @param libro Datos actualizados del libro
     * @return Libro actualizado
     * @throws com.um.biblioteca.exception.LibroNoEncontradoException si el libro no existe
     */
    Libro actualizar(Long id, Libro libro);
    
    /**
     * Elimina un libro
     * @param id ID del libro a eliminar
     * @throws com.um.biblioteca.exception.LibroNoEncontradoException si el libro no existe
     */
    void eliminar(Long id);
    
    /**
     * Cambia el estado de un libro
     * @param id ID del libro
     * @param estado Nuevo estado
     * @return Libro actualizado
     * @throws com.um.biblioteca.exception.LibroNoEncontradoException si el libro no existe
     */
    Libro cambiarEstado(Long id, EstadoLibro estado);
} 