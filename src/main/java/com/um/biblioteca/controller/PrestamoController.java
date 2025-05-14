package com.um.biblioteca.controller;

import com.um.biblioteca.model.Libro;
import com.um.biblioteca.model.Prestamo;
import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.service.PrestamoService;
import com.um.biblioteca.service.LibroService;
import com.um.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {
    
    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;
    private final LibroService libroService;
    
    @Autowired
    public PrestamoController(PrestamoService prestamoService, UsuarioService usuarioService, LibroService libroService) {
        this.prestamoService = prestamoService;
        this.usuarioService = usuarioService;
        this.libroService = libroService;
    }
    
    @GetMapping
    public ResponseEntity<List<Prestamo>> obtenerTodos() {
        List<Prestamo> prestamos = prestamoService.obtenerTodos();
        return ResponseEntity.ok(prestamos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPorId(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.buscarPorId(id);
        return ResponseEntity.ok(prestamo);
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Prestamo>> buscarPorUsuario(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        List<Prestamo> prestamos = prestamoService.buscarPorUsuario(usuario);
        return ResponseEntity.ok(prestamos);
    }
    
    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<Prestamo>> buscarPorLibro(@PathVariable Long libroId) {
        Libro libro = libroService.buscarPorId(libroId);
        List<Prestamo> prestamos = prestamoService.buscarPorLibro(libro);
        return ResponseEntity.ok(prestamos);
    }
    
    @GetMapping("/fecha")
    public ResponseEntity<List<Prestamo>> buscarPorFechaPrestamo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Prestamo> prestamos = prestamoService.buscarPorFechaPrestamo(fecha);
        return ResponseEntity.ok(prestamos);
    }
    
    @GetMapping("/vencidos")
    public ResponseEntity<List<Prestamo>> buscarVencidos() {
        List<Prestamo> prestamos = prestamoService.buscarVencidos();
        return ResponseEntity.ok(prestamos);
    }
    
    @PostMapping
    public ResponseEntity<Prestamo> crearPrestamo(
            @RequestParam Long usuarioId,
            @RequestParam Long libroId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDevolucion) {
        Prestamo prestamo = prestamoService.crearPrestamo(usuarioId, libroId, fechaDevolucion);
        return new ResponseEntity<>(prestamo, HttpStatus.CREATED);
    }
    
    @PatchMapping("/{id}/devolver")
    public ResponseEntity<Prestamo> finalizarPrestamo(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.finalizarPrestamo(id);
        return ResponseEntity.ok(prestamo);
    }
    
    @PatchMapping("/{id}/extender")
    public ResponseEntity<Prestamo> extenderPrestamo(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nuevaFechaDevolucion) {
        Prestamo prestamo = prestamoService.extenderPrestamo(id, nuevaFechaDevolucion);
        return ResponseEntity.ok(prestamo);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        prestamoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
} 