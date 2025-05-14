package com.um.biblioteca.service;

import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.model.EstadoUsuario;

import java.util.List;

/**
 * Servicio para gestionar usuarios
 */
public interface UsuarioService {
    
    /**
     * Busca un usuario por su ID
     * @param id ID del usuario
     * @return Usuario encontrado
     * @throws com.um.biblioteca.exception.UsuarioNoEncontradoException si el usuario no existe
     */
    Usuario buscarPorId(Long id);
    
    /**
     * Busca un usuario por su email
     * @param email Email del usuario
     * @return Usuario encontrado
     * @throws com.um.biblioteca.exception.UsuarioNoEncontradoException si el usuario no existe
     */
    Usuario buscarPorEmail(String email);
    
    /**
     * Obtiene todos los usuarios
     * @return Lista de todos los usuarios
     */
    List<Usuario> obtenerTodos();
    
    /**
     * Busca usuarios por nombre
     * @param nombre Nombre del usuario (búsqueda parcial)
     * @return Lista de usuarios que coinciden con el nombre
     */
    List<Usuario> buscarPorNombre(String nombre);
    
    /**
     * Busca usuarios por estado
     * @param estado Estado del usuario
     * @return Lista de usuarios en el estado especificado
     */
    List<Usuario> buscarPorEstado(EstadoUsuario estado);
    
    /**
     * Guarda un usuario
     * @param usuario Usuario a guardar
     * @return Usuario guardado
     */
    Usuario guardar(Usuario usuario);
    
    /**
     * Actualiza un usuario existente
     * @param id ID del usuario a actualizar
     * @param usuario Datos actualizados del usuario
     * @return Usuario actualizado
     * @throws com.um.biblioteca.exception.UsuarioNoEncontradoException si el usuario no existe
     */
    Usuario actualizar(Long id, Usuario usuario);
    
    /**
     * Elimina un usuario
     * @param id ID del usuario a eliminar
     * @throws com.um.biblioteca.exception.UsuarioNoEncontradoException si el usuario no existe
     */
    void eliminar(Long id);
    
    /**
     * Cambia el estado de un usuario
     * @param id ID del usuario
     * @param estado Nuevo estado
     * @return Usuario actualizado
     * @throws com.um.biblioteca.exception.UsuarioNoEncontradoException si el usuario no existe
     */
    Usuario cambiarEstado(Long id, EstadoUsuario estado);
} 