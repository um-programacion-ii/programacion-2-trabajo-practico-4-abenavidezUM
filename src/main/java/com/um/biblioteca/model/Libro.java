package com.um.biblioteca.model;

import java.util.Objects;

public class Libro {
    
    private Long id;
    private String isbn;
    private String titulo;
    private String autor;
    private EstadoLibro estado;
    
    public Libro() {
        this.estado = EstadoLibro.DISPONIBLE;
    }
    
    public Libro(Long id, String isbn, String titulo, String autor) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.estado = EstadoLibro.DISPONIBLE;
    }
    
    public Libro(Long id, String isbn, String titulo, String autor, EstadoLibro estado) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.estado = estado;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getAutor() {
        return autor;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    public EstadoLibro getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoLibro estado) {
        this.estado = estado;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return Objects.equals(id, libro.id) &&
               Objects.equals(isbn, libro.isbn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, isbn);
    }
    
    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", estado=" + estado +
                '}';
    }
} 