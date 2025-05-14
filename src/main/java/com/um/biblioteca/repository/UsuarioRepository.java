package com.um.biblioteca.repository;

import com.um.biblioteca.model.Usuario;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario
 */
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su email
     * @param email Email del usuario
     * @return Optional con el usuario encontrado o vacío si no existe
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Busca usuarios por nombre
     * @param nombre Nombre del usuario (búsqueda parcial)
     * @return Lista de usuarios que coinciden con el nombre
     */
    java.util.List<Usuario> findByNombreContaining(String nombre);
} 