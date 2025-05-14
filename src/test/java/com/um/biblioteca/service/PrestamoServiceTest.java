package com.um.biblioteca.service;

import com.um.biblioteca.exception.DatosInvalidosException;
import com.um.biblioteca.exception.LibroNoEncontradoException;
import com.um.biblioteca.exception.PrestamoNoEncontradoException;
import com.um.biblioteca.exception.RecursoNoDisponibleException;
import com.um.biblioteca.exception.UsuarioNoEncontradoException;
import com.um.biblioteca.model.EstadoLibro;
import com.um.biblioteca.model.EstadoUsuario;
import com.um.biblioteca.model.Libro;
import com.um.biblioteca.model.Prestamo;
import com.um.biblioteca.model.Usuario;
import com.um.biblioteca.repository.PrestamoRepository;
import com.um.biblioteca.service.impl.PrestamoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;
    
    @Mock
    private LibroService libroService;
    
    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private PrestamoServiceImpl prestamoService;

    private Prestamo prestamo1;
    private Prestamo prestamo2;
    private Usuario usuario;
    private Libro libro;
    private Libro libroNoPrestado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
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
        
        libroNoPrestado = new Libro();
        libroNoPrestado.setId(2L);
        libroNoPrestado.setIsbn("987654321");
        libroNoPrestado.setTitulo("Cien años de soledad");
        libroNoPrestado.setAutor("Gabriel García Márquez");
        libroNoPrestado.setEstado(EstadoLibro.DISPONIBLE);
        
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
        prestamo2.setLibro(libroNoPrestado);
        prestamo2.setFechaPrestamo(LocalDate.now().minusDays(10));
        prestamo2.setFechaDevolucion(LocalDate.now().minusDays(3));
        prestamo2.setFechaDevolucionReal(null);
    }

    @Test
    void obtenerTodos_debeRetornarListaDePrestamos() {
        // Arrange
        when(prestamoRepository.findAll()).thenReturn(Arrays.asList(prestamo1, prestamo2));
        
        // Act
        List<Prestamo> result = prestamoService.obtenerTodos();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(prestamo1));
        assertTrue(result.contains(prestamo2));
        verify(prestamoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExisteId_debeRetornarPrestamo() {
        // Arrange
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo1));
        
        // Act
        Prestamo result = prestamoService.buscarPorId(1L);
        
        // Assert
        assertEquals(prestamo1, result);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExisteId_debeLanzarExcepcion() {
        // Arrange
        when(prestamoRepository.findById(3L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(PrestamoNoEncontradoException.class, () -> {
            prestamoService.buscarPorId(3L);
        });
        verify(prestamoRepository, times(1)).findById(3L);
    }

    @Test
    void buscarPorUsuario_debeRetornarPrestamosDelUsuario() {
        // Arrange
        when(prestamoRepository.findByUsuario(usuario)).thenReturn(Arrays.asList(prestamo1, prestamo2));
        
        // Act
        List<Prestamo> result = prestamoService.buscarPorUsuario(usuario);
        
        // Assert
        assertEquals(2, result.size());
        verify(prestamoRepository, times(1)).findByUsuario(usuario);
    }

    @Test
    void buscarPorLibro_debeRetornarPrestamosDelLibro() {
        // Arrange
        when(prestamoRepository.findByLibro(libro)).thenReturn(Arrays.asList(prestamo1));
        
        // Act
        List<Prestamo> result = prestamoService.buscarPorLibro(libro);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(prestamo1, result.get(0));
        verify(prestamoRepository, times(1)).findByLibro(libro);
    }

    @Test
    void buscarPorFechaPrestamo_debeRetornarPrestamosDeLaFecha() {
        // Arrange
        LocalDate hoy = LocalDate.now();
        when(prestamoRepository.findByFechaPrestamo(hoy)).thenReturn(Arrays.asList(prestamo1));
        
        // Act
        List<Prestamo> result = prestamoService.buscarPorFechaPrestamo(hoy);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(prestamo1, result.get(0));
        verify(prestamoRepository, times(1)).findByFechaPrestamo(hoy);
    }

    @Test
    void buscarVencidos_debeRetornarPrestamosVencidos() {
        // Arrange
        LocalDate hoy = LocalDate.now();
        when(prestamoRepository.findByFechaDevolucionRealIsNullAndFechaDevolucionBefore(hoy)).thenReturn(Arrays.asList(prestamo2));
        
        // Act
        List<Prestamo> result = prestamoService.buscarVencidos();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(prestamo2, result.get(0));
        verify(prestamoRepository, times(1)).findByFechaDevolucionRealIsNullAndFechaDevolucionBefore(hoy);
    }

    @Test
    void crearPrestamo_conDatosValidos_debeCrearPrestamo() {
        // Arrange
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);
        when(libroService.buscarPorId(2L)).thenReturn(libroNoPrestado);
        when(libroService.cambiarEstado(2L, EstadoLibro.PRESTADO)).thenReturn(libroNoPrestado);
        
        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setUsuario(usuario);
        nuevoPrestamo.setLibro(libroNoPrestado);
        nuevoPrestamo.setFechaPrestamo(LocalDate.now());
        nuevoPrestamo.setFechaDevolucion(LocalDate.now().plusDays(7));
        
        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(nuevoPrestamo);
        
        // Act
        Prestamo result = prestamoService.crearPrestamo(1L, 2L, LocalDate.now().plusDays(7));
        
        // Assert
        assertNotNull(result);
        assertEquals(usuario, result.getUsuario());
        assertEquals(libroNoPrestado, result.getLibro());
        assertEquals(LocalDate.now().plusDays(7), result.getFechaDevolucion());
        verify(usuarioService, times(1)).buscarPorId(1L);
        verify(libroService, times(1)).buscarPorId(2L);
        verify(libroService, times(1)).cambiarEstado(2L, EstadoLibro.PRESTADO);
        verify(prestamoRepository, times(1)).save(any(Prestamo.class));
    }

    @Test
    void crearPrestamo_conUsuarioInexistente_debeLanzarExcepcion() {
        // Arrange
        when(usuarioService.buscarPorId(3L)).thenThrow(new UsuarioNoEncontradoException(3L));
        
        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> {
            prestamoService.crearPrestamo(3L, 2L, LocalDate.now().plusDays(7));
        });
        verify(usuarioService, times(1)).buscarPorId(3L);
        verify(libroService, never()).buscarPorId(anyLong());
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    @Test
    void crearPrestamo_conLibroInexistente_debeLanzarExcepcion() {
        // Arrange
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);
        when(libroService.buscarPorId(3L)).thenThrow(new LibroNoEncontradoException(3L));
        
        // Act & Assert
        assertThrows(LibroNoEncontradoException.class, () -> {
            prestamoService.crearPrestamo(1L, 3L, LocalDate.now().plusDays(7));
        });
        verify(usuarioService, times(1)).buscarPorId(1L);
        verify(libroService, times(1)).buscarPorId(3L);
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    @Test
    void crearPrestamo_conLibroNoPrestable_debeLanzarExcepcion() {
        // Arrange
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);
        when(libroService.buscarPorId(1L)).thenReturn(libro); // libro ya prestado
        
        // Act & Assert
        assertThrows(RecursoNoDisponibleException.class, () -> {
            prestamoService.crearPrestamo(1L, 1L, LocalDate.now().plusDays(7));
        });
        verify(usuarioService, times(1)).buscarPorId(1L);
        verify(libroService, times(1)).buscarPorId(1L);
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    @Test
    void finalizarPrestamo_cuandoExistePrestamo_debeFinalizarlo() {
        // Arrange
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo1));
        when(libroService.cambiarEstado(1L, EstadoLibro.DISPONIBLE)).thenReturn(libro);
        
        Prestamo prestamoFinalizado = new Prestamo();
        prestamoFinalizado.setId(1L);
        prestamoFinalizado.setUsuario(usuario);
        prestamoFinalizado.setLibro(libro);
        prestamoFinalizado.setFechaPrestamo(prestamo1.getFechaPrestamo());
        prestamoFinalizado.setFechaDevolucion(prestamo1.getFechaDevolucion());
        prestamoFinalizado.setFechaDevolucionReal(LocalDate.now());
        
        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(prestamoFinalizado);
        
        // Act
        Prestamo result = prestamoService.finalizarPrestamo(1L);
        
        // Assert
        assertNotNull(result.getFechaDevolucionReal());
        verify(prestamoRepository, times(1)).findById(1L);
        verify(libroService, times(1)).cambiarEstado(1L, EstadoLibro.DISPONIBLE);
        verify(prestamoRepository, times(1)).save(any(Prestamo.class));
    }

    @Test
    void finalizarPrestamo_cuandoNoExistePrestamo_debeLanzarExcepcion() {
        // Arrange
        when(prestamoRepository.findById(3L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(PrestamoNoEncontradoException.class, () -> {
            prestamoService.finalizarPrestamo(3L);
        });
        verify(prestamoRepository, times(1)).findById(3L);
        verify(libroService, never()).cambiarEstado(anyLong(), any(EstadoLibro.class));
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    @Test
    void extenderPrestamo_conDatosValidos_debeExtenderPrestamo() {
        // Arrange
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo1));
        
        LocalDate nuevaFecha = LocalDate.now().plusDays(14);
        Prestamo prestamoExtendido = new Prestamo();
        prestamoExtendido.setId(1L);
        prestamoExtendido.setUsuario(usuario);
        prestamoExtendido.setLibro(libro);
        prestamoExtendido.setFechaPrestamo(prestamo1.getFechaPrestamo());
        prestamoExtendido.setFechaDevolucion(nuevaFecha);
        prestamoExtendido.setFechaDevolucionReal(null);
        
        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(prestamoExtendido);
        
        // Act
        Prestamo result = prestamoService.extenderPrestamo(1L, nuevaFecha);
        
        // Assert
        assertEquals(nuevaFecha, result.getFechaDevolucion());
        verify(prestamoRepository, times(1)).findById(1L);
        verify(prestamoRepository, times(1)).save(any(Prestamo.class));
    }

    @Test
    void extenderPrestamo_conFechaInvalida_debeLanzarExcepcion() {
        // Arrange
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo1));
        
        LocalDate fechaPasada = LocalDate.now().minusDays(1);
        
        // Act & Assert
        assertThrows(DatosInvalidosException.class, () -> {
            prestamoService.extenderPrestamo(1L, fechaPasada);
        });
        verify(prestamoRepository, times(1)).findById(1L);
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    @Test
    void extenderPrestamo_cuandoNoExistePrestamo_debeLanzarExcepcion() {
        // Arrange
        when(prestamoRepository.findById(3L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(PrestamoNoEncontradoException.class, () -> {
            prestamoService.extenderPrestamo(3L, LocalDate.now().plusDays(7));
        });
        verify(prestamoRepository, times(1)).findById(3L);
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    @Test
    void eliminar_debeEliminarPrestamo() {
        // Arrange
        doNothing().when(prestamoRepository).deleteById(anyLong());
        
        // Act
        prestamoService.eliminar(1L);
        
        // Assert
        verify(prestamoRepository, times(1)).deleteById(1L);
    }
} 