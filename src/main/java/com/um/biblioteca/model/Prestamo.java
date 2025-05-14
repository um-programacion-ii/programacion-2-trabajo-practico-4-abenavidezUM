package com.um.biblioteca.model;

import java.time.LocalDate;
import java.util.Objects;

public class Prestamo {
    
    private Long id;
    private Libro libro;
    private Usuario usuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    
    public Prestamo() {
        this.fechaPrestamo = LocalDate.now();
    }
    
    public Prestamo(Long id, Libro libro, Usuario usuario) {
        this.id = id;
        this.libro = libro;
        this.usuario = usuario;
        this.fechaPrestamo = LocalDate.now();
        // Por defecto, el préstamo es por 15 días
        this.fechaDevolucion = fechaPrestamo.plusDays(15);
    }
    
    public Prestamo(Long id, Libro libro, Usuario usuario, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        this.id = id;
        this.libro = libro;
        this.usuario = usuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Libro getLibro() {
        return libro;
    }
    
    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }
    
    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
    
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }
    
    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
    
    public boolean estaVencido() {
        return fechaDevolucion != null && LocalDate.now().isAfter(fechaDevolucion);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prestamo prestamo = (Prestamo) o;
        return Objects.equals(id, prestamo.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", libro=" + libro +
                ", usuario=" + usuario +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                '}';
    }
} 