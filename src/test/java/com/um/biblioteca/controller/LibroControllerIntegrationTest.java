package com.um.biblioteca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.um.biblioteca.model.EstadoLibro;
import com.um.biblioteca.model.Libro;
import com.um.biblioteca.service.LibroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibroController.class)
class LibroControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibroService libroService;

    private Libro libro1;
    private Libro libro2;

    @BeforeEach
    void setUp() {
        libro1 = new Libro();
        libro1.setId(1L);
        libro1.setIsbn("123456789");
        libro1.setTitulo("El principito");
        libro1.setAutor("Antoine de Saint-Exupéry");
        libro1.setEstado(EstadoLibro.DISPONIBLE);
        
        libro2 = new Libro();
        libro2.setId(2L);
        libro2.setIsbn("987654321");
        libro2.setTitulo("Cien años de soledad");
        libro2.setAutor("Gabriel García Márquez");
        libro2.setEstado(EstadoLibro.DISPONIBLE);
    }

    @Test
    void obtenerTodos() throws Exception {
        List<Libro> libros = Arrays.asList(libro1, libro2);
        when(libroService.obtenerTodos()).thenReturn(libros);

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].titulo", is("El principito")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].titulo", is("Cien años de soledad")));
    }

    @Test
    void obtenerPorId() throws Exception {
        when(libroService.buscarPorId(1L)).thenReturn(libro1);

        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("El principito")))
                .andExpect(jsonPath("$.autor", is("Antoine de Saint-Exupéry")))
                .andExpect(jsonPath("$.estado", is("DISPONIBLE")));
    }

    @Test
    void crear() throws Exception {
        when(libroService.guardar(any(Libro.class))).thenReturn(libro1);

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(libro1)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("El principito")));
    }

    @Test
    void actualizar() throws Exception {
        when(libroService.actualizar(eq(1L), any(Libro.class))).thenReturn(libro1);

        mockMvc.perform(put("/api/libros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(libro1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("El principito")));
    }

    @Test
    void cambiarEstado() throws Exception {
        Libro libroActualizado = new Libro();
        libroActualizado.setId(1L);
        libroActualizado.setIsbn("123456789");
        libroActualizado.setTitulo("El principito");
        libroActualizado.setAutor("Antoine de Saint-Exupéry");
        libroActualizado.setEstado(EstadoLibro.PRESTADO);

        when(libroService.cambiarEstado(1L, EstadoLibro.PRESTADO)).thenReturn(libroActualizado);

        mockMvc.perform(patch("/api/libros/1/estado")
                .param("estado", "PRESTADO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.estado", is("PRESTADO")));
    }

    @Test
    void eliminar() throws Exception {
        mockMvc.perform(delete("/api/libros/1"))
                .andExpect(status().isNoContent());
    }
} 