# Sistema de Manejo de Excepciones

Este documento describe el sistema de manejo de excepciones implementado en la API del Sistema de Gestión de Biblioteca.

## Arquitectura de Excepciones

El sistema utiliza una arquitectura de excepciones en capas que proporciona un manejo consistente de errores en toda la aplicación:

```
Exception (Java)
    └── BibliotecaException (Base)
        ├── LibroNoEncontradoException
        ├── UsuarioNoEncontradoException
        ├── PrestamoNoEncontradoException
        ├── RecursoNoDisponibleException
        └── DatosInvalidosException
```

## Jerarquía de Excepciones

### BibliotecaException

Clase base para todas las excepciones personalizadas del sistema. Extiende `RuntimeException` y añade:
- Campo `status`: Código de estado HTTP correspondiente
- Métodos constructores para diferentes escenarios

```java
public class BibliotecaException extends RuntimeException {
    private int status;
    
    public BibliotecaException(String mensaje) {
        super(mensaje);
        this.status = 500;
    }
    
    public BibliotecaException(String mensaje, int status) {
        super(mensaje);
        this.status = status;
    }
    
    // Otros constructores...
    
    public int getStatus() {
        return status;
    }
}
```

### Excepciones de "Recurso No Encontrado"

Estas excepciones se lanzan cuando se intenta acceder a un recurso que no existe en el sistema:

#### LibroNoEncontradoException
```java
public class LibroNoEncontradoException extends BibliotecaException {
    public LibroNoEncontradoException(Long id) {
        super("No se encontró el libro con id: " + id, 404);
    }
    
    public LibroNoEncontradoException(String isbn) {
        super("No se encontró el libro con ISBN: " + isbn, 404);
    }
}
```

#### UsuarioNoEncontradoException
```java
public class UsuarioNoEncontradoException extends BibliotecaException {
    public UsuarioNoEncontradoException(Long id) {
        super("No se encontró el usuario con id: " + id, 404);
    }
    
    public UsuarioNoEncontradoException(String email) {
        super("No se encontró el usuario con email: " + email, 404);
    }
}
```

#### PrestamoNoEncontradoException
```java
public class PrestamoNoEncontradoException extends BibliotecaException {
    public PrestamoNoEncontradoException(Long id) {
        super("No se encontró el préstamo con id: " + id, 404);
    }
}
```

### Excepciones de Validación y Estado

Estas excepciones se lanzan cuando existen problemas con la validez de los datos o el estado de los recursos:

#### DatosInvalidosException
```java
public class DatosInvalidosException extends BibliotecaException {
    public DatosInvalidosException(String mensaje) {
        super(mensaje, 400);
    }
    
    public DatosInvalidosException(String campo, String motivo) {
        super("El campo " + campo + " es inválido: " + motivo, 400);
    }
}
```

#### RecursoNoDisponibleException
```java
public class RecursoNoDisponibleException extends BibliotecaException {
    public RecursoNoDisponibleException(String mensaje) {
        super(mensaje, 409);
    }
    
    public RecursoNoDisponibleException(String recurso, String motivo) {
        super("El recurso " + recurso + " no está disponible: " + motivo, 409);
    }
}
```

## Manejador Global de Excepciones

La clase `GlobalExceptionHandler` centraliza el manejo de todas las excepciones lanzadas por la aplicación:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BibliotecaException.class)
    public ResponseEntity<ApiError> handleBibliotecaException(BibliotecaException ex, WebRequest request) {
        ApiError apiError = new ApiError(
            ex.getStatus(),
            HttpStatus.valueOf(ex.getStatus()).getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(ex.getStatus()));
    }
    
    // Manejadores específicos para cada tipo de excepción...
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
            500,
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "Ocurrió un error inesperado: " + ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

### Respuesta de Error Estandarizada

Todas las excepciones se convierten en objetos `ApiError` que siguen un formato consistente:

```java
public class ApiError {
    private LocalDateTime timestamp; // Momento en que ocurrió el error
    private int status;              // Código de estado HTTP
    private String error;            // Descripción del tipo de error
    private String message;          // Mensaje detallado del error
    private String path;             // Ruta del endpoint que generó el error
    
    // Constructores y getters/setters...
}
```

## Mapeo de Excepciones a Códigos HTTP

| Excepción | Código HTTP | Descripción |
|-----------|-------------|-------------|
| LibroNoEncontradoException | 404 NOT_FOUND | El libro solicitado no existe |
| UsuarioNoEncontradoException | 404 NOT_FOUND | El usuario solicitado no existe |
| PrestamoNoEncontradoException | 404 NOT_FOUND | El préstamo solicitado no existe |
| DatosInvalidosException | 400 BAD_REQUEST | Los datos proporcionados no son válidos |
| RecursoNoDisponibleException | 409 CONFLICT | El recurso existe pero no está disponible |
| Otras excepciones | 500 INTERNAL_SERVER_ERROR | Error interno del servidor |

## Ejemplos de Uso

### Lanzamiento de Excepciones en Servicios

```java
// En un servicio
public Libro buscarPorId(Long id) {
    return libroRepository.findById(id)
        .orElseThrow(() -> new LibroNoEncontradoException(id));
}

public Libro guardar(Libro libro) {
    if (libro.getTitulo() == null || libro.getTitulo().isEmpty()) {
        throw new DatosInvalidosException("titulo", "no puede estar vacío");
    }
    return libroRepository.save(libro);
}

public Prestamo crearPrestamo(Long usuarioId, Long libroId, LocalDate fechaDevolucion) {
    // ...
    if (libro.getEstado() != EstadoLibro.DISPONIBLE) {
        throw new RecursoNoDisponibleException("libro", "no está disponible para préstamo");
    }
    // ...
}
```

### Ejemplo de Respuesta JSON de Error

```json
{
  "timestamp": "2023-05-10T15:30:45.123Z",
  "status": 404,
  "error": "Not Found",
  "message": "No se encontró el libro con id: 123",
  "path": "/api/libros/123"
}
```

## Beneficios del Sistema

1. **Consistencia**: Todas las respuestas de error siguen el mismo formato
2. **Trazabilidad**: Información detallada sobre el error para facilitar la depuración
3. **Separación de responsabilidades**: Las capas de servicio solo se preocupan por lanzar excepciones, mientras que la capa de controlador las maneja
4. **Experiencia del usuario**: Mensajes de error claros y significativos
5. **Seguridad**: No se filtran detalles sensibles de implementación en los errores 