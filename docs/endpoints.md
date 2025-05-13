# Documentación de los Endpoints API

Este documento describe en detalle todos los endpoints disponibles en la API del Sistema de Gestión de Biblioteca.

## Libros

### Obtener todos los libros
- **URL**: `/api/libros`
- **Método**: `GET`
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Libro

### Obtener libro por ID
- **URL**: `/api/libros/{id}`
- **Método**: `GET`
- **Parámetros URL**: `id=[Long]` ID del libro
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Objeto Libro
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Libro no encontrado con id: {id}" }`

### Buscar libro por ISBN
- **URL**: `/api/libros/isbn/{isbn}`
- **Método**: `GET`
- **Parámetros URL**: `isbn=[String]` ISBN del libro
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Objeto Libro
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Libro no encontrado con isbn: {isbn}" }`

### Buscar libros por título
- **URL**: `/api/libros/titulo/{titulo}`
- **Método**: `GET`
- **Parámetros URL**: `titulo=[String]` Título o parte del título del libro
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Libro

### Buscar libros por autor
- **URL**: `/api/libros/autor/{autor}`
- **Método**: `GET`
- **Parámetros URL**: `autor=[String]` Nombre o parte del nombre del autor
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Libro

### Buscar libros por estado
- **URL**: `/api/libros/estado/{estado}`
- **Método**: `GET`
- **Parámetros URL**: `estado=[EstadoLibro]` Enum: DISPONIBLE, PRESTADO, EN_REPARACION
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Libro

### Crear un nuevo libro
- **URL**: `/api/libros`
- **Método**: `POST`
- **Cuerpo de solicitud**: Objeto Libro (sin id)
- **Respuesta exitosa**:
  - **Código**: 201
  - **Contenido**: Objeto Libro creado

### Actualizar un libro
- **URL**: `/api/libros/{id}`
- **Método**: `PUT`
- **Parámetros URL**: `id=[Long]` ID del libro
- **Cuerpo de solicitud**: Objeto Libro
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Objeto Libro actualizado
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Libro no encontrado con id: {id}" }`

### Cambiar estado de un libro
- **URL**: `/api/libros/{id}/estado`
- **Método**: `PATCH`
- **Parámetros URL**: `id=[Long]` ID del libro
- **Parámetros Query**: `estado=[EstadoLibro]` Enum: DISPONIBLE, PRESTADO, EN_REPARACION
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Objeto Libro actualizado
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Libro no encontrado con id: {id}" }`

### Eliminar un libro
- **URL**: `/api/libros/{id}`
- **Método**: `DELETE`
- **Parámetros URL**: `id=[Long]` ID del libro
- **Respuesta exitosa**:
  - **Código**: 204
  - **Contenido**: Ninguno

## Usuarios

### Obtener todos los usuarios
- **URL**: `/api/usuarios`
- **Método**: `GET`
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Usuario

### Obtener usuario por ID
- **URL**: `/api/usuarios/{id}`
- **Método**: `GET`
- **Parámetros URL**: `id=[Long]` ID del usuario
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Objeto Usuario
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Usuario no encontrado con id: {id}" }`

### Buscar usuario por email
- **URL**: `/api/usuarios/email/{email}`
- **Método**: `GET`
- **Parámetros URL**: `email=[String]` Email del usuario
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Objeto Usuario
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Usuario no encontrado con email: {email}" }`

### Buscar usuarios por nombre
- **URL**: `/api/usuarios/nombre/{nombre}`
- **Método**: `GET`
- **Parámetros URL**: `nombre=[String]` Nombre o parte del nombre del usuario
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Usuario

### Buscar usuarios por estado
- **URL**: `/api/usuarios/estado/{estado}`
- **Método**: `GET`
- **Parámetros URL**: `estado=[EstadoUsuario]` Enum: ACTIVO, INACTIVO, SUSPENDIDO
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Usuario

### Crear un nuevo usuario
- **URL**: `/api/usuarios`
- **Método**: `POST`
- **Cuerpo de solicitud**: Objeto Usuario (sin id)
- **Respuesta exitosa**:
  - **Código**: 201
  - **Contenido**: Objeto Usuario creado

### Actualizar un usuario
- **URL**: `/api/usuarios/{id}`
- **Método**: `PUT`
- **Parámetros URL**: `id=[Long]` ID del usuario
- **Cuerpo de solicitud**: Objeto Usuario
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Objeto Usuario actualizado
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Usuario no encontrado con id: {id}" }`

### Cambiar estado de un usuario
- **URL**: `/api/usuarios/{id}/estado`
- **Método**: `PATCH`
- **Parámetros URL**: `id=[Long]` ID del usuario
- **Parámetros Query**: `estado=[EstadoUsuario]` Enum: ACTIVO, INACTIVO, SUSPENDIDO
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Objeto Usuario actualizado
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Usuario no encontrado con id: {id}" }`

### Eliminar un usuario
- **URL**: `/api/usuarios/{id}`
- **Método**: `DELETE`
- **Parámetros URL**: `id=[Long]` ID del usuario
- **Respuesta exitosa**:
  - **Código**: 204
  - **Contenido**: Ninguno

## Préstamos

### Obtener todos los préstamos
- **URL**: `/api/prestamos`
- **Método**: `GET`
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Prestamo

### Obtener préstamo por ID
- **URL**: `/api/prestamos/{id}`
- **Método**: `GET`
- **Parámetros URL**: `id=[Long]` ID del préstamo
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Objeto Prestamo
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Préstamo no encontrado con id: {id}" }`

### Buscar préstamos por usuario
- **URL**: `/api/prestamos/usuario/{usuarioId}`
- **Método**: `GET`
- **Parámetros URL**: `usuarioId=[Long]` ID del usuario
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Prestamo
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Usuario no encontrado con id: {usuarioId}" }`

### Buscar préstamos por libro
- **URL**: `/api/prestamos/libro/{libroId}`
- **Método**: `GET`
- **Parámetros URL**: `libroId=[Long]` ID del libro
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Prestamo
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Libro no encontrado con id: {libroId}" }`

### Buscar préstamos por fecha
- **URL**: `/api/prestamos/fecha`
- **Método**: `GET`
- **Parámetros Query**: `fecha=[LocalDate]` Fecha en formato ISO (YYYY-MM-DD)
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Prestamo

### Buscar préstamos vencidos
- **URL**: `/api/prestamos/vencidos`
- **Método**: `GET`
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Lista de objetos Prestamo

### Crear un nuevo préstamo
- **URL**: `/api/prestamos`
- **Método**: `POST`
- **Parámetros Query**:
  - `usuarioId=[Long]` ID del usuario
  - `libroId=[Long]` ID del libro
  - `fechaDevolucion=[LocalDate]` (opcional) Fecha de devolución esperada en formato ISO (YYYY-MM-DD)
- **Respuesta exitosa**:
  - **Código**: 201
  - **Contenido**: Objeto Prestamo creado
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Usuario/Libro no encontrado con id: {id}" }`
  - **Código**: 409
  - **Contenido**: `{ "status": "CONFLICT", "message": "El libro no está disponible para préstamo" }`

### Finalizar un préstamo (devolución)
- **URL**: `/api/prestamos/{id}/devolver`
- **Método**: `PATCH`
- **Parámetros URL**: `id=[Long]` ID del préstamo
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Objeto Prestamo finalizado
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Préstamo no encontrado con id: {id}" }`

### Extender un préstamo
- **URL**: `/api/prestamos/{id}/extender`
- **Método**: `PATCH`
- **Parámetros URL**: `id=[Long]` ID del préstamo
- **Parámetros Query**: `nuevaFechaDevolucion=[LocalDate]` Nueva fecha de devolución esperada en formato ISO (YYYY-MM-DD)
- **Respuesta exitosa**:
  - **Código**: 200
  - **Contenido**: Objeto Prestamo actualizado
- **Respuesta de error**:
  - **Código**: 404
  - **Contenido**: `{ "status": "NOT_FOUND", "message": "Préstamo no encontrado con id: {id}" }`
  - **Código**: 400
  - **Contenido**: `{ "status": "BAD_REQUEST", "message": "La nueva fecha de devolución debe ser posterior a la fecha actual" }`

### Eliminar un préstamo
- **URL**: `/api/prestamos/{id}`
- **Método**: `DELETE`
- **Parámetros URL**: `id=[Long]` ID del préstamo
- **Respuesta exitosa**:
  - **Código**: 204
  - **Contenido**: Ninguno 