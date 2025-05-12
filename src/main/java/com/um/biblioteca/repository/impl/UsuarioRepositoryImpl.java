package com.um.biblioteca.repository.impl;

import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
    
    private final Map<Long, Usuario> usuarios = new HashMap<>();
    private Long nextId = 1L;
    
    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(nextId++);
        }
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }
    
    @Override
    public Optional<Usuario> findById(Long id) {
        return Optional.ofNullable(usuarios.get(id));
    }
    
    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios.values());
    }
    
    @Override
    public void deleteById(Long id) {
        usuarios.remove(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return usuarios.containsKey(id);
    }
    
    @Override
    public Optional<Usuario> findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return Optional.empty();
        }
        
        return usuarios.values().stream()
                .filter(usuario -> usuario.getEmail() != null && 
                        usuario.getEmail().equals(email))
                .findFirst();
    }
    
    @Override
    public List<Usuario> findByNombreContaining(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return Collections.emptyList();
        }
        
        return usuarios.values().stream()
                .filter(usuario -> usuario.getNombre() != null && 
                        usuario.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
} 