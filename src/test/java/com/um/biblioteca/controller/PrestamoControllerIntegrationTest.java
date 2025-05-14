package com.um.biblioteca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.um.biblioteca.model.EstadoLibro;
import com.um.biblioteca.model.EstadoUsuario;
import com.um.biblioteca.model.Libro;
import com.um.biblioteca.model.Prestamo;
import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.service.LibroService;
import com.um.biblioteca.service.PrestamoService;
import com.um.biblioteca.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrestamoController.class)
class PrestamoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PrestamoService prestamoService;
    
    @MockBean
    private UsuarioService usuarioService;
    
    @MockBean
    private LibroService libroService;

    private Prestamo prestamo1;
    private Prestamo prestamo2;
    private Usuario usuario;
    private Libro libro;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan@example.com");
        usuario.setEstado(EstadoUsuario.ACTIVO);
        
        libro = new Libro();
        libro.setId(1L);
        libro.setIsbn("123456789");
        libro.setTitulo("El principito");
        libro.setAutor("Antoine de Saint-Exupéry");
        libro.setEstado(EstadoLibro.PRESTADO);
        
        prestamo1 = new Prestamo();
        prestamo1.setId(1L);
        prestamo1.setUsuario(usuario);
        prestamo1.setLibro(libro);
        prestamo1.setFechaPrestamo(LocalDate.now());
        prestamo1.setFechaDevolucion(LocalDate.now().plusDays(7));
        prestamo1.setFechaDevolucionReal(null);
        
        prestamo2 = new Prestamo();
        prestamo2.setId(2L);
        prestamo2.setUsuario(usuario);
        prestamo2.setLibro(libro);
        prestamo2.setFechaPrestamo(LocalDate.now().minusDays(3));
        prestamo2.setFechaDevolucion(LocalDate.now().plusDays(4));
        prestamo2.setFechaDevolucionReal(null);
    }

    @Test
    void obtenerTodos() throws Exception {
        List<Prestamo> prestamos = Arrays.asList(prestamo1, prestamo2);
        when(prestamoService.obtenerTodos()).thenReturn(prestamos);

        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void obtenerPorId() throws Exception {
        when(prestamoService.buscarPorId(1L)).thenReturn(prestamo1);

        mockMvc.perform(get("/api/prestamos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.usuario.id", is(1)))
                .andExpect(jsonPath("$.libro.id", is(1)));
    }

    @Test
    void buscarPorUsuario() throws Exception {
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);
        when(prestamoService.buscarPorUsuario(usuario)).thenReturn(Arrays.asList(prestamo1, prestamo2));

        mockMvc.perform(get("/api/prestamos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void buscarPorLibro() throws Exception {
        when(libroService.buscarPorId(1L)).thenReturn(libro);
        when(prestamoService.buscarPorLibro(libro)).thenReturn(Arrays.asList(prestamo1, prestamo2));

        mockMvc.perform(get("/api/prestamos/libro/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void buscarPorFechaPrestamo() throws Exception {
        LocalDate fecha = LocalDate.now();
        when(prestamoService.buscarPorFechaPrestamo(fecha)).thenReturn(Arrays.asList(prestamo1));

        mockMvc.perform(get("/api/prestamos/fecha")
                .param("fecha", fecha.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void buscarVencidos() throws Exception {
        when(prestamoService.buscarVencidos()).thenReturn(Arrays.asList(prestamo2));

        mockMvc.perform(get("/api/prestamos/vencidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)));
    }

    @Test
    void crearPrestamo() throws Exception {
        LocalDate fechaDevolucion = LocalDate.now().plusDays(7);
        when(prestamoService.crearPrestamo(1L, 1L, fechaDevolucion)).thenReturn(prestamo1);

        mockMvc.perform(post("/api/prestamos")
                .param("usuarioId", "1")
                .param("libroId", "1")
                .param("fechaDevolucion", fechaDevolucion.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void finalizarPrestamo() throws Exception {
        Prestamo prestamoFinalizado = new Prestamo();
        prestamoFinalizado.setId(1L);
        prestamoFinalizado.setUsuario(usuario);
        prestamoFinalizado.setLibro(libro);
        prestamoFinalizado.setFechaPrestamo(prestamo1.getFechaPrestamo());
        prestamoFinalizado.setFechaDevolucion(prestamo1.getFechaDevolucion());
        prestamoFinalizado.setFechaDevolucionReal(LocalDate.now());
        
        when(prestamoService.finalizarPrestamo(1L)).thenReturn(prestamoFinalizado);

        mockMvc.perform(patch("/api/prestamos/1/devolver"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fechaDevolucionReal").exists());
    }

    @Test
    void extenderPrestamo() throws Exception {
        LocalDate nuevaFecha = LocalDate.now().plusDays(14);
        
        Prestamo prestamoExtendido = new Prestamo();
        prestamoExtendido.setId(1L);
        prestamoExtendido.setUsuario(usuario);
        prestamoExtendido.setLibro(libro);
        prestamoExtendido.setFechaPrestamo(prestamo1.getFechaPrestamo());
        prestamoExtendido.setFechaDevolucion(nuevaFecha);
        prestamoExtendido.setFechaDevolucionReal(null);
        
        when(prestamoService.extenderPrestamo(1L, nuevaFecha)).thenReturn(prestamoExtendido);

        mockMvc.perform(patch("/api/prestamos/1/extender")
                .param("nuevaFechaDevolucion", nuevaFecha.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fechaDevolucion").value(nuevaFecha.toString()));
    }

    @Test
    void eliminar() throws Exception {
        mockMvc.perform(delete("/api/prestamos/1"))
                .andExpect(status().isNoContent());
    }
} 