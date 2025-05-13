package com.um.biblioteca.controller;

import com.um.biblioteca.model.EstadoLibro;
import com.um.biblioteca.model.Libro;
import com.um.biblioteca.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {
    
    private final LibroService libroService;
    
    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }
    
    @GetMapping
    public ResponseEntity<List<Libro>> obtenerTodos() {
        List<Libro> libros = libroService.obtenerTodos();
        return ResponseEntity.ok(libros);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        Libro libro = libroService.buscarPorId(id);
        return ResponseEntity.ok(libro);
    }
    
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Libro> obtenerPorIsbn(@PathVariable String isbn) {
        Libro libro = libroService.buscarPorIsbn(isbn);
        return ResponseEntity.ok(libro);
    }
    
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@PathVariable String titulo) {
        List<Libro> libros = libroService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(libros);
    }
    
    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<Libro>> buscarPorAutor(@PathVariable String autor) {
        List<Libro> libros = libroService.buscarPorAutor(autor);
        return ResponseEntity.ok(libros);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Libro>> buscarPorEstado(@PathVariable EstadoLibro estado) {
        List<Libro> libros = libroService.buscarPorEstado(estado);
        return ResponseEntity.ok(libros);
    }
    
    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
        Libro nuevoLibro = libroService.guardar(libro);
        return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        Libro libroActualizado = libroService.actualizar(id, libro);
        return ResponseEntity.ok(libroActualizado);
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Libro> cambiarEstado(
            @PathVariable Long id, 
            @RequestParam EstadoLibro estado) {
        Libro libro = libroService.cambiarEstado(id, estado);
        return ResponseEntity.ok(libro);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        libroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
} 