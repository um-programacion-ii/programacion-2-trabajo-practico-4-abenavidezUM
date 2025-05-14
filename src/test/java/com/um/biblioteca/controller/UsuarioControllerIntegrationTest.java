package com.um.biblioteca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.um.biblioteca.model.EstadoUsuario;
import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.service.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Juan Pérez");
        usuario1.setEmail("juan@example.com");
        usuario1.setEstado(EstadoUsuario.ACTIVO);
        
        usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("María López");
        usuario2.setEmail("maria@example.com");
        usuario2.setEstado(EstadoUsuario.ACTIVO);
    }

    @Test
    void obtenerTodos() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioService.obtenerTodos()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("María López")));
    }

    @Test
    void obtenerPorId() throws Exception {
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario1);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.email", is("juan@example.com")))
                .andExpect(jsonPath("$.estado", is("ACTIVO")));
    }

    @Test
    void obtenerPorEmail() throws Exception {
        when(usuarioService.buscarPorEmail("juan@example.com")).thenReturn(usuario1);

        mockMvc.perform(get("/api/usuarios/email/juan@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.email", is("juan@example.com")));
    }

    @Test
    void buscarPorNombre() throws Exception {
        when(usuarioService.buscarPorNombre("Juan")).thenReturn(Arrays.asList(usuario1));

        mockMvc.perform(get("/api/usuarios/nombre/Juan"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Juan Pérez")));
    }

    @Test
    void buscarPorEstado() throws Exception {
        when(usuarioService.buscarPorEstado(EstadoUsuario.ACTIVO)).thenReturn(Arrays.asList(usuario1, usuario2));

        mockMvc.perform(get("/api/usuarios/estado/ACTIVO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void crear() throws Exception {
        when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuario1);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario1)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")));
    }

    @Test
    void actualizar() throws Exception {
        when(usuarioService.actualizar(eq(1L), any(Usuario.class))).thenReturn(usuario1);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")));
    }

    @Test
    void cambiarEstado() throws Exception {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setNombre("Juan Pérez");
        usuarioActualizado.setEmail("juan@example.com");
        usuarioActualizado.setEstado(EstadoUsuario.INACTIVO);

        when(usuarioService.cambiarEstado(1L, EstadoUsuario.INACTIVO)).thenReturn(usuarioActualizado);

        mockMvc.perform(patch("/api/usuarios/1/estado")
                .param("estado", "INACTIVO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.estado", is("INACTIVO")));
    }

    @Test
    void eliminar() throws Exception {
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }
} 