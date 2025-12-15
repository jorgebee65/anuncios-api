# Documentación de API - Anuncios API

## Información General

- **Base URL**: `http://localhost:8585`
- **Puerto**: `8585`
- **Autenticación**: JWT Bearer Token (donde aplique)

---

## Tabla de Endpoints

| Método HTTP | URL | Descripción | Autenticación |
|------------|-----|-------------|---------------|
| POST | `/api/v1/auth/register` | Registra un nuevo usuario | No |
| POST | `/api/v1/auth/login` | Autentica un usuario y devuelve un token JWT | No |
| GET | `/api/v1/auth/me` | Obtiene la información del usuario autenticado | Sí |
| GET | `/api/v1/advertises` | Obtiene una lista paginada de anuncios con filtros opcionales | No |
| GET | `/api/v1/advertises/{id}` | Obtiene los detalles de un anuncio por ID | No |
| POST | `/api/v1/advertises` | Crea un nuevo anuncio (sin imagen) | Sí |
| POST | `/api/v1/advertises/with-image` | Crea un nuevo anuncio con imagen | Sí |
| PUT | `/api/v1/advertises/{id}/with-image` | Actualiza un anuncio existente con imagen opcional | Sí |
| DELETE | `/api/v1/advertises/{id}` | Elimina un anuncio por ID | Sí |
| GET | `/api/v1/categories` | Obtiene todas las categorías disponibles | No |
| POST | `/api/v1/upload/image` | Sube una imagen y devuelve la URL | Sí |
| GET | `/actuator/health` | Verifica el estado de salud de la aplicación | No |

---

## Ejemplos de Comandos cURL

### 1. Autenticación

#### 1.1. Registrar Usuario
```bash
curl -X POST http://localhost:8585/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario_nuevo",
    "password": "password123",
    "firstName": "Juan"
  }'
```

#### 1.2. Iniciar Sesión
```bash
curl -X POST http://localhost:8585/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario_nuevo",
    "password": "password123"
  }'
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### 1.3. Obtener Información del Usuario Autenticado
```bash
curl -X GET http://localhost:8585/api/v1/auth/me \
  -H "Authorization: Bearer <TU_TOKEN>"
```

---

### 2. Anuncios (Advertises)

#### 2.1. Obtener Todos los Anuncios (Paginado)
```bash
curl -X GET "http://localhost:8585/api/v1/advertises?page=0&size=10&sort=creation,desc" \
  -H "Content-Type: application/json"
```

#### 2.2. Obtener Anuncios con Filtros
```bash
# Filtrar por estado activo
curl -X GET "http://localhost:8585/api/v1/advertises?page=0&size=10&active=true" \
  -H "Content-Type: application/json"

# Filtrar por categoría
curl -X GET "http://localhost:8585/api/v1/advertises?page=0&size=10&category=Electrónicos" \
  -H "Content-Type: application/json"

# Combinar filtros
curl -X GET "http://localhost:8585/api/v1/advertises?page=0&size=10&active=true&category=Electrónicos&sort=price,asc" \
  -H "Content-Type: application/json"
```

#### 2.3. Obtener Anuncio por ID
```bash
curl -X GET http://localhost:8585/api/v1/advertises/1 \
  -H "Content-Type: application/json"
```

#### 2.4. Crear Anuncio (Sin Imagen)
```bash
curl -X POST http://localhost:8585/api/v1/advertises \
  -H "Authorization: Bearer <TU_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "shareLink": "https://example.com/anuncio/123",
    "title": "iPhone 13 Pro Max - Excelente Estado",
    "description": "iPhone 13 Pro Max de 256GB en perfecto estado. Incluye cargador y caja original.",
    "price": 899.99,
    "image": "https://s3.amazonaws.com/bucket/imagen.jpg",
    "phone": "+1234567890",
    "email": "vendedor@example.com",
    "facebook": "https://facebook.com/vendedor",
    "instagram": "https://instagram.com/vendedor",
    "zone": "Centro de la ciudad",
    "category": {
      "id": 1,
      "description": "Electrónicos",
      "icon": "phone",
      "color": "#FF5733"
    }
  }'
```

#### 2.5. Crear Anuncio con Imagen
```bash
curl -X POST http://localhost:8585/api/v1/advertises/with-image \
  -H "Authorization: Bearer <TU_TOKEN>" \
  -F "file=@/ruta/a/tu/imagen.jpg" \
  -F 'data={
    "shareLink": "https://example.com/anuncio/124",
    "title": "Bicicleta de Montaña Trek",
    "description": "Bicicleta de montaña Trek en excelente estado. Ideal para senderos.",
    "price": 450.00,
    "phone": "+1234567891",
    "email": "ciclista@example.com",
    "zone": "Zona Norte",
    "category": {
      "id": 2,
      "description": "Deportes",
      "icon": "bike",
      "color": "#33FF57"
    }
  }'
```

#### 2.6. Actualizar Anuncio con Imagen (Opcional)
```bash
# Actualizar sin cambiar la imagen (file opcional)
curl -X PUT http://localhost:8585/api/v1/advertises/1/with-image \
  -H "Authorization: Bearer <TU_TOKEN>" \
  -F 'data={
    "title": "iPhone 13 Pro Max - Precio Reducido",
    "description": "iPhone 13 Pro Max de 256GB. Precio negociable.",
    "price": 799.99,
    "phone": "+1234567890",
    "email": "vendedor@example.com",
    "zone": "Centro de la ciudad",
    "category": {
      "id": 1,
      "description": "Electrónicos",
      "icon": "phone",
      "color": "#FF5733"
    }
  }'

# Actualizar cambiando la imagen
curl -X PUT http://localhost:8585/api/v1/advertises/1/with-image \
  -H "Authorization: Bearer <TU_TOKEN>" \
  -F "file=@/ruta/a/nueva/imagen.jpg" \
  -F 'data={
    "title": "iPhone 13 Pro Max - Precio Reducido",
    "description": "iPhone 13 Pro Max de 256GB. Precio negociable.",
    "price": 799.99,
    "phone": "+1234567890",
    "email": "vendedor@example.com",
    "zone": "Centro de la ciudad",
    "category": {
      "id": 1,
      "description": "Electrónicos",
      "icon": "phone",
      "color": "#FF5733"
    }
  }'
```

#### 2.7. Eliminar Anuncio
```bash
curl -X DELETE http://localhost:8585/api/v1/advertises/1 \
  -H "Authorization: Bearer <TU_TOKEN>"
```

---

### 3. Categorías

#### 3.1. Obtener Todas las Categorías
```bash
curl -X GET http://localhost:8585/api/v1/categories \
  -H "Content-Type: application/json"
```

---

### 4. Carga de Archivos

#### 4.1. Subir Imagen
```bash
curl -X POST http://localhost:8585/api/v1/upload/image \
  -H "Authorization: Bearer <TU_TOKEN>" \
  -F "file=@/ruta/a/tu/imagen.jpg"
```

**Respuesta esperada:**
```
https://s3.amazonaws.com/bucket/imagen-generada.jpg
```

---

### 5. Health Check

#### 5.1. Verificar Estado de la Aplicación
```bash
curl -X GET http://localhost:8585/actuator/health \
  -H "Content-Type: application/json"
```

---

## Notas Importantes

1. **Autenticación JWT**: Los endpoints que requieren autenticación necesitan el header `Authorization: Bearer <TU_TOKEN>`. Obtén el token mediante el endpoint `/api/v1/auth/login`.

2. **Parámetros de Paginación**:
   - `page`: Número de página (inicia en 0)
   - `size`: Tamaño de la página (por defecto 10)
   - `sort`: Ordenamiento en formato `campo,direccion` (ej: `creation,desc`, `price,asc`)

3. **Filtros de Anuncios**:
   - `active`: Filtra por estado activo/inactivo (true/false)
   - `category`: Filtra por nombre de categoría

4. **Formato de Fechas**: Las fechas en las respuestas siguen el formato ISO 8601.

5. **Carga de Imágenes**: 
   - El endpoint `/api/v1/advertises/with-image` requiere `multipart/form-data`
   - El campo `file` contiene la imagen
   - El campo `data` contiene el JSON del anuncio como string

6. **Validaciones**:
   - `title`, `description`, `shareLink`, `phone`, `email`, `image` son campos obligatorios
   - `price` debe ser un número positivo
   - `email` debe tener un formato válido
   - `category` es obligatorio y debe ser un objeto CategoryDTO válido

---

## Códigos de Estado HTTP

- `200 OK`: Solicitud exitosa
- `201 Created`: Recurso creado exitosamente
- `204 No Content`: Recurso eliminado exitosamente (DELETE)
- `400 Bad Request`: Solicitud inválida (validación fallida)
- `401 Unauthorized`: No autenticado o token inválido
- `403 Forbidden`: No tiene permisos para realizar la acción
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error del servidor

---

## Ejemplo de Flujo Completo

1. **Registrar un usuario:**
```bash
curl -X POST http://localhost:8585/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "test123", "firstName": "Test"}'
```

2. **Iniciar sesión y obtener token:**
```bash
TOKEN=$(curl -s -X POST http://localhost:8585/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "test123"}' | jq -r '.token')
```

3. **Obtener categorías disponibles:**
```bash
curl -X GET http://localhost:8585/api/v1/categories
```

4. **Crear un anuncio:**
```bash
curl -X POST http://localhost:8585/api/v1/advertises \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "shareLink": "https://example.com/anuncio/125",
    "title": "Laptop Dell XPS 15",
    "description": "Laptop Dell XPS 15 con 16GB RAM y SSD 512GB",
    "price": 1299.99,
    "image": "https://s3.amazonaws.com/bucket/laptop.jpg",
    "phone": "+1234567892",
    "email": "tech@example.com",
    "zone": "Zona Sur",
    "category": {
      "id": 1,
      "description": "Electrónicos",
      "icon": "laptop",
      "color": "#FF5733"
    }
  }'
```

5. **Listar anuncios:**
```bash
curl -X GET "http://localhost:8585/api/v1/advertises?page=0&size=10"
```

