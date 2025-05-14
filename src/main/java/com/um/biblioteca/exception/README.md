# Manejo de Excepciones en el Sistema de Biblioteca

Este módulo contiene las clases relacionadas con el manejo de excepciones en la aplicación.

## Estructura

- `BibliotecaException`: Clase base para todas las excepciones personalizadas del sistema
- `GlobalExceptionHandler`: Controlador centralizado para manejar todas las excepciones y convertirlas en respuestas HTTP apropiadas
- `ApiError`: Clase que representa la estructura de los errores en la API

## Excepciones de Recurso No Encontrado

- `LibroNoEncontradoException`: Cuando no se puede encontrar un libro
- `UsuarioNoEncontradoException`: Cuando no se puede encontrar un usuario
- `PrestamoNoEncontradoException`: Cuando no se puede encontrar un préstamo

## Excepciones de Validación y Estado

- `DatosInvalidosException`: Cuando los datos proporcionados no son válidos
- `RecursoNoDisponibleException`: Cuando un recurso existe pero no está disponible (ej. libro prestado)

## Flujo de Manejo de Excepciones

1. Las capas de servicio lanzan excepciones específicas cuando se producen errores
2. El `GlobalExceptionHandler` captura estas excepciones
3. El handler convierte las excepciones en objetos `ApiError` con información relevante
4. Se devuelve una respuesta HTTP con el código de estado apropiado y el objeto `ApiError`

## Códigos de Estado HTTP

- `404 NOT_FOUND`: Cuando un recurso no existe
- `400 BAD_REQUEST`: Cuando los datos de entrada son inválidos
- `409 CONFLICT`: Cuando hay un conflicto de estado (ej. intentar prestar un libro ya prestado)
- `500 INTERNAL_SERVER_ERROR`: Para errores inesperados

## Ejemplo de Respuesta de Error

```json
{
  "code": 404,
  "status": "NOT_FOUND",
  "message": "Libro no encontrado con id: 123",
  "path": "/api/libros/123",
  "timestamp": "2023-05-10T15:30:45.123Z"
}
``` 