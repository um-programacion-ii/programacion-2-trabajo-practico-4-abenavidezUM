package com.um.biblioteca.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz base para repositorios
 * @param <T> Tipo de entidad
 * @param <ID> Tipo de ID
 */
public interface BaseRepository<T, ID> {
    
    /**
     * Guarda una entidad
     * @param entity Entidad a guardar
     * @return Entidad guardada
     */
    T save(T entity);
    
    /**
     * Busca una entidad por su ID
     * @param id ID de la entidad
     * @return Optional con la entidad encontrada o vacío si no existe
     */
    Optional<T> findById(ID id);
    
    /**
     * Obtiene todas las entidades
     * @return Lista de entidades
     */
    List<T> findAll();
    
    /**
     * Elimina una entidad por su ID
     * @param id ID de la entidad a eliminar
     */
    void deleteById(ID id);
    
    /**
     * Verifica si existe una entidad con el ID especificado
     * @param id ID a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsById(ID id);
} 