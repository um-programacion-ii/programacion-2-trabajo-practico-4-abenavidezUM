package com.um.biblioteca.service.impl;

import com.um.biblioteca.exception.UsuarioNoEncontradoException;
import com.um.biblioteca.model.EstadoUsuario;
import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.repository.UsuarioRepository;
import com.um.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    
    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }
    
    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException(email));
    }
    
    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    
    @Override
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContaining(nombre);
    }
    
    @Override
    public List<Usuario> buscarPorEstado(EstadoUsuario estado) {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getEstado() == estado)
                .collect(Collectors.toList());
    }
    
    @Override
    public Usuario guardar(Usuario usuario) {
        // Validación básica
        validarUsuario(usuario);
        
        if (usuario.getEstado() == null) {
            usuario.setEstado(EstadoUsuario.ACTIVO);
        }
        
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        // Verificar que el usuario existe
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNoEncontradoException(id);
        }
        
        // Establecer el ID correcto
        usuario.setId(id);
        
        // Validación básica
        validarUsuario(usuario);
        
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public void eliminar(Long id) {
        // Verificar que el usuario existe
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNoEncontradoException(id);
        }
        
        usuarioRepository.deleteById(id);
    }
    
    @Override
    public Usuario cambiarEstado(Long id, EstadoUsuario estado) {
        Usuario usuario = buscarPorId(id);
        
        // Establecer el nuevo estado
        usuario.setEstado(estado);
        
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Valida los datos del usuario
     * @param usuario Usuario a validar
     * @throws IllegalArgumentException si los datos son inválidos
     */
    private void validarUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio");
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del usuario es obligatorio");
        }
        
        // Validación básica de formato de email
        if (!usuario.getEmail().contains("@")) {
            throw new IllegalArgumentException("El formato del email es inválido");
        }
    }
} 