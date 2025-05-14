package com.um.biblioteca.model;

import java.util.Objects;

public class Usuario {
    
    private Long id;
    private String nombre;
    private String email;
    private EstadoUsuario estado;
    
    public Usuario() {
        this.estado = EstadoUsuario.ACTIVO;
    }
    
    public Usuario(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.estado = EstadoUsuario.ACTIVO;
    }
    
    public Usuario(Long id, String nombre, String email, EstadoUsuario estado) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.estado = estado;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public EstadoUsuario getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) &&
               Objects.equals(email, usuario.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", estado=" + estado +
                '}';
    }
} 